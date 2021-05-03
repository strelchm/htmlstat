# htmlstat
Statistics text parsing (unique text entry) of html document to DB. Apllication has three modes:
* PARSING
* HISTORY
* CLEAR

**Features:**
- [x] Unique word statistics of html-document
- [x] Statistics is saved to DB. History getting posibility
- [x] Posibility of reading big documents - parsing is going througt the input streams
- [x] Integration test covers biggest bug possibility
- [x] Useful exception handling and logging ("logs" folder)

**Technologies:**
- JavaSE
- Maven
- PostgreSQL
- Hibernate
- JSoup
- JUnit
- 
At first own html parsing was started to coding but later the ready choice of Jsoup was choosing. Hibernate was 

1. **DB creating.** By default, PostgreSQL is configured, but driver can be changed. Example for creating PostgreSQL DB from psql:
```
postgres=# create database htmlstat;
postgres=# create user statuser with encrypted password '12345';
postgres=# grant all privileges on database htmlstat to statuser;
```
2. **DB settings configuration.** DB settings are situated in hibernate.cfg.xml file in project resourse folder. Main DB settings are:
```
<property name="hibernate.connection.url">jdbc:postgresql://localhost/htmlstat</property>
<property name="hibernate.connection.username">statuser</property>
<property name="hibernate.connection.password">12345</property>
 ```
3. **Project packaging.** Enter next command from your folder:
```
mvn package
```
After packaging procces jar-file will be created in target folder. This is "fat JAR", so no depenencies libs needed for launching app

4. **Starting app.** All modes have two arguments - mode key and URL.
* **PARSING**. Parsing of html-page by input url and output statistics. Launch jar by next command:
```
java -jar ./urlstat-1.0-SNAPSHOT-shaded.jar --p https://vk.com
```
* **HISTORY**. History statistics output ordered by date. Output will be word statistics like PARSING-mode but severel times dependent of previous parsing sessions:
```
java -jar ./urlstat-1.0-SNAPSHOT-shaded.jar --h https://vk.com
```
* **CLEAR**. Clearing all parsing sessions and statistics by input url. Launch jar by next command:
```
java -jar ./urlstat-1.0-SNAPSHOT-shaded.jar --c https://vk.com
```
