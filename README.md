###Purpose
This is a basic template to quickly scaffold a reactive functional REST service that supposed to serve frontend with data.

###What's on board
- Kotlin
- Spring Boot 2+
- Maven
- Spring Data MongoDB Reactive

###How to start
Ensure you have a local instance of MongoDB running. Run the application with profile to quickly generate 1000 random events:
> -Dspring.profiles.active=init-db  

###Some interesting calls:  

Counting all events placed up to 50km in the sphere around Łódź - 27ms on the indexed collection with 1 million of events:

http://localhost:8080/event/count?boundingSphere=52.758299,17.4555023,50000
