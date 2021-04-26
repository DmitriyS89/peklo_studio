package com.peklo.peklo.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final DataSource dataSource;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/chats/add", "/chats", "/message/add").authenticated()
//                .antMatchers("/users/user").authenticated()
//                .anyRequest().permitAll()
                .antMatchers("/**").permitAll()
                .and()
                .httpBasic();

        http.formLogin()
                .defaultSuccessUrl("/chats/");

        http.logout()
                .logoutSuccessUrl("/chats/");
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        String fetchUserQuery = "select email, password, enabled from users where email = ?";
//        String fetchRolesQuery = "select email, role from users where email = ?";
//
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery(fetchUserQuery)
//                .authoritiesByUsernameQuery(fetchRolesQuery);
//    }
}