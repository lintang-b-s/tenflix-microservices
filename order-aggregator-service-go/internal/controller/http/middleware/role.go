package middleware

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func IsUser(c *gin.Context) {
	jwtToken, err := extractBearerToken(c.GetHeader("Authorization"))
	if err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, UnsignedResponse{
			Message: err.Error(),
		})
		return
	}

	roles, err := client.GetRoles(jwtToken, cfg.realm)
	for _, role := range *roles {
		if role.Name != "user" {
			c.AbortWithStatusJSON(http.StatusBadRequest, UnsignedResponse{
				Message: err.Error(),
			})
			return
		}
	}

	c.Next()
}

func IsAdmin(c *gin.Context) {
	jwtToken, err := extractBearerToken(c.GetHeader("Authorization"))
	if err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, UnsignedResponse{
			Message: err.Error(),
		})
		return
	}

	roles, err := client.GetRoles(jwtToken, cfg.realm)
	for _, role := range *roles {
		if role.Name != "admin" {
			c.AbortWithStatusJSON(http.StatusBadRequest, UnsignedResponse{
				Message: err.Error(),
			})
			return
		}
	}

	c.Next()
}
