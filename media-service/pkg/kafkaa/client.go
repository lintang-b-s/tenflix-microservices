package kafkaa

import (
	"context"
	"fmt"
	"github.com/pkg/errors"
	"tenflix/lintang/order-aggregator-service/config"
	"tenflix/lintang/order-aggregator-service/pkg/logger"

	"github.com/segmentio/kafka-go"
)

var errChan chan error

type KafkaClient struct {
	conn *kafka.Conn
	l    logger.Interface
}

func New(ctx context.Context, cfg *config.Config, l logger.Interface) (*kafka.Conn, error) {
	//var kafkaConn *kafkaa.Conn
	kafkaConn, err := NewKafkaConn(ctx, cfg)
	if err != nil {
		return nil, errors.Wrap(err, "kafkaa.NewKafkaCon")
	}

	//var brokers []kafkaa.Broker
	brokers, err := kafkaConn.Brokers()
	if err != nil {
		return nil, errors.Wrap(err, "kafkaConn.Brokers")
	}
	l.Info(fmt.Sprintf("kafkaa connected to brokers :%+v", brokers))

	return kafkaConn, nil

}

// NewKafkaConn create new kafkaa connection
func NewKafkaConn(ctx context.Context, kafkaCfg *config.Config) (*kafka.Conn, error) {
	return kafka.DialContext(ctx, "tcp", kafkaCfg.Kafka.Brokers[0])
}
