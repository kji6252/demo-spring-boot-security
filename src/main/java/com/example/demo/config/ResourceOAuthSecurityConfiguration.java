package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

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
