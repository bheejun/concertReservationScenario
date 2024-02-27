#!/bin/sh

# Secret Manager에서 설정 값을 가져옵니다.
application_yml_content=$(gcloud secrets versions access 1 --secret="projects/838833205273/secrets/concert-application-yml")


# 설정 값을 파일로 저장합니다.
echo "$application_yml_content" > /app/config/application.yml

# 원래의 애플리케이션 실행 명령어를 실행합니다.
exec java -jar /app/app.jar