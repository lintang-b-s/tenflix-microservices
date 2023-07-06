
echo ""
echo "******************************************"
echo "* [1/3] Compile and package all services *"
echo "******************************************"
echo ""

cd proto
mvn compile
cd ..

if ! mvn  package -DskipTests; then
    echo ""
    echo "Error: Maven encountered errors, unable to continue!"
    exit
fi

echo ""
echo "*****************************************************************"
echo "* [3/3] Start all services (and their dependencies) with Docker *"
echo "*****************************************************************"
echo ""


docker-compose up -d
echo ""
echo "Congratulations, everything was successful!"
