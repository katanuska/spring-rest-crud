1. Create PostgreSQL database.
2. Run task bootRun of gradle wrapper with database connection properties:

```
./gradlew bootRun --args='--spring.datasource.url=jdbc:postgresql://<localhost:5432>/<products>
--spring.datasource.username=<username> --spring.datasource.password=<password>'
```

REST API

- Get all products

```
  curl --location 'http://localhost:8080/products'
```

- Create product

```
curl --location 'http://localhost:8080/products' \
--header 'Content-Type: application/json' \
--data '{
    "code": "1234567890",
    "name": "Mlijeko",
    "priceEur": 1.4,
    "description": "Svježe mlijeko, 1L",
    "isAvailable": true
}'
```

- Get product

```
  curl --location 'http://localhost:8080/products/1'
```

- Get product

```
curl --location --request DELETE 'http://localhost:8080/products/1'
```

- Update product

```
curl --location --request PUT 'http://localhost:8080/products' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "code": "0987654321",
    "name": "Mlijeko",
    "priceEur": 1.6,
    "description": "Svježe mlijeko, 1L",
    "isAvailable": false
}'```