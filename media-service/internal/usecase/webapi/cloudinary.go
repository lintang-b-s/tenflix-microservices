package webapi

import (
	"bytes"
	"context"
	"fmt"
	"tenflix/lintang/order-aggregator-service/config"
	"tenflix/lintang/order-aggregator-service/internal/entity"

	"github.com/cloudinary/cloudinary-go/v2"
	"github.com/cloudinary/cloudinary-go/v2/api"
	"github.com/cloudinary/cloudinary-go/v2/api/uploader"
)

type CloudinaryWebAPI struct {
	url string
}

func NewCloudinary(cfg *config.Config) *CloudinaryWebAPI {
	return &CloudinaryWebAPI{
		url: cfg.Cloudinary.CloudinaryURL,
	}
}

func (c *CloudinaryWebAPI) Upload(ctx context.Context, m entity.UploadVideoMessage) (entity.UploadedVideoMessage, error) {
	cld, err := cloudinary.NewFromURL(c.url)

	if err != nil {
		fmt.Println("CloudinaryWebAPI - Upload - cloudinary.NewFromURL: %w", err)
		return entity.UploadedVideoMessage{}, fmt.Errorf("CloudinaryWebAPI - Upload - cloudinary.NewFromURL: %w", err)
	}
	ioReaderFile := bytes.NewReader(m.File)

	resp, err := cld.Upload.Upload(context.Background(),
		ioReaderFile, uploader.UploadParams{
			ResourceType: "video",
			Eager:        "sp_sd/f_m3u8|sp_hd/f_m3u8",
			EagerAsync:   api.Bool(true),
			PublicID:     m.PublicId,
		},
	)

	if err != nil {
		fmt.Println("CloudinaryWebAPI - Upload - cld.Upload.Upload : ", err)
		return entity.UploadedVideoMessage{}, fmt.Errorf("CloudinaryWebAPI - Upload - cld.Upload.Upload: %w", err)
	}

	uMessage := entity.UploadedVideoMessage{
		Url:      resp.SecureURL,
		PublicId: resp.PublicID,
		Id:       m.Id,
	}
	fmt.Println("url cloudinary video: ", resp.SecureURL, " publicId: ", resp.PublicID)
	return uMessage, nil
}
