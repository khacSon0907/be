package com.example.backendshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())  // Vô hiệu hóa CSRF
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("api/register","api/login","api/users").permitAll() // Cho phép truy cập API đăng ký
//                        .anyRequest().authenticated() // Các API khác yêu cầu xác thực
//                );
        http
                .csrf(csrf -> csrf.disable())  // Vô hiệu hóa CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Cho phép truy cập tất cả API
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}