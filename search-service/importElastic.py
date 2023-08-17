import json
import requests
import sys
from elasticsearch import Elasticsearch, helpers



# create mapping for index movieswiki
# if exists delete index
headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

urlCheck = "http://localhost:9200/movieswiki/_mapping"
urlDelete= "http://localhost:9200/movieswiki"
rMap = requests.get(urlCheck, headers=headers)
if (rMap.status_code == 200):
    r  = requests.delete(urlDelete, headers=headers)




urlMap  = "http://localhost:9200/movieswiki"
fMap = open('movie-mapping.json')


dataMap = json.load(fMap)


rMap = requests.put(urlMap, data=json.dumps(dataMap["peta"]), headers=headers)
print(rMap.status_code)
print(rMap.text)

# fMap.close()


# indexing movieswiki index with document in movies-tenflix.json
url = "http://localhost:9200/_bulk".format("movieswiki")


# bulk indexing
f = open('bulk-movies.json')

# returns JSON object as
# a dictionary
data = json.load(f)


count = len(data)
print(count)
es_client = Elasticsearch('http://localhost:9200')


# index bulk document
document_list = []
i =0
for each in range(count):
    movie = data[each]
    document_list.append(movie)
    if i == count-1:
        helpers.bulk(es_client, document_list, index='movieswiki')
    i +=1
        

f.close()
