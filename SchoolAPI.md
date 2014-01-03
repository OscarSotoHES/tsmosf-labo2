HOST: https://schoolapi.apiary.io/

# SchoolAPI
School API is a *simple API* that allow to manage students and lessons.

# Group Students
Students related resources of the **School API**

## Students Collection [/students]
### List all Students and lessons where they are registred [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1, 
                "lessons": [], 
                "name": "Joe Smith"
            },
            {
                "id": 2, 
                "lessons": 
                [
                    {
                        "id": 1,
                        "studentId": 2, 
                        "lessonId":1
                    }
                ], 
                "name": "Komanda Phanzu"
            }
        ]

### Create a Student [POST]
+ Request (application/json)

        {
            "name": "Aurelien Thevo"
        }

+ Response 201 (application/json)

        {
            "id": 3, 
            "lessons": [], 
            "name": "Aurelien Thevo"
        }

## Student [/students/{id}]
A single Student object with all its lessons and details

+ Parameters
    + id (required, number, `2`) ... Numeric `id` of the Student to perform action with. Has example value.

### Retrieve a Student [GET]
+ Response 200 (application/json)
    + Body

            {
                "id": 2, 
                "lessons": 
                [
                    {
                        "id": 1,
                        "studentId": 2, 
                        "lessonId":1
                    }
                ], 
                "name": "Komanda Phanzu"
            }

### Update a Student [PUT]
+ Request (application/json)

            {
                "name": "Aurelien Thevoz"
            }

+ Response 200 (application/json)
    + Body
    
            {
                "id": 3, 
                "lessons": [], 
                "name": "Aurelien Thevoz"
            }

### Remove a Student [DELETE]
+ Response 200

        Deleted 3


# Group Lessons
Lessons related resources of the **School API**

## Lessons Collection [/lessons]
### List all Lessons where they are registred [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1,
                "students": 
                [
                    {
                        "id": 1, 
                        "studentId": 2, 
                        "lessonId": 1
                    }
                ],
                "name": "OSF"
            },
            {
                "id": 2,
                "students": [],
                "name": "NSA"
            }
        ]

### Create a Lesson [POST]
+ Request (application/json)

        {
            "name": "CorpCom"
        }

+ Response 201 (application/json)

        {
            "id": 3, 
            "students": [],
            "name": "CorpCom"
        }

## Lessons [/lessons/{id}]
A single Lesson object with all his registred students

+ Parameters
    + id (required, number, `2`) ... Numeric `id` of the Lesson to perform action with. Has example value.

### Retrieve a Lesson [GET]
+ Response 200 (application/json)
    + Body
    
            {
                "id": 2,
                "students": [],
                "name": "NSA"
            }

### Update a Lesson, used primarly to add students in a lesson [PUT]
+ Request (application/json)

        {
            "students": 
            [
                {
                    "studentId": 3
                }
            ], 
            "name": "NSA"
        }
        
+ Response 200 (application/json)

        {
            "id": 2, 
            "students": 
            [
                {
                    "id": 4, 
                    "studentId": 3, 
                    "lessonId": 2
                }
            ],
            "name": "NSA"
        }

### Remove a Lesson [DELETE]
+ Response 200

        Deleted 2
 
