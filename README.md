1. Create PostgreSQL database.
2. Run task bootRun of gradle wrapper with database connection properties:

```
./gradlew bootRun --args='--spring.datasource.url=jdbc:postgresql://<localhost:5432>/<products>
--spring.datasource.username=<username> --spring.datasource.password=<password>'
```