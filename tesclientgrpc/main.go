package main

import (
	"context"
	_ "github.com/mbobakov/grpc-consul-resolver"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"log"
	"tenflix/lintang/order-aggregator-service/consul"
	"tenflix/lintang/order-aggregator-service/pb"
)

const (
	consulScheme = "consul"
	target       = "consul://127.0.0.1:8500/subscriptionService"
)

func main() {
	//conn, err := grpc.Dial("localhost:9000", grpc.WithTransportCredentials(insecure.NewCredentials()))

	// with consul resolver

	consul.Init()
	//ctx, _ := context.WithTimeout(context.Background(), 5*time.Second)
	//
	conn, err := grpc.Dial(target, grpc.WithBlock(), grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithDefaultServiceConfig(`{"loadBalancingPolicy": "round_robin"}`),
	)

	if err != nil {
		log.Fatal("failed to connect :%v", err)
	}

	defer conn.Close()

	client := pb.NewSubscriptionServiceClient(conn)
	//ctxe, _ := context.WithTimeout(context.Background(), time.Second)
	plan, err := client.CreatePlan(context.Background(), &pb.CreatePlanGrpcRequest{
		CreatePlanDto: &pb.CreatePlanDto{
			Name:          "30 days subscription",
			Price:         30,
			Description:   "30 days subscription",
			ActivePeriod:  30,
			DiscountPrice: 30,
		},
	})
	if err != nil {
		log.Fatal("failed to save plan!: %v", err)
	}

	log.Printf("plan: %v", plan)

}
