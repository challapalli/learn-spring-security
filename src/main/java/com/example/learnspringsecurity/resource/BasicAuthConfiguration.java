package com.example.learnspringsecurity.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

//@Configuration
public class BasicAuthConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.anyRequest().authenticated();
        });
        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.csrf().disable();
        //http.formLogin();
        http.httpBasic();
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }

/*    @Bean
    UserDetailsService userDetailsService() {
        var user = User.builder()
                .username("chandu")
                .password("{noop}chandu")
                .roles(Roles.USER.name()).build();
        var admin = User.builder()
                .username("nihal")
                .password("{noop}nihal")
                .roles(Roles.ADMIN.name()).build();
        return new InMemoryUserDetailsManager(user, admin);
    }*/

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        var user = User.builder()
                .username("chandu")
                //.password("{noop}chandu")
                .password("chandu")
                .passwordEncoder(str -> passwordEncoder().encode(str))
                .roles(Roles.USER.name()).build();
        var admin = User.builder()
                .username("nihal")
                //.password("{noop}nihal")
                .password("nihal")
                .passwordEncoder(str -> passwordEncoder().encode(str))
                .roles(Roles.ADMIN.name(), Roles.USER.name()).build();
        var userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);
        return userDetailsManager;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}