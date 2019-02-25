# 스프링부트 2.1.3 실습
주요 실습 모듈
* spring-boot-starter-data-jpa
* spring-boot-starter-data-rest
* spring-data-rest-hal-browser

# 실습시 유의 사항
* hibernate5 버전 부터는
기존 Naming strategy가 Implicit Naming Strategy와 Physical Naming Strategy로 둘로 나눠 지면서 naming strategy를 구체적으로 설정이 가능해졌다
```properties
#카멜케이스Entity > 언더스코어DB_Table
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
```

* Spring boot 2부터는 실행중에 data.sql, schema.sql이 자동으로 실행이 안되므로 아래의 옵션을 넣어주어야 한다
```properties
spring.datasource.initialization-mode=always
```

그리고 autoincrement 기능이 안먹혀서 아래의 속성을 넣어야 정상 작동 함
```properties
spring.jpa.hibernate.use-new-id-generator-mappings=false
```


# 알게 된 점
* JsonSerializer를 상속받아 도메인 get메서드에 직접 @JsonSerialize를 설정 해주었고
해당 설정을 거치면 HttpMessageConverter<T>클래스를 통해 내부에서 알아서 처리됨
* Spring data rest를 추가하고 실행한 뒤 localhost:8080으로 접속하면
  응답값의 Header의 Content-type값에 application/hal+json형식으로 날라 온다.
  이는 Hypertext Application Language로써 Rest의 끝점을 관리하는 방법이다.
  그래서 어떤 자원들이 있고 어떻게 접속하는지에 대해서 나오게 된다.