package entity

import (
	"github.com/google/uuid"
	"time"
)

type Subscription struct {
	Id                  int32     `json:"id"`
	UserId              uuid.UUID `json:"userId"`
	EndSubscriptionDate time.Time `json:"endSubscriptionDate"`
	Status              string    `json:"status"`
	Plan                Plan      `json:"plan"`
}
