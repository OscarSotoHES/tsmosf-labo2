# OSF Labo 2
## Theme 1: Play Framework & Distributed Caching

### But du projet

Le but du projet est de tester le framework Play dans un cluster ainsi que les gains de performances lors de l'ajout d'une couche de cache.

### Play VS Java EE
*Explain how Play approaches HTTP session state management and how it is different from the traditional Java EE approach. Explain the benefits of this approach*

Play permet de stocker des données de session dans le cookie de la prochaine requête HTTP. Ce qui signifie que les données de session sont stockées sur le client et non sur le serveur. La taille des cookies est limitée à 4 Ko et elle ne peut contenir que des chaînes de caractères clé - valeur. Les cookies sont signés avec une clé secrète de sorte que le client ne puisse pas modifier les données du cookie. Attention, les données de session ne sont pas destinées à être utilisées comme un cache, au besoin Play intègre le mécanisme de cache sur le côté serveur (Ehcache / memcached).

Java EE permet l'utilisation de conteneur EJB stateful du côté serveur. Ces EJB stocke non seulement les données de la session mais ils gèrent aussi le cycle de vie, les transactions, la sécurité, le cache d'instance, sont thread safe, etc. De ce faite, il n'y a pas besoin d'écrire du code pour ajouter tous ces services. Par contre, les performances sont moins bonnes qu'avec les EJB stateless ou le gestionnaire de session du framework Play.

Après ce bref descriptif, nous pouvons relever que les données de session avec Play sont stockées sur le côté client, contrairement à Java EE avec les EJB Stateful. Les serveurs web implémentés avec Play peuvent être interrogés par plusieurs clients à la fois et ils peuvent traiter n'importe quelle requête de n'importe quel client. Pour se faire, le code serveur récupère les données de session stockées dans le cookie pour personnaliser ces réponses HTTP en fonction de chacun de ces clients. Avec Java EE chaque client est traité par un EJB stateful différent, de se faite le serveur sera plus rapidement saturé qu'un serveur qui utilise un framework Play. Bien évidement, un EJB est beaucoup plus complet en terme de fonctionnalité que le gestionnaire de session simpliste de Play. Rien ne nous empêche avec Java EE pour palier aux problèmes de performance d'utiliser l'approche du framework Play. Nous pouvons toujours utiliser les EJB stateless et la HttpSession qui gères les sessions par l'intermédiaire des cookies soit la réécriture des URLs.


### API Rest
*Description de l'API hébergée sur https://schoolapi.apiary.io/*. School API est une *API simpliste* qui permet de gérer des cours et des élèves. Toutes les méthodes CRUD sont impémentées. Aucun test d'intégrité n'est effectué dans le code. Cette API est uniquement utilisée comme contexte pour les tests de performances du présent projet. L'API contient deux ressources nommées Students et Lessons. À titre d'exemple voici quelques exemples d'utilisations :

##### Students Collection [/students]
###### List all Students and lessons where they are registred [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1, 
                "lessons": [], 
                "name": "Joe Smith"
            },
            {
                "id": 2, 
                "lessons": 
                [
                    {
                        "id": 1,
                        "studentId": 2, 
                        "lessonId":1
                    }
                ], 
                "name": "Komanda Phanzu"
            }
        ]

###### Create a Lesson [POST]
+ Request (application/json)

        {
            "name": "CorpCom"
        }

+ Response 201 (application/json)

        {
            "id": 3, 
            "students": [],
            "name": "CorpCom"
        }

###### Update a Lesson, used primarly to add students in a lesson [PUT]
+ Request (application/json)

        {
            "students": 
            [
                {
                    "studentId": 3
                }
            ], 
            "name": "NSA"
        }
        
+ Response 200 (application/json)

        {
            "id": 2, 
            "students": 
            [
                {
                    "id": 4, 
                    "studentId": 3, 
                    "lessonId": 2
                }
            ],
            "name": "NSA"
        }

