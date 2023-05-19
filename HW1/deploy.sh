# Change the path to tomcat to your actual path
mvn clean install
rm -rf /opt/homebrew/opt/tomcat/libexec/webapps/workflix-1.0.war
rm -rf /opt/homebrew/opt/tomcat/libexec/webapps/workflix-1.0
cp target/workflix-1.0.war /opt/homebrew/opt/tomcat/libexec/webapps
echo "Deployed to Tomcat"