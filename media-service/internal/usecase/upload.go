package usecase

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/segmentio/kafka-go"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	kafkaClient "tenflix/lintang/order-aggregator-service/pkg/kafkaa"
	"time"
)

type UploadUseCase struct {
	cloudinaryWebAPI CloudinaryWebAPI
	kafkaProducer    kafkaClient.Producer
}

func NewUploadUseCase(c CloudinaryWebAPI, k kafkaClient.Producer) *UploadUseCase {
	return &UploadUseCase{
		cloudinaryWebAPI: c,
		kafkaProducer:    k,
	}
}

func (uc *UploadUseCase) UploadVideo(ctx context.Context, u entity.UploadVideoMessage) error {

	uploadedVideo, err := uc.cloudinaryWebAPI.Upload(ctx, u)
	if err != nil {
		return fmt.Errorf("UploadUseCase - uploadVideo - o.cloudinaryWebAPI.Upload: %w", err)
	}

	dtoBytes, err := json.Marshal(uploadedVideo)
	if err != nil {
		return fmt.Errorf("UploadUseCase - uploadVideo - o.json.Marshal: %w", err)
	}

	err =  uc.kafkaProducer.PublishMessage(ctx, kafka.Message{
		Topic: "t.upload.response",
		Value: dtoBytes,
		Time:  time.Now().UTC(),
	}) 
	fmt.Println("send uploaded video url to movie-service!!!!")
	return err 
}
