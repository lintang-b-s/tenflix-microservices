package usecase

import (
	"context"
	"fmt"
	"tenflix/lintang/order-aggregator-service/internal/entity"
)

type OrderUseCase struct {
	orderGrpcAPI        OrderGrpcAPI
	subscriptionGrpcAPI SubscriptionGrpcAPI
	keycloakWebAPI      KeycloakWebAPI
	paymentGrpcAPI      PaymentGrpcAPI
}

func NewOrderUseCase(o OrderGrpcAPI, s SubscriptionGrpcAPI, k KeycloakWebAPI, p PaymentGrpcAPI) *OrderUseCase {
	return &OrderUseCase{
		orderGrpcAPI:        o,
		subscriptionGrpcAPI: s,
		keycloakWebAPI:      k,
		paymentGrpcAPI:      p,
	}
}

func (uc *OrderUseCase) CreateOrder(ctx context.Context, c entity.CreateOrderRequest, userId string) (entity.Order, string, string, error) {
	planDto, err := uc.subscriptionGrpcAPI.GetPlan(ctx, c, userId)
	if err != nil {
		return entity.Order{}, "", "", fmt.Errorf("OrderUseCase - CreateOrder - o.subscriptionGrpcAPI.GetPlan: %w", err)
	}

	keycloakUser, err := uc.keycloakWebAPI.GetUserDetail(ctx, userId)
	if err != nil {
		return entity.Order{}, "", "", fmt.Errorf("OrderUseCase - CreateOrder - o.keycloakWebAPI.GetUserDetail: %w", err)
	}

	err = uc.subscriptionGrpcAPI.GetActiveSubscription(ctx, userId)
	if err != nil {
		return entity.Order{}, "", "", fmt.Errorf("OrderUseCase - CreateOrder - o.subscriptionGrpcAPI.GetActiveSubscription: %w", err)
	}

	order, err := uc.orderGrpcAPI.CreateOrder(ctx, c, planDto, userId)
	if err != nil {
		return entity.Order{}, "", "", fmt.Errorf("OrderUseCase - CreateOrder - o.orderGrpcAPI.CreateOrder: %w", err)
	}

	redirectUrl, err := uc.paymentGrpcAPI.GetPaymentRedirectUrl(ctx, order, keycloakUser, planDto)
	if err != nil {
		return entity.Order{}, "", "", fmt.Errorf("OrderUseCase - CreateOrder - o.paymentGrpcAPI.GetPaymentRedirectUrl: %w", err)
	}

	return order, order.OrderStatus, redirectUrl, nil

}

func (uc *OrderUseCase) ProcessOrder(ctx context.Context, notificationRes map[string]interface{}) error {
	err := uc.orderGrpcAPI.ProcessOrderGrpc(ctx, notificationRes)
	if err != nil {
		return fmt.Errorf("OrderUseCase - ProcessOrder - o.orderGrpcAPI.ProcessOrderGrpc: %w", err)
	}

	return nil
}

func (uc *OrderUseCase) GetOrderDetail(ctx context.Context, orderId string, userId string) (entity.Subscription, entity.Order, entity.Payment, error) {
	order, err := uc.orderGrpcAPI.GetUserOrderDetail(ctx, orderId, userId)
	if err != nil {
		return entity.Subscription{}, entity.Order{}, entity.Payment{}, fmt.Errorf("OrderUseCase - GetOrderDetail - o.orderGrpcAPI.GetUserOrderDetail: %w", err)
	}
	payment, err := uc.paymentGrpcAPI.GetPaymentDetailFromPaymentService(ctx, orderId)
	if err != nil {
		return entity.Subscription{}, entity.Order{}, entity.Payment{}, fmt.Errorf("OrderUseCase - GetOrderDetail - o.paymentGrpcAPI.GetPaymentDetailFromPaymentService: %w", err)
	}
	subscription, err := uc.subscriptionGrpcAPI.GetSubscriptionDetail(ctx, orderId, userId)
	if err != nil {
		return entity.Subscription{}, entity.Order{}, entity.Payment{}, fmt.Errorf("OrderUseCase - GetOrderDetail - o.subscriptionGrpcAPI.GetSubscriptionDetail: %w", err)
	}

	return subscription, order, payment, nil
}

func (uc *OrderUseCase) GetOrderHistory(ctx context.Context, userId string) ([]entity.Order, error) {
	orders, err := uc.orderGrpcAPI.GetUserOrderHistory(ctx, userId)
	if err != nil {
		return []entity.Order{}, fmt.Errorf("OrderUseCase - GetOrderHistory - o.orderGrpcAPI.GetUserOrderHistory: %w", err)
	}

	return orders, nil
}
