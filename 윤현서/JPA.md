스프링 부트는 복잡한 스프링 기술을 간단하게 쓸 수 있도록 한 프레임워크

-   JPA는 강력한 Java ORM 표준 기술
    -> 스프링부트 + JPA = 높은 개발 생산성 유지 + 빠르게 개발 가능

[섹션 2. 프로젝트 환경설정]

-   프로젝트 세팅 - Thymeleaf, Spring Data JPA, H2 Database, Lombok 라이브러리 사용

    -   jpashop 이름으로 프로젝트 생성

# Thymeleaf

    -   View 환경 설정
    -   스프링 부트 thymeleaf viewName 매핑 : `resources:templates/`+{ViewName}+.html

# JPA와 DB 설정

    -   H2 데이터베이스 설정: H2/bin/h2.bat 실행 ->서버 켜짐( jdbc:h2:tcp://localhost/~/jpashop)
    -   H2 데이터베이스의 JDBC URL - tcp로 경로를 설정하면 네트워크 모드로 접근

    -   application.yml

        -   ddl-auto: create => 애플리케이션 실행 시점에서 테이블 drop, 다시 생성

    -   동작 테스트
        -   memberEntity 생성 후 레포지토리에서 save, find 구현 -> 테스트

[JPA 관련 추가 스터디]

# JPA : ORM(Object-relational mapping) -> 객체지향과 DB간 발생하는 차이를 매핑함

-   객체는 객체대로 설계하고, DB와의 차이점에 대해서 ORM 통해 매핑함.
    -> 객체 중심 개발 가능, DB에 종속적이지 않게 됨.
-   JDBC API를 통해서 DB에 접근함(JPA - java application, JDBD API 사이에 존재함)
-   JPA는 인터페이스의 모음 / Hibernates는 그 구현체(EclipseLink, DataNucleus 등 더 있음)

# Entity : DB 테이블과 매핑되는 객체

# Entity Manager Factory : Entity Manager를 만들고 구성하는 법을 제공하는 interface

-   Hibernate, EclipseLink에 의해서 생성됨 - Entity Manager 인스턴스 생성에 사용됨.

# Entity Manager : DB table과 매핑된 객체인 엔티티에 대한 CRUD 작업을 수행하기 위한 메소드 제공

-   엔티티의 라이프 사이클, 영속성 관리 등 담당

@PersistenceContext(영속성 컨텍스트)

-   entity의 영속화에 관여함.
-   entity들이 DB에 바로 가지 않고 저장되는 환경으로서 작동함.
-   엔티티를 메모리에 캐시함 -> DB에 접근하는 횟수 감소, 로드 감소 가능
-   데이터베이스 엑세스 단순화
    -   낮은 수준의 데이터베이스 엑세스를 위한 쿼리보다 애플리케이션의 비즈니스 로직에 집중할 수 있음.
-   엔티티 라이프 사이클을 자동적으로 관리하여 엔티티가 일관성있도록 보장
-   확장성에 이점이 있음.
-   Entity 데이터 수정 시 update를 별도로 하지 않아도 영속성 컨텍스트에서 변경을 감지하여 insert, update가 실행됨.

1. Transaction scope persistence context

    - transaction에 바인딩 됨.
      -> transaction이 완료되는 즉시 영속성 컨텍스트에 있는 엔티티들이 DB로 flush 됨.
    - default 타입

2. Extended scope persistence context
    - 영속성 컨텍스트가 multiple transaction에 걸쳐 존재할 수 있음
    - transaction 없이 엔티티를 연속성 컨텍스트에 유지시킬 수는 있지만 DB에 flush할 수 없음.

Cf. flush: 영속성 컨텍스트의 변경 내용을 DB에 반영하는 것

EntityManager.persist(entity);
: db에 저장되는 것이 아니라, 영속성 컨텍스트에 보관, 관리됨

EntityManager.find(entity.class, entity.id);

1. 1차 캐시(영속성 컨텍스트 내부에 존재하는 캐시)에서 Entity 조회
2. 1차 캐시에 있으면 메모리에 있는 Entity 조회/없으면 DB에서 조회
3. 조회한 데이터로 Entity 생성 -> 1차 캐시에 저장 == 영속화
4. Entity 반환

# 엔티티의 생명주기(Entity LifeCycle)

-   비영속 (new/transient)
    : 영속성 컨텍스트와 무관한 상태(인스턴스를 생성만 한 상태)

-   영속 (managed)
    : 영속성 컨텍스트에 저장된 상태
-   Entity가 영속성 컨텍스트에 의해 관리되고 있는 상태
-   persist() -> commit -> 저장

-   준영속 (detached)
    : 영속성 컨텍스트에서 저장되었다가 분리된 상태 (제거된 상태)
-   detach(entity), clear(), close()

-   삭제 (remove)
    : 엔티티를 영속성 컨텍스트, DB에서 삭제
-   remove(entity)

영속성 컨텍스트는 애플리케이션이랑 db의 중간보관소인 줄 알았는데
엔티티를 영구적으로 저장하는 환경이라고 정의하네..

(아마 의도적으로 삭제하지 않는 한 영구적으로 가지고 있다는 뜻인 듯)

# Setter 주의

-   변경 포인트가 많으면 유지보수가 힘듦

# 연관관계 지연로딩 설정

-   즉시로딩(Eager)는 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어려움
    -   한 테이블을 사용하고 싶어도 연관된 테이블/데이터를 다 끌고와서 쿼리가 다수 발생함.(jpa 성능 하락 문제 발생 가능)
-   연관된 엔티티를 함께 DB에서 조회해야 한다면 fetch join, 엔티티 그래프 기능을 사용하는 걸 권장

# 컬렉션은 필드에서 초기화하자

-   하이버네이트가 persist하는 순간 컬렉션을 감싸는 내부 동작 발생 -> 컬랙션을 생성한 이후로 밖으로 꺼내거나 수정하지 않는 게 안전함.

# @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)

-   persist(order)만 해줘도 persist(orderItems)가 종속돼서 같이 저장됨
