package v1

import (
	"github.com/gin-gonic/gin"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"net/http"
	"tenflix/lintang/order-aggregator-service/internal/controller/http/middleware"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"tenflix/lintang/order-aggregator-service/internal/usecase"
	"tenflix/lintang/order-aggregator-service/pkg/logger"
)

type subscriptionRoutes struct {
	s usecase.Subscription
	l logger.Interface
}

func newSubscriptionRoutes(handler *gin.RouterGroup, s usecase.Subscription, l logger.Interface) {
	r := &subscriptionRoutes{s, l}

	h := handler.Group("/subscription")
	{
		h.POST("/", middleware.IsAdmin, r.createPlan)
		h.GET("/check", middleware.IsUser, r.checkUserSubscription)
	}
}

type createPlanRequest struct {
	Name          string `json:"name" binding:"required"`
	Price         int64  `json:"price" binding:"required"`
	Description   string `json:"description" binding:"required"`
	ActivePeriod  int32  `json:"activePeriod" binding:"required"`
	DiscountPrice int32  `json:"discountPrice" binding:"required"`
}

type createPlanResponse struct {
	Plan entity.Plan `json:"plan"`
}

// @Summary     Create Plan in subscription Db
// @Description Create Plan in subscription Db
// @ID          create-plan
// @Tags  	    subscription
// @Accept      json
// @Produce     json
// @Param       request body createPlanRequest true "Set up plan"
// @Success     200 {object} createPlanResponse
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /subscription [post]
// Author: https://github.com/lintang-b-s
func (r *subscriptionRoutes) createPlan(c *gin.Context) {
	var request createPlanRequest
	if err := c.ShouldBindJSON(&request); err != nil {
		r.l.Error(err, "http - v1 - createPlan")
		errorResponse(c, http.StatusBadRequest, "invalid request body")

		return
	}

	plan, err := r.s.CreatePlan(
		c.Request.Context(),
		entity.CreatePlanRequest{
			Name:          request.Name,
			Price:         request.Price,
			Description:   request.Description,
			ActivePeriod:  request.ActivePeriod,
			DiscountPrice: request.DiscountPrice,
		},
	)
	if err != nil {
		r.l.Error(err, "http - v1 - createPlan")
		errorResponse(c, http.StatusInternalServerError, "subscription service problems")
		return
	}

	c.JSON(http.StatusOK, createPlanResponse{plan})
}

type checkSubscriptionResponse struct {
	Subscription entity.Subscription `json:"subscription"`
}

// @Summary     Check subscription in subscription Db
// @Description Check subscription in subscription Db
// @ID          check-subscription
// @Tags  	    subscription
// @Accept      json
// @Produce     json
// @Success     200 {object} checkSubscriptionResponse
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /subscription/check [get]
// Author: https://github.com/lintang-b-s
func (r *subscriptionRoutes) checkUserSubscription(c *gin.Context) {
	principalId := c.MustGet("principalId").(string)
	subscription, err := r.s.CheckUserSubscription(c.Request.Context(), principalId)
	if err != nil {
		r.l.Error(err, "http - v1 - checkUserSubscription")
		if statusErr, ok := status.FromError(err); ok {
			if statusErr.Code() == codes.FailedPrecondition {
				errorResponse(c, http.StatusBadRequest, statusErr.Message())
				return
			}
		}
		errorResponse(c, http.StatusInternalServerError, "database/grpc problems")
		return
	}

	c.JSON(http.StatusOK, checkSubscriptionResponse{subscription})

}
