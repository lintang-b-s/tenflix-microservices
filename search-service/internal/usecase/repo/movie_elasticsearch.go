package repo

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	esv8 "github.com/elastic/go-elasticsearch/v8"
	esv8api "github.com/elastic/go-elasticsearch/v8/esapi"
	"github.com/evrone/go-clean-template/internal/entity"
	"io"
)

type MovieRepo struct {
	client *esv8.Client
	index  string
}

func NewMovieEsRepo(client *esv8.Client) *MovieRepo {
	return &MovieRepo{
		client: client,
		index:  "movieswiki",
	}
}

type indexedMovie struct {
	ReleaseYear int      `json:"releaseYear"`
	Title       string   `json:"title"`
	Director    []string `json:"director"`
	Cast        []string `json:"cast"`
	Genre       []string `json:"genre"`
	Synopsis    string   `json:"synopsis"`
	Url         []string `json:"url"`
	Rating      float64  `json:"rating"`
	Image       string   `json:"image"`
}

func (r *MovieRepo) Index(ctx context.Context, m entity.Movie) error {
	body := indexedMovie{
		ReleaseYear: m.ReleaseYear,
		Title:       m.Title,
		Director:    m.Director,
		Cast:        m.Cast,
		Genre:       m.Genre,
		Synopsis:    m.Synopsis,
		Url:         m.Url,
		Rating:      m.Rating,
		Image:       m.Image,
	}

	var buf bytes.Buffer

	if err := json.NewEncoder(&buf).Encode(body); err != nil {
		return fmt.Errorf("MovieRepo - Index - r.json.NewEncoder(&buf).Encode(body): %w", err)
	}

	req := esv8api.IndexRequest{
		Index: r.index,
		Body:  &buf,
		//DocumentId
		Refresh: "true",
	}
	resp, err := req.Do(ctx, r.client)
	if err != nil {
		return fmt.Errorf("MovieRepo - Index - r.req.Do(ctx, r.client): %w", err)
	}
	defer resp.Body.Close()

	if resp.IsError() {
		return fmt.Errorf("MovieRepo - Index - r.req.Do(ctx, r.client): %w", err)
	}

	io.Copy(io.Discard, resp.Body)

	return nil
}

