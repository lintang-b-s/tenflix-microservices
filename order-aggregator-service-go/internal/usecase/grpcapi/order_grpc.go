package grpcapi

import (
	"context"
	"fmt"
	"github.com/google/uuid"
	"github.com/midtrans/midtrans-go"
	"github.com/midtrans/midtrans-go/coreapi"
	"io"
	"tenflix/lintang/order-aggregator-service/config"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"tenflix/lintang/order-aggregator-service/pb"
	"tenflix/lintang/order-aggregator-service/pkg/grpc"
)

var cMid coreapi.Client

type OrderGrpcAPI struct {
	c *grpc.ServiceClient
}

func NewOrderGrpc(orderGrpc *grpc.ServiceClient) *OrderGrpcAPI {
	return &OrderGrpcAPI{orderGrpc}
}

func (o *OrderGrpcAPI) CreateOrder(ctx context.Context, c entity.CreateOrderRequest, plan pb.PlanDto, userId string) (entity.Order, error) {
	orderClient := o.c.OrderClient

	createOrderGrpcRequest := &pb.CreateOrderGrpcRequest{
		Order: &pb.OrderDto{
			Id:              uuid.NewString(),
			UserId:          userId,
			Price:           plan.Price,
			OrderStatus:     pb.OrderStatus_PENDING,
			PaymentId:       uuid.NewString(),
			FailureMessages: "",
			Plan: &pb.OrderPlanDto{
				Id:       uuid.NewString(),
				PlanId:   plan.PlanId,
				Price:    plan.Price,
				SubTotal: plan.Price,
			},
		},
	}
	res, err := orderClient.CreateOrder(context.Background(), createOrderGrpcRequest)
	if err != nil {
		return entity.Order{}, fmt.Errorf("OrderGrpcAPI - CreateOrder - orderClient.CreateOrder: %w", err)
	}

	order := entity.Order{
		Id:              res.CreatedOrder.Id,
		UserId:          res.CreatedOrder.UserId,
		Price:           int64(res.CreatedOrder.Price),
		OrderStatus:     res.CreatedOrder.OrderStatus.String(),
		PaymentId:       res.CreatedOrder.PaymentId,
		FailureMessages: res.CreatedOrder.FailureMessages,
		Plan: entity.OrderPlan{
			Id:          res.CreatedOrder.Id,
			Name:        plan.Name,
			Description: plan.Description,
			PlanId:      int64(res.CreatedOrder.Plan.PlanId),
			Price:       int64(plan.Price),
			Subtotal:    int64(plan.Price),
		},
	}

	return order, nil
}

func (o *OrderGrpcAPI) ProcessOrderGrpc(ctx context.Context, notificationRes map[string]interface{}, cfg *config.Config) error {

	cMid.New(cfg.Mt.Server, midtrans.Sandbox)


	orderClient := o.c.OrderClient
	var notifProto = map[string]string{}

	orderId, _ := notificationRes["order_id"].(string)
	transaction, _ := cMid.CheckTransaction(orderId)

	notifProto["order_id"] = orderId
	notifProto["transaction_status"] = transaction.TransactionStatus
	notifProto["fraud_status"] = transaction.FraudStatus
	notifProto["transaction_id"] = transaction.TransactionID

	notifProto["gross_amount"] = transaction.GrossAmount
	notifProto["transaction_time"] = transaction.TransactionTime
	notifProto["payment_type"] = transaction.PaymentType
	notifProto["currency"] = transaction.Currency

	_, err := orderClient.ProcessOrderSaga(context.Background(), &pb.ProcessOrderRequest{
		PaymentNotification: &pb.PaymentNotification{
			NotificationRes: notifProto,
			Bank:            transaction.VaNumbers[0].Bank,
			VaNum:           transaction.VaNumbers[0].VANumber,
		},
	})
	if err != nil {
		return fmt.Errorf("OrderGrpcAPI - ProcessOrderGrpc - orderClient.ProcessOrderSaga: %w", err)
	}

	return nil
}

func (o *OrderGrpcAPI) GetUserOrderDetail(ctx context.Context, orderId string, userId string) (entity.Order, error) {
	orderClient := o.c.OrderClient

	res, err := orderClient.GetUserOrderDetail(context.Background(), &pb.GetUserOrderDetailRequest{
		OrderId: orderId,
		UserId:  userId,
	})

	if err != nil {
		return entity.Order{}, fmt.Errorf("OrderGrpcAPI - GetUserOrderDetail - orderClient.GetUserOrderDetail: %w", err)
	}
	order := entity.Order{
		Id:              res.OrderDto.Id,
		UserId:          res.OrderDto.UserId,
		Price:           int64(res.OrderDto.Price),
		OrderStatus:     res.OrderDto.OrderStatus.String(),
		PaymentId:       res.OrderDto.PaymentId,
		FailureMessages: res.OrderDto.FailureMessages,
		Plan: entity.OrderPlan{
			Id:       res.OrderDto.Id,
			PlanId:   int64(res.OrderDto.Plan.PlanId),
			Price:    int64(res.OrderDto.Price),
			Subtotal: int64(res.OrderDto.Price),
		},
	}
	return order, nil
}

func (o *OrderGrpcAPI) GetUserOrderHistory(ctx context.Context, userId string) ([]entity.Order, error) {
	orderClient := o.c.OrderClient

	stream, err := orderClient.GetUserOrderHistory(context.Background(), &pb.GetUserOrderHistoryRequest{
		UserId: userId,
	})

	if err != nil {
		return []entity.Order{}, fmt.Errorf("OrderGrpcAPI - GetUserOrderHistory - orderClient.GetUserOrderHistory: %w", err)
	}
	var orders []entity.Order
	for {
		res, err := stream.Recv()
		if err == io.EOF {
			return orders, nil
		}
		if err != nil {
			return []entity.Order{}, fmt.Errorf("OrderGrpcAPI - GetUserOrderHistory - orderClient.GetUserOrderHistory (stream): %w", err)
		}
		order := entity.Order{
			Id:              res.OrderDto.Id,
			UserId:          res.OrderDto.UserId,
			Price:           int64(res.OrderDto.Price),
			OrderStatus:     res.OrderDto.OrderStatus.String(),
			PaymentId:       res.OrderDto.PaymentId,
			FailureMessages: res.OrderDto.FailureMessages,
			Plan: entity.OrderPlan{
				Id:       res.OrderDto.Id,
				PlanId:   int64(res.OrderDto.Plan.PlanId),
				Price:    int64(res.OrderDto.Price),
				Subtotal: int64(res.OrderDto.Price),
			},
		}
		_ = append(orders, order)

	}
	return orders, nil

}
