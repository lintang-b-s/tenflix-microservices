package grpc

import (
	"fmt"
	"google.golang.org/grpc/credentials/insecure"
	"log"
	"tenflix/lintang/order-aggregator-service/pkg/consul"

	"google.golang.org/grpc"
	"tenflix/lintang/order-aggregator-service/pb"
)

type ServiceClient struct {
	OrderClient        pb.OrderServiceClient
	PaymentClient      pb.PaymentServiceClient
	SubscriptionClient pb.SubscriptionServiceClient
}

func InitOrderServiceClient() pb.OrderServiceClient {
	consul.Init()
	cc, err := grpc.Dial("consul://172.1.1.17:8500/orderService?wait=30s", grpc.WithInsecure(), grpc.WithDefaultServiceConfig(`{"loadBalancingPolicy": "round_robin"}`))
	if err != nil {
		fmt.Println("Could not connect:", err)
	}
	defer cc.Close()

	return pb.NewOrderServiceClient(cc)
}

func InitPaymentServiceClient() pb.PaymentServiceClient {
	consul.Init()
	cc, err := grpc.Dial("consul://172.1.1.17:8500/paymentService?wait=30s", grpc.WithInsecure(), grpc.WithDefaultServiceConfig(`{"loadBalancingPolicy": "round_robin"}`))
	// harus paymentService tanpa - , karena kalo pake - gak diresolve sama consul
	if err != nil {
		fmt.Println("Could not connect:", err)
	}
	defer cc.Close()

	return pb.NewPaymentServiceClient(cc)
}

func InitSubscriptionServiceClient() pb.SubscriptionServiceClient {

	consul.Init()
	// kalo di docker ganti ke 172.1.1.17:8500

	cc, err := grpc.Dial("consul://127.0.0.1:8500/subscriptionService", grpc.WithBlock(), grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithDefaultServiceConfig(`{"loadBalancingPolicy": "round_robin"}`),
	)

	if err != nil {
		log.Fatal("Could not connect:", err)
	}
	//defer cc.Close()

	return pb.NewSubscriptionServiceClient(cc)
}
