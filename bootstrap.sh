
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

if ! ./mvnw  package -DskipTests; then
    echo ""
    echo "Error: Maven encountered errors, unable to continue!"
    exit
fi


