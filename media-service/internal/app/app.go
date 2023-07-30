// Package app configures and runs application.
package app

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"syscall"
	"tenflix/lintang/order-aggregator-service/internal/usecase"
	readerKafka "tenflix/lintang/order-aggregator-service/internal/usecase/kafka"
	"tenflix/lintang/order-aggregator-service/internal/usecase/webapi"

	"github.com/gin-gonic/gin"

	"tenflix/lintang/order-aggregator-service/config"
	"tenflix/lintang/order-aggregator-service/pkg/httpserver"
	kafkaClient "tenflix/lintang/order-aggregator-service/pkg/kafka"
	"tenflix/lintang/order-aggregator-service/pkg/logger"
)

// Run creates objects via constructors.
func Run(cfg *config.Config) {
	l := logger.New(cfg.Log.Level)
	ctx, cancel := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM, syscall.SIGINT)
	defer cancel()

	// kafka producer
	kafkaProducer := kafkaClient.NewProducer(l, cfg.Kafka.Brokers)
	defer kafkaProducer.Close()

	uploadUseCase := usecase.NewUploadUseCase(
		webapi.NewCloudinary(cfg),
		kafkaProducer,
	)

	// HTTP Server
	handler := gin.New()
	//v1.NewRouter(handler, l, translationUseCase)
	//v1.NewRouter(handler, l)

	httpServer := httpserver.New(handler, httpserver.Port(cfg.HTTP.Port))

	// kafka consumer
	readerMessageProcessor := readerKafka.NewReaderMessageProcessor(cfg, uploadUseCase, l)
	cg := kafkaClient.NewConsumerGroup(cfg.Kafka.Brokers, cfg.Kafka.GroupId, l)
	go cg.ConsumeTopic(ctx, []string{cfg.Kafka.UploadTopicsName}, readerKafka.PoolSize, readerMessageProcessor.ProcessMessages)
	kafkaConn, err := kafkaClient.New(ctx, cfg, l)
	if err != nil {
		l.Fatal(fmt.Errorf("app - Run - kafkaClient.New: %w", err))
	}
	defer kafkaConn.Close()

	// Waiting signal
	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt, syscall.SIGTERM)

	select {
	case s := <-interrupt:
		l.Info("app - Run - signal: " + s.String())
	case err := <-httpServer.Notify():
		l.Error(fmt.Errorf("app - Run - httpServer.Notify: %w", err))

	}

	// Shutdown
	err = httpServer.Shutdown()
	if err != nil {
		l.Error(fmt.Errorf("app - Run - httpServer.Shutdown: %w", err))
	}

}
