# GraphQL in Java using SPQR

This is the tutorial Spring Boot application to develop GraphQL API using [SPQR](https://github.com/leangen/graphql-spqr-spring-boot-starter) and Spring Data JPA using hierarchical datamodel shown below.  
The application can be started using `mvn spring-boot:run` and access the **GraphQL PlayGround** using [http://localhost:8083/gui](http://localhost:8083/gui) and
use the [payloads](src/main/input-payloads) specified for various Query and Mutation methods.  Use the following [blog post](https://medium.com/@gdprao/graphql-with-spqr-and-spring-data-jpa-973e50746ad5) for more details.

## Data Model

![DataModel](src/main/screenshots/DepartmentDataModelDiagram.png?raw=true)

## Get Department details

![GetDepartmentDetail](src/main/screenshots/Get-Department.png?raw=true)