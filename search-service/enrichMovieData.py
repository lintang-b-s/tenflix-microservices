#  movies.csv & links.csv from movielens

import csv
from pyspark import SparkContext
from pyspark.sql import SQLContext
import re
import tmdbsimple as tmdb
import json

sc = SparkContext('local', 'Spark SQL')
sqlContext = SQLContext(sc)



with open("movies.csv") as f:
    reader = csv.reader(f)
    cols = next(reader)
    cols.append("year")
    movie = []
    for l in reader:
        # get year
        rgx = re.compile(r"(?:\((\d{4})\))?\s*$")
        match = rgx.search(l[1])
        year = match.group(1)
        l.append(year)

        # append to movie
        movie.append(l)
    raw_movies = sqlContext.createDataFrame(movie, cols)

with open("links.csv") as f:
    reader = csv.reader(f)
    cols = next(reader)
    link_data = sqlContext.createDataFrame([l for l in reader], cols)



movie_data = raw_movies.join(link_data, raw_movies.movieId == link_data.movieId)\
    .select(raw_movies.movieId, raw_movies.title, raw_movies.genres, link_data.tmdbId,raw_movies.year )\
    .filter(raw_movies.year >= 2020)

num_movies = movie_data.count()
movie_data.show(5)
data = movie_data.collect()
print( "total movies > year 2020: " + str(num_movies))



tmdb.API_KEY = "api_key_tdmb_kamu"
# base URL for TMDB poster images
IMAGE_URL = 'https://image.tmdb.org/t/p/w500'
import csv
from requests import HTTPError
enriched = []
i = 0



data = data[3000:]


for row in data:
    try:
        if (len(enriched) == 1500):
            break
        m = tmdb.Movies(row.tmdbId).videos()

        videos = m['results']
        trailer = ''
        for vi in videos:
            if vi['type'] == 'Trailer':
                trailer ="https://www.youtube.com/watch?v="+ vi['key']
                break


        m = tmdb.Movies(row.tmdbId).info()
        credit =  tmdb.Movies(row.tmdbId).credits()
        cast = []

        for c in credit["cast"]:
          if (len(cast) == 5):
              break
          cast.append(c["name"])

        director =  []
        for c in credit["crew"]:
            if (c["job"] == "Director"):
              director.append(c["name"])
              break

        genre = []
        for g in m['genres']:
            genre.append(g['name'])

        movie = {
          "title": m['title'],
          "genre": genre,
          "synopsis": m['overview'],
          "releaseYear": m['release_date'],
          "image": m['backdrop_path'],
          "rating": m['vote_average'],
          "director": director,
          "cast": cast,
          "url": trailer
      }

        enriched.append(movie)
    except HTTPError as e:
        continue
    i += 1
    if i % 1 == 0: print("Enriched movie %s of %s" % (i, num_movies))


new_file_name= "movies-tenflix-3000-4500.json"
with open(new_file_name, 'w', encoding='utf-8') as f:
      json.dump(enriched, f, indent=4)



