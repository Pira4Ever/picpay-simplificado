[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[POSTGRES_BADGE]: https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white
# Simplified PicPay

![spring][SPRING_BADGE]
![java][JAVA_BADGE]
![mariadb][POSTGRES_BADGE]

This is simplified version of PicPay's back-end built using Java and the Spring framework.

## Features
- Register new users
- Login
- Create transactions

## Tech Stack
- Java
- Spring Boot
- Maven
## Class Diagram (API domain)
```mermaid
classDiagram
    class Transaction {
        long id
        float value
        User payer
        User payee
    }

    class User {
        long id
        string name
        string document
        string email
        string password
        float balance
        string userType
    }

    Transaction --* User : payer
    Transaction --* User : payee
```
## 🚀 Getting Started
### Prerequisites
- Java 17+
- Maven 3.6+
### Running the Project
1. Clone the repository:
    ```bash
   git clone https://github.com/Pira4Ever/picpay-simplificado.git
    ```
2. Navigate to the project directory:
    ```bash
   cd picpay-simplificado
    ```
3. Build and run the project:
    ```bash
   mvn spring-boot:run
    ```
## Usage
After starting the app, you can access it using http://localhost:8080

## 📍 API Endpoints
| route                            | description                                          |                                      
|----------------------------------|------------------------------------------------------|
| <kbd>GET /user</kbd>             | retrieve all created users                           |
| <kbd>GET /user/{id}</kbd>        | retrieve user with specified id                      |
| <kbd>POST /user/register</kbd>   | register a new user and return a JWT token           |
| <kbd>POST /user/login</kbd>      | login and return a JWT token                         |
| <kbd>PUT /user/{id}</kbd>        | update the information of the user with specified id |
| <kbd>DELETE /user/{id}</kbd>     | delete an user by id                                 |
| <kbd>GET /transaction</kbd>      | retrieve all created transactions                    |
| <kbd>GET /transaction/{id}</kbd> | retrieve transaction with specified id               |
| <kbd>POST /transaction</kbd>     | create a transaction                                 |

### GET /user
**RESPONSE**
```json
[
   {
      "id": 1,
      "name": "Google",
      "document": "06947284000104",
      "email": "google@gmail.com",
      "password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
      "balance": 10000000,
      "userType": "MERCHANT"
   },
   {
      "id": 2,
      "name": "Test",
      "document": "12345678909",
      "email": "example@example.com",
      "password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
      "balance": 500.00,
      "userType": "COMMON"
   }
]
```
### GET /user/{id}
**RESPONSE**
```json
{
       "id": 1,
       "name": "Google",
       "document": "06947284000104",
       "email": "google@gmail.com",
       "password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
       "balance": 10000000,
       "userType": "MERCHANT"
}
```
### POST /user/register
**REQUEST**
```json
{
   "name": "Google",
   "document": "06.947.284/0001-04",
   "email": "google@gmail.com",
   "password": "mys3cr3tpassw0rd",
   "balance": 10000000,
   "userType": "MERCHANT"
}
```
**RESPONSE**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwaWNwYXkiLCJzdWIiOiJnb29nbGVAZ21haWwuY29tIiwiZXhwIjoxNzI3NzMwMTU2fQ.4lrQKUaaOpYX8jfSJv1pzFxoi7NmaDO99yV6PmdbICOcYaVC6mOzAgLqwVTljnuaxpIR8TlTQzZlBaahAGB5XA"
}
```
### POST /user/login
**REQUEST**
```json
{
  "email": "google@gmail.com",
  "password": "mys3cr3tpassw0rd"
}
```
**RESPONSE**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwaWNwYXkiLCJzdWIiOiJnb29nbGVAZ21haWwuY29tIiwiZXhwIjoxNzI3NzMwMTU2fQ.4lrQKUaaOpYX8jfSJv1pzFxoi7NmaDO99yV6PmdbICOcYaVC6mOzAgLqwVTljnuaxpIR8TlTQzZlBaahAGB5XA"
}
```
### PUT /user/{id}
**REQUEST**
```json
{
	"id": 1,
	"password": "pass123"
}
```
**RESPONSE**
```json
{
   "name": "Google",
   "document": "06.947.284/0001-04",
   "email": "google@gmail.com",
   "password": "pass123",
   "balance": 10000000,
   "userType": "MERCHANT"
}
```
### DELETE /user/{id}
**REQUEST**
```json
{
   "id": 1
}
```
### GET /transaction
**RESPONSE**
```json
[
   {
      "id": 1,
      "name": "Google",
      "document": "06947284000104",
      "email": "google@gmail.com",
      "password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
      "balance": 10000000,
      "userType": "MERCHANT"
   },
   {
      "id": 2,
      "name": "Test",
      "document": "12345678909",
      "email": "example@example.com",
      "password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
      "balance": 400.00,
      "userType": "COMMON"
   }
]
```
### GET /transaction/{id}
**RESPONSE**
```json
{
	"id": 1,
	"transactionValue": 100.00,
	"payer": {
		"id": 2,
		"name": "Test",
		"document": "12345678909",
		"email": "example@example.com",
		"password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
		"balance": 400.00,
		"userType": "COMMON"
	},
	"payee": {
       "id": 1,
       "name": "Google",
       "document": "06947284000104",
       "email": "google@gmail.com",
       "password": null,
       "balance": null,
       "userType": "MERCHANT"
    },
	"createdAt": "2024-09-30T09:56:03.883737"
}
```
```json
{
	"id": 1,
	"transactionValue": 100.00,
	"payer": {
		"id": 2,
		"name": "Test",
		"document": "12345678909",
		"email": "example@example.com",
		"password": null,
		"balance": null,
		"userType": "COMMON"
	},
	"payee": {
       "id": 1,
       "name": "Google",
       "document": "06947284000104",
       "email": "google@gmail.com",
       "password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
       "balance": 10000000,
       "userType": "MERCHANT"
    },
	"createdAt": "2024-09-30T09:56:03.883737"
}
```
### POST /transaction
**REQUEST**
```json
{
   "transactionValue": 100.00,
   "payerId": 2,
   "payeeId": 1
}
```
**RESPONSE**
```json
{
	"id": 1,
	"transactionValue": 100.00,
	"payer": {
		"id": 2,
		"name": "Test",
		"document": "12345678909",
		"email": "example@example.com",
		"password": "$2a$10$sdrzRgle6HPThulBxymz/OVYlsm4ImJ1XU3ztDxSPkLr//WeTONaa",
		"balance": 400.00,
		"userType": "COMMON"
	},
	"payee": {
       "id": 1,
       "name": "Google",
       "document": "06947284000104",
       "email": "google@gmail.com",
       "password": null,
       "balance": null,
       "userType": "MERCHANT"
    },
	"createdAt": "2024-09-30T09:56:03.883737"
}
```
### OPENAPI
All endpoints were documented with OpenAPI 3.0: https://picpay-simplificado-production.up.railway.app/swagger-ui/index.html
## License
Simplified PicPay is [MIT licensed](./LICENSE)