# Cart APIs

### Prerequisites

* Java 1.8
* Maven installed

### Starting the server

1. Clone the codebase - `git clone https://github.com/vikaskulkarni/cartAPIs.git`
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

POST
<br/> 
http://localhost:8081/cart
<br/>
Optional Cart body - In this first step, the endpoint creates a new empty Cart
<br/>

PATCH - add product
<br/>
Copy the id from the previous response
<br/>
Hit the below PATCH end point to update the cart with products
<br/>
`http://localhost:8081/cart/{id}`
<br/>
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
<br/>

PATCH - add sales tax
<br/>
`http://localhost:8081/cart/c220ab46-b0db-4a43-bb4a-4ec8d0482aeb`
<br/>
`
[
    {
        "op": "replace",
        "path": "/salesTax",
        "value": 12.5
    }
]
`

GET
<br/>
Copy the id from the POST call that created the cart and hit the below end point
<br/>
`http://localhost:8081/cart/{id}`
