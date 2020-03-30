# microprofile-example
Example of Thorntail microservice example for my personal study, if you want try to look it your are free

## Intro
I'm studing the Thorntail technology by Red Hat company. This is my small e compact microservice.

## Import and Run
Import it in Eclipse or your favorite editor as Maven project, for start type in console:
```console
mvn thorntail:run
```

## Full requirement
For full functionalities the application required a Database and an external REST service
### Database
For the API under `/database` endpoint, the application required a MariaDB database, with a database called **test**.<br/>
All information are specify in class: [DBUtils.java](https://github.com/Martins96/microprofile-example/blob/master/src/main/java/com/example/demo/rest/persistency/DBUtils.java)

```java
static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
static final String DB_URL = "jdbc:mariadb://127.0.0.1/test";
static final String USER = "app_user";
static final String PASS = "atp830udm02kg";
```
Change or create the database/user with same configuration

### REST Service
I create a call to external service REST, if you want try this function you need to run the SimpleRestServer from my Git Repo,
downolad the code, or simply the jar file in *release* section from this [LINK](https://github.com/Martins96/SimpleRestProject)
