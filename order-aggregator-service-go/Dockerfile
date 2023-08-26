# Step 1: Modules caching
FROM golang:1.19-alpine as modules
COPY go.mod go.sum /modules/
WORKDIR /modules
#RUN #go mod tidy  #go mod download
RUN go mod download # go mod tidy consul bisa

# Step 2: Builder
FROM golang:1.19-alpine as builder
COPY --from=modules /go/pkg /go/pkg
COPY . /app
WORKDIR /app
RUN mkdir tmp
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 \
    go build   -o /bin/app ./cmd/app

# Step 3: Final
FROM scratch
COPY --from=builder /app/config /config
COPY --from=builder /bin/app /app
COPY --from=builder /app/tmp /tmp
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
CMD ["/app"]
