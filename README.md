# springboot-service-items

Microservice for items.

### Setup

Follow the next commands to generate the server image:

### Generate jar

```bash
$ ./mvnw clean package -DskipTests
```

### Create network (ignore if the network was already created)

```bash
$ docker network create springcloud
```

### Generate image

```bash
$ docker build -t service-items:v1 .
```

### Start this project without docker-compose

```bash
$ docker run -d -p 8082:8082 -p 8005:8005 -p 8007:8007 --name service-items --network springcloud service-items:v1
```
### How to start the whole project

To start the complete project, please take a look at **docker-compose** project and follow the instructions from its README file.