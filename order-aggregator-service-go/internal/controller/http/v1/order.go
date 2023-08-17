package v1

import (
	"github.com/gin-gonic/gin"
	"github.com/tbaehler/gin-keycloak/pkg/ginkeycloak"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"

	"net/http"
	"tenflix/lintang/order-aggregator-service/config"
	"tenflix/lintang/order-aggregator-service/internal/controller/http/middleware"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"tenflix/lintang/order-aggregator-service/internal/usecase"
	"tenflix/lintang/order-aggregator-service/pkg/logger"
)

type orderRoutes struct {
	o usecase.Order
	l logger.Interface
}

var configF *config.Config

func newOrderRoutes(handler *gin.RouterGroup, o usecase.Order, l logger.Interface, cfg *config.Config) {
	r := &orderRoutes{o, l}
	configF = cfg

	var sbbEndpoint = ginkeycloak.KeycloakConfig{
		Url:           "http://" + cfg.KC.Hostname,
		Realm:         "tenflix",
		FullCertsPath: nil,
	}
	h := handler.Group("/orders")
	//h.Use(ginkeycloak.Auth(ginkeycloak.AuthCheck(), sbbEndpoint))
	{
		h.POST("/", ginkeycloak.Auth(ginkeycloak.AuthCheck(), sbbEndpoint), middleware.IsUser, r.createOrder)
		h.POST("/notificationMidtrans", r.processOrder)
		h.GET("/:id", ginkeycloak.Auth(ginkeycloak.AuthCheck(), sbbEndpoint), middleware.IsUser, r.getOrderDetail) //
		h.GET("/", ginkeycloak.Auth(ginkeycloak.AuthCheck(), sbbEndpoint), middleware.IsUser, r.getOrderHistory)

	}

}

type createOrderRequest struct {
	PlanId int64 `json:"planId"`
}

type createOrderResponse struct {
	OrderStatus string       `json:"orderStatus"`
	RedirectUrl string       `json:"redirectUrl"`
	Order       entity.Order `json:"order"`
}

// @Summary     Create Order in order Db
// @Description  Create Order in order Db
// @ID          create-order
// @Tags  	    order
// @Accept      json
// @Produce     json
// @Param       request body createOrderRequest true "Set up order"
// @Success     200 {object} createOrderResponse
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /subscription [post]
// Author: https://github.com/lintang-b-s
func (r *orderRoutes) createOrder(c *gin.Context) {
	var request createOrderRequest
	principalId := c.MustGet("principalId").(string)

	if err := c.ShouldBindJSON(&request); err != nil {
		r.l.Error(err, "http - v1 - createOrder")
		errorResponse(c, http.StatusBadRequest, "invalid request body")
		return
	}

	order, orderStatus, redirectUrl, err := r.o.CreateOrder(
		c.Request.Context(),
		entity.CreateOrderRequest{
			PlanId: request.PlanId,
		},
		principalId,
	)
	if err != nil {
		r.l.Error(err, "http - v1 - createOrder")
		if statusErr, ok := status.FromError(err); ok {
			if statusErr.Code() == codes.FailedPrecondition {
				errorResponse(c, http.StatusBadRequest, statusErr.Message())
				return
			}
			if statusErr.Code() == codes.NotFound {
				errorResponse(c, http.StatusNotFound, statusErr.Message())
				return
			}

		}
		errorResponse(c, http.StatusInternalServerError, "rest http/grpc problems")
		return
	}

	resp := createOrderResponse{Order: order, OrderStatus: orderStatus, RedirectUrl: redirectUrl}
	c.JSON(http.StatusCreated, resp)
}

type processOrderResponse struct {
	Message string `json:"message"`
}

type notificationPayload map[string]interface{}

