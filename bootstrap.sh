
echo ""
echo "******************************************"
echo "* [1/3] Compile and package all services *"
echo "******************************************"
echo ""

mvn clean && 

cd proto && 
mvn compile &&
cd .. && 

if ! mvn  package -DskipTests; then # kalo install gakbisa jadi 404
    echo ""
    echo "Error: Maven encountered errors, unable to continue!"
    exit
fi


