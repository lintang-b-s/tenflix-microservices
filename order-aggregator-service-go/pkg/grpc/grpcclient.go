package grpc

import (
	"fmt"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"tenflix/lintang/order-aggregator-service/pkg/consul"

	//"google.golang.org/grpc/resolver"
	"log"
	"tenflix/lintang/order-aggregator-service/pb"
	//"github.com/simplesurance/grpcconsulresolver/consul"
)

type ServiceClient struct {
	OrderClient        pb.OrderServiceClient
	PaymentClient      pb.PaymentServiceClient
	SubscriptionClient pb.SubscriptionServiceClient
}

func InitOrderServiceClient(consulHost string) pb.OrderServiceClient {

	consul.Init()
	cc, err := grpc.Dial("consul://"+consulHost+":8500/orderService?wait=30s", grpc.WithTransportCredentials(insecure.NewCredentials()), grpc.WithDefaultServiceConfig(`{"loadBalancingPolicy": "round_robin"}`))
	//cc, err := grpc.Dial("http://localhost:9600?wait=30s", grpc.WithTransportCredentials(insecure.NewCredentials()))

	if err != nil {
		fmt.Println("Could not connect:", err)
	}

	return pb.NewOrderServiceClient(cc)
}

func InitPaymentServiceClient(consulHost string) pb.PaymentServiceClient {
	consul.Init()
	cc, err := grpc.Dial("consul://"+consulHost+":8500/paymentService?wait=30s", grpc.WithTransportCredentials(insecure.NewCredentials()), grpc.WithDefaultServiceConfig(`{"loadBalancingPolicy": "round_robin"}`))
	if err != nil {
		fmt.Println("Could not connect:", err)
	}

	return pb.NewPaymentServiceClient(cc)
}

func InitSubscriptionServiceClient(consulHost string) pb.SubscriptionServiceClient {

	consul.Init()
	// kalo di docker ganti ke 172.1.1.17:8500

	cc, err := grpc.Dial("consul://"+consulHost+":8500/subscriptionService", grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithDefaultServiceConfig(`{"loadBalancingPolicy": "round_robin"}`),
	)

	if err != nil {
		log.Fatal("Could not connect:", err)
	}
	//defer cc.Close()

	return pb.NewSubscriptionServiceClient(cc)
}
