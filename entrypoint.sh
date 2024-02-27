#!/bin/sh

# Secret Manager에서 설정 값을 가져옵니다.
application_yml_content=$(gcloud secrets versions access 1 --secret="concert-application-yml")

# Secret retrieval validation
if [ -z "$application_yml_content" ]; then
    echo "Failed to retrieve application.yml content from Secret Manager"
    exit 1
fi

# 설정 값을 파일로 저장합니다.
echo "$application_yml_content" > /app/config/application.yml

# 원래의 애플리케이션 실행 명령어를 실행합니다, 위치를 지정합니다.
exec java -jar /app/concert-0.0.1-SNAPSHOT.jar --spring.config.location=/app/config/application.yml