### Architecture 
*serveurs, base de données, etc*

Tous les tests ont été effectués sur des machines virtuelles (VMware ESXi 4.1) installées sur un même serveur (IBM x3100 M4). Quatre machines ont été créées : 

- 1 load balancer
- 2 serveurs Play (avec la couche de cache)
- 1 serveur de base de données

Toutes les machines ont été installées avec Ubuntu 12.04 LTS 64 bit. Chaque machine s'est vue attribuée un coeur (partagée avec d'autres machines). Le load balancer a reçu 256MB de ram et les autres serveurs 384MB.

#### Load balancer

Le load balancer a la responsabilité de distribué les requêtes des utilisateurs vers les serveurs Play de manière a répartir la charge. Ainsi, il écoute les requêtes sur le port 80 et les redirige alternativement vers le port 9000 des serveurs Play. 

Pour réaliser ceci, il suffit de configurer un serveur HTTP. En effet, les principaux serveurs HTTP permettent le load balancing (Apache, nginx, lighttpd, etc.) Nous avons choisi *lighttpd* car il a apparemment de meilleures performances que les serveurs Apache et qu'il est plus facile a mettre en place.

##### Installation et configuration de lighttpd

**Installation depuis Ubuntu :**

	sudo apt-get update
	sudo apt-get install lighttpd

**Modification du fichier de configuration */etc/lighttpd/lighttpd.conf***

Dans le fichier de configuration, il faut activer les modules suivants :

	server.modules = (
        "mod_access",
        "mod_alias",
        "mod_compress",
        "mod_redirect",
        "mod_accesslog",
        "mod_proxy"
        )
 
Et ajouter les paramètres, ci-dessous, en fin de fichier :

	$HTTP["host"] =~ "*ADDRESS_EXTERN*" {
        proxy.balance = "round-robin" proxy.server = ( "/" =>
                (( "host" => "*IP_SERVER_PLAY1*", "port" => 9000), ( "host" => "*IP_SERVER_PLAY2*", "port" => 9000)))
	}
	
**Redémarrage du serveur *lighttpd***

	sudo /etc/init.d/lighttpd restart
	
#### Serveurs Play

Deux serveurs play ont été configurés avec une configuration identique. Ils contiennent chacun le code l'API REST et écoute les requêtes transmises par le *load balancer* sur le port 9000. Le programme *memcached* est utilisé pour la gestion du cache.

##### Installation du framework Play (sur Ubuntu)

Pour utilisé play, il faut tout d'abord installé Java :

	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java7-installer
	
