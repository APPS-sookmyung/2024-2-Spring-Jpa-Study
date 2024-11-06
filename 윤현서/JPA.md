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

    # 지연로딩 전략

    : 연관관계의 엔티티가 실제로 필요해질 때 쿼리를 실행하여 연관관계의 엔티티를 조회하는 전략

    -   조회 대상 엔티티의 연관관계에 엔티티를 흉내내는 가짜 프록시 객체를 넣고, 실제로 프록시 객체에 요청이 들어오면 실제 연관관계의 엔티티를 조회함

    # 프록시 패턴

    : 특정 객체로의 접근을 제어하는 대리인을 제공하는 것

    -   클라이언트는 대리인(프록시)이 마치 진짜 객체라고 생각하여 데이터를 주고 받음
        -> 클라이언트와 진짜 객체가 직접 데이터를 주고받을 수 없는 경우에 둘 사이의 접근을 제어하는 역할을 함.
    -   원격 프록시: 원격 객체로의 접근 제어
    -   가상 프록시: 생성하기 힘든 자원으로의 접근 제어
    -   보호 프록시: 접근 권한이 필요한 자원으로의 접근 제어

# 컬렉션은 필드에서 초기화하자

-   하이버네이트가 persist하는 순간 컬렉션을 감싸는 내부 동작 발생 -> 컬랙션을 생성한 이후로 밖으로 꺼내거나 수정하지 않는 게 안전함.

# @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)

-   persist(order)만 해줘도 persist(orderItems)가 종속돼서 같이 저장됨

[섹션 6. 상품 도메인 개발]

-   재고를 넣고 빼는 비즈니스 로직의 stockquantity를 가지고 있는 item 엔티티 도메인 안에 추가/감소 로직을 넣어 관리하기 편하도록 설정

    -   응집력 향상
    -   도메인 모델 패턴 (엔티티가 비즈니스 로직을 가지고 객체 지향의 특징을 적극 활용하는 것)
    -   엔티티 밖 서비스 계층에서 처리하는 것 = 트랜잭션 스크립트 패턴
    -   어떤 패턴이 유지보수에 용이한지 고민해보는 것이 필요함
    -   각 패턴의 장단점이나 활용 분야를 찾아보는 게 좋을 것 같음

-   id이 null = 새로 생성하는 객체

    -   이미 있는 id에 save => update 개념

-   트랜잭션 옵션은 메서드에 가까운게 우선권을 가짐

    **도메인 모델 패턴**

: 비즈니스 로직이 엔티티 안에 구성되어 서비스 계층은 엔티티에 필요한 역할을 위임함

-   엔티티 안에 비즈니스 로직을 가지고 객체지향을 활용하는 기법
-   주요 비즈니스 로직이 객체 내부에 캡슐화되어 있어 데이터와 메서드가 객체로 묶여 있음
-   구현방법
    → 비즈니스 영역에서 사용되는 객체 판별
    → 객체가 제공해야 할 목록 추출
    → 객체간의 관계 정립
-   **도메인 모델 장점**
    -   객체 지향에 기반한 재사용성, 확장성, 유지 보수의 편리함
    -   도메인에 대한 직관적 표현이 가능해 코드를 이해하기 쉬움
-   **도메인 모델 단점**
    -   초기 설계가 어려움
    -   객체와 데이터베이스 사이의 매핑에 대한 어려움
    -   코드가 복잡해질 수 있음
-   은행 입출금 예시

    ```java
    @Entity
    public class Account {
    	private String accountNumber;
    	private double balance;

    	public Account(String accountNumber, double balance){
    		this.accountNumber = accountNumber;
    		this.balance = balance;
    	}

    	public void deposit(double amount){
    		if (amount <= 0){
    			throw new IllegalArgumentException("must be positive");
    		}
    		this.balance += amount;
    	}

    	public void withdraw(double amount){
    		if (amount > this.balance){
    			throw new IllegalArgumentException("amount not enough");
    		}
    		this.balance -= amount;
    	}
    }
    ```

**트랜잭션 스크립트 패턴**

: 서비스 계층에서 비즈니스 로직을 처리하는 것

-   하나의 트랜잭션으로 구성된 로직
-   단일 함수 / 단일 스크립트에서 처리하는 구조
-   엔티티는 단순하게 데이터를 전달하는 역할만 수행함

-   **트랜잭션 코드 장점**
    -   구현이 쉬움
    -   직관적임
-   **트랜잭션 코드 단점**
    -   비즈니스 로직이 확장될수록 코드가 복잡해짐
    -   도메인 분석, 설계 개념이 약해 코드 중복을 막기 어려움
    -   공통된 코드를 공통 모듈로 분리하지 않고 중복 코드를 발생시킬 위험이 있음
    -
-   **적합한 상황**
    -   비즈니스 로직이 단순한 경우
    -   CRUD 중심의 애플리케이션과 같이 적은 코드량이 요구되는 프로젝트
    -   트랜잭션이 데이터베이스 내에서 독립적으로 실행되고, 공유 상태가 별로 없는 경우
-   은행 입출금 예시

    ```java
    @Service
    public class BankService {
    // 서비스에서 로직이 구현됨
        public void deposit(String accountNumber, double amount) {
            if (!accounts.containsKey(accountNumber)) {
                throw new IllegalArgumentException("Account not found.");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive.");
            }
            accounts.put(accountNumber, accounts.get(accountNumber) + amount);
        }

        public void withdraw(String accountNumber, double amount) {
            if (!accounts.containsKey(accountNumber)) {
                throw new IllegalArgumentException("Account not found.");
            }
            if (amount > accounts.get(accountNumber)) {
                throw new IllegalArgumentException("Insufficient funds.");
            }
            accounts.put(accountNumber, accounts.get(accountNumber) - amount);
        }
    }
    ```

**다른 디자인 패턴**

1. 액티브 레코드 패턴
    - 모든 query 메소드를 모델에 정의하고 객체의 저장, 제거, 조회 기능을 모델의 메소드를 통해 사용하는 패턴
2. 테이블 모듈 패턴
    - 데이터베이스에 있는 테이블마다 하나의 클래스에 대응하는 도메인 로직 구성
    - 클래스의 단일 인스턴스는 데이터에 행동할 수 있는 다양한 프로시저를 포함함
3. 서비스 레이어 패턴
    - 서비스를 제공하는 레이어를 별도로 제공하는 구조
    - 클라이언트들이 공통적으로 필요로 하는 기능을 제공해야 할 때 알맞은 구조
    - 어플리케이션 로직을 담기 적당함
4. 데이터 매퍼 패턴
    - 모든 쿼리 메소드들을 별도의 클래스에 정의
