#!/bin/sh

echo "Retrieving application.yml from Secret Manager..."

application_yml_content=$(gcloud secrets versions access 1 --secret="concert-application-yml")

if [ -z "$application_yml_content" ]; then
    echo "Failed to retrieve application.yml content from Secret Manager"
    exit 1
else
    echo "Successfully retrieved application.yml content"
fi

echo "Writing application.yml content to /app/config/application.yml..."
echo "$application_yml_content" > /app/config/application.yml

if [ $? -eq 0 ]; then
    echo "Successfully wrote application.yml"
else
    echo "Failed to write application.yml"
    exit 1
fi

cat /app/config/application.yml

chmod +r /app/config/application.yml
echo "Set permissions for application.yml"

echo "Executing java application..."
exec java -jar /app/concert-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/app/config/
