#!/bin/sh

echo "Retrieving application.yml from Docker Volume..."

if [ ! -f /app/config/concert-application-yml:latest ]; then
    ls /app/config
    echo "Failed to retrieve application.yml content from Secret Manager"
    exit 1
else
    echo "Successfully retrieved application.yml content"
fi


echo "Writing application.yml content to /app/config/application.yml..."
cp /app/config/concert-application-yml:latest /app/config/application.yml

if [ $? -eq 0 ]; then
    echo "Successfully wrote application.yml"
else
    echo "Failed to write application.yml"
    exit 1
fi

cat /app/config/application.yml

chmod +rwx /app/config/application.yml
echo "Set permissions for application.yml"

echo "Executing java application..."
exec java -jar /app/concert-0.0.1-SNAPSHOT.jar --spring.config.location=file:/app/config/application.yml
