package grpcapi

import (
	"context"
	"fmt"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"tenflix/lintang/order-aggregator-service/pb"
	"tenflix/lintang/order-aggregator-service/pkg/grpc"
)

type PaymentGrpcAPI struct {
	c *grpc.ServiceClient
}

func NewPaymentGrpc(payment *grpc.ServiceClient) *PaymentGrpcAPI {
	return &PaymentGrpcAPI{payment}
}

func (p *PaymentGrpcAPI) GetPaymentRedirectUrl(ctx context.Context, order entity.Order, user entity.KeycloakUserDto, plan pb.PlanDto) (string, error) {
	paymentClient := p.c.PaymentClient
	transactionDetails := &pb.TransactionDetails{
		OrderId:     order.Id,
		GrossAmount: uint64(order.Price),
		CreditCard:  false,
		CustomerDetails: &pb.CustomerDetails{
			Id:      user.Id,
			Name:    user.FirstName + " " + user.LastName,
			Email:   user.Email,
			Phone:   "+62",
			Address: "indonesia",
		},
		ItemDetails: &pb.ItemDetails{
			PlanId: plan.PlanId,
			Price:  plan.Price,
			Name:   plan.Name,
		},
	}
	res, err := paymentClient.GetPaymentRedirectUrl(context.Background(), &pb.PaymentGetRedirectUrlGrpcRequest{
		TransactionDetail: transactionDetails,
	})
	if err != nil {
		return "", fmt.Errorf("PaymentGrpcAPI - GetPaymentRedirectUrl - paymentClient.GetPaymentRedirectUrl")
	}

	return res.GetRedirectUrl.RedirectUrl, nil

}

func (p *PaymentGrpcAPI) GetPaymentDetailFromPaymentService(ctx context.Context, orderId string) (entity.Payment, error) {
	paymentClient := p.c.PaymentClient

	res, err := paymentClient.GetPaymentByOrderId(context.Background(),
		&pb.GetPaymentByOrderIdRequest{
			OrderId: orderId,
		})
	if err != nil {
		return entity.Payment{}, fmt.Errorf("PaymentGrpcAPI - GetPaymentDetailFromPaymentService - paymentClient.GetPaymentByOrderId")
	}
	payemnt := entity.Payment{
		Id:                res.PaymentDetail.Id,
		Status:            res.PaymentDetail.Status,
		GrossAmount:       res.PaymentDetail.GrossAmount,
		TransactionTime:   res.PaymentDetail.TransactionTime,
		TransactionId:     res.PaymentDetail.TransactionId,
		TransactionStatus: res.PaymentDetail.TransactionStatus,
		PaymentType:       res.PaymentDetail.PaymentType,
		FraudStatus:       res.PaymentDetail.FraudStatus,
		Bank:              res.PaymentDetail.Bank,
		VaNumber:          res.PaymentDetail.VaNumber,
		Currency:          res.PaymentDetail.Currency,
		OrderId:           res.PaymentDetail.OrderId,
	}
	return payemnt, nil
}
