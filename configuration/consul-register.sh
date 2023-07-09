
curl \
  --request PUT \
  --data @kong-db-api.json \
  http://localhost:8500/v1/agent/service/register