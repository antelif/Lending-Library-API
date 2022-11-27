# Lending-Library-API

___

#### Lending library application implemented in Java, using Spring framework. <br/>Contains common operations needed in a lending library such as manipulation of books and their copies, or creating and finalizing transaction with customers, etc.* <br/><br/>HTTP requests are used for each operation and data is saved in database.

#####                     * a detailed list of available operations provided below in `Operations` section.

## Technologies

___

- Java 17
- Spring Boot
- Spring Security
- Liquibase
- PostgreSQL

## Operations

___
The personnel user has a variety of operations they can perform. They are responsible for customer
and transaction creation, handling fee repays and adding new books and their copies in the database.
Below they are sectioned by the object of interest for each operation:

- <b>Author:</b>  Contains information about the author of a book.
    - Add new authors
- <b>Book: </b> Contains information about the book, such as title, isbn, author, etc.
    - Add new book
    - Get all books from database
    - Get a book by their given id.
- <b>Book Copies:</b> Each book has some copies for lending. Customers cannot borrow the books
  directly, rather a list of book copies.
    - Add new book copies.
- <b>Customer:</b> The customer that will borrow books.
    - Add new customer
    - Get a customer by their id
    - Update the customer fee - repay fee.
- <b>Personnel:</b> The personnel responsible for transactions.
    - Add new personnel
- <b>Publisher</b>
    - Add new publisher
    - Get all publishers
    - Get a publisher by their id
- <b>Transactions: </b> The process of lending a list of books to a customer.
    - Create new transaction.
    - Update or/and finalize existing transaction
    - Cancel existing transaction.

## How to run

___
The structure of the API can be viewed in Swagger. All requests are sectioned by the object of
interest for each operation.

- When running in an IDE you can access Swagger at `localhost:8080`.
- Pending update for running in a docker container.
- Postgres database is required.

After adding spring security you need to authenticate your user. By default, there is a root user
available you can use, with credentials:

```
username: root
password: root
``` 

In order to be able to create new transactions: lending books, return books etc. you will need to
initialize the database with some data.
At this point there is no script to preload data, however it will be implemented in the future.

---

1. If you are using swagger for requests you can log in at `library/login` using credentials.
2. If you are using postman for requests when sending each request, add basic authentication using
   credentials for all requests.

### Author:

1. <b>Add new author:</b>
   <br/><b>URL:</b> POST request at `/library/authors`.
   <br/><b>Request body:</b>
    ```json
    {
        "middleName": "string",
        "name": "string",
        "surname": "string"
    }
    ```
   The `middleName` field is optional.
   <br/><b>Expected response:</b>

    ```json
    {
        "id": 0,
        "middleName": "string",
        "name": "string",
        "surname": "string"
    }
    ```
   This `id` is referred as `authorId` in this document, and it will be needed to create new books.

### Publisher

1. <b>Add new publisher:</b>
   <br/><b>URL:</b> POST request at `/library/publishers`.
   <br/><b>Request body:</b>
   ```json
   {
     "name": "string"
   }
   ```
   <br/><b>Expected response:</b>
   ```json
   {
       "id": 0,
       "name": "string"
   }
   ```
   This `id` is referred as `publisherId` in this document, and it will be needed to create new
   books.
2. <b>Get all publisher:</b>
   <br/><b>URL:</b> GET request at `/library/publishers`.
   <br/><b>Request body:</b> none
   <br/><b>Expected response:</b>
   ```json
   [
       {
           "id": 0,
           "name": "string"
       }
   ]
   ```
3. <b>Get a publisher by their id:</b>
   <br/><b>URL:</b> GET request at `/library/publishers/{publisherId}`.
   <br/><b>Request body:</b> none
   <br/><b>Expected response:</b>
   ```json
   {
     "id": 0,
     "name": "string"
   }
   ```

### Book:

In order to add new books you need to have their author and publisher already in database. After
following steps shown above to add an author and a publisher you can create a new book.

