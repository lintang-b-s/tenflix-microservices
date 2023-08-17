package config

import (
	"fmt"
	"github.com/ilyakaznacheev/cleanenv"
)

type (
	// Config -.
	Config struct {
		App  `yaml:"app"`
		HTTP `yaml:"http"`
		Log  `yaml:"logger"`
		Mt   `yaml:"mt"`
		KC   `yaml:"kc"`
		Con  `yaml:"consul"`
	}

	// App -.
	App struct {
		Name    string `env-required:"true" yaml:"name"    env:"APP_NAME"`
		Version string `env-required:"true" yaml:"version" env:"APP_VERSION"`
	}

	// HTTP -.
	HTTP struct {
		Port string `env-required:"true" yaml:"port" env:"HTTP_PORT"`
	}

	// Log -.
	Log struct {
		Level string `env-required:"true" yaml:"log_level"   env:"LOG_LEVEL"`
	}

	// PG -.
	//PG struct {
	//	PoolMax int    `env-required:"true" yaml:"pool_max" env:"PG_POOL_MAX"`
	//	URL     string `env-required:"true" yaml:"pg_url"               env:"PG_URL"`
	//}

	KC struct {
		ClientId     string `env-required:"true" yaml:"kc_clientId"`
		ClientSecret string `env-required:"true" yaml:"kc_clientSecret"`
		Realm        string `env-required:"true" yaml:"kc_realm"`
		Hostname     string `env-required:"true" yaml:"kc_hostname"`
	}

	Con struct {
		ConsulHost string `env-required:"true" yaml:"consul_host" `
	}

	Mt struct {
		Server string `env-required:"true"    env:"MIDTRANS_SERVERKEY"`
	}

	//// RMQ -.
	//RMQ struct {
	//	ServerExchange string `env-required:"true" yaml:"rpc_server_exchange" env:"RMQ_RPC_SERVER"`
	//	ClientExchange string `env-required:"true" yaml:"rpc_client_exchange" env:"RMQ_RPC_CLIENT"`
	//	URL            string `env-required:"true"                            env:"RMQ_URL"`
	//}
)

// NewConfig returns app config.
func NewConfig() (*Config, error) {
	cfg := &Config{}

	err := cleanenv.ReadConfig("./config/config.yml", cfg)
	if err != nil {
		return nil, fmt.Errorf("config error: %w", err)
	}

	err = cleanenv.ReadEnv(cfg)
	if err != nil {
		return nil, err
	}

	return cfg, nil
}
