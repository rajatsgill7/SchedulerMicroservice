# SchedulerMicroservice
SchedulerMicroservice is a Java-based application designed to provide efficient and reliable scheduling capabilities for
various tasks and events. Built using the Spring Boot framework, it offers a robust set of features to easily integrate 
scheduling functionality into your projects.

# Prerequisites
Java JDK 11 or later

# Installation
* Clone the repository

* git clone https://github.com/rajatsgill7/SchedulerMicroservice.git
* cd SchedulerMicroservice

* Build the project

* mvn clean install

* Run the application

* java -jar target/schedulermicroservice-0.0.1-SNAPSHOT.jar

*discovery microservice will run on port 8761
scheduler microservice will run on a random port and will register itself on the discovery microservice*

# Endpoints

  ## GET /scheduler/getAllOperators

  * curl --location --request GET 'http://localhost:port/scheduler/getAllOperators'

  ## GET /scheduler/getAllSchedules

  * curl --location --request GET 'http://localhost:port/scheduler/getAllSchedules'

  ## GET /scheduler/getOperator/{id}

  * curl --location --request GET 'http://localhost:port/scheduler/getOperator/2'

  ## POST /scheduler/bookAppointment

  * curl --location --request POST 'http://localhost:port/scheduler/bookAppointment' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "numberOfHours": 6
  }'

  ## PATCH /scheduler/rescheduleAppointment/{id}

  * curl --location --request PATCH 'http://localhost:port/scheduler/rescheduleAppointment/1' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "numberOfHours": 4
  }'
     
 ## DELETE /scheduler/cancelAppointment

  * curl --location --request DELETE 'http://localhost:port/scheduler/cancelAppointment' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "scheduleId": 9,
  "operatorId": 3,
  "appointmentId":9}'

