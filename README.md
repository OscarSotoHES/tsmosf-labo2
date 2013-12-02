tsmosf-labo2
============

##VMs Configuration

###Users

| Service               | UserName
|-----------------------|---------       
| Servers administrator | osfadmin
| MySQL                 | root     

###Machines

| Hostname     | IPAddress    | SSH Port (In/Out) | HTTP Port (In/Out)
|--------------|--------------|-------------------|--------------------
| loadbalancer | 192.168.2.50 | 22 / 2200         | 80 / 8080
| play1        | 192.168.2.51 | 22 / 2201         | 9000 / 8081
| play2        | 192.168.2.52 | 22 / 2202         | 9000 / 8082
| serverdb     | 192.168.2.53 | 22 / 2203         | -
