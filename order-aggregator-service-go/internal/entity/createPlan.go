package entity

type CreatePlanRequest struct {
	Name          string `json:"name"`
	Price         int64  `json:"price"`
	Description   string `json:"description"`
	ActivePeriod  int32  `json:"activePeriod"`
	DiscountPrice int32  `json:"discountPrice"`
}

type Plan struct {
	PlanId        int32  `json:"planId"`
	Name          string `json:"name"`
	Price         int32  `json:"price"`
	Description   string `json:"description"`
	ActivePeriod  int32  `json:"activePeriod"`
	DiscountPrice int32  `json:"discountPrice"`
}
