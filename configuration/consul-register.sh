
curl \
  --request PUT \
  --data @kong-db-api.json \
  http://localhost:8500/v1/agent/service/register &&

curl \
  --request PUT \
  --data @order-aggregator.json \
  http://localhost:8500/v1/agent/service/register