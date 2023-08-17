// Package usecase implements application business logic. Each logic group in own file.
package usecase

import (
	"context"

	"github.com/evrone/go-clean-template/internal/entity"
)

//go:generate mockgen -source=interfaces.go -destination=./mocks_test.go -package=usecase_test

type (
	// Translation -.
	Translation interface {
		Translate(context.Context, entity.Translation) (entity.Translation, error)
		History(context.Context) ([]entity.Translation, error)
	}

	// TranslationRepo -.
	TranslationRepo interface {
		Store(context.Context, entity.Translation) error
		GetHistory(context.Context) ([]entity.Translation, error)
	}

	// TranslationWebAPI -.
	TranslationWebAPI interface {
		Translate(entity.Translation) (entity.Translation, error)
	}

	Movie interface {
		Index(context.Context, entity.Movie) error
		Search(context.Context, entity.Search) ([]entity.Movie, error)
		AutoComplete(context.Context, entity.AutoComplete) ([]entity.Movie, error)
		GetByGenre(context.Context, entity.GetByGenre) ([]entity.Movie, error)
	}

	MovieElasticSearchRepo interface {
		Index(context.Context, entity.Movie) error
		Search(context.Context, entity.Search) ([]entity.Movie, error)
		AutoComplete(context.Context, entity.AutoComplete) ([]entity.Movie, error)
		GetByGenre(context.Context, entity.GetByGenre) ([]entity.Movie, error)
	}
)
