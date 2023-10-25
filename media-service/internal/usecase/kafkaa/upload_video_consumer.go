package kafkaa

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/avast/retry-go"
	"github.com/segmentio/kafka-go"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"time"
)

const (
	retryAttempts = 3
	retryDelay    = 300 * time.Millisecond
)

var (
	retryOptions = []retry.Option{retry.Attempts(retryAttempts), retry.Delay(retryDelay), retry.DelayType(retry.BackOffDelay)}
)

func (s *readerMessageProcessor) processUploadVideo(ctx context.Context, r *kafka.Reader, m kafka.Message) {

	msg := &entity.UploadVideoMessage{}

	if err := json.Unmarshal(m.Value, msg); err != nil {
		s.logger.Error(fmt.Errorf(" kafkaa unmarshaling json error : %w", err))
		s.commitErrMessage(ctx, r, m)
		return
	}

	message := entity.UploadVideoMessage{
		Id:   msg.Id,
		File: msg.File,
	}

	s.uu.UploadVideo(ctx, message)
	fmt.Println("success upload video!!")
	s.commitMessage(ctx, r, m)
}
