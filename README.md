# Description
microservices orders and payments (it's actually a movie streaming microservices, but it's not finished yet), built using microservices architecture, Saga distributed transactions pattern, kafka, grpc, concurrency, api gateway, service discovery, etc.



# Architecture
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692240124/tenflix_6_qi5mma.png)



### Saga Distributed Transactions Pattern
![alt text](https://res.cloudinary.com/tutorial-lntng/image/upload/v1692205447/tenflix_copy_kd6pos.png)

### concurrency
[https://github.com/lintang-b-s/tenflix-microservices/blob/main/order-aggregator-service-go/internal/usecase/order.go]  getOrderDetail usecase using concurrency to call the other three services

# Quick Start

### prequisite
1. install protobuf compiler
2. install apache maven
3. install docker & docker compose

### start application
```
    fill the env file 
    bash ./bootstrap.sh
    
    docker-compose -f docker-compose-order-test.yml up -d, wait until all container up & running
    cd configuration
     bash ./consul-register.sh
    if kafka-connect is not running or kong, execute the above command again
    bash ./kong-order.sh
    check all service registered in localhost:8500, wait until order-service, subscription-service, paymentservice registered in consul
    wait until kafka-connect load all plugin
    bash ./kafka-connect-debezium.sh
```


### setting payment midtrans
```
    docker exec -it tenflix-microservices-ngrok-1 bash
    
    ngrok http 172.1.1.19:9900
    
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

note:  you must provide an access token to each protected endpoint
note: if order-aggregator-service container not running , start container again and run bash ./kong-order.sh

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





