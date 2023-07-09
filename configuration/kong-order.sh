#   for docker-compose-order-test
# curl -i -X POST \
#  --url 'http://localhost:8001/services' \
# --header 'Content-Type: application/json' \
# --data '{
# 	"host" : "order-aggregator-service.service.consul",
# 	"name" : "order-aggregator-service",
# 	"retries" : 5,
#      "path" : "/api/v1/subscription",
# 	"connect_timeout" : 60000,
# 	"write_timeout" : 60000,
# 	"read_timeout" : 60000
# }' &&


# curl -i -X POST \
# --url http://localhost:8001/services/order-aggregator-service/routes \
# --data 'hosts[]=localhost' \
# --data 'paths=/api/v1/subscription'






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





# curl --location 'http://localhost:8001/services/order-aggregator-service/routes' \
# --header 'Content-Type: application/json' \
# --data '{
# 		"name": "subscription-api",
#     "protocols": [
#         "http",
#         "https"
#     ],
#     "paths": [
#         "/api/v1/subscription"
#     ],
#     "methods": [
# 			"POST"
# 		]
# }'


# # pake upstream
# curl -X POST http://localhost:8001/upstreams \
#      --data "name=order-aggregator" \
#      --data 'healthchecks.active.healthy.interval=5' \
#      --data 'healthchecks.active.unhealthy.interval=5' \
#      --data 'healthchecks.active.unhealthy.http_failures=5' \
#      --data 'healthchecks.active.healthy.successes=5' &&



# curl -X POST http://localhost:8001/upstreams/order-aggregator/targets \
#      --data "target=order-aggregator-service.service.consul:9900" \
#      --data "weight=100" &&

# curl -X POST http://localhost:8001/services/ \
#      --data "name=order-aggregator-router" \
#      --data "host=order-aggregator" \
#      --data "path=/api/v1" &&

# curl -X POST http://localhost:8001/services/order-aggregator-router/routes/ \
#      --data "paths[]=/subscription"