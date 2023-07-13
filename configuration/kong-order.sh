#   for docker-compose-order-test



curl -i -X POST \
 --url 'http://localhost:8001/services' \
--header 'Content-Type: application/json' \
--data '{
	"host" : "172.1.1.15",
	"port" : 9900,
	"name" : "order-aggregator-service-subscription",
	"retries" : 5,
     "path" : "/api/v1/subscription",
	"connect_timeout" : 60000,
	"write_timeout" : 60000,
	"read_timeout" : 60000
}' &&

curl -i -X POST \
--url http://localhost:8001/services/order-aggregator-service-subscription/routes \
--data 'hosts[]=localhost' \
--data 'paths=/api/v1/subscription' && 

curl -i -X POST \
 --url 'http://localhost:8001/services' \
--header 'Content-Type: application/json' \
--data '{
	"host" : "172.1.1.15",
	"port" : 9900,
	"name" : "order-aggregator-service-orders",
	"retries" : 5,
     "path" : "/api/v1/orders",
	"connect_timeout" : 60000,
	"write_timeout" : 60000,
	"read_timeout" : 60000
}'  && 

curl -i -X POST \
--url http://localhost:8001/services/order-aggregator-service-orders/routes \
--data 'hosts[]=localhost' \
--data 'paths=/api/v1/orders'



