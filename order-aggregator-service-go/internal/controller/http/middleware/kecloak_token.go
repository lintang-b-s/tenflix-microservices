package middleware

import (
	"github.com/gin-gonic/gin"
	"github.com/tbaehler/gin-keycloak/pkg/ginkeycloak"
	"golang.org/x/exp/slices"
	"golang.org/x/oauth2"
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

type UnsignedResponse struct {
	Message string `json:"message"`
}

func ExtractBearerToken(token string) string {
	return strings.Replace(token, "Bearer ", "", 1)
}

func IsUser(c *gin.Context) {

	jwtToken := ExtractBearerToken(c.GetHeader("Authorization"))
	var keycloakConfig = ginkeycloak.KeycloakConfig{
		Url:           "http://" + cfg.Hostname,
		Realm:         cfg.Realm,
		FullCertsPath: nil,
	}
	token := &oauth2.Token{
		AccessToken: jwtToken,
	}
	tokenContainer, err := ginkeycloak.GetTokenContainer(token, keycloakConfig)
	if err != nil {
		c.AbortWithStatusJSON(http.StatusUnauthorized, UnsignedResponse{
			Message: "you are not authorized",
		})
		return
	}
	claim := tokenContainer.KeyCloakToken

	principalId := tokenContainer.KeyCloakToken.Sub
	roles := claim.RealmAccess.Roles
	if !slices.Contains(roles, "default-roles-tenflix") {
		c.AbortWithStatusJSON(http.StatusUnauthorized, UnsignedResponse{
			Message: "you are not authorized",
		})
		return
	}

	c.Set("principalId", principalId)
	c.Next()
}

func IsAdmin(c *gin.Context) {

	jwtToken := ExtractBearerToken(c.GetHeader("Authorization"))
	var keycloakConfig = ginkeycloak.KeycloakConfig{
		Url:           "http://" + cfg.Hostname,
		Realm:         cfg.Realm,
		FullCertsPath: nil,
	}
	token := &oauth2.Token{
		AccessToken: jwtToken,
	}
	tokenContainer, err := ginkeycloak.GetTokenContainer(token, keycloakConfig)
	if err != nil {
		c.AbortWithStatusJSON(http.StatusUnauthorized, UnsignedResponse{
			Message: "you are not authorized",
		})
		return
	}
	claim := tokenContainer.KeyCloakToken

	principalId := tokenContainer.KeyCloakToken.Sub
	roles := claim.RealmAccess.Roles
	if !slices.Contains(roles, "admin") {
		c.AbortWithStatusJSON(http.StatusUnauthorized, UnsignedResponse{
			Message: "you are not authorized",
		})
		return
	}

	c.Set("principalId", principalId)
	c.Next()
}
