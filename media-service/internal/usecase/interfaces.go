// Package usecase implements application business logic. Each logic group in own file.
package usecase

import (
	"context"

	"tenflix/lintang/order-aggregator-service/internal/entity"
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

	Upload interface {
		UploadVideo(ctx context.Context, u entity.UploadVideoMessage) error
	}

	CloudinaryWebAPI interface {
		Upload(tx context.Context, message entity.UploadVideoMessage) (entity.UploadedVideoMessage, error)
	}
)
