CREATE MATERIALIZED VIEW get_all_movies
AS
SELECT
    m.id AS id ,
    m.name,
    m.type,
    m.synopsis,
    m.mpa_rating,
    m.r_year,
    m.idmb_rating,
    m.image,
    a.id AS actor_id,
    a.name AS actor_name,
    c.id AS creator_id,
    c.name AS creator_name,
    v.id AS video_id,
    v.length,
    v.synopsis AS video_synopsis,
    v.title,
    v.url


FROM
    movies m
        LEFT JOIN videos v
                  ON m.id = v.movie_id
        LEFT JOIN movie_creator
                  ON m.id = movie_creator.movie_id
        LEFT JOIN creators c
                  ON movie_creator.creator_id = c.id
        LEFT JOIN movie_actor
                  ON m.id = movie_actor.movie_id
        LEFT JOIN actors a
                  ON movie_actor.actor_id = a.id
ORDER BY m.name
WITH NO DATA;
CREATE UNIQUE INDEX get_all_movies_id ON get_all_movies (id);

REFRESH MATERIALIZED VIEW  get_all_movies;

CREATE OR REPLACE PROCEDURE refresh_get_all_movies_view()
    LANGUAGE plpgsql
AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY get_all_movies;
END;
$$;


