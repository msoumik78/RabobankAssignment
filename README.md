<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>

  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This small project is about customer's accounts' access management (or "Power of Attorney" management) abilities and hence exposes the below REST end points :
1. A GET endpoint at /manage-acccess which retrieves the list of accounts for which a grantee/POA holder has access/authorizations
2. A POST endpoint at the same URI to provide grantees the ability to add read / write authorization to specific accounts

This project has been built using Spring Boot framework and uses MongoDB (embedded) for storing account and authorization related info

### Built With

* [SpringBoot](https://spring.io/projects/spring-boot)
* [MongoDB](https://www.mongodb.com/)



<!-- GETTING STARTED -->
## Getting Started


### Prerequisites

1. Java 11 should be already installed in the system and JAVA_HOME environment variable is set to refer to JDK 11
2. At least Maven 3.5.2 should be already installed in the system used for running the maven commands (which means MAVEN_HOME/bin is set in PATH environment variable)

### Installation

1. Clone the repo
2. Open the file data/src/main/resources/logback-spring.xml
and change the value of the property named 'LOGS' to point to an existing folder/ directory in your system. 
3. Build the application using the command :
   ```JS
   mvn clean install
   ```
4. Navigate to the folder 'api'
5. Execute the below command to start the spring boot app:
   ```JS
   mvn spring-boot:run
   ```

<!-- USAGE EXAMPLES -->
## Usage

Once the application is up and running fine, the endpoints can be accessed in either of the folliowing ways:
1. Using swagger by navigating to the URL : http://localhost:8080/swagger-ui.html
2. Using CURL from the command line :  

```JS
curl -X GET "http://localhost:8080/account-access?granteeName=Grantee2" -H  "accept: */*"
```

```JS
curl -X POST "http://localhost:8080/account-access" -H  "accept: */*" -H  "Content-Type: application/json" -d "{\"accountNumber\":\"12345\",\"granteeName\":\"Grantee2\",\"authType\":\"R\"}"
```