func (r *MovieRepo) Search(ctx context.Context, args entity.Search) ([]entity.Movie, error) {

	must := make([]interface{}, 0, 1)
	filter := make([]interface{}, 0, 5)
	if args.Query != nil {
		must = append(must, map[string]interface{}{
			"multi_match": map[string]interface{}{
				"query":     args.Query,
				"fields":    []string{"title", "synopsis"},
				"fuzziness": "AUTO",
			},
		})
	}

	oneQuery := map[string]interface{}{
		"multi_match": map[string]interface{}{
			"query":     args.Query,
			"fields":    []string{"title^3", "synopsis", "director", "cast"},
			"fuzziness": "AUTO",
		},
	}

	if args.ReleaseYear != nil {
		filter = append(filter, map[string]interface{}{
			"terms": map[string]interface{}{
				"releaseYear": args.ReleaseYear,
			},
		})
	}

	if args.Director != nil {
		filter = append(filter, map[string]interface{}{
			"term": map[string]interface{}{
				"director.raw": args.Director,
			},
		})
	}
	if args.Cast != nil {
		filter = append(filter, map[string]interface{}{
			"terms": map[string]interface{}{
				"cast.raw": args.Cast,
			},
		})
	}

	if args.Genre != nil {
		filter = append(filter, map[string]interface{}{
			"terms": map[string]interface{}{
				"genre": args.Genre,
			},
		})
	}

	if args.MinRating != nil {
		filter = append(filter, map[string]interface{}{
			"range": map[string]interface{}{
				"rating": map[string]interface{}{
					"gte": args.MinRating,
				},
			},
		})
	}

	var query map[string]interface{}

	if len(filter) > 0 {
		query = map[string]interface{}{
			"query": map[string]interface{}{
				"bool": map[string]interface{}{
					"must":   oneQuery,
					"filter": filter,
				},
			},
		}
	} else {
		query = map[string]interface{}{
			"query": oneQuery,
		}
	}

	query["sort"] = []interface{}{
		"_score",
	}
	query["from"] = args.From
	query["size"] = args.Size

	var buf bytes.Buffer

	if err := json.NewEncoder(&buf).Encode(query); err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - Search - json.NewEncoder(&buf).Encode(query): %w", err)
	}

	req := esv8api.SearchRequest{
		Index: []string{r.index},
		Body:  &buf,
	}

	resp, err := req.Do(ctx, r.client)

	if err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - Search - req.Do: %w", err)
	}

	defer resp.Body.Close()

	if resp.IsError() {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - Search - req.Do: %w", resp.String())

	}

	var hits struct {
		Hits struct {
			Total struct {
				Value int64 `json:"value"`
			} `json:"total"`
			Hits []struct {
				Source indexedMovie `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&hits); err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - Search - json.NewDecoder(resp.Body).Decode(&hits): %w", err)
	}
	var res []entity.Movie

	for _, hit := range hits.Hits.Hits {
		//time := time.Unix(int64(hit.Source.ReleaseYear), 0)
		//releaseYear, _ := strconv.Atoi(time.Format("2006"))
		mov := entity.Movie{
			ReleaseYear: hit.Source.ReleaseYear,
			Title:       hit.Source.Title,
			Director:    hit.Source.Director,
			Cast:        hit.Source.Cast,
			Genre:       hit.Source.Genre,
			Synopsis:    hit.Source.Synopsis,
			Url:         hit.Source.Url,
			Rating:      hit.Source.Rating,
			Image:       hit.Source.Image,
		}
		res = append(res, mov)
	}

	return res, nil
}

func (r *MovieRepo) AutoComplete(ctx context.Context, args entity.AutoComplete) ([]entity.Movie, error) {

	var query map[string]interface{}
	query = map[string]interface{}{
		"size": 10,
		"query": map[string]interface{}{
			"multi_match": map[string]interface{}{
				"query": args.Word,
				"type":  "bool_prefix",
				"fields": []string{
					"title.raw",
					"title.raw._2gram",
					"title.raw._3gram",
				},
			},
		},
	}
	var buf bytes.Buffer

	if err := json.NewEncoder(&buf).Encode(query); err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - AutoComplete - json.NewEncoder(&buf).Encode(query): %w", err)
	}
	req := esv8api.SearchRequest{
		Index: []string{r.index},
		Body:  &buf,
	}

	resp, err := req.Do(ctx, r.client)

	if err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - AutoComplete - req.Do: %w", err)
	}

	defer resp.Body.Close()

	if resp.IsError() {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - AutoComplete - req.Do: %w", resp.String())

	}

	var hits struct {
		Hits struct {
			Total struct {
				Value int64 `json:"value"`
			} `json:"total"`
			Hits []struct {
				Source indexedMovie `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&hits); err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - AutoComplete - json.NewDecoder(resp.Body).Decode(&hits): %w", err)
	}
	var res []entity.Movie

	for _, hit := range hits.Hits.Hits {
		mov := entity.Movie{
			ReleaseYear: hit.Source.ReleaseYear,
			Title:       hit.Source.Title,
			Director:    hit.Source.Director,
			Cast:        hit.Source.Cast,
			Genre:       hit.Source.Genre,
			Synopsis:    hit.Source.Synopsis,
			Url:         hit.Source.Url,
			Rating:      hit.Source.Rating,
			Image:       hit.Source.Image,
		}
		res = append(res, mov)
	}

	return res, nil
}

func (r *MovieRepo) GetByGenre(ctx context.Context, args entity.GetByGenre) ([]entity.Movie, error) {
	var query map[string]interface{}

	filter := make([]interface{}, 0, 2)

	if args.ReleaseYear != nil {
		filter = append(filter, map[string]interface{}{
			"terms": map[string]interface{}{
				"releaseYear": args.ReleaseYear,
			},
		})
	}

	filter = append(filter, map[string]interface{}{
		"term": map[string]interface{}{
			"genre": args.Genre,
		},
	})

	query = map[string]interface{}{
		"query": map[string]interface{}{
			"bool": map[string]interface{}{
				"filter": filter,
			},
		},
	}

	query["from"] = args.From
	query["size"] = args.Size

	var buf bytes.Buffer

	if err := json.NewEncoder(&buf).Encode(query); err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - GetByGenre - json.NewEncoder(&buf).Encode(query): %w", err)
	}
	req := esv8api.SearchRequest{
		Index: []string{r.index},
		Body:  &buf,
	}

	resp, err := req.Do(ctx, r.client)

	if err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - GetByGenre - req.Do: %w", err)
	}

	defer resp.Body.Close()

	if resp.IsError() {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - GetByGenre - req.Do: %w", resp.String())

	}

	var hits struct {
		Hits struct {
			Total struct {
				Value int64 `json:"value"`
			} `json:"total"`
			Hits []struct {
				Source indexedMovie `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&hits); err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieRepo - GetByGenre - json.NewDecoder(resp.Body).Decode(&hits): %w", err)
	}
	var res []entity.Movie

	for _, hit := range hits.Hits.Hits {
		mov := entity.Movie{
			ReleaseYear: hit.Source.ReleaseYear,
			Title:       hit.Source.Title,
			Director:    hit.Source.Director,
			Cast:        hit.Source.Cast,
			Genre:       hit.Source.Genre,
			Synopsis:    hit.Source.Synopsis,
			Url:         hit.Source.Url,
			Rating:      hit.Source.Rating,
			Image:       hit.Source.Image,
		}
		res = append(res, mov)
	}

	return res, nil

}