1. <b>Add new book:</b>
   <br/><b>URL:</b> POST request at `library/books`.
   <br/><b>Request body:</b>
   ```json
   {
   "authorId": 0,
   "isbn": "string",
   "publisherId": 0,
   "title": "string"
   }
   ```
   <br/><b>Expected response:</b>
   ```json
   {
       "author": {
         "id": 0,
         "middleName": "string",
         "name": "string",
         "surname": "string"
       },
       "id": 0,
       "isbn": "string",
       "publisher": {
         "id": 0,
         "name": "string"
       },
       "title": "string"
    }
   ```
   Now you have successfully added a book in database.

2. <b>Get all books from database:</b>
   <br/><b>URL:</b> GET request at `/library/books`.
   <br/><b>Request body:</b> none
   <br/><b>Expected response:</b>
   ```json
   [
     {
       "author": {
         "id": 0,
         "middleName": "string",
         "name": "string",
         "surname": "string"
       },
       "id": 0,
       "isbn": "string",
       "publisher": {
         "id": 0,
         "name": "string"
       },
       "title": "string"
     }
   ]
   ```

3. <b>Get a book from database by id:</b>
   <br/><b>URL:</b> GET request at `/library/books/{bookId}`.
   <br/><b>Request body:</b> none
   <br/><b>Expected response:</b>
   ```json
   {
     "author": {
       "id": 0,
       "middleName": "string",
       "name": "string",
       "surname": "string"
     },
     "id": 0,
     "isbn": "string",
     "publisher": {
       "id": 0,
       "name": "string"
     },
     "title": "string"
   }
   ```

### Book Copy

1. <b>Add new book copy:</b>
   <br/><b>URL:</b> POST request at `library/copies`.
   <br/><b>Request body:</b>
   ```json
   {
   "isbn": "string",
   "state": "NEW",
   "status": "AVAILABLE"
   }
   ```
    - `status` is optional. if not provided it is set to `AVAILABLE`.
    - `state` should contain one of the following values [`NEW`, `GOOD`,`BAD`].

   <br/><b>Expected response:</b>
   ```json
   {
       "book": {
         "author": {
           "id": 0,
           "middleName": "string",
           "name": "string",
           "surname": "string"
         },
         "id": 0,
         "isbn": "string",
         "publisher": {
           "id": 0,
           "name": "string"
         },
         "title": "string"
       },
       "id": 0,
       "state": "BAD",
       "status": "AVAILABLE"
   }
   ```
   This `id` is referred as `bookCopyId` or `copyId` in this document, and it will be needed to
   create new transactions.

### Personnel

1. <b>Add new personnel:</b>
   <br/><b>URL:</b> POST request at `library/personnel`.
   <br/><b>Request body:</b>
   ```json
   {
     "name": "string",
     "password": "string",
     "role": "ADMIN"
   }
   ```
    - `password` will be hashed in database.
    - `role` should contain one of the following values [`ADMIN`], if not provided it is set
      to `ADMIN`.

   <br/><b>Expected response:</b>
   ```json
   {
       "id": 0,
       "name": "string",
       "role": "ADMIN"
   }
   ```
   This `id` is referred as `personnel` in this document, and it will be needed to create new
   transactions.

### Customer

1. <b>Add new personnel:</b>
   <br/><b>URL:</b> POST request at `library/customers`.
   <br/><b>Request body:</b>
   ```json
   {
     "email": "string",
     "fee": 0,
     "name": "string",
     "phoneNo": "string",
     "surname": "string"
   }
   ```
   <br/><b>Expected response:</b>
   ```json
   {
       "email": "string",
       "fee": 0,
       "id": 0,
       "name": "string",
       "phoneNo": "string",
       "surname": "string"
   }
   ```
   This `id` is referred as `customerId` in this document, and it will be needed to create and
   update
   new transactions.
2. <b>Get customer by id:</b>
   <br/><b>URL:</b> GET request at `library/customers/{customerId}`.
   <br/><b>Expected response:</b>
   ```json
   {
       "email": "string",
       "fee": 0,
       "id": 0,
       "name": "string",
       "phoneNo": "string",
       "surname": "string"
   }
   ```
