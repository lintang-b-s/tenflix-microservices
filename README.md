
step:
1. bash ./bootstrap.sh
2. ubah tipe data kolom payload di outbox table jadi TEXT di pgadmin(localhost:5050)
3. akses api lewat api-gateway (localhost:8181)
4. moviecommandservice: localhost:8181/api/v1/movies
5. moviequeryservice: localhost:8181/api/query/v1/movies


