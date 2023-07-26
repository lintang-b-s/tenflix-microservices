
echo ""
echo "******************************************"
echo "* [1/3] Compile and package all services *"
echo "******************************************"
echo ""

mvn clean 

cd proto 
mvn compile 
cd .. 
make proto-order-aggregator

if ! mvn  package -DskipTests; then
    echo ""
    echo "Error: Maven encountered errors, unable to continue!"
    exit
fi


