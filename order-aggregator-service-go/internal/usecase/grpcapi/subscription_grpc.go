package grpcapi

import (
	"context"
	"fmt"
	"tenflix/lintang/order-aggregator-service/internal/helper"
	//"github.com/satori/go.uuid"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"tenflix/lintang/order-aggregator-service/pb"
	"tenflix/lintang/order-aggregator-service/pkg/grpc"
)

type SubscriptionGrpcAPI struct {
	c *grpc.ServiceClient
}

func (s *SubscriptionGrpcAPI) GetActiveSubscription(ctx context.Context, userId string) error {
	//TODO implement me
	subscriptionClient := s.c.SubscriptionClient
	_, err := subscriptionClient.GetActiveSubscription(context.Background(), &pb.GetActiveSubscriptionRequest{
		GetActiveSubscriptionDto: &pb.GetActiveSubscriptionDto{
			UserId: userId,
		},
	})
	if err != nil {
		return fmt.Errorf("SubscriptionGrpcAPI - GetActiveSubscription - subscriptionClient.GetActiveSubscription: %w", err)
	}

	return nil

}

func New(subscGrpc *grpc.ServiceClient) *SubscriptionGrpcAPI {
	return &SubscriptionGrpcAPI{subscGrpc}
}

func (s *SubscriptionGrpcAPI) CreatePlanGrpc(ctx context.Context, createPlanRequest entity.CreatePlanRequest) (entity.Plan, error) {
	subscriptionClient := s.c.SubscriptionClient

	res, err := subscriptionClient.CreatePlan(context.Background(), &pb.CreatePlanGrpcRequest{
		CreatePlanDto: &pb.CreatePlanDto{
			Name:          createPlanRequest.Name,
			Price:         uint64(createPlanRequest.Price),
			Description:   createPlanRequest.Description,
			ActivePeriod:  uint32(createPlanRequest.ActivePeriod),
			DiscountPrice: uint32(createPlanRequest.DiscountPrice),
		},
	})

	if err != nil {
		return entity.Plan{}, fmt.Errorf("SubscriptionGrpcAPI - CreatePlanGrpc - subscriptionClient.CreatePlan: %w", err)
	}

	plan := entity.Plan{
		PlanId:        int32(res.PlanDto.PlanId),
		Name:          res.PlanDto.Name,
		Price:         int32(res.PlanDto.Price),
		Description:   res.PlanDto.Description,
		ActivePeriod:  int32(res.PlanDto.ActivePeriod),
		DiscountPrice: int32(res.PlanDto.DiscountPrice),
	}

	return plan, nil

}

func (s *SubscriptionGrpcAPI) GetUserCurrentSubscription(ctx context.Context, userId string) (entity.Subscription, error) {
	subscriptionClient := s.c.SubscriptionClient

	res, err := subscriptionClient.GetUserCurrentSubscription(context.Background(), &pb.GetUserCurrentSubscriptionRequest{
		GetSubscriptionRequestDto: &pb.GetActiveSubscriptionDto{
			UserId: userId,
		},
	})

	if err != nil {
		return entity.Subscription{}, fmt.Errorf("SubscriptionGrpcAPI - GetUserCurrentSubscription - subscriptionClient.GetUserCurrentSubscription: %w", err)
	}

	//uId, err := uuid.Parse(res.SubscriptionDto.UserId)

	endDate := res.SubscriptionDto.EndSubscriptionDate.AsTime()
	subscription := entity.Subscription{
		Id:                  res.SubscriptionDto.Id,
		UserId:              res.SubscriptionDto.UserId,
		EndSubscriptionDate: endDate,
		Status:              res.SubscriptionDto.Status,
		Plan:                helper.PlanDtoProtoToPlan(res.SubscriptionDto.Plan),
	}

	return subscription, nil
}

func (s *SubscriptionGrpcAPI) GetPlan(ctx context.Context, createOrderRequest entity.CreateOrderRequest, userId string) (pb.PlanDto, error) {
	subscriptionClient := s.c.SubscriptionClient

	res, err := subscriptionClient.GetPlan(context.Background(), &pb.GetPlanGrpcRequest{
		CreateOrder: &pb.CreateOrder{
			UserId: userId,
			PlanId: uint64(createOrderRequest.PlanId),
		},
	})

	if err != nil {
		return pb.PlanDto{}, fmt.Errorf("SubscriptionGrpcAPI - GetPlan - subscriptionClient.GetPlan: %w", err)
	}

	planDto := *res.Plan
	return planDto, nil
}

func (s *SubscriptionGrpcAPI) GetSubscriptionDetail(ctx context.Context, orderId string, userId string) (entity.Subscription, error) {
	subscriptionClient := s.c.SubscriptionClient

	res, err := subscriptionClient.GetUserSubscriptionByOrderId(context.Background(),
		&pb.GetUserSubscriptionByOrderIdRequest{
			OrderId: orderId,
			UserId:  userId,
		})
	if err != nil {

		return entity.Subscription{}, fmt.Errorf("SubscriptionGrpcAPI - GetSubscriptionDetail - subscriptionClient.GetUserSubscriptionByOrderId: %w", err)
	}

	//uId, _ := uuid.Parse(res.SubscriptionDto.UserId)

	endDate := res.SubscriptionDto.EndSubscriptionDate.AsTime()
	subscription := entity.Subscription{
		Id:                  res.SubscriptionDto.Id,
		UserId:              res.SubscriptionDto.UserId,
		EndSubscriptionDate: endDate,
		Status:              res.SubscriptionDto.Status,
		Plan:                helper.PlanDtoProtoToPlan(res.SubscriptionDto.Plan),
	}

	return subscription, nil
}
