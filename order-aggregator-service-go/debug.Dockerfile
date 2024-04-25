# Step 1: Modules caching
#FROM golang:1.21-alpine as modules
FROM golang:1.19-alpine as modules
COPY go.mod go.sum /modules/
WORKDIR /modules
RUN go mod download


# Step 2: Builder
#FROM golang:1.21-alpine as builder
FROM golang:1.19-alpine as builder
COPY --from=modules /go/pkg /go/pkg
RUN go install github.com/go-delve/delve/cmd/dlv@latest
COPY . /app
WORKDIR /app
# goos = linux goarch amd64
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 \
    go build -gcflags "all=-N -l"   -o /bin/app ./cmd/app

# Step 3: Final
#FROM scratch
FROM alpine
EXPOSE 9900 40000
COPY --from=builder /go/bin/dlv dlv
# COPY --from=builder /app/.env .env 
COPY --from=builder /app/config /config
COPY --from=builder /app/migrations /migrations
COPY --from=builder /bin/app /app
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
CMD ["./dlv", "--listen=:40000", "--headless=true", "--api-version=2", "--accept-multiclient", "exec", "/app"]
#CMD ["/app"]