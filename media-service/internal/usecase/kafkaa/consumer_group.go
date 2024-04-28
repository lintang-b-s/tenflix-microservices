package kafkaa

import (
	"context"
	"fmt"
	"tenflix/lintang/order-aggregator-service/config"
	"tenflix/lintang/order-aggregator-service/internal/usecase"
	"tenflix/lintang/order-aggregator-service/pkg/logger"

	"github.com/segmentio/kafka-go"

	"sync"
)

const (
	PoolSize = 30
)

type readerMessageProcessor struct {
	cfg *config.Config
	uu  usecase.UploadUseCase

	logger logger.Interface
}

func NewReaderMessageProcessor(cfg *config.Config, uu *usecase.UploadUseCase, logger logger.Interface) *readerMessageProcessor {

	return &readerMessageProcessor{cfg: cfg, uu: *uu, logger: logger}
}

func (s *readerMessageProcessor) ProcessMessages(ctx context.Context, r *kafka.Reader, wg *sync.WaitGroup, workerID int) {
	defer wg.Done()

	for {
		select {
		case <-ctx.Done():
			return
		default:
		}

		m, err := r.FetchMessage(ctx)
		if err != nil {
			s.logger.Error(fmt.Errorf("error :%w", err))
			continue
		}

		s.logger.KafkaInfo(m, workerID)

		switch m.Topic {
		case s.cfg.Kafka.UploadTopicsName:
			s.processUploadVideo(ctx, r, m)
		}

	}
}