Ensuite, il faut télécharger Play Framework à cette adresse : [http://downloads.typesafe.com/play/2.2.1/play-2.2.1.zip](http://downloads.typesafe.com/play/2.2.1/play-2.2.1.zip) et le décompresser.

Il reste à ajouter la commande *play* dans le path : 

	export PATH=$PATH:/path/to/play
	
##### *Memcached*

Depuis la version 2.0, Play utilise le système de cache *EHCache* (au lieu de *Memcached*). Depuis la version 2.2, le module cache n'est pas activé par défaut. La documentation, disponible sur le site de Play Framework, indique uniquement la procédure pour l'activer avec *scala* et omet complètement la configuration pour *java*. De plus, aucune explication n'est donnée sur l'utilisation de *EHCache* en cluster.

Après quelques recherches, nous avons remarqué que la plus part des développeurs conseillent d'utiliser *memcached* lors d'une utilisation en cluster. Ceci pour plusieurs raisons :

- Meilleures performances
- Plus facile à configurer
- EHCache nécessite un programme supplémentaire pour être utilisé en cluster (par exemple Terracotta)

###### Installation et configuration de *memcached*

**Installation de *memcached***

Tout d'abord, il faut installer *memcached* sur la machine :

	sudo apt-get update
	sudo apt-get install memcached
	
**Configuration de *memcached***

Pour que Play puisse attendre plusieurs serveur *memcached* il faut remplacer la ligne 

	-l 127.0.0.1 

dans le fichier "/etc/memcached.conf" par :

	-l 192.168.2.52


**Activation de *memcached* dans Play 2.2**

Pour activer *memcached* sur Play 2.2, il faut utiliser un plugin externe. Nous avons utiliser le plugin suivant : [https://github.com/mumoshu/play2-memcached](https://github.com/mumoshu/play2-memcached).

*Dans le dossier du projet*

Il faut éditer le fichier *conf/play.plugins* et ajouter la ligne suivante  :

	5000:com.github.mumoshu.play2.memcached.MemcachedPlugin

Dans le fichier *conf/application.conf*, il faut ajouter les lignes suivantes :

	ehcacheplugin=disabled
	memcached.host="ADDRESS_SERVEUR_PLAY1:11211"
	memcached.host="ADDRESS_SERVEUR_PLAY2:11211"

Dans le fichier *build.sbt*, il faut ajouter les dépendances suivantes :

    libraryDependencies ++= Seq(
            "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
            "mysql" % "mysql-connector-java" % "5.1.18",
            "org.eclipse.persistence" % "javax.persistence" % "2.0.0",
            "org.webjars" %% "webjars-play" % "2.2.0",
            "org.webjars" % "bootstrap" % "2.3.1",
            "com.github.mumoshu" %% "play2-memcached" % "0.3.0.2",
            "com.typesafe.play" %% "play-cache" % "2.2.0" withSources
    )
    resolvers ++= Seq(
            "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository",
        //"Local Maven Repository" at "file:///e:/_shared/_repository/maven/3.x.x",
        "Spy Repository" at "http://files.couchbase.com/maven2"
    )

##### Démarrer le serveur Play

Le code du projet doit être télécharger sur la machine [https://github.com/yenyen/tsmosf-labo2/archive/master.zip](https://github.com/yenyen/tsmosf-labo2/archive/master.zip)

Depuis le dossier *hello-play-java*, lancer la commande :

	play run

#### Serveur de base de données

Le dernier serveur contient la base de données utilisée par tous les serveurs Play. Nous avons utiliser un serveur MySQL.

##### Installation et configuration

Ci-dessous, la commande pour installer MySQL sur Ubuntu 12.04 :

	sudo apt-get update
	sudo apt-get install mysql-server
	
Puis, les commandes pour se connecter au serveur, créer la base de données et créer un utilisateur :

	mysql -u root -p
	CREATE DATABASE nom_de_la_base;
	CREATE USER "nom_utilisateur"@"%"; 
	SET password FOR "nom_utilisateur"@"%" = password('mot_de_passe');
	GRANT ALL ON nom_de_la_base.* TO "nom_utilisateur"@"%";
	
##### Configuration de Play

Le fichier de configuration du projet Play (conf/application.conf) doit être configuré sur tous les serveurs Play :

	db.default.driver=org.hibernate.dialect.MySQL5Dialect
	db.default.url="jdbc:mysql://mysql:3306/nom_de_la_base"
	db.default.user="nom_utilisateur"
	db.default.password="mot_de_passe"


### Test - JMeter

*Describe what kind of test data you have generated (and how) and what kind of traffic you have simulated. Describe the conditions of the experiment.*

*Explain how someone can do another run of the experiment (how to setup a test environment, how to use tools to generate the data and simulate the traffic, etc.).*

### Questions

#### Question 1: What is the performance impact of using a caching layer when implementing a REST API with Play?

#### Question 2: How is it possible to use a caching layer in a cluster environment, when several Play “nodes” are setup to serve HTTP requests?

##### Memcached
*Explain what is means to use a caching layer in a cluster environment and what are the issues to consider. Explain how Ehcache addresses these issues.*

### Conclusion
*Avis sur Play Framework (et sa documentation...) et sur le cache*
