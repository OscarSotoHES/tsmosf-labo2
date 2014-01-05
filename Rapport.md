# OSF Labo 2
## Theme 1: Play Framework & Distributed Caching

### But du projet

Le but du projet est de tester le framework Play dans un cluster ainsi que les gains de performances lors de l'ajout d'une couche de cache.

### Play VS Java EE

Play permet de stocker des données de session dans le cookie de la prochaine requête HTTP. Ce qui signifie que les données de session sont stockées sur le client et non sur le serveur. La taille des cookies est limitée à 4 Ko et elle ne peut contenir que des chaînes de caractères clé - valeur. Les cookies sont signés avec une clé secrète de sorte que le client ne puisse pas modifier les données du cookie. Attention, les données de session ne sont pas destinées à être utilisées comme un cache, au besoin Play intègre le mécanisme de cache sur le côté serveur (Ehcache / memcached).

Java EE permet l'utilisation de conteneur EJB stateful du côté serveur. Ces EJB stocke non seulement les données de la session mais ils gèrent aussi le cycle de vie, les transactions, la sécurité, le cache d'instance, sont thread safe, etc. De ce faite, il n'y a pas besoin d'écrire du code pour ajouter tous ces services. Par contre, les performances sont moins bonnes qu'avec les EJB stateless ou le gestionnaire de session du framework Play.

Après ce bref descriptif, nous pouvons relever que les données de session avec Play sont stockées sur le côté client, contrairement à Java EE avec les EJB Stateful. Les serveurs web implémentés avec Play peuvent être interrogés par plusieurs clients à la fois et ils peuvent traiter n'importe quelle requête de n'importe quel client. Pour se faire, le code serveur récupère les données de session stockées dans le cookie pour personnaliser ces réponses HTTP en fonction de chacun de ces clients. Avec Java EE chaque client est traité par un EJB stateful différent, de se faite le serveur sera plus rapidement saturé qu'un serveur qui utilise un framework Play. Bien évidement, un EJB est beaucoup plus complet en terme de fonctionnalité que le gestionnaire de session simpliste de Play. Rien ne nous empêche avec Java EE pour palier aux problèmes de performance d'utiliser l'approche du framework Play. Nous pouvons toujours utiliser les EJB stateless et la HttpSession qui gères les sessions par l'intermédiaire des cookies soit la réécriture des URLs.

### API Rest

School API est une *API simpliste* qui permet de gérer des cours et des élèves. Toutes les méthodes CRUD sont impémentées. Aucun test d'intégrité n'est effectué dans le code. Cette API est uniquement utilisée comme contexte pour les tests de performances du présent projet. L'API contient deux ressources nommées Students et Lessons. Voici quelques exemples d'utilisations :

#### Students Collection [/students]
##### List all Students [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1, 
                "name": "Joe Smith"
            },
            {
                "id": 2, 
                "name": "Komanda Phanzu"
            }
        ]
#### Lesson Collection [/lessons]
##### Create a Lesson [POST]
+ Request (application/json)

        {
            "name": "CorpCom"
        }

+ Response 201 (application/json)

        {
            "id": 3, 
            "name": "CorpCom",
            "students": []
        }

##### Update a Lesson, used primarly to add students in a lesson [PUT]
+ Request (application/json)

        {
             "name": "NSA"
             "students": 
             [
                {
                    "id": 3
                }
             ]
        }
        
+ Response 200 (application/json)

        {
            "id": 2, 
            "name": "NSA"
            "students": 
            [
                {
                    "id": 3, 
                    "name": "John Doe"
                }
            ]
        }

La documentation complète de l'API se trouve à cette adresse : [docs.schoolapi.apiary.io](http://docs.schoolapi.apiary.io/)

#### Gestion du cache

Pour utiliser la version utilisant le cache de School API, il faut précédé toutes les requête par "cache". Par exemple, pour avoir la liste de tous les étudiants en utilisant le cache :

	get /cache/students

### Architecture 
*serveurs, base de données, etc*

Tous les tests ont été effectués sur des machines virtuelles (VMware ESXi 4.1) installées sur un même serveur (IBM x3100 M4). Quatre machines ont été créées : 

- 1 load balancer
- 2 serveurs Play (avec la couche de cache)
- 1 serveur de base de données

Toutes les machines ont été installées avec Ubuntu 12.04 LTS 64 bit. Chaque machine s'est vue attribuée un coeur (partagée avec d'autres machines). Le load balancer a reçu 256MB de ram et les autres serveurs 384MB.

#### Load balancer

Le load balancer a la responsabilité de distribué les requêtes des utilisateurs vers les serveurs Play de manière a répartir la charge. Ainsi, il écoute les requêtes sur le port 80 et les redirige alternativement vers le port 9000 des serveurs Play. 

