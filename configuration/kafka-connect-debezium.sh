curl --location 'localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data @./debezium-order.json &&

curl --location 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data @./debezium-payment.json &&

curl --location 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data @./debezium-subscription.json


# curl -i -X DELETE localhost:8083/connectors/process-order-connector
# curl -i -X DELETE localhost:8083/connectors/process-payment-connector
# curl -i -X DELETE localhost:8083/connectors/process-subscription-connector
#
#docker run --tty --network net confluentinc/cp-kafkacat:7.0.9 kafkacat -b kafka:29092 \
#-C -J -t t.saga.order.outbox.payment-validate.request
#
#
#curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" 127.0.0.1:8083/connectors/ -d @./debezium-order.json
