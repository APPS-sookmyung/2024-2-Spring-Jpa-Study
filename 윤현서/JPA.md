스프링 부트는 복잡한 스프링 기술을 간단하게 쓸 수 있도록 한 프레임워크

-   JPA는 강력한 Java ORM 표준 기술
    -> 스프링부트 + JPA = 높은 개발 생산성 유지 + 빠르게 개발 가능

[섹션 2. 프로젝트 환경설정]

-   프로젝트 세팅 - Thymeleaf, Spring Data JPA, H2 Database, Lombok 라이브러리 사용

    -   jpashop 이름으로 프로젝트 생성

-   Thymeleaf

    -   View 환경 설정
    -   스프링 부트 thymeleaf viewName 매핑 : `resources:templates/`+{ViewName}+.html

-   JPA와 DB 설정

    -   H2 데이터베이스 설정: H2/bin/h2.bat 실행 ->서버 켜짐( jdbc:h2:tcp://localhost/~/jpashop)
    -   H2 데이터베이스의 JDBC URL - tcp로 경로를 설정하면 네트워크 모드로 접근

    -   application.yml

        -   ddl-auto: create => 애플리케이션 실행 시점에서 테이블 drop, 다시 생성

    -   동작 테스트
        -   memberEntity 생성 후 레포지토리에서 save, find 구현 -> 테스트

em.persist

```

```
