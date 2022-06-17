# triple-project
여행갈 도시를 등록, 조회할 수 있는 서비스입니다.

## 기능
### 여행
- 여행 등록 : Post /api/trip
- 여행 조회 : GET /api/trip/{id}

### 도시
- 도시 등록 : Post /api/city
- 도시 조회 : GET /api/city/{id}
- 도시 리스트 조회 : GET /api/city

### 유저
- 사용자 회원가입 : Post /api/user/join
- 사용자 로그인 : GET /api/user/login

### 특이사항
* 테이블 설계     
</br>

![스크린샷 2022-06-17 오전 9 00 41](https://user-images.githubusercontent.com/68800994/174216396-9bbf334f-20f1-413d-b96c-bac104e3441d.png)

* 위 기능들을 검증하기 위한 테스트를 개발했습니다.   
* 자세한 내용은 서버 기동 후 Swagger 문서를 확인하면 됩니다. (http://localhost:8080/swagger-ui/index.html)
## 실행 방법
1. clone 받기
2. Mysql를 Docker로 설치하기
~~~shell
docker-compose up -d
~~~
3. 스프링 부트 실행하기
4. Swagger로 API 확인 및 테스트하기
5. 사용자 로그인 API를 통해 얻은 AccessToken을 사용해 다른 API를 호출합니다.

## 사용 기술
* Java 11
* Spring Boot 2.6.6
* spring-data-jpa
* mysql 8.0 
* H2 Database (테스트 디비)
* swagger 3.0
* spring-security 
* java-jwt
* lombok
* modelmapper
* yaml-resource-bundle (예외 코드, 메세지 정리를 위함)
