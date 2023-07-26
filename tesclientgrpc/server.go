package main

import (
	"context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
	"log"
	"net"
	pb "tenflix/lintang/order-aggregator-service/pb"
)

type server struct {
	pb.UnimplementedSubscriptionServiceServer
}

func (c *server) CreatePlan(ctx context.Context, in *pb.CreatePlanGrpcRequest) (*pb.CreatePlanGrpcResponse, error) {
	log.Printf("received request: %v", in.ProtoReflect().Descriptor().FullName())
	return &pb.CreatePlanGrpcResponse{
		PlanDto: &pb.PlanDto{
			Name:          in.CreatePlanDto.Name,
			PlanId:        1,
			Price:         in.CreatePlanDto.Price,
			Description:   in.CreatePlanDto.Description,
			ActivePeriod:  in.CreatePlanDto.ActivePeriod,
			DiscountPrice: in.CreatePlanDto.DiscountPrice,
		},
	}, nil

}

func main() {
	listener, err := net.Listen("tcp", ":9000")
	if err != nil {
		panic(err)
	}
	s := grpc.NewServer()
	reflection.Register(s)
	pb.RegisterSubscriptionServiceServer(s, &server{})
	if err := s.Serve(listener); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
	log.Printf("server started")
}
