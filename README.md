# authorization-server

Authorization Server based on protocol oauth2. Microservice for betting-app project - https://github.com/kurzelakamil/betting-app

* port 7070
* inMemory JWT tokens, signed by symmetric key 
* postgresql database - to store users and roles data 
* based on authoriation_code flow
* act as resource server retrieving user-info
* liquibase