3. <b>Update customer fee:</b>
   <br/><b>URL:</b> PATCH request at `library/customers/{customerId}`.
   <br/><b>Request body:</b>
   ``` 
   0.0
   ```
   <br/><b>Expected response:</b>
   ```json
   {
       "email": "string",
       "fee": 0,
       "id": 0,
       "name": "string",
       "phoneNo": "string",
       "surname": "string"
   }
   ```

### Transaction:

In order to create new transactions you need to have previously added at least one book and a book
copy for this book, and have created a customer and a personnel.

1. <b>Create new transaction:</b>
   <br/><b>URL:</b> POST request at `library/transactions`.
   <br/><b>Expected response:</b>
   ```json
   {
     "copyIds": [
       0
     ],
     "customerId": 0,
     "daysUntilReturn": 0,
     "personnelId": 0,
     "status": "ACTIVE"
   }
   ```
    - `daysUntilReturn` is optional, if not provided it is set to `5`.
    - `status` is optional. if not provided it is set to `ACTIVE`.
      This `id` is referred as `transactionId` in this document, and it will be needed to update and
      finalize the transaction.

   <br/><b>Expected response:</b>
   ```json
   {
       "books": [
         {
           "book": {
             "author": {
               "id": 0,
               "middleName": "string",
               "name": "string",
               "surname": "string"
             },
             "id": 0,
             "isbn": "string",
             "publisher": {
               "id": 0,
               "name": "string"
             },
             "title": "string"
           },
           "id": 0,
           "state": "BAD",
           "status": "AVAILABLE"
         }
       ],
       "creationDate": "2022-10-08T12:26:01.415Z",
       "customer": {
         "email": "string",
         "fee": 0,
         "id": 0,
         "name": "string",
         "phoneNo": "string",
         "surname": "string"
       },
       "id": 0,
       "personnel": {
         "id": 0,
         "username": "string"
       },
       "returnDate": "2022-10-08T12:26:01.415Z",
       "status": "ACTIVE"
   }
   ```
   Now you have successfully created a new transaction in database.

2. <b>Return books for an active transaction:</b>
   <br/><b>URL:</b> PATCH request at `/library/transactions/customer/{customerId}.`
   <b>Request body:</b>
   ```json
   [
     0
   ]
   ```
   <br/><b>Expected response:</b>
   ```json
   [
       {
         "books": [
           {
             "book": {
               "author": {
                 "id": 0,
                 "middleName": "string",
                 "name": "string",
                 "surname": "string"
               },
               "id": 0,
               "isbn": "string",
               "publisher": {
                 "id": 0,
                 "name": "string"
               },
               "title": "string"
             },
             "id": 0,
             "state": "BAD",
             "status": "AVAILABLE"
           }
         ],
         "creationDate": "2022-10-08T12:33:15.927Z",
         "customer": {
           "email": "string",
           "fee": 0,
           "id": 0,
           "name": "string",
           "phoneNo": "string",
           "surname": "string"
         },
         "id": 0,
         "personnel": {
           "id": 0,
           "username": "string"
         },
         "returnDate": "2022-10-08T12:33:15.927Z",
         "status": "ACTIVE"
       }
   ]
   ```
    - All books returned now should have their statues as `AVAILABLE`.
    - All transactions that have all books returned should have their status as `FINALIZED`.

3. <b>Cancel a transaction:</b>
   <br/><b>URL:</b> PATCH request at `/library/cancel/transaction/{transactionId}.`
   <br/><b>Request body:</b> none
   <br/>In order to cancel a transaction, the transaction:
    - should not be 'FINALIZED'.
    - should not contain returned books. Returned books are all books of a transaction that
      their `status` is `AVAILABLE`.

   <br/><b>Expected response:</b>
      ```json
      {
          "books": [
            {
              "book": {
                "author": {
                  "id": 0,
                  "middleName": "string",
                  "name": "string",
                  "surname": "string"
                },
                "id": 0,
                "isbn": "string",
                "publisher": {
                  "id": 0,
                  "name": "string"
                },
                "title": "string"
              },
              "id": 0,
              "state": "BAD",
              "status": "AVAILABLE"
            }
          ],
          "creationDate": "2022-10-08T12:40:53.969Z",
          "customer": {
            "email": "string",
            "fee": 0,
            "id": 0,
            "name": "string",
            "phoneNo": "string",
            "surname": "string"
          },
          "id": 0,
          "personnel": {
            "id": 0,
            "username": "string"
          },
          "returnDate": "2022-10-08T12:40:53.969Z",
          "status": "CANCELLED"
      }
      ```

