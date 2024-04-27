# Description
Movie Streaming microservices like Netflix, built using microservices architecture, Saga distributed transactions patternm , CQRS, Outbox Pattern, kafka, grpc, concurrency, CDC debezium, keycloak oauth server, kong api gateway, consul service discovery, etc.


This project provides an example of the Saga distributed transactions pattern.


# Architecture
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1714231729/tenflix_2_lb3slg.png)


### Saga Distributed Transactions Pattern
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1714240559/tenflix_copy_j4zm4v.png)

### concurrency
[https://github.com/lintang-b-s/tenflix-microservices/blob/main/order-aggregator-service-go/internal/usecase/order.go]  getOrderDetail usecase using concurrency to call the other three services

### Microservice
1. Movie-command-service: This microservice writes and updates movie data.Movies data is stored in Postgres. Movie data also saved as payload in outbox table movie-command-service. Debezium read/listen data from write-ahead log outbox table in postgres and then send movie data to kafka.

2. Movie-Query-service: This microservices reads movie data from mongodb. The movies data is obtained from Kafka, where the Kafka event message is sent by debezium, and the movie data from the movie-command-service database. When a user wants to see a movie, the user sends a request to this microservice, then this microservice checks whether the user has a subscription by sending a GRPC request to the subscription service. If the user has a subscription, the video movie data is sent to the user

3. Media-service: This microservice receives video data from Kafka, where video data is sent by movie-command-service when the admin adds and uploads movie videos.

4. Notification-service: This microservices sends notifications to certain users every time the admin adds a new movie in the movie-command-service.

5. Order-aggregator-service: When a user wants to buy a Tenflix subscription plan, the user places an order in the system, making the order is done by this microservice. Order creation involves requests to payment and subscription microservices. The return from createOrder is a midtrans snap link. This microservice also provides a midtrans notification endpoint, if midtrans sends a notification to this microservice, a saga transaction will be executed to complete the order. This microservice also provides an endpoint to display user order data.

6. Order-Service: The microservice also performs CRUD on order data stored in the Postgres order-service database.

7. Payment-service: This microservice interacts directly with the midtrans api. This microservice also stores payment data from Midtrans into its database.

8. Subcription-Service: This microservice performs CRUD on its database. The subscription-service database stores user subscription data and subscription plans on Tenflix.


### Choreography-based Saga Transaction Flow
Sending data in here using outbox pattern & cdc. The data that the publisher wants to send is saved to the outbox table, then debezium listens for changes to the outbox table data (via Postgres write-ahead-log) and sends the data to Kafka every time there is new data in the outbox table.
#### Happy Case (Payment PAID/Accepted)
1. order-aggregator-service receives notification from midtrans, then it sends a GRPC request to order-service so that order-service, subscription-service, payment-service carry out choreography saga transactions.
2. order-service receives a GRPC request from order-aggregator-service, then it checks whether the order data from the midtrans orderId field exists in the database. If there is, order-service sends payment message from order-aggregator-service to payment-service via kafka. Sending data using outbox pattern & cdc. The data that the order-service wants to send is saved to the outbox table, then debezium listens for changes to the outbox table data (via Postgres write-ahead-log) and sends the data to Kafka every time there is new data in the outbox table.
3. The payment service receives payment message from order-service (via Kafka), then checks the Midtrans payment status on the payment data. In this case the payment status is "accept", then the payment data is saved to the payment database. and payment -service sends a ValidatedPayment message to order-service
4. order-service receives a message from payment-service (via kafka), then updates the order status to PAID , then sends an Add_Subscription message to subscription-service
5. subscription-service receives the AddedSubscription Message from order-service, then saves the user's subscription data to its database, then sends the AddedSubscription message to order-service
6. order-service receives the AddedSubscription message from subscription-service , then it sends a CompleteOrder Message to the order-request topic in kafka.
7. order-service receives a message from the kafka order-request topic, then it updates the order status to "COMPLETED".

#### Bad Case 1 (The subscription plan ordered by the user does not exist in the subscription-service database )
besok , dah ngantuk 

# Quick Start

### prequisite
1. install protobuf compiler
2. install apache maven
3. install docker & docker compose
4. free RAM memory on your PC  >= 10gb
5. min cpu: 6 core 12 thread (same as my laptop)


### start application
```
    fill the env file  & .ngrok.yml file
    bash ./bootstrap.sh
    
    docker compose -f docker-compose-all-test.yml up -d, wait until all container up & running
    cd configuration
     bash ./consul-register.sh
    if kafka-connect is not running or kong, execute the above command again
    bash ./kong-order.sh
    check all service registered in localhost:8500, wait until order-service, subscription-service, paymentservice registered in consul
    wait until kafka-connect load all plugin
    bash ./kafka-connect-debezium.sh
    docker exec -it zookeeper kafka-configs --bootstrap-server kafka:29092 --alter --entity-type topics --entity-name t.upload.request --add-config max.message.bytes=104858800

```


### setting payment midtrans
```

    cek dashboard ngrok anda di bagian menul tunnel, copy link tunnel ngrok nya buat 172.1.1.19:9900
    tulis redirect url https yang dikasih ngrok  di dashboard midtrans sandbox di settings-configuration-payment notification url
    write the https redirect url given by ngrok on the midtrans dashboard sandbox in settings-configuration-payment notification url
    <URLNGROK>/api/v1/orders/notificationMidtrans
    save

```


### register && login, create order & get order
```
   import postman collection  in docs/Tenflix - now version.postman_collection
    1. open auth&user copyfolder
    2. register in auth code postman request
    for example register using email: tes@gmail.com, password: tes
    3. login using tes account in auth code url, then copy the code value in query param
    4. open access token postman request , and paste the code. then send the request. Copy the access Token
    
    for creating order and get order detail
    5. open subscription copy folder & open create plan postman request , paste the access token in oauth2 postman. send the request
    6. open order-service folder & open create order request. paste access token and send it
    7.  open redirect url given in  create order response, and click bca payment, copy the virtual account number
    8. open https://simulator.sandbox.midtrans.com/bca/va/index, paste virtual account number, and click inquire
    & pay
    9. open get order detail postman request ,paste access token in oauth2 token bar, copy orderId given in  create order response and paste in path parameter . send the request
    10. open get order history, paste accces token. send the request
```

note:  you must provide an access token to each protected endpoint using oauth2 postman
note: if order-aggregator-service container not running , start container again and run bash ./kong-order.sh

### Insert Movie, Upload Movie video, Watch Movie
```
1. Make sure your account has purchased a subscription using the method I explained above.
2. cd configuration && bash ./kong-order.sh
3. make sure media-service up & running (check with 'docker ps -a -f "status=exited" ), if not running , restart the container (make sure kafka is up & running too)
4. Execute all http request in the movie_folder postman, make sure it is in the order from top to bottom & dont create duplicate category
5. Read movie & video data in movie query folder postman
```


### 2. register
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692204057/1_idxpen.png )


### 3. login
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692204389/2_juh4rh.png)

### 3. get auth code
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692204790/3_imiizw.png)


### 4. get access token
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692204869/4_dyaxcf.png)


### 6.  create orders
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692205085/6_euqpvw.png)


### 7. open payment url
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692205162/7_xop65l.png)

### 8. simulate payment
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692205213/8_ktsek5.png)

### 9.  get order detail
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692205283/9_fkxizj.png)


### 10. Execute all http request in the movie_folder postman
![get movie](https://res.cloudinary.com/tutorial-lntng/image/upload/v1714232170/Screenshot_from_2024-04-27_22-35-55_wphyzv.png)
