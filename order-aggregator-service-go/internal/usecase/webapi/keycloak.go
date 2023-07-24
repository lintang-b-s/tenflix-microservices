package webapi

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"tenflix/lintang/order-aggregator-service/internal/entity"
)

type KeycloakWebAPI struct {
}

func NewKeycloak() *KeycloakWebAPI {

	return &KeycloakWebAPI{}
}

type AccTokenResp struct {
	access_token string
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
	url := "http://keycloak-tenflix:8080/realms/tenflix/protocol/openid-connect/token"

	resp, err := http.PostForm(url, body)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail - http.PostForm: %w", err)
	}

	defer resp.Body.Close()
	var dataAccToken AccTokenResp

	err = json.NewDecoder(resp.Body).Decode(&dataAccToken)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail -json.NewDecoder(resp.Body).Decode: %w", err)
	}
	accToken := dataAccToken.access_token

	var client = &http.Client{}

	urlUserDetail := "http://keycloak-tenflix:8080/admin/realms/tenflix/users/" + userId
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

	var uDetail UserDetail
	err = json.NewDecoder(respUserDetail.Body).Decode(&uDetail)
	if err != nil {
		return entity.KeycloakUserDto{}, fmt.Errorf("KeycloakWebAPI - GetUserDetail - json.NewDecoder(respUserDetail.Body).Decode: %w", err)
	}

	kUser :=  entity.KeycloakUserDto{
		Id: uDetail.id,
		Username: uDetail.username,
		FirstName: uDetail.firstName,
		LastName: uDetail.lastName,
		Email: uDetail.email,
	}

	return kUser, nil

}
