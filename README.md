# htmlstat
Parsing text statistics of html document to DB


Technologies: 
Maven
Hibernate
JSoup

Creating db. Example from psql:
postgres=# create database htmlstat;
postgres=# create user statuser with encrypted password '12345';
postgres=# grant all privileges on database htmlstat to statuser;

java -jar ./urlstat-1.0-SNAPSHOT-shaded.jar --p https://vk.com
java -jar ./urlstat-1.0-SNAPSHOT-shaded.jar --h https://vk.com
java -jar ./urlstat-1.0-SNAPSHOT-shaded.jar --c https://vk.com


mvn package
