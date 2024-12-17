<a id="readme=top"></a>

# Securing API Gateway

<details>
    <summary>Table of Contents</summary>
    <ol>
        <li>
            <a href="#about-the-project">About The Project</a>
        </li>
        <li>
            <a href="#getting-started">Getting Started</a>
            <ul>
                <li><a href="#prerequisites">Prerequisites</a></li>
                <li><a href="#installation">Installation</a></li>
            </ul>
        </li>
        <li><a href="#usage">Usage</a></li>
        <li>
            <a href="#detailing-the-services">Detailing The Services</a>
            <ul>
                <li><a href="#music-service">music-service</a></li>
                <li><a href="#authentication-service">authentication-service</a></li>
                <li><a href="#api-gateway">api-gateway</a></li>
            </ul>
        </li>   
    </ol>
</details>

## About The Project

The project uses a working example to show what an API gateway is, how it works,
and most importantly, how it can be secured using JWT. 

An API gateway is a component/service that sits between a client and a collection of 
backend services and acts as a reverse proxy to accept all API calls, aggregate the 
various services required to fulfill them, and then return the appropriate result. In 
simpler terms, an API gateway is a piece of software that intercepts API calls from a 
user and routes them to the appropriate backend service.

Besides their main purpose of acting as a common entry point for all services of an 
application, it's usual for API gateways to also handle common tasks such as user 
authentication, rate limiting, and statistics. For instance:

* Using an authentication service and rate limiting to protect the API from overuse and
abuse
* Adding analytics and monitoring tools to understand how people use the API
* Connecting to a billing system if there are monetized APIs
* In case of a microservice architecture, a single request could require calls to
dozens of distinct applications
* Over time, new API services are added and others are retired, but the clients 
will still want to find all the services in the same place.

The challenge is to offer the clients a simple and dependable experience in the 
face of all the possible complexity. Thus, an API gateway is a way to decouple the
client interface from the backend implementation. When a client makes a request, 
the API gateway breaks it into multiple requests, routes them to the right places, 
produces a response, and keeps track of everything.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

### Prerequisites

Make sure you have installed these before trying to run the project:

* Java 17
* PostgreSQL (but you can also use a different database of your choice)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Installation

1. Clone the repo
```shell
git clone https://github.com/DariaManu/securing-api-gateway.git
```

