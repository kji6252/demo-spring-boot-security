package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

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
