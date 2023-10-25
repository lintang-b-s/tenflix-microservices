package kafkaa

import (
	"context"
	"github.com/segmentio/kafka-go"
	"tenflix/lintang/order-aggregator-service/pkg/logger"
)

type Producer interface {
	PublishMessage(ctx context.Context, msgs ...kafka.Message) error
	Close() error
}

type producer struct {
	log     logger.Interface
	brokers []string
	w       *kafka.Writer
}

// NewProducer create new kafkaa producer
func NewProducer(log logger.Interface, brokers []string) *producer {
	return &producer{log: log, brokers: brokers, w: NewWriter(brokers, kafka.LoggerFunc(log.KafkaError))}
}

func (p *producer) PublishMessage(ctx context.Context, msgs ...kafka.Message) error {
	return p.w.WriteMessages(ctx, msgs...)
}

func (p *producer) Close() error {
	return p.w.Close()
}
