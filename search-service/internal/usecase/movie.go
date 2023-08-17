package usecase

import (
	"context"
	"fmt"
	"github.com/evrone/go-clean-template/internal/entity"
)

type MovieUseCase struct {
	movieRepo MovieElasticSearchRepo
}

// New -.
func NewMovie(m MovieElasticSearchRepo) *MovieUseCase {
	return &MovieUseCase{
		movieRepo: m,
	}
}

func (uc *MovieUseCase) Index(ctx context.Context, m entity.Movie) error {
	err := uc.movieRepo.Index(ctx, m)
	if err != nil {
		return fmt.Errorf("MovieUseCase - Index - s.movieRepo.Index: %w", err)
	}
	return nil
}

func (uc *MovieUseCase) Search(ctx context.Context, s entity.Search) ([]entity.Movie, error) {
	movies, err := uc.movieRepo.Search(ctx, s)
	if err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieUseCase - Search - s.movieRepo.Search: %w", err)
	}

	return movies, nil

}

func (uc *MovieUseCase) AutoComplete(ctx context.Context, s entity.AutoComplete) ([]entity.Movie, error) {
	movies, err := uc.movieRepo.AutoComplete(ctx, s)

	if err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieUseCase - AutoComplete - s.movieRepo.AutoComplete(ctx, s): %w", err)
	}

	return movies, nil

}

func (uc *MovieUseCase) GetByGenre(ctx context.Context, g entity.GetByGenre) ([]entity.Movie, error) {
	movies, err := uc.movieRepo.GetByGenre(ctx, g)
	if err != nil {
		return []entity.Movie{}, fmt.Errorf("MovieUseCase - GetByGenre - s.movieRepo.GetByGenre(ctx, s): %w", err)
	}
	return movies, nil
}
