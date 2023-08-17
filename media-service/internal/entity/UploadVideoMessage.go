package entity

type UploadVideoMessage struct {
	Id       int    `json:"id"`
	File     []byte `json:"file"`
	PublicId string `json:"publicId"`
}

type UploadedVideoMessage struct {
	Url      string `json:"url"`
	PublicId string `json:"publicId"`
	Id       int    `json:"id"`
}
