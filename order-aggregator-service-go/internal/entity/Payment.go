package entity

type Payment struct {
	Id                string `json:"id"`
	Status            string `json:"status"`
	GrossAmount       string `json:"grossAmount"`
	TransactionTime   string `json:"transactionTime"`
	TransactionId     string `json:"transactionId"`
	TransactionStatus string `json:"transactionStatus"`
	PaymentType       string `json:"paymentType"`
	FraudStatus       string `json:"fraudStatus"`
	Bank              string `json:"bank"`
	VaNumber          string `json:"vaNumber"`
	Currency          string `json:"currency"`
	OrderId           string `json:"orderId"`
}
