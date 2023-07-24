package middleware

import (
	"errors"
	"github.com/Nerzal/gocloak"
	"github.com/dgrijalva/jwt-go"
	"github.com/gin-gonic/gin"
	"net/http"
	"tenflix/lintang/order-aggregator-service/pkg/keycloak"

	"strings"
)

type KeycloakConfig struct {
	*keycloak.ConfigKeycloak
}

var cfg KeycloakConfig

// New -.
func New(kc *keycloak.ConfigKeycloak) {
	cfg = KeycloakConfig{kc}
}

var client gocloak.GoCloak

type UnsignedResponse struct {
	Message interface{} `json:"message"`
}

func Initialize() {
	client = gocloak.NewClient(cfg.Hostname)
}

func KecloakTokenCheck(c *gin.Context) {

	jwtToken, err := extractBearerToken(c.GetHeader("Authorization"))
	if err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, UnsignedResponse{
			Message: err.Error(),
		})
		return
	}

	rptResult, err := client.RetrospectToken(jwtToken, cfg.ClientId, cfg.ClientSecret, cfg.Realm)
	if err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, UnsignedResponse{
			Message: err.Error(),
		})
		return
	}
	isTokenValid := rptResult.Active
	if !isTokenValid {
		c.AbortWithStatusJSON(http.StatusBadRequest, UnsignedResponse{
			Message: err.Error(),
		})
		return
	}

	claims, _ := extractClaims(jwtToken)
	principalId := claims["sub"].(string)
	c.Set("principalId", principalId)
	c.Next()
}

func extractBearerToken(header string) (string, error) {
	if header == "" {
		return "", errors.New("bad header value given")
	}

	jwtToken := strings.Split(header, " ")
	if len(jwtToken) != 2 {
		return "", errors.New("incorrectly formatted authorization header")
	}

	return jwtToken[1], nil
}

type JWTMaker struct {
	secretKey string
}

func extractClaims(tokenStr string) (jwt.MapClaims, bool) {
	hmacSecretString := "y57aHOaRWrUO5PHdzk5jcUIm3RGWsKEg" // Value
	hmacSecret := []byte(hmacSecretString)
	token, err := jwt.Parse(tokenStr, func(token *jwt.Token) (interface{}, error) {
		// check token signing method etc
		return hmacSecret, nil
	})

	if err != nil {
		return nil, false
	}

	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		return claims, true
	} else {
		return nil, false
	}
}
