tsmosf-labo2
============

##VMs Configuration

###Users

| Service               | UserName  | Password
|-----------------------|-----------|-----------
| Servers administrator | osfadmin  | ********
| MySQL                 | root      | ********
| MySQL                 | adm_tsm   | OSFMysql2

###Machines

| Hostname     | IPAddress    | SSH Port (In/Out) | HTTP Port (In/Out) | Other port
|--------------|--------------|-------------------|--------------------|--------------
| loadbalancer | 192.168.2.50 | 22 / 2200         | 80 / 8080          |
| play1        | 192.168.2.51 | 22 / 2201         | 9000 / 8081        |
| play2        | 192.168.2.52 | 22 / 2202         | 9000 / 8082        |
| mysql        | 192.168.2.53 | 22 / 2203         | -                  | Mysql: 3306
