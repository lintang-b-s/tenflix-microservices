# movie-elasticsearch
elasticsearch with movie data

api untuk searching movie pakai elasticsearch
step to import movie data:
1. docker compose up -d
2. python3 importElastic.py

 
 pagination size 10 , from document ke 5000:
curl -XGET '127.0.0.1:9200/movieswiki/_search?size=10&from=5000&pretty=true'

