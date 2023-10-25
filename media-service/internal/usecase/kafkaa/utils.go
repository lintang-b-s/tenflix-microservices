package kafkaa

import (
	"context"
	"fmt"
	"github.com/segmentio/kafka-go"
)

func (s *readerMessageProcessor) commitMessage(ctx context.Context, r *kafka.Reader, m kafka.Message) {
	s.logger.KafkaLogCommittedMessage(m.Topic, m.Partition, m.Offset)

	if err := r.CommitMessages(ctx, m); err != nil {
		s.logger.Warn(fmt.Sprintf("commitMessage :%v", err))
	}
}

func (s *readerMessageProcessor) logProcessMessage(m kafka.Message, workerID int) {
	s.logger.KafkaProcessMessage(m.Topic, m.Partition, string(m.Value), workerID, m.Offset, m.Time)

}

func (s *readerMessageProcessor) commitErrMessage(ctx context.Context, r *kafka.Reader, m kafka.Message) {
	s.logger.KafkaLogCommittedMessage(m.Topic, m.Partition, m.Offset)
	if err := r.CommitMessages(ctx, m); err != nil {
		s.logger.Warn(fmt.Sprintf("commitMessage :%v", err))
	}
}
