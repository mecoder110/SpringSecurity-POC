package com.spring_security.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {


        //        .authorizeHttpRequests(auth -> auth
        //            .requestMatchers("/h2-console/**", "/public", "/auth/**").permitAll() // ⬅️ Add this line
        //            .anyRequest().authenticated()
        //        )
        http.csrf(csrf->csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers("api/public/**","/h2-console/**")
                                .permitAll()
                                .anyRequest().authenticated())
                // http.formLogin(withDefaults());
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {

        UserDetails user1 = User.withUsername("user")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build();
        UserDetails user2 = User.withUsername("admin")
                .password(passwordEncoder().encode("12346"))
                .roles("ADMIN")
                .build();

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.createUser(user1);
        jdbcUserDetailsManager.createUser(user2);
        return jdbcUserDetailsManager;
        //return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
