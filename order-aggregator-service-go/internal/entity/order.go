package entity

type CreateOrderRequest struct {
	PlanId int64 `json:"planId"`
}

type Order struct {
	Id              string    `json:"id"`
	UserId          string    `json:"userId"`
	Price           int64     `json:"price"`
	OrderStatus     string    `json:"orderStatus"`
	PaymentId       string    `json:"paymentId"`
	FailureMessages string    `json:"failureMessages"`
	Plan            OrderPlan `json:"plan"`
}

type OrderPlan struct {
	Id          string `json:"id"`
	Name        string `json:"name"`
	Description string `json:"description"`
	PlanId      int64  `json:"planId"`
	Price       int64  `json:"price"`
	Subtotal    int64  `json:"subTotal"`
}
