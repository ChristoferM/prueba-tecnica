package com.fondos.inversion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(basic -> basic.realmName("API Fondos de Inversión"))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/usuarios/registro").permitAll()
                .requestMatchers("/api/usuarios/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/usuarios/").hasRole("ADMIN")  // Solo ADMIN ve lista
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()   // Usuario autenticado ve su perfil
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN") // Solo ADMIN borra
                .requestMatchers("/api/**").authenticated()                  // Todo lo demás requiere login
                
                .anyRequest().authenticated()
            )
            
            .csrf(csrf -> csrf.disable())
            
            .cors(cors -> cors.disable())
            
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        
        return http.build();
    }
}

