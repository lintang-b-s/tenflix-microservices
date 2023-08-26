package keycloak

type ConfigKeycloak struct {
	ClientId     string
	ClientSecret string
	Realm        string
	Hostname     string
}

//var cfg ConfigKeycloak

func NewKeycloak(clientId string, clientSecret string, realm string, hostname string) *ConfigKeycloak {
	cfg := &ConfigKeycloak{
		ClientId:     clientId,
		ClientSecret: clientSecret,
		Realm:        realm,
		Hostname:     hostname,
	}
	return cfg
}