// @Summary     processs Order after get payment notification
// @Description  processs Order after get payment notification
// @ID          process-order
// @Tags  	    order
// @Accept      json
// @Produce     json
// @Param       request body notificationPayload true "Set up paymentNotification"
// @Success     200 {object} processOrderResponse
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /subscription [post]
// Author: https://github.com/lintang-b-s
func (r *orderRoutes) processOrder(c *gin.Context) {
	var request notificationPayload
	if err := c.ShouldBindJSON(&request); err != nil {
		r.l.Error(err, "http - v1 - processOrder")
		errorResponse(c, http.StatusBadRequest, "invalid request body")
		return
	}

	err := r.o.ProcessOrder(
		c.Request.Context(),
		request,
		configF,
	)
	if err != nil {
		r.l.Error(err, "http - v1 - processOrder")
		if statusErr, ok := status.FromError(err); ok {
			if statusErr.Code() == codes.NotFound {
				errorResponse(c, http.StatusNotFound, statusErr.Message())
				return
			}
			if statusErr.Code() == codes.InvalidArgument {
				errorResponse(c, http.StatusInternalServerError, statusErr.Message())
				return
			}

		}

		errorResponse(c, http.StatusInternalServerError, "rest http/grpc problems")
		return
	}
	c.JSON(http.StatusOK, processOrderResponse{
		Message: "ok, notification received",
	})
}

type getOrderDetailRequest struct {
	Id string `uri:"id" binding:"required,min=1"`
}

type getOrderDetailResponse struct {
	Subscription entity.Subscription `json:"subscriptionDto"`
	Order        entity.Order        `json:"order"`
	Payment      entity.Payment      `json:"payment"`
}

// @Summary     Get Order Detail from  order-service, payment-service, subscription-service concurrently
// @Description  Get Order Detail from  order-service, payment-service, subscription-service concurrently
// @ID          get-order-detail
// @Tags  	    order
// @Accept      json
// @Produce     json
// @Param       request body createOrderRequest true "Set up orderId"
// @Success     200 {object} createOrderResponse
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /subscription [post]
// Author: https://github.com/lintang-b-s
func (r *orderRoutes) getOrderDetail(c *gin.Context) {
	var req getOrderDetailRequest
	if err := c.ShouldBindUri(&req); err != nil {
		errorResponse(c, http.StatusBadRequest, "invalid request body")
		return
	}
	orderId := req.Id
	principalId := c.MustGet("principalId").(string)

	s, o, p, err := r.o.GetOrderDetail(
		c.Request.Context(),
		orderId, principalId,
	)
	if err != nil {
		if statusErr, ok := status.FromError(err); ok {
			if statusErr.Code() == codes.NotFound {
				errorResponse(c, http.StatusNotFound, statusErr.Message())
				return
			}
			if statusErr.Code() == codes.InvalidArgument {
				errorResponse(c, http.StatusBadRequest, statusErr.Message())
				return
			}
			if statusErr.Code() == codes.FailedPrecondition {
				errorResponse(c, http.StatusBadRequest, statusErr.Message())
				return
			}
		}
		errorResponse(c, http.StatusInternalServerError, "rest http/grpc problems")
		return
	}
	c.JSON(http.StatusOK, getOrderDetailResponse{
		Subscription: s,
		Order:        o,
		Payment:      p,
	})
}

type getOrderHistoryResponse struct {
	Orders []entity.Order `json:"orders"`
}

// @Summary     Get Order history from  order-service
// @Description  Get Order history from  order-service
// @ID          get-order-history
// @Tags  	    order
// @Accept      json
// @Produce     json
// @Success     200 {object} getOrderHistoryResponse
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /subscription [post]
// Author: https://github.com/lintang-b-s
func (r *orderRoutes) getOrderHistory(c *gin.Context) {
	principalId := c.MustGet("principalId").(string)

	orders, err := r.o.GetOrderHistory(
		c.Request.Context(), principalId)
	if err != nil {
		if statusErr, ok := status.FromError(err); ok {
			if statusErr.Code() == codes.NotFound {
				errorResponse(c, http.StatusNotFound, statusErr.Message())
				return
			}
			if statusErr.Code() == codes.InvalidArgument {
				errorResponse(c, http.StatusBadRequest, statusErr.Message())
				return
			}
			if statusErr.Code() == codes.FailedPrecondition {
				errorResponse(c, http.StatusBadRequest, statusErr.Message())
				return
			}
		}
		errorResponse(c, http.StatusInternalServerError, "rest http/grpc problems")
		return
	}
	c.JSON(http.StatusOK, getOrderHistoryResponse{
		Orders: orders,
	})
}
