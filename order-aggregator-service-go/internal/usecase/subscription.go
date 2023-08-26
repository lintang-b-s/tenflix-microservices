package usecase

import (
	"context"
	"fmt"
	"tenflix/lintang/order-aggregator-service/internal/entity"
)

// SubscriptionUseCase -.
type SubscriptionUseCase struct {
	grpcAPI SubscriptionGrpcAPI
}

func NewSubscriptionUseCase(g SubscriptionGrpcAPI) *SubscriptionUseCase {
	return &SubscriptionUseCase{
		grpcAPI: g,
	}
}

func (uc *SubscriptionUseCase) CreatePlan(ctx context.Context, c entity.CreatePlanRequest) (entity.Plan, error) {
	createPlanResponse, err := uc.grpcAPI.CreatePlanGrpc(ctx, c)

	if err != nil {
		return entity.Plan{}, fmt.Errorf("SubscriptionUseCase - CreatePlan - s.grpcAPI.CreatePlanGrpc: %w", err)
	}

	return createPlanResponse, nil
}

func (uc *SubscriptionUseCase) CheckUserSubscription(ctx context.Context, userId string) (entity.Subscription, error) {
	subscription, err := uc.grpcAPI.GetUserCurrentSubscription(ctx, userId)
	if err != nil {
		return entity.Subscription{}, fmt.Errorf("SubscriptionUseCase - CheckUserSubscription - s.grpcAPI.GetUserCurrentSubscription: %w", err)
	}

	return subscription, nil

}
