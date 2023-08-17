
export

LOCAL_BIN:=$(CURDIR)/bin
PATH:=$(LOCAL_BIN):$(PATH)

# HELP =================================================================================================================
# This will output the help for each task
# thanks to https://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
.PHONY: help

help: ## Display this help screen
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make \033[36m<target>\033[0m\n"} /^[a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)

proto-order-aggregator:
	rm -f order-aggregator-service-go/pb/*.go
	mkdir order-aggregator-service-go/pb
	protoc --proto_path=proto/src/main/proto --go_out=order-aggregator-service-go/pb --go_opt=paths=source_relative \
	--go-grpc_out=order-aggregator-service-go/pb --go-grpc_opt=paths=source_relative \
	proto/src/main/proto/*.proto
#.PHONY: proto
.PHONY: proto-order-aggregator


#protoc --proto_path=proto --go_out=pb --go_opt=paths=source_relative \
#	--go-grpc_out=pb --go-grpc_opt=paths=source_relative \
#	proto/*.proto

bin-deps:
	GOBIN=$(LOCAL_BIN) go install -tags 'postgres' github.com/golang-migrate/migrate/v4/cmd/migrate@latest
	GOBIN=$(LOCAL_BIN) go install github.com/golang/mock/mockgen@latest