Pour réaliser ceci, il suffit de configurer un serveur HTTP. En effet, les principaux serveurs HTTP permettent le load balancing (Apache, nginx, lighttpd, etc.) Nous avons choisi *lighttpd*, car grâce à lui, on obtient de meilleures performances qu'avec les serveurs Apache et qu'il est plus facile a mettre en place.

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
            ...,
            "com.github.mumoshu" %% "play2-memcached" % "0.3.0.2",
            "com.typesafe.play" %% "play-cache" % "2.2.0" withSources
    )
    resolvers ++= Seq(
        "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository",
        "Spy Repository" at "http://files.couchbase.com/maven2"
    )

##### Démarrer le serveur Play

Le code du projet doit être téléchargé sur la machine [https://github.com/yenyen/tsmosf-labo2/archive/master.zip](https://github.com/yenyen/tsmosf-labo2/archive/master.zip)

Depuis le dossier *SchoolAPI*, lancer la commande :

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

### Test de performances

Les tests de performances ont été réalisés avec Apache JMeter. 

#### Installation de JMeter

Il faut tout d'abord télécharger la dernière version à cette adresse : [jmeter.apache.org/download_jmeter.cgi](http://jmeter.apache.org/download_jmeter.cgi)

Ensuite, il suffit de décompresser l'archive et de lancer le script *bin/jmeter.bat* sous Windows ou *bin/jmeter.sh* sour Unix.

Une fois ouvert, il reste a ouvrir le plan de test (cliquer sur le menu /File/Open et choisir le fichier "SchoolAPITest_remote.jmx".)

Pour finir, cliquer sur le bouton "Play" pour démarrer les tests.

#### Plan de test

Le plan de test développé, effectue trois étapes :

##### 1 - Generate Data

Cette première étape génére les donnnées nécessaires au test. 200 étudiants (100 "John Smith" et 100 "Jane Doe" et 100 cours ("OSF") sont créés. A chaque cours est attribué deux étudiants.

##### 2 - Test

Cette étape teste les performances du service dans quatre configurations différentes.

| Configuration                   | Description
|---------------------------------|--------------------
| One Play Server                 | 1 serveur Play sans cache
| Cluster Play Servers            | Cluster de deux serveurs Play (avec le loadbalancer devant) sans cache
| One Play Server with Cache      | 1 serveur Play avec le cache
| Cluster Play Servers with Cache | Cluster de deux serveurs Play avec le cache

Le test s'effectue, pour chaque configuration, en douze étapes simulant une utilisation *normale* du service et utilisant toutes les méthodes proposées par l'API :

1.  Création d'un étudiant (POST /students)
2.  Get de l'étudiant créé (GET /students/id)
3.  Mise à jour de l'étudiant créé (PUT /students/id)
4.  Get de l'étudiant mis à jour (GET /students/id)
5.  10x Get de tous les étudiants (GET /students)
6.  Création d'un cours (POST /lessons)
7.  Get du cours créé (GET /lessons/id)
8.  Mise à jour du cours en inscrivant l'étudiant créé dans le cours (PUT /lessons/id)
9.  10x Get du cours créé (GET /lessons/id)
10. Suppression du cours (DELETE /lessons/id)
11. Suppression de l'étudiant (DELETE /students/id)
12. 10x Get de tous les étudiants (GET /students)

Pour observer les différences entre les configurations lors d'une montée en charge, ce test a été lancé avec différents nombres de threads (10 / 50 / 100). Pour avoir des statistiques cohérantes les mesures sont calculées avec 5 itérations.

##### 3 - Remove all Data

Cette dernière étape consiste à supprimer toutes les données créées pour ce test.

#### Condition des tests

Pour éviter au maximum les pertes de performances dues au réseau, les tests ont été effectués depuis un ordinateur connecté sur le même réseau local que les serveurs, en utilisant les adresses IP locales.

#### Résultats obtenu

##### 10 threads simultanés (x5 itérations)

Voici les résultats obtenus regroupés par opération et configuration. Les valeurs sont les temps de réponses moyens.

| Opération  | One server | Cluster   | One server + Cache | Cluster + Cache
|------------|-----------:|----------:|-------------------:|-----------------:
| POST       | 11.5 ms    | 10 ms     | 9.5 ms             | 10.5 ms 
| PUT        | 13 ms      | 10.5 ms   | 10.5 ms            | 12 ms
| GET (one)  | 13.7 ms    | 11.5 ms   | 5.7 ms             | 5 ms
| GET (all)  | 94.3 ms    | 96.4 ms   | 54 ms              | 60.4 ms
| DELETE     | 9.5 ms     | 9 ms      | 10.5 ms            | 9.5 ms
| **Total**  | **44 ms**  | **44 ms** | **25 ms**          | **28 ms**

Comme on peut le constater dans ces résultats, l'utilisation d'un cluster avec une faible charge n'amène pas de meilleures performances. Cela a même tendance à ajouter un certains temps de latence supplémentaire, probablement dû au temps pris pour passer par le *load balancer*.

Par contre, l'utilisation du cache améliore grandement les performances lors des opérations *GET* (environ 50%) et ne péjore pas vraiment les perforances lors des autres opérations.

##### 50 threads simultanés (x5 itérations)

Ci-dessous, les résultats obtenus avec 50 threads simultanés :

| Opération  | One server  | Cluster    | One server + Cache | Cluster + Cache
|------------|------------:|-----------:|-------------------:|-----------------:
| POST       | 213 ms      | 151.5 ms   | 117 ms             | 93.5 ms 
| PUT        | 192.5 ms    | 150.5 ms   | 117 ms             | 84 ms
| GET (one)  | 207.8 ms    | 159.3 ms   | 119.1 ms           | 83.9 ms
| GET (all)  | 273.8 ms    | 266.7 ms   | 167.9 ms           | 159.9 ms
| DELETE     | 282 ms      | 159 ms     | 142.5 ms           | 92.5 ms
| **Total**  | **237 ms**  | **200 ms** | **139 ms**         | **114 ms**

Avec cinquante threads, l'utilisation d'un cluster améliore, cette fois les performances d'environ 20%. Sans le cluster, le serveur est surchargé et n'arrive plus a répondre rapidement aux requêtes.

L'utilisation du cache reste, cependant, nettement plus avantageux avec environ 40% d'amélioration.

##### 100 threads simultanés (x5 itérations)

Ci-dessous, les résultats obtenus avec 100 threads simultanés :

| Opération  | One server  | Cluster    | One server + Cache | Cluster + Cache
|------------|------------:|-----------:|-------------------:|-----------------:
| POST       | 483 ms      | 439.5 ms   | 259 ms             | 221 ms 
| PUT        | 497.5 ms    | 432.5 ms   | 261.5 ms           | 252 ms
| GET (one)  | 521.5 ms    | 442.8 ms   | 263.6 ms           | 239.5 ms
| GET (all)  | 581 ms      | 569.3 ms   | 299.5 ms           | 306 ms
| DELETE     | 562.5 ms    | 465 ms     | 284 ms             | 271.5 ms
| **Total**  | **543 ms**  | **492 ms** | **278 ms**         | **267 ms**

Avec cent threads nous obtenons sensiblement le même rapport de performances entre les différentes configurations qu'avec cinquante threads. Ainsi, tous les temps de latences ont un peu plus de doublés.

Le cache améliore, donc, d'environ 40% les performances et le cluster de seulement 20%.

### Questions

#### Question 1: What is the performance impact of using a caching layer when implementing a REST API with Play?

L'utilisation du cache améliore nettement les performances. D'après nos tests, ceci diminue d'environ 40% les temps de réponses généraux. 

Avec une faible charge, seules les opérations "GET" sont améliorée par l'utilisation du cache. Les autres opérations ne sont pas tellement impactée. En effet, la mise à jour du cache est rapide et ne péjore pas signifivativement les performances.

Par contre, lors d'une montée en charge, les serveurs et la base de données étant moins solicitées par les opérations "GET", les autres opérations peuvent ainsi bénéficier de plus de ressources et gagner en performance.

Le cache ne s'utilise, cependant pas dans toutes les utilisations et peut nuire aux performances s'il est mal utilisé. 

Tout d'abord, si les clients réalisent beaucoups plus d'opération de mise à jour (PUT, POST, DELETE) que de "GET" alors le cache n'améliora pas les performances.

Ensuite, il faut faire attention de ne mettre que les informations utiles en cache. En effet, il ne faut pas oublié que chaque valeur mise en cache utilise de la mémoire. Si à cause du cache, la machine doit paginé alors les performances peuvent être désastreuse. 

En résumé, le cache, s'il est bien utilisé et dans certains cas, peut grandement améliorer les performances d'une API Rest avec Play.

#### Question 2: How is it possible to use a caching layer in a cluster environment, when several Play “nodes” are setup to serve HTTP requests?

##### Memcached
*Explain what is means to use a caching layer in a cluster environment and what are the issues to consider. Explain how Ehcache addresses these issues.*

### Conclusion
*Avis sur Play Framework (et sa documentation...) et sur le cache*
