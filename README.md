# Prerequisites

Java 1.8
Maven installed

### Starting the server

1. Clone the codebase
2. Navigate to the directory cartAPIs
3. `mvn clean install -DskipTests`
3. `mvn spring-boot:run`

### Swagger UI
`http://localhost:8081/swagger-ui.html`

### Testing the APIs

1. Navigate to the directory cartAPIs
2. `mvn clean test`

The above command will run both Junit and Cucumber tests, so the server should be running for cucumber tests to pass

### Testing APIs from Postman

POST - http://localhost:8081/cart
Optional Cart body - In this first step, the endpoint creates a new empty Cart

PATCH
Copy the id from the previous response
Hit the below PATCH end point to update the cart with products
http://localhost:8081/cart/{id}

`[
    {
        "op": "add",
        "path": "/cartItems/0",
        "value": {
            "product": {
            "price": 39.99,
            "name": "Dove Soap"
            },
            "quantity": 5
        }
    }
]`

GET
Copy the id from the POST call that created the cart and hit the below end point
`http://localhost:8081/cart/{id}`