2. Open the project in your preferred IDE (IntelliJ for example)
3. Generate a keystore in JKS format. Since you have Java installed, you 
can use the <i>keytool</i> command. You can follow the tutorial
on the Oracle page [here](https://docs.oracle.com/cd/E19509-01/820-3503/ggfen/index.html),
or use the code below:
```shell
keytool -genkeypair -alias <alias> -keyalg RSA -keysize 2048 -storetype JKS -keystore <keystore name> -validity <validity> -storepass <password> -keypass <password> -dname "CN=<first and last name>, OU=<organizational unit name>, O=<organization name>, L=city<>, S=<state>, C=<country>"
```
but make sure you replace everything that is between < > brackets with your own data. The keystore is 
important because it is used in creating JWT.

4. After generating the keystore, add a copy of it in the <i>resources</i> folder of the <i>api-gateway</i> 
and the <i>authentication-service</i> projects.
5. Change the ports of the three services if they overlap with other applications
running on your machine. You can do this in the <i>application.yaml</i> files in
the resources folder of each service:
```shell
server:
  port: <server port>
```

6. Create a database in PostreSQL (or the DB of your choice)
7. In <i>authentication-service application.yaml </i> file, change the datasource information to match what you
have on your machine
```shell
spring:
  application:
    name: authentication-service
  datasource:
    url: <database url>
    username: <username>
    password: <password>
```
If you have chosen to use another DB the <b>spring.datasource.driver-class-name</b> and the <b>spring.jpa.properties.hibernate.dialect</b>

8. In order to use the keystore to generate the Web Token, modify the following lines of code in <i>authentication-service</i> 
and <i>api-gateway</i>:
```shell
keyStore:
  path: <path to keystore>
  password: <keystore password>
  alias: <alias value>
```
* Since you have copied the keystore in the <i>resources</i> folder, you can just add the name of the 
keystore as path
* You must include the same password as the one used when generating the keystore
* You must include the same alias as the one used when generating the keystore

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Usage

In order to use the project, run each service either from within the IDE, or using the locally installed maven, 
or using the mvnw/mvnw.cmd files that come with the project. It does not matter in what order you start 
the services.

To test if the API gateway is indeed secured, you can send some HTTP requests to the exposed APIs. It is 
important to make these calls using the port defined in application-gateway.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Detailing The Services

The following section explains what each service is responsible for and what APIs they expose.

### music-service

<i>music-service</i> does not contain much logic, its purpose being to simply expose some APIs that
have to be secured (the HTTP calls must include the Authentication header). Therefore, we have created 
a simple class Song with some basic fields, and in the Controller, we have defined two simple APIs:
`/api/songs` (which returns a list of songs) and `/api/songs/{songId}` (which returns the song with the 
specified id). Each time a user wants to make a call to these APIs, they must be in possession of a 
valid token.

If you want to add more logic, more APIs, or entirely change the service, feel free to do so! Again, this
service does not impact how the gateway works. It is there in order to demonstrate how the gateway secures 
the requests made to this service.

### authentication-service

<i>authentication-service</i>, however, contains the logic for registering users and logging them in, which 
generates a JWT token for them, which is then used by the gateway to check whether a user is allowed to send requests 
to a certain service (<i>music-service</i> in our case). You can use whatever implementation you want for creating users, 
adding them to the database and logging them in, as well as for creating a JWT encoder. 

The two APIs exposed are `/api/aut/register`, used to create a new user, and `/api/auth/login`, used to receive a JWT token, 
that is valid for a limited period of time, and can be used to access secured endpoints of other services.

### api-gateway

The <i>api-gateway</i> service is the focus point of this project. First of all, to 
create your own API gateway application, head over to [Spring Initializr](https://start.spring.io/) to crate the project and 
add all the necessary dependencies. For the gateway functionality, it is required that you add the Reactive Gateway dependency. 
Besides that, feel free to add any other dependencies you may need. Click on the Generate button, unzip the downloaded file and open 
it in your favourite IDE. Congratulations, you can now start coding!

So what exactly does Spring Cloud Gateway do?
* It is able to match routes on any request attribute
* Predicates and filters are specific to routes
* Circuit Breaker integration
* Spring Cloud DiscoveryClient integration
* Easy to write Predicates and Filters (which we will use for the securing part)
* Request rate limiting
* Path rewriting 

What is the chain of operations done by the gateway? First, a client makes a request to a Spring Cloud Gateway. If the 
Gateway Handler Mapping determines that the request matches a route, it sends it to the Gateway Web Handler. This handler runs
the request through a filter chain specific to that request. The filter can run logic both before and after the proxy request is sent. 
Then the proxy request is made.

What are some important notions you must know beforehand?
* Route = The basic building block of the gateway. It is defines by an ID, a destination URI, a collection of predicates, and a collection 
of filters. A route is matched if the aggregate predicate is true
* Predicate = This is a Java 8 Function Predicate. The input type is a Spring Framework ServerWebExchange. Thi lets you match
on anything from the HTTP request, such as headers or parameters.
* Filters = These are instances of GatewayFilter that have been constructed using a specific factory. Here, you can modify requests 
and responses after sending the downstream request.

Configuring the gateway can be done by writing Java code, or using a configuration file (the <i>application.yaml</i> file from the <i>resources</i>
directory in our case). This working example uses the configuration file option, as it is easier to understand and more 
straight forward. Let's look at an example and see what each line does.
```shell
spring:
  cloud:
    gateway:
      routes:
        - id: authentication-service
          uri: ${AUTH_SERVICE_ROUTE_URI:http://localhost:8081}
          predicates:
              - Path=/api/auth/**
```

In this short example, we can see how to configure routes to our downstream services. Each route needs to 
have an id, a uri (you can hardcode the uri in the config file, or you can use the syntax above to provide the uri through environment variables), 
and a list of predicates. A predicate is a way for us to determine if this is a request that we are going to send down to the
proxied service. In this case, we are trying to route incoming requests to the exposed APIs of <i>authentication-service</i>, which are
`/api/auth/register` and `/api/auth/login`. In order to match both of these paths, we can use a more general way of writing them: `/api/auth/**`. 
In natural language, this translates to "Please see if incoming requests match with the `/api/auth/**`" pattern, and if they do, redirect them to 
the above specified uri (the <i>authentication-service</i>).

Repeat this process for all the endpoints that you want to make visible through the gateway.

Now let's see how we can secure the APIs of the <i>music-service</i>. To begin with, we add another entry to spring.cloud.entry.routes to redirect 
requests that match the path `/api/songs/**` to <i>music-service</i>:
```shell
spring:
  cloud:
    gateway:
      routes:
        - id: authentication-service
          uri: ${AUTH_SERVICE_ROUTE_URI:http://localhost:8081}
          predicates:
              - Path=/api/auth/**
        - id: music-service
          uri: ${MUSIC_SERVICE_ROUTE_URI:http://localhost:8082}
          predicates:
            - Path=/api/songs/**
```
Then we add the filters property in which we put the name of the class (AuthorizationFilter in our case) where we will write the 
authorization logic. You can include as many filters as you want, both custom and predefined (see Spring documentation).
```shell
spring:
  cloud:
    gateway:
      routes:
        - id: authentication-service
          uri: ${AUTH_SERVICE_ROUTE_URI:http://localhost:8081}
          predicates:
              - Path=/api/auth/**
        - id: music-service
          uri: ${MUSIC_SERVICE_ROUTE_URI:http://localhost:8082}
          predicates:
            - Path=/api/songs/**
          filters:
            - AuthenticationFilter
```
What this does is it intercepts the requests, performs custom logic, and then passes down the request. When it gets the request, the first thing 
it does is it checks whether the route that is being called needs or does not need authorization. If it does, then it checks whether the 
client is passing down a certain token. This is the logic that must be implemented in your AuthenticationFilter.

Step-by-step, make sure that your AuthenticationFilter class does the following:
* It is annotated using @Component to let Spring know that it needs to create an instance
* It extends the AbstractGatewayFilterFactory so that we can override the method that passes the exchange and request through our 
custom filter, where we will include the authorization logic
* It includes the path to the keystore, the password and the alias; you can hardcode them or inject them 
from the configuration file
* Create a method that loads the public key from the keystore
* Create an inner static class Config that contains a list of patterns of excluded paths. Why do we need to exclude some paths? 
Because it would not make sense to authorize them. For instance, the `/register` and `/login` paths must allow all users to access them 
because their purpose is to add new users to the application and generate tokens for them to use when accessing other endpoints.
* Implement the overwritten apply method. 
  * Extract the HTTP request from the exchange
  * Extract the path, headers and token from the request
  * Check if the path is part of the excluded URLs. You can do this in the same method, or add a new private one
    (called isExcluded for example), and check if the extracted path matched any of the patterns of the excluded URLs. You can hardcode the exclude URLs
    in the AuthenticationFilter class, or you can inject them from the config file, just like we did with the keystore 
  * If the path is excluded, then we don't have to check for anything authentication related
  * If the path is not excluded, then we have to check if the token exists. If the token is null, or it doesn't
  start with "Bearer ", then you can either throw a Java error or handle the error by returning a custom response (handleAuthError method)
  * If the token exists, we extract from the String only the part containing the value of the token (the substring starting from index 7), 
  get the public key by calling the implemented method, and lastly, get the claims from the token. When generating the token during login, 
  if you've included the username and the role, extract them from the claims and add them to the header of the intercepted request.
  * Lastly, we return the chain filter


<p align="right">(<a href="#readme-top">back to top</a>)</p>