HOST: https://app.apiary.io/schoolapi

# SchoolAPI
School API is a *simple API* that allow to manage students and courses.

# Group Students
Students related resources of the **School API**

## Students Collection [/students]
### List all Students and courses where they are registred [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1, "firstName": "Joe", "lastName": "Smith"
            },
            {
                "id": 2, "firstName": "Yann", "lastName": "Andrey"
            }
        ]

### Create a Student [POST]
+ Request (application/json)

        {
            "firstName": "Paul", "lastName": "Wlker"
        }

+ Response 201 (application/json)

        {
            "id": 3, "firstName": "Paul", "lastName": "Wlker"
        }

## Student [/students/{id}]
A single Student object with all its courses and details

+ Parameters
    + id (required, number, `3`) ... Numeric `id` of the Student to perform action with. Has example value.

### Retrieve a Student [GET]
+ Response 200 (application/json)
    + Body

            {
                "id": 2, "firstName": "Paul", "lastName": "Wlker", "courses":
                [
                    {
                        "id": "1",
                        "Name": "Math"
                    },
                    {
                        "id": "3",
                        "Name": "History"
                    }
                ]
            }

### Update a Student [POST]
+ Request (application/json)

            {
                "id": 2, "firstName": "Paul", "lastName": "Walker"
            }

+ Response 200 (application/json)
    + Body
    
            {
                "id": 2, "firstName": "Paul", "lastName": "Walker"
            }

### Remove a Student [DELETE]
+ Response 204


# Group Courses
Courses related resources of the **School API**

## Courses Collection [/courses]
### List all Courses where they are registred [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1, "Name": "Math"
            },
            {
                "id": 2, "Name": "History"
            }
        ]

### Create a Course [POST]
+ Request (application/json)

        {
            "Name": "Chemistry"
        }

+ Response 201 (application/json)

        {
            "id": 3, "Name": "Chemistry"
        }

## Courses [/courses/{id}]
A single Course object with all his registred students

+ Parameters
    + id (required, number, `1`) ... Numeric `id` of the Course to perform action with. Has example value.

### Retrieve a Course [GET]
+ Response 200 (application/json)
    + Body

            {
                "id": 1, "Name": "Math", "students":
                [
                    {
                        "id": "1",
                        "firstName": "Joe",
                        "lastName": "Smith"
                    },
                    {
                        "id": "2",
                        "fistName": "Yann",
                        "lastName": "Andrey"
                    }
                ]
            }

### Update a Course, used primarily to register students in the course [POST]
+ Request (application/json)

        {
            "id": 3, "students":
            [
                1,
                3
            ]
        }

+ Response 201 (application/json)

        {
            "id": 3, "Name": "Chemistry", "students":
            [
                {
                    "id": "1",
                    "firstName": "Joe",
                    "lastName": "Smith"
                },
                {
                    "id": "3",
                    "fistName": "Paul",
                    "lastName": "Walker"
                }
            ]
        }


### Remove a Course [DELETE]
+ Response 204
