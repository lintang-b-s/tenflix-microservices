package logger

import (
	"fmt"
	"github.com/segmentio/kafka-go"
	"os"
	"strings"
	"time"

	"github.com/rs/zerolog"
)

// Interface -.
type Interface interface {
	Debug(message interface{}, args ...interface{})
	Info(message string, args ...interface{})
	Warn(message string, args ...interface{})
	Error(message interface{}, args ...interface{})
	Fatal(message interface{}, args ...interface{})
	KafkaInfo(message kafka.Message, workerId int, args ...interface{})
	KafkaError(message string, args ...interface{})
	KafkaLogCommittedMessage(topic string, partition int, offset int64)
	KafkaProcessMessage(topic string, partition int, message string, workerID int, offset int64, time time.Time)
}

// Logger -.
type Logger struct {
	logger *zerolog.Logger
}

var _ Interface = (*Logger)(nil)

// New -.
func New(level string) *Logger {
	var l zerolog.Level

	switch strings.ToLower(level) {
	case "error":
		l = zerolog.ErrorLevel
	case "warn":
		l = zerolog.WarnLevel
	case "info":
		l = zerolog.InfoLevel
	case "debug":
		l = zerolog.DebugLevel
	default:
		l = zerolog.InfoLevel
	}

	zerolog.SetGlobalLevel(l)

	skipFrameCount := 3
	logger := zerolog.New(os.Stdout).With().Timestamp().CallerWithSkipFrameCount(zerolog.CallerSkipFrameCount + skipFrameCount).Logger()

	return &Logger{
		logger: &logger,
	}
}

// Debug -.
func (l *Logger) Debug(message interface{}, args ...interface{}) {
	l.msg("debug", message, args...)
}

// Info -.
func (l *Logger) Info(message string, args ...interface{}) {
	l.log(message, args...)
}

// KafkaInfo -.
func (l *Logger) KafkaInfo(message kafka.Message, workerId int, args ...interface{}) {
	//TODO implement me
	l.log(fmt.Sprintf("Processing Kafka Message, topic: %v , partition: %v, message :%v, workerId: %v, Offset: %v, Time: %v", message.Topic, message.Partition, string(message.Value), workerId, message.Offset, message.Time), args...)
}

func (l *Logger) KafkaLogCommittedMessage(topic string, partition int, offset int64) {
	l.log(fmt.Sprintf("Committed Kafka message : topic :%v, partition: %v, offset: %v", topic, partition, offset))
}

func (l *Logger) KafkaProcessMessage(topic string, partition int, message string, workerID int, offset int64, time time.Time) {
	l.log(fmt.Sprintf("Proccessing Kafkaa Message: topic: %v, partition: %v, message: %v, workerId: %v , offset: %v, time: %v ", topic, partition, message, workerID, offset, time))
}

// Warn -.
func (l *Logger) Warn(message string, args ...interface{}) {
	l.log(message, args...)
}

// Error -.
func (l *Logger) Error(message interface{}, args ...interface{}) {
	if l.logger.GetLevel() == zerolog.DebugLevel {
		l.Debug(message, args...)
	}

	l.msg("error", message, args...)
}

func (l *Logger) KafkaError(message string, args ...interface{}) {
	l.msg("error", message, args...)
}

// Fatal -.
func (l *Logger) Fatal(message interface{}, args ...interface{}) {
	l.msg("fatal", message, args...)

	os.Exit(1)
}

func (l *Logger) log(message string, args ...interface{}) {
	if len(args) == 0 {
		l.logger.Info().Msg(message)
	} else {
		l.logger.Info().Msgf(message, args...)
	}
}

func (l *Logger) msg(level string, message interface{}, args ...interface{}) {
	switch msg := message.(type) {
	case error:
		l.log(msg.Error(), args...)
	case string:
		l.log(msg, args...)
	default:
		l.log(fmt.Sprintf("%s message %v has unknown type %v", level, message, msg), args...)
	}
}
