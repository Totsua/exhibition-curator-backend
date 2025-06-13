# Exhibition Curator Backend API
# About the project
This  API allows users to explore artworks from public museum APIs and curate their own virtual exhibitions. Users can search for artworks, retrieve artwork details, and create, update, and manage exhibitions containing selected pieces.
# Dependencies
- Java - JDK 21

- Spring Boot 3.5.0

- Spring Boot Web

- Spring Test

- Spring Validation

- Spring Security

- Spring JPA

- MySQL Driver

- Lombok

- H2 Database

# Frontend Technology
For the Android frontend, please see: [repo](https://github.com/Totsua/exhibition-curator-android)

<p align="right">(<a href="#exhibition-curator-backend-api">back to top</a>)</p>

# API Overview
### Base Url: '/api/v1/exhibitioncurator'
#### 
- The server is hosted on 8080 by default unless changed.
    - Therefore, a full url would be `localhost:8080/api/v1/exhibitioncurator/`
## Artwork Endpoints
### `GET /random`
Returns a random artwork from the Metropolitan Museum of Art Collection API.

**Response Example**
```json
{
  "id": 362330,
  "title": "The Terra-Cotta Guide to Chicago",
  "description": "No Description Provided From The Metropolitan Museum",
  "altText": "Artwork from the MET: The Terra-Cotta Guide to Chicago",
  "apiOrigin": "The MET",
  "imageUrl": "https://images.metmuseum.org/CRDImages/dp/original/DP-29909-001.jpg",
  "artist": {
    "apiID": 91052,
    "name": "G. H. H."
  }
}
   ```
### `GET /search?query={query}&page={page}`
Searches for artworks with query and paginated page number.


Searches the 'Chicago Institute Of Art' API.

- Parameters required:
    - `query`: Search term (eg: "Monet")
    - `page`:  Page number (integer)

**Response Example**
```jsonc
{
   "query": "monet",
   "page": 1,
   "artworks": [
      {
         "id": 16568,
         "title": "Water Lilies",
         "description": "There is no description for this artpiece",
         "altText": "Painting of a pond seen up close spotted with thickly painted pink and white water lilies and a shadow across the top third of the picture.",
         "apiOrigin": "Chicago Institute",
         "imageUrl": "https://www.artic.edu/iiif/2/3c27b499-af56-f0d5-93b5-a7f2f1ad5813/full/843,/0/default.jpg",
         "artist": {
            "apiID": 35809,
            "name": "Claude Monet"
         }
      },
     // ...  rest of the results
      
   ],
   "total_pages": 32
}
   ```

### `POST /search`
Returns a detailed artwork.
- Requested Body:
```jsonc
{
   "artId": 129884,
   "apiOrigin":"Chicago Institute"
}
  ```

**Response Example**
```jsonc
{
   "id": 129884,
   "title": "Starry Night and the Astronauts",
   "description": "Alma Thomas was enthralled by astronauts and outer space. This painting, made when she was 81, showcases that fascination through her signature style of short, rhythmic strokes of paint. “Color is life, and light is the mother of color,” she once proclaimed. In 1972, she became the first African American woman to have a solo exhibition at the Whitney Museum of American Art in New York.",
   "altText": "Abstract painting composed of small vertical dabs of multiple shades of blue with a small area of similar strokes of red, orange, and yellow in the upper right.",
   "apiOrigin": "Chicago Institute",
   "imageUrl": "https://www.artic.edu/iiif/2/e966799b-97ee-1cc6-bd2f-a94b4b8bb8f9/full/843,/0/default.jpg",
   "artist": {
      "apiID": 44708,
      "name": "Alma Thomas"
   }
}
   ```
<p align="right">(<a href="#exhibition-curator-backend-api">back to top</a>)</p>

## Exhibitions Endpoints
### `POST /exhibitions/create`
Creates a new exhibition and returns it.

**Request Body**
```jsonc
{
   "title": "Example 30"
}
```
**Response Example**
```jsonc
{
   "id": 1,
   "title": "Example 30",
   "description": "",
   "artworks": []
}
```
### `GET /exhibitions`
Returns a list of all saved exhibitions

**Response Example**

```jsonc
[
    {
        "id": 1,
        "title": "Example 30",
        "description": "",
        "artworks": [
           // Array of Artworks
        ]
    }
   // ... rest of the exhibitions
]
```
### `GET /exhibitions/{id}`
Returns a specified exhibition

**Response Example**

```jsonc
[
    {
        "id": 1,
        "title": "Example 30",
        "description": "",
        "artworks": [
           /* Array of Artworks*/
        ]
    }
]
```

### `PATCH /exhibitions/{id}`
Updates exhibition metadata

**Request Body**

```jsonc
{
   "title": "Updated Title",
   "description": "Updated Description (optional)"
}
```

**Response Example**

```jsonc
[
    {
        "id": 1,
        "title": "Updated Title",
        "description": "",
        "artworks": [
           // Array of Artworks
        ]
    }
]
```
### `Delete /exhibitions/{id}`
Deletes an exhibition.

<p align="right">(<a href="#exhibition-curator-backend-api">back to top</a>)</p>

## Exhibition-Artwork Endpoints
### `POST /exhibitions/{id}/artworks`
Adds an artwork to the specified exhibition.

**Request Body**
```jsonc
{
   "artId": 12345,
   "apiOrigin": "Chicago Institute"
}
```

**Response Example**
```jsonc
[
    {
        "id": 1,
        "title": "Example 30",
        "description": "",
        "artworks": [
           // Array of Artworks
        ]
    }
]
```

### `DELETE /exhibitions/{id}/artworks`
Removes an artwork from the specified exhibition.

**Request Body**
```jsonc
{
   "artId": 12345,
   "apiOrigin": "Chicago Institute"
}
```

**Response Example**
```jsonc
[
    {
        "id": 1,
        "title": "Example 30",
        "description": "",
        "artworks": [
            // Array of Artworks
        ]
    }
]
```
<p align="right">(<a href="#exhibition-curator-backend-api">back to top</a>)</p>

## Object Summary
### `ArtworkDTO`
Represents an artwork.

| Field       | Type        | Description                    |
|-------------|-------------|--------------------------------|
| id          | Long        | Artwork ID                     |
| title       | String      | Title of the artwork           |
| description | String      | Artwork description            |
| altText     | String      | Alt text for accessibility     |
| apiOrigin   | String      | Originating API (e.g: THE MET) |
| imageUrl    | String      | Link to artwork image          |
| artist      | ArtistDTO   | Artist information             |

### `Exhibition`
Represents an exhibition with its associated artworks.

| Field       | Type              | Description                      |
|-------------|-------------------|----------------------------------|
| id          | Long              | Exhibition ID                    |
| title       | String            | Exhibition title                 |
| description | String (optional) | Description of the exhibition    |
| artworks    | List (ArtworkDTO) | List of artwork objects          |


## Validation Rules

- **ApiArtworkIdDTO**
    - `artId`: Must not be null and must be greater than or equal to 0.
    - `apiOrigin`: Required and must not be blank.


- **ExhibitionCreateDTO**
    - `title`: Must be between 1 and 80 characters.


- **ExhibitionPatchDTO**
    - `title`: Optional, if provided must be 1–80 characters.
    - `description`: Optional, max 500 characters.

<p align="right">(<a href="#exhibition-curator-backend-api">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may set up the project locally.
To get a local copy up and running follow these simple steps.

### Prerequisites

* Install [MySQL](https://dev.mysql.com/downloads/installer/)
* Create a server on MySQL with a named database


### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/Totsua/exhibition-curator-backend.git
   ```
2. Install NPM packages
   ```sh
   npm install
   ```
3. Enter your credentials in `application-prod.properties`
   ```js
    spring.datasource.url=jdbc:mysql://localhost:3306/ ** enter database name **
    spring.datasource.username= ** enter server username **
    spring.datasource.password= ** enter password **
    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update
   ```
   
4. Enter your email in `ApiServiceImpl - sendGetRequest method`
   ```js
   uncomment and add your email
   // .header("AIC-User-Agent","exhibition-curator (*Insert Email*)")
   ```
5. Change git remote url to avoid accidental pushes to base project
   ```sh
   git remote set-url origin github_username/repo_name
   git remote -v # confirm the changes
   ```

<p align="right">(<a href="#exhibition-curator-backend-api">back to top</a>)</p>