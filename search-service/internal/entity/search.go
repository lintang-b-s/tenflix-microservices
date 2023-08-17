package entity

type Search struct {
	ReleaseYear *[]int
	Query       *string
	Director    *string
	Cast        *[]string
	Genre       *[]string
	MinRating   *float64
	From        *int64
	Size        int64
}

type AutoComplete struct {
	Word string
}

type GetByGenre struct {
	Genre       *string
	From        *int64
	Size        int64
	ReleaseYear *[]int
}
