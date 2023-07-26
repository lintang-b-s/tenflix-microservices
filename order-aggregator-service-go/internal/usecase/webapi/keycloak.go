package webapi

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"tenflix/lintang/order-aggregator-service/pkg/keycloak"
)

type KeycloakWebAPI struct {
}

type KeycloakConfig struct {
	*keycloak.ConfigKeycloak
}

var cfg KeycloakConfig

func NewKeycloak(kc *keycloak.ConfigKeycloak) *KeycloakWebAPI {
	cfg = KeycloakConfig{kc}
	return &KeycloakWebAPI{}
}

type AccTokenResp struct {
	access_token       string
	expires_in         int
	refresh_expires_in int
	refresh_token      string
	token_type         string
	session_state      string
	scope              string
}

type UserDetail struct {
	email     string
	username  string
	id        string
	firstName string
	lastName  string
}

func (k *KeycloakWebAPI) GetUserDetail(ctx context.Context, userId string) (entity.KeycloakUserDto, error) {
	body := url.Values{}
	body.Set("grant_type", "password")
	body.Set("username", "lintang@gmail.com")
	body.Set("password", "lintang")
	body.Set("client_id", "tenflix-client")
	body.Set("client_secret", "y57aHOaRWrUO5PHdzk5jcUIm3RGWsKEg")
	url := "http://" + cfg.Hostname + "/realms/tenflix/protocol/openid-connect/token"

	//resp, err := http.PostForm(url, body)
	var payload = bytes.NewBufferString(body.Encode())
	request, err := http.NewRequest("POST", url, payload)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail - http.PostForm: %w", err)
	}
	request.Header.Set("Content-Type", "application/x-www-form-urlencoded")
	var client = &http.Client{}
	resp, err := client.Do(request)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail - http.PostForm: %w", err)
	}
	defer resp.Body.Close()

	var accTokenRes map[string]interface{}

	err = json.NewDecoder(resp.Body).Decode(&accTokenRes)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail -json.NewDecoder(resp.Body).Decode: %w", err)
	}
	accToken := accTokenRes["access_token"].(string)

	urlUserDetail := "http://" + cfg.Hostname + "/admin/realms/tenflix/users/" + userId
	requestUser, err := http.NewRequest("GET", urlUserDetail, nil)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail - http.NewRequest: %w", err)
	}
	requestUser.Header.Add("Authorization", "Bearer "+accToken)
	respUserDetail, err := client.Do(requestUser)

	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail -  client.Do: %w", err)
	}
	defer respUserDetail.Body.Close()

	var uDetail map[string]interface{}
	err = json.NewDecoder(respUserDetail.Body).Decode(&uDetail)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail - json.NewDecoder(respUserDetail.Body).Decode: %w", err)
	}

	kUser := entity.KeycloakUserDto{
		Id:        uDetail["id"].(string),
		Username:  uDetail["username"].(string),
		FirstName: uDetail["firstName"].(string),
		LastName:  uDetail["lastName"].(string),
		Email:     uDetail["email"].(string),
	}

	return kUser, nil

}
