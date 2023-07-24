// Package app configures and runs application.
package app

import (
	"fmt"
	"os"
	"os/signal"
	"syscall"
	"tenflix/lintang/order-aggregator-service/internal/controller/http/middleware"
	"tenflix/lintang/order-aggregator-service/internal/usecase/webapi"
	"tenflix/lintang/order-aggregator-service/pkg/consul"
	"tenflix/lintang/order-aggregator-service/pkg/keycloak"

	"github.com/gin-gonic/gin"

	"tenflix/lintang/order-aggregator-service/config"
	v1 "tenflix/lintang/order-aggregator-service/internal/controller/http/v1"
	"tenflix/lintang/order-aggregator-service/internal/usecase"
	"tenflix/lintang/order-aggregator-service/internal/usecase/grpcapi"
	"tenflix/lintang/order-aggregator-service/pkg/grpc"
	"tenflix/lintang/order-aggregator-service/pkg/httpserver"
	"tenflix/lintang/order-aggregator-service/pkg/logger"
)

// Run creates objects via constructors.
func Run(cfg *config.Config) {
	l := logger.New(cfg.Log.Level)

	//// Repository
	//pg, err := postgres.New(cfg.PG.URL, postgres.MaxPoolSize(cfg.PG.PoolMax))
	//
	//if err != nil {
	//	l.Fatal(fmt.Errorf("app - Run - postgres.New: %w", err))
	//}
	//defer pg.Close()

	kc := keycloak.NewKeycloak(cfg.KC.ClientId, cfg.KC.ClientSecret, cfg.KC.Realm, cfg.KC.Hostname)

	// Use case
	//translationUseCase := usecase.New(
	//	repo.New(pg),
	//	webapi.New(),
	//)

	// grpc client
	subscGrpc := &grpc.ServiceClient{
		SubscriptionClient: grpc.InitSubscriptionServiceClient(),
	}

	orderGrpc := &grpc.ServiceClient{
		OrderClient: grpc.InitOrderServiceClient(),
	}

	paymentGrpc := &grpc.ServiceClient{
		PaymentClient: grpc.InitPaymentServiceClient(),
	}

	subscriptionUseCase := usecase.NewSubscriptionUseCase(
		grpcapi.New(subscGrpc),
	)

	orderUseCase := usecase.NewOrderUseCase(
		grpcapi.NewOrderGrpc(orderGrpc),
		grpcapi.New(subscGrpc),
		webapi.NewKeycloak(),
		grpcapi.NewPaymentGrpc(paymentGrpc),
	)

	// consul registration
	consul.ServiceRegistryWithConsul()

	//keycloak
	middleware.New(kc)
	middleware.Initialize()

	// HTTP Server
	handler := gin.New()
	v1.NewRouter(handler, l, subscriptionUseCase, orderUseCase)
	httpServer := httpserver.New(handler, httpserver.Port(cfg.HTTP.Port))

	// Waiting signal
	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt, syscall.SIGTERM)

	select {
	case s := <-interrupt:
		l.Info("app - Run - signal: " + s.String())
	case err := <-httpServer.Notify():
		l.Error(fmt.Errorf("app - Run - httpServer.Notify: %w", err))
		//case err = <-rmqServer.Notify():
		//	l.Error(fmt.Errorf("app - Run - rmqServer.Notify: %w", err))
	}

	// Shutdown
	err := httpServer.Shutdown()
	if err != nil {
		l.Error(fmt.Errorf("app - Run - httpServer.Shutdown: %w", err))
	}

	//err = rmqServer.Shutdown()
	//if err != nil {
	//	l.Error(fmt.Errorf("app - Run - rmqServer.Shutdown: %w", err))
	//}
}
