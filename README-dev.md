# Solution -> Domus Back-End Developer Challenge.

The challenge required creating a REST API endpoint to return a list of directors who have directed more movies than a specified threshold. Here's how the problem was approached and solved:

## Key Considerations

1. Movies API interaction:
	I implemented WebClient from Spring WebFlux, which allows for making non-blocking requests in an efficient way.
2. Pagination Handling:
	To get all the necessary information. I made sure to first get the total number of pages and then retrieve each page individually, using Flux.range.
3. Processing the Data:
	Once I had all the movie data, I needed to count how many movies each director had directed, Using a ConcurrentHashMap  and once all pages were processed, I filtered the results based on the provided threshold.
4. Sorting the Results:
	I use a TreeSet() to store the filtered director names.
5. Error Handling:
	I handled runtime, validation, and external API exceptions. Returnnig a consistent ApiError format.
	In some cases I got this error "reactor.netty.http.client.PrematureCloseException: Connection prematurely closed BEFORE response" from the Movie API. To improve resilience against transient connection issues, I added .retry(3) to automatically retry the request up to three times.
6. Testing:
	I used mocks to simulate the external API’s responses, and integration tests were used to verify the API endpoint itself.
7. API Documentation:
	Swagger was integrated into the project.
	```
	http://localhost:8080/webjars/swagger-ui/index.html#/
	```
	
	Collection for Postman:
	```
	{
	"info": {
		"name": "Domus Challenge API",
		"_postman_id": "f9d43c5d-e1b6-4b1b-9c8f-8edc9ccfaa00",
		"description": "API to retrieve directors with more movies than a specific threshold.",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
		"name": "Get Directors Above Threshold",
		"request": {
			"method": "GET",
			"header": [
			{
				"key": "Accept",
				"value": "application/json",
				"type": "text"
			}
			],
			"url": {
			"raw": "http://localhost:8080/api/directors?threshold=4",
			"protocol": "http",
			"host": ["localhost"],
			"port": "8080",
			"path": ["api", "directors"],
			"query": [
				{
				"key": "threshold",
				"value": "4"
				}
			]
			},
			"description": "Returns directors with more than 4 movies, sorted alphabetically."
		},
		"response": []
		}
	]
	}

	```	

# Architecture Overview

This project is a **Reactive Spring Boot API** built using:


- **SOLID Principles**
- **Layered architecture**
- **Spring WebFlux** (non-blocking, asynchronous API)
- **Reactor** for reactive programming (Mono, Flux)
- **WebClient** for non-blocking HTTP client
- **Lombok** for boilerplate code reduction
- **OpenAPI** for API documentation
- **JUnit 5 + Mockito + MockWebServer** for unit testing

# Folder Structure

```	

	src/main/java/com/domus/challenge/
	│
	├── client/            # WebClient interface and implementation
	├── config/            # Swagger & WebClient beans
	├── controller/        # REST controller
	├── dto/               # DTOs for request/response
	├── exception/         # Custom exceptions and global handler
	├── model/             # Movie and MovieResponse model classes
	├── service/           # Business logic (Director filtering)
	└── ChallengeApplication.java
```	