# Exhibition Curator Backend API

# About the project

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
For the Android frontend, please see: [repo](https://github.com/Totsua/Exhibition-Curator-Android-Frontend) 

# Current Work



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may set up the project locally.
To get a local copy up and running follow these simple steps.

### Prerequisites

* Install [MySQL](https://dev.mysql.com/downloads/installer/)
* Create a server on MySQL with a named database


### Installation

1. Get an API Key at TBA
2. Clone the repo
   ```sh
   git clone https://github.com/Totsua/exhibition-curator-backend.git
   ```
3. Install NPM packages
   ```sh
   npm install
   ```
4. Enter your credentials in `application-prod.properties`
   ```js
    spring.datasource.url=jdbc:mysql://localhost:3306/ ** enter database name **
    spring.datasource.username= ** enter server username **
    spring.datasource.password= ** enter password **
    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update
   ```
5. Change git remote url to avoid accidental pushes to base project
   ```sh
   git remote set-url origin github_username/repo_name
   git remote -v # confirm the changes
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- ROADMAP -->
## Roadmap



<p align="right">(<a href="#readme-top">back to top</a>)</p>