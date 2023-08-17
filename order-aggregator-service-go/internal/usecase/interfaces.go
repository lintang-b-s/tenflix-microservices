// Package usecase implements application business logic. Each logic group in own file.
package usecase

import (
	"context"
	"tenflix/lintang/order-aggregator-service/config"
	"tenflix/lintang/order-aggregator-service/pb"

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

	// Subscription
	Subscription interface {
		CreatePlan(context.Context, entity.CreatePlanRequest) (entity.Plan, error)
		CheckUserSubscription(context.Context, string) (entity.Subscription, error)
	}

	//SubscriptionGrpcAPI
	SubscriptionGrpcAPI interface {
		CreatePlanGrpc(context.Context, entity.CreatePlanRequest) (entity.Plan, error)
		GetUserCurrentSubscription(context.Context, string) (entity.Subscription, error)
		GetPlan(context.Context, entity.CreateOrderRequest, string) (pb.PlanDto, error)
		GetActiveSubscription(context.Context, string) error
		GetSubscriptionDetail(context.Context, string, string) (entity.Subscription, error)
	}

	// OrderUseCase -.
	Order interface {
		CreateOrder(context.Context, entity.CreateOrderRequest, string) (entity.Order, string, string, error)
		ProcessOrder(context.Context, map[string]interface{}, *config.Config) error
		GetOrderDetail(context.Context, string, string) (entity.Subscription, entity.Order, entity.Payment, error)
		GetOrderHistory(context.Context, string) ([]entity.Order, error)
	}

	// OrderGrpcAPI
	OrderGrpcAPI interface {
		CreateOrder(context.Context, entity.CreateOrderRequest, pb.PlanDto, string) (entity.Order, error)
		ProcessOrderGrpc(context.Context, map[string]interface{}, *config.Config) error
		GetUserOrderDetail(context.Context, string, string) (entity.Order, error)
		GetUserOrderHistory(context.Context, string) ([]entity.Order, error)
	}

	// KeycloakWebAPI
	KeycloakWebAPI interface {
		GetUserDetail(context.Context, string) (entity.KeycloakUserDto, error)
	}

	// PaymentGrpcAPI
	PaymentGrpcAPI interface {
		GetPaymentRedirectUrl(context.Context, entity.Order, entity.KeycloakUserDto, pb.PlanDto) (string, error)
		GetPaymentDetailFromPaymentService(context.Context, string) (entity.Payment, error)
	}
)
