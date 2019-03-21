package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @EnableAuthorizationServer
 * OAuth2 ���� ������ �����մϴ�. ���� ����(/oauth/authorize)�� �����ڰ� ���� ������ �ɾ�� �ϸ�, ��ū ����(/oauth/token)�� ����� ũ������(���⼭ DB���� ��ȸ�� username�� password)�� ���� HTTP �⺻ ���� ������� ������ �ڵ� ����˴ϴ�.
 *
 * @EnableResourceServer
 * ���ҽ� ������ �����մϴ�. �˸��� OAuth2 ��ū�� ���� ��û�� �����Ű�� ������ ��ť��Ƽ ���͸� �մϴ�.
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
