package com.spring_security.demo.configuration;

import com.spring_security.demo.configuration.jwt.AuthEntryPointJwt;
import com.spring_security.demo.configuration.jwt.AuthTokenCustomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Bean
    AuthTokenCustomFilter authTokenCustomFilter() {
        return new AuthTokenCustomFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers("api/public/**", "/h2-console/**").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                .anyRequest().authenticated())
                // http.formLogin(withDefaults());
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //httpBasic(withDefaults());
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt));

        http.addFilterBefore(authTokenCustomFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    CommandLineRunner initData(UserDetailsService userDetailsService) {
        return args -> {
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

            //return new InMemoryUserDetailsManager(user1, user2);
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

}
