# 1️⃣ Java 21 기반 Docker 이미지 사용
FROM openjdk:21
LABEL authors="seoyeome"

ENTRYPOINT ["top", "-b"]

# 2️⃣ 작업 디렉토리 설정
WORKDIR /app

# 3️⃣ 소스 코드 복사
COPY . /app

# 4️⃣ 개발 환경에서는 Maven을 사용하여 서버 실행
CMD ["./mvnw", "spring-boot:run"]
