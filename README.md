# 스프링 부트 보안
Spring Boot 버전 2.1.3
  
  스프링 시큐리티
  스프링의 인증(authorization), 인가(authentication), 보안 세션(secure session), 신원확인(identification), 암호화(encryption)등을 담당
  
  AuthenticationProvider를 중심으로 UserDetailsService를 제공 LDAP, 액티브 디렉터리, 커버로스, PAM, AOuth등의 신원 공급자 시스템과 통합 가능
  
  # 보안 적용 방법
  
  ## Default 적용
  AuthenticationManager인터페이스 구현체는 기본적으로 user와 GUID(실행 시 생성)를 가짐
  
  ## application.properties 적용
  
```properties
security.user.name = springboot
security.user.password = isawesome
```
    
  ```sh
  $ curl -I http://springboot:isawesome@localhost:8080/api
  ```
  
  ## 인메모리
  
  ```java
  @Configuration
  @EnableGlobalAuthentication
  public class InmemorySecurityConfiguration {
    @Autowired
    public void configureGlobal (AythenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication().withUser(“user”).password(“password”).roles(“USER”);
    }
  }
  ```
  
  ## DB
  ```java
  @Configuration
  @EnableGlobalAuthentication
  public class JdbcSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
  
      public JdbcSecurityConfiguration(UserDetailsService userDetailsService) {
          this.userDetailsService = userDetailsService;
      }
  
      public UserDetailsService userDetailsService(JdbcTemplate jdbcTemplate) {
          RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getString("ACCOUNT_NAME"),
            rs.getString("PASSWORD"),
            rs.getBoolean("ENABLED"),
                  rs.getBoolean("ENABLED"),
                  rs.getBoolean("ENABLED"),
                  rs.getBoolean("ENABLED"),
                  AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN")
          );
          return username -> jdbcTemplate.queryForObject("SELECT * FROM ACCOUNT where ACCOUNT_NAME = ?", userRowMapper, username);
      }
  
      private final UserDetailsService userDetailsService;
  
      @Override
      public void init(AuthenticationManagerBuilder auth) throws Exception {
          auth.userDetailsService(this.userDetailsService);
      }
  }
  ```
  
  ## 리소스
  HTTP의 특정 URL의 보안 설정을 할때 사용
  ```java
  @Configuration
  public class ResourceSecutiyuConfiguration extends WebSecurityConfigurerAdapter {
      @Override
      protected void configure(HttpSecurity http) throws Exception {
          http.authorizeRequests()
                  .antMatchers("/").permitAll()
                  .antMatchers("/api/**").authenticated()
                  .and()
                  .httpBasic();
      }
  }
  ```
  뒤의 httpBasic()대신 formLogin()으로 바꾸면 login Page로 이동 가능함
  
  ```java
  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
              .antMatchers("/").permitAll()
              .antMatchers("/api/**").authenticated()
              .and()
              .formLogin().loginPage("/login").permitAll()
              .and()
              .logout().permitAll();
  }
  ```
  
  
  # Oauth2
  build.gradle 파일에 Oauth2 라이브러리를 추가
  
  ```
  implementation 'org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure'
  ```
  
  ```java
  /**
   * @EnableAuthorizationServer
   * OAuth2 인증 서버를 구성합니다. 인증 끝점(/oauth/authorize)은 개발자가 직접 보안을 걸어야 하며, 토큰 끝점(/oauth/token)은 사용자 크레덴셜(여기서 DB에서 조회한 username과 password)에 따라 HTTP 기본 인증 방식으로 보안이 자동 적용됩니다.
   *
   * @EnableResourceServer
   * 리소스 서버를 구성합니다. 알맞은 OAuth2 토큰을 보낸 요청만 통과시키는 스프링 시큐리티 필터를 켭니다.
   */
  
  @Configuration
  @EnableAuthorizationServer
  @EnableResourceServer
  public class ResourceOAuthSecurityConfiguration extends ResourceServerConfigurerAdapter {
      @Override
      public void configure(HttpSecurity http) throws Exception {
          http.authorizeRequests()
                  .antMatchers("/").permitAll()
                  .antMatchers("/api/**").authenticated();
      }
  }
  ```
 
  토큰을 방행 받는 방식
  ```sh
  curl -i localhost:8080/oauth/token -d "grant_type=password&scope=read&username=springboot&password=isawesome" -u 클라이언트ID:비밀키
  
  결과
  {"access_token":"5651109b-a1ba-4001-9f4b-a6b9e22f21ea","token_type":"bearer","refresh_token":"74ccb0fe-a3f6-4602-b63b-90435f92bf18","expires_in":43199,"scope":"read"}
  ```
  
  위에서 발행된 토큰(access_token)을 가지고 보안에 걸린 리소스에 접근이 가능 합니다.
  
  ```sh
  curl -i -H "Authorization: bearer 5651109b-a1ba-4001-9f4b-a6b9e22f21ea" localhost:8080/api
  ```
  
  DB의 사용자 정보에 Oauth2의 각 사용자의 클라이언트ID와 비밀키를 담아둔 다음 access_token을 발행 하고 이 access_token을 가지고 개발을 진행 하면 좋습니다.
  개발 가이드 링크를 참조해서 개발하면 좋을듯
  https://spring.io/guides/tutorials/spring-boot-oauth2/