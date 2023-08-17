
echo ""
echo "******************************************"
echo "* [1/3] Compile and package all services *"
echo "******************************************"
echo ""

./mvnw clean

cd proto
./mvnw compile
cd ..
make proto-order-aggregator
protoc --proto_path=proto/src/main/proto --go_out=order-aggregator-service-go/pb --go_opt=paths=source_relative  --go-grpc_out=order-aggregator-service-go/pb --go-grpc_opt=paths=source_relative proto/src/main/proto/*.proto
echo
cd order-aggregator-service-go
go mod tidy
cd ..

if ! ./mvnw  package -DskipTests; then
    echo ""
    echo "Error: Maven encountered errors, unable to continue!"
    exit
fi


