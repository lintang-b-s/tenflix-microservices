package usecase

import (
	"context"
	"fmt"
	"sync"
	"tenflix/lintang/order-aggregator-service/config"
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

	// conccurent
	var Wait = sync.WaitGroup{}
	//var err error
	errcSubsc := make(chan error)
	errcKc := make(chan error)

	var keycloakUser entity.KeycloakUserDto
	Wait.Add(2)
	go func() {
		defer Wait.Done()
		keycloakUser, err = uc.keycloakWebAPI.GetUserDetail(ctx, userId)
		if err != nil {
			errcKc <- fmt.Errorf("OrderUseCase - CreateOrder - o.keycloakWebAPI.GetUserDetail: %w", err)
		} else {
			errcKc <- nil
		}
	}()

	go func() {
		defer Wait.Done()
		err = uc.subscriptionGrpcAPI.GetActiveSubscription(ctx, userId)
		if err != nil {
			errcSubsc <- fmt.Errorf("OrderUseCase - CreateOrder - o.subscriptionGrpcAPI.GetActiveSubscription: %w", err)
		} else {
			errcSubsc <- nil
		}
	}()

	errKc := <-errcKc
	if errKc != nil {
		return entity.Order{}, "", "", errKc
	}

	errSubs := <-errcSubsc
	if errSubs != nil {
		return entity.Order{}, "", "", errSubs
	}

	Wait.Wait()
	close(errcKc)
	close(errcSubsc)

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

func (uc *OrderUseCase) ProcessOrder(ctx context.Context, notificationRes map[string]interface{}, cfg *config.Config) error {
	err := uc.orderGrpcAPI.ProcessOrderGrpc(ctx, notificationRes, cfg)
	if err != nil {
		return fmt.Errorf("OrderUseCase - ProcessOrder - o.orderGrpcAPI.ProcessOrderGrpc: %w", err)
	}

	return nil
}

func (uc *OrderUseCase) GetOrderDetail(ctx context.Context, orderId string, userId string) (entity.Subscription, entity.Order, entity.Payment, error) {
	var Wait = sync.WaitGroup{}
	errcO := make(chan error)
	errcP := make(chan error)
	errcS := make(chan error)
	var err error

	Wait.Add(3)
	var order entity.Order
	go func() {
		defer Wait.Done()
		order, err = uc.orderGrpcAPI.GetUserOrderDetail(ctx, orderId, userId)
		if err != nil {
			errcO <- fmt.Errorf("OrderUseCase - GetOrderDetail - o.orderGrpcAPI.GetUserOrderDetail: %w", err)
		} else {
			errcO <- nil
		}
	}()

	var payment entity.Payment
	go func() {
		defer Wait.Done()
		payment, err = uc.paymentGrpcAPI.GetPaymentDetailFromPaymentService(ctx, orderId)
		if err != nil {
			errcP <- fmt.Errorf("OrderUseCase - GetOrderDetail - o.paymentGrpcAPI.GetPaymentDetailFromPaymentService: %w", err)
		} else {
			errcP <- nil
		}
	}()

	var subscription entity.Subscription
	go func() {
		defer Wait.Done()
		subscription, err = uc.subscriptionGrpcAPI.GetSubscriptionDetail(ctx, orderId, userId)
		if err != nil {
			errcS <- fmt.Errorf("OrderUseCase - GetOrderDetail - o.subscriptionGrpcAPI.GetSubscriptionDetail: %w", err)
		} else {
			errcS <- nil
		}
	}()
	errMOrder := <-errcO

	errMPayment := <-errcP

	errcSubsc := <-errcS

	Wait.Wait()
	close(errcO)
	close(errcP)
	close(errcS)

	if errMOrder != nil {
		return entity.Subscription{}, entity.Order{}, entity.Payment{}, errMOrder
	}

	// payment not proceessed
	if errMPayment != nil {
		return subscription, order, payment, nil
	}

	// order pending proceessed
	if errcSubsc != nil {
		return subscription, order, payment, nil
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
