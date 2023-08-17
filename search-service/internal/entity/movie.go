package entity

type Movie struct {
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
