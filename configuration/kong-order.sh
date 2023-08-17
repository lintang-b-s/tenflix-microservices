#   for docker-compose-order-test

curl -i -X POST \
 --url 'http://localhost:8001/services' \
--header 'Content-Type: application/json' \
--data '{
	"host" : "order-aggregator.service.consul",

	"name" : "order-aggregator-service-subscription",
	"retries" : 5,

	"connect_timeout" : 60000,
	"write_timeout" : 60000,
	"read_timeout" : 60000
}' &&
#     "path" : "/",
#	"port" : 9900,

curl -i -X POST \
--url http://localhost:8001/services/order-aggregator-service-subscription/routes \
--data 'hosts[]=localhost' \
--data 'paths='
#
#curl -i -X POST \
# --url 'http://localhost:8001/services' \
#--header 'Content-Type: application/json' \
#--data '{
#	"host" : "172.1.1.19",
#	"port" : 9900,
#	"name" : "order-aggregator-service-orders",
#	"retries" : 5,
#
#	"connect_timeout" : 60000,
#	"write_timeout" : 60000,
#	"read_timeout" : 60000
#}'  &&
#
##     "path" : "/api/v1/orders",
#curl -i -X POST \
#--url http://localhost:8001/services/order-aggregator-service-orders/routes \
#--data 'hosts[]=localhost' \
#--data 'paths=/api/v1/orders'
#
#
#
