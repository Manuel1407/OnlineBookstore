# Simple Online Bookstore API

**Requirements**
You need the following to build and run the application:

1. JDK 17
2. Maven 3.8.1 (Optional as code already contains maven wrapper)

## Setup Instructions

1. Clone the repository 
2. MySqlWorkBench must be running locally
3. Navigate to the project directory
4. Run `mvn clean install` to build the project
5. Run `mvn spring-boot:run` to start the application

## API Endpoints

- `GET http://localhost:4040/api/v1/books` - View a list of available books
- `POST http://localhost:4040/api/v1/addBook` - Add new books to the store.
- `PUT http://localhost:4040/api/v1//update/{bookId}}` - Update book details
- `DELETE http://localhost:4040/api/v1//delete/{bookId}` - Delete books from the store.

## Testing the API endpoints with various inputs

**EndPoints**

1. View a list of available books 
    http://localhost:4040/api/v1/books
    Request: {}
    Response: {
    "totalPages": 1,
    "totalElements": 1,
    "size": 10,
    "content": [
    {
    "id": 1,
    "title": "Harry Porter",
    "author": "Sam Larry",
    "isbn": "978-0062315007",
    "count": 5,
    "status": "AVAILABLE"
    }
    ],
    "number": 0,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "first": true,
    "last": true,
    "numberOfElements": 1,
    "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "empty": false
    }}
    "statusCodeValue": 200,
    "statusCode": "OK"

2. Add new books to the store
    http://localhost:4040/api/v1/addBook
    Request: {
    "title": "Harry Porter",
    "author": "Sam Larry",
    "isbn": "978-0062315007"
    "count": 5
    }
    Response: {
    "message": "Book added successfully",
    "data": {
    "title": "Harry Porter",
    "author": "Sam Larry",
    "isbn": "978-0062315007",
    "count": 5
    }
    }
    "statusCodeValue": 201,
    "statusCode": "CREATED"

3. Update book details
    http://localhost:4040/api/v1/update/1
    Request: {
    "title": "Harry Porter 6",
    "author": "Sam Larry",
    "isbn": "978-0062315007",
    "count": "2"
    }
    Response: {
    "message": "Updated successfully",
    "data": {
    "title": "Harry Porter 6",
    "author": "Sam Larry",
    "isbn": "978-0062315007",
    "count": "2"
    }
    }
    "statusCodeValue": 200,
    "statusCode": "OK"

4. Delete books from the store
    http://localhost:4040/api/v1/delete/1
    Request: {}
    Response: {
    "message": "Book deleted successfully"
    "data": null
    }  
    "statusCodeValue": 200,
    "statusCode": "OK"

