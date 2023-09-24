# Base Image 설정
FROM openjdk:11

# 배포 디렉토리 생성
RUN mkdir -p deploy

# 도커 이미지 내부에서 명령이 실행될 디렉토리 설정
WORKDIR /deploy

# 도커 이미지의 파일 시스템으로 복사
COPY ./build/libs/*.jar petnuri.jar

# 이미지 실행시 항상 실행되야 하는 커맨드 설정
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/deploy/petnuri.jar"]
