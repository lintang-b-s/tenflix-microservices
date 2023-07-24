// Package v1 implements routing paths. Each services in own file.
package v1

import (
	"net/http"
	"tenflix/lintang/order-aggregator-service/internal/controller/http/middleware"

	"github.com/gin-gonic/gin"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"

	// Swagger docs.
	_ "tenflix/lintang/order-aggregator-service/docs"
	"tenflix/lintang/order-aggregator-service/internal/usecase"
	"tenflix/lintang/order-aggregator-service/pkg/logger"
)

// NewRouter -.
// Swagger spec:
// @title       Go Clean Template API
// @description Using a translation service as an example
// @version     1.0
// @host        localhost:9900
// @BasePath    /v1
func NewRouter(handler *gin.Engine, l logger.Interface, s usecase.Subscription, o usecase.Order) {
	// Options
	handler.Use(gin.Logger())
	handler.Use(gin.Recovery())

	// Swagger
	swaggerHandler := ginSwagger.DisablingWrapHandler(swaggerFiles.Handler, "DISABLE_SWAGGER_HTTP_HANDLER")
	handler.GET("/swagger/*any", swaggerHandler)

	// K8s probe
	handler.GET("/healthz", func(c *gin.Context) { c.Status(http.StatusOK) })

	// Prometheus metrics
	handler.GET("/metrics", gin.WrapH(promhttp.Handler()))

	// Routers
	h := handler.Group("/api/v1")
	{
		//newTranslationRoutes(h, t, l)
		newSubscriptionRoutes(h, s, l)
		newOrderRoutes(h, o, l)
	}
	h.Use(middleware.KecloakTokenCheck)
}