## Validations and Requirements.

___

### Functional Validations

In order to do anything you need to log in using basic authentication.

#### Book

1. In order to create a new book you need to have a publisher and an author persisted in database.
2. The book ISBN should be unique for each book.

#### Transaction Creation - Borrow books

1. In order to create a transaction you need to have a customer, a personnel, a book, and a book
   copy persisted in database.
2. When creating a new transaction:
    - All book copies provided should exist in database.
    - Customer should not have a pending fee.
    - Customer cannot borrow the same book when he haas previously borrowed this book and has not
      returned it yet - (See `Known Issues` 1).
    - Customer cannot borrow a book of state `BAD`.
    - Customer cannot borrow the same book twice in the same transaction - (See `Known Issues` 2).
    - Customer cannot borrow a book copy of BAD state or LENT status.

#### Transaction Update - Return books

When returning book by sending a patch transaction:

- Book copies provided should exist in an `ACTIVE` transactions of the customer.
- Book copies should have status `LENT` in order to be returned.

#### Transaction Cancellation

- The transaction cannot be already finalized.
- The transaction cannot contain returned books.
  Essentially this happens because a transaction is to be cancelled on the moment of creation. Else
  the customer should return the books and the transaction instead of cancelled should be '
  FINALIZED'.

#### Customer Fee Update

- The return fee cannot be negative.
- The return fee cannot be greater than the fee owed.

### Validations for request bodies:

1. <b>Create new author:</b>
    ```
    name: String - not blank - max 50 characters
    middleName: String - not required - max 50 characters
    surname: String - not blank - max 50 characters
    ```
2. <b>Create new book copy:<b/>
    ```
    isbn: String - not blank - to be added ISBN validation
    state: String - not null - available values: [NEW,  GOOD, BAD]
    status: String - available values [AVAILABLE, LENT], if value is not provided ‘AVAILABLE’ is default.
    ```
3. <b>Create a new book:</b>
    ```
    title: String - not blank - max 50 characters
    isbn: String - not blank to be added ISB validation
    authorId: Long - not null
    publisherId: Long - not null
    ```
4. <b>Create a new customer:</b>
    ```
    name: String - not blank - max 50 characters
    surname: String - not blank - max 50 characters
    phoneNo: String - only digits allowed - between 10 and 15 characters
    email: String - must be of email format somethin@something.com
    fee: double, by default 0.
    ```
5. <b>Create new personnel:</b>
    ```
    username: String - not blank - max 20 characters
    password: String - not null
    role: available values [ADMIN], if value is not provided ‘ADMIN’ is default.
    ```

6. <b>Create new publisher:</b>
    ```
    name: String - not null
    ```

7. <b>Create new transaction:</b>
   ```
   daysUntilReturn: Integer between 1 and 7, if not provided 5 is default.
   customerId: Long -not null
   personnelId: Long - not null
   status: String, available values [ACTIVE, RETURNED], if not provided ACTIVE is default
   copyIds: List of Long, not empty.
   ```

## Known Issues

___

1. ~~(2022-10-08) Customer cannot borrow a book copy that is contained in an active transaction,
   event
   though thy have returned it.~~
2. ~~(2022-10-08) Customer can borrow more than one copies of the same book in the same
   transaction.~~

## Ideas for the future.

___

1. Create endpoint to update book copy status.
2. ~~Personnel log in.~~
3. ~~Password encryption.~~
4. Create script to initialize test data in database.
5. Add ISBN validations.
6. ~~Authorize requests to be performed only by authenticated personnel.~~










