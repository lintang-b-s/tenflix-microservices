curl -i -X POST \
 --url 'http://localhost:8001/services' \
--header 'Content-Type: application/json' \
--data '{
	"host" : "movie-query-service.service.consul",

	"name" : "movie-query-service",
	"retries" : 5,

	"connect_timeout" : 60000,
	"write_timeout" : 60000,
	"read_timeout" : 60000
}' &&



curl -i -X POST \
--url http://localhost:8001/services/movie-query-service/routes \
--data 'hosts[]=localhost' \
--data 'paths=/movie-query' \
--data 'strip_path=true' && 


curl -i -X POST \
 --url 'http://localhost:8001/services' \
--header 'Content-Type: application/json' \
--data '{
	"host" : "movie-service.service.consul",

	"name" : "movie-service",
	"retries" : 5,

	"connect_timeout" : 60000,
	"write_timeout" : 60000,
	"read_timeout" : 60000
}' &&



curl -i -X POST \
--url http://localhost:8001/services/movie-service/routes \
--data 'hosts[]=localhost' \
--data 'paths=/movie-command' \
--data 'strip_path=true'