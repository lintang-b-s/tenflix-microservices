import json
import requests
import sys


# kong-order sh
# add subscription service
urlMap  = "http://localhost:8001/services"
fSubscription = open('kong-subscription.json')
headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
dataMap = json.load(fSubscription)
# print(dataMap["peta"])
rMap = requests.post(urlMap, data=json.dumps(dataMap), headers=headers)
print(rMap.status_code)

fSubscription.close()

payload = {'hosts[]': 'localhost', 'paths': '/api/v1/subscription'}
url = "http://localhost:8001/services/order-aggregator-service-subscription/routes"
r = requests.post(url, data=payload)
print(r.status_code)

# add order service















# indexing movieswiki index with document in movies-tenflix.json
url = "http://localhost:9200/{}/_doc".format("movieswiki")

# Opening JSON file
f = open('movies-tenflix.json')

# returns JSON object as
# a dictionary
data = json.load(f)

# print json data
#print(data['prizes'][0])

# print number of documents in the json
count = len(data)
print(count)

# index each document
for each in range(count):
    movie = data[each]
    r = requests.post(url, data=json.dumps(movie), headers=headers)
    # print(r.status_code)

# Closing file
f.close()