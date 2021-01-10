
# QuizMania API Dokumentation

Quizmanias API ger tillgång till quiz om flertalet kategorier relaterade till systemutveckling och informationsarkitektur, samt video-id:n för att enkelt kunna använda till inbäddade Youtube-videor kring samma kategori. API:et följer RESTs principer genom att anrop till ändpunkten /api/categories returnerar de tillgängliga kategorierna. Anrop till en kategori-ändpunkt ger sedan frågor med tillhörande svarsalternativ, booleans kring huruvida svaren är sanna eller falska samt video-id:n.

## Headers

HTTP-headern Accept: application/json kan användas. Returdatan representeras i JSON.

## GET /api/categories

Returnerar de kategorier som API:et erbjuder. 
Exempel på returdata:


```json
[
    {
        "description": "Returns quiz data for the html category.",
        "category": "html",
        "url": "http://localhost:4567/api/categories/html"
    },
    {
        "description": "Returns quiz data for the javascript category.",
        "category": "javascript",
        "url": "http://localhost:4567/api/categories/javascript"
    },
    {
        "description": "Returns quiz data for the php category.",
        "category": "php",
        "url": "http://localhost:4567/api/categories/php"
    },
    {
        "description": "Returns quiz data for the wordpress category.",
        "category": "wordpress",
        "url": "http://localhost:4567/api/categories/wordpress"
    }
]
```

## GET /api/categories/{category}

En viss frågekategori nås med hjälp av ett kategorinamn. De skrivs nedan som {category}. Ersätt därför hela {category} med kategorinamnet. Exempel på kategorinamn: html.
Ger en lista med frågor, svarsalternativ, booleans kring huruvida svaren är sanna eller falska samt video-id:n. 

För returdatan nedan har kategoriparametern html använts och parametern nbrOfQuestions har satts till 2.
Exempel på returdata:




```json
{
    "questions": [
        {
            "question": "What is the correct HTML element for playing video files?",
            "answers": [
                {
                    "text": "<movie>",
                    "correct": false
                },
                {
                    "text": "<media>",
                    "correct": false
                },
                {
                    "text": "<type=\"video\">",
                    "correct": false
                },
                {
                    "text": "<video>",
                    "correct": true
                }
            ]
        },
        {
            "question": "Tags and text that are not directly displayed on the page are written in _____ section.",
            "answers": [
                {
                    "text": "<html>",
                    "correct": false
                },
                {
                    "text": "<body>",
                    "correct": false
                },
                {
                    "text": "<title>",
                    "correct": false
                },
                {
                    "text": "<head>",
                    "correct": true
                }
            ]
        }
    ],
    "videoLinks": [
        "pQN-pnXPaVg",
        "bWPMSSsVdPk",
        "UB1O30fR-EE",
        "-G7bJVAIiEI",
        "PlxWf493en4"
    ]
}
```

## Parametrar

- nbrOfQuestions. Bestämmer hur många quizfrågor som returneras. Kan vara mellan 1-20. Om inget anges så returneras 20 frågor. 
- nbrOfVideos. Bestämmer hur många video-id:n som returneras. Kan vara mellan 1-20. Om inget anges så returneras 5 video-id:n.

Exempel: http://localhost:4567/api/categories/html?nbrOfQuestions=3&nbrOfVideos=2


## Felhantering

### 404

"404 Not Found" returneras om en resurs som ej finns specificerad i /api/categories anropas.

### 503

"503 Service Unavailable" returneras om servern inte kan hämta nödvändig data från externa API:er och därmed ej kan generera ett korrekt svar på anropet.

