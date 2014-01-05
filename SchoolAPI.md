HOST: https://schoolapi.apiary.io/

# SchoolAPI
School API is a *simple API* that allow to manage students and lessons. Students can enroll in lessons.

# Group Students
Students related resources of the **School API**

## Students Collection [/students]
### List all Students [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1, 
                "name": "Joe Smith"
            },
            {
                "id": 2, 
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
                "name": "Aurelien Thevoz"
            }

### Remove a Student [DELETE]
+ Response 200

        Deleted 3


# Group Lessons
Lessons related resources of the **School API**

## Lessons Collection [/lessons]
### List all lessons [GET]
+ Response 200 (application/json)

        [
            {
                "id": 1,
                "students": 
                [
                    {
                        "id": 2, 
                        "name": "Komanda Phanzu"
                    }
                ],
                "name": "OSF"
            },
            {
                "id": 2,
                "name": "NSA",
                "students": []
            }
        ]

### Create a lesson [POST]
+ Request (application/json)

        {
            "name": "CorpCom"
        }

+ Response 201 (application/json)

        {
            "id": 3, 
            "name": "CorpCom",
            "students": []
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
                "name": "NSA",
                "students": []
            }

### Update a Lesson, used primarly to add students in a lesson [PUT]
+ Request (application/json)

        {
             "name": "NSA",
				"students": 
            [
                {
                    "id": 3
                }
            ]
        }
        
+ Response 200 (application/json)

        {
            "id": 2, 
            "name": "NSA",
            "students": 
            [
                {
                    "id": 3, 
                    "name": "Aurelien Thevoz"
                }
            ]
        }

### Remove a Lesson [DELETE]
+ Response 200

        Deleted 2
 
