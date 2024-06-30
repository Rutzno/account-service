package accountservice.security;

import accountservice.exception.CustomAccessDeniedHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

@Configuration
public class SecurityConfig {
    public static List<String> breachedPasswords = new ArrayList<>();
    private final String fileName;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(@Value("${login.breached-passwords-file}") String fileName, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.fileName = fileName;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @PostConstruct
    public void init() throws URISyntaxException, IOException {
        URL dataFile = ClassLoader.getSystemResource(fileName);
        breachedPasswords = Files.readAllLines(Path.of(dataFile.toURI()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)     // Handle auth errors
                .and()
                .csrf(AbstractHttpConfigurer::disable) // For Postman
                .headers(headers -> headers.frameOptions().disable()) // For the H2 console
                .authorizeHttpRequests(auth -> auth  // manage access
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        .requestMatchers("/api/empl/**").hasAnyRole("USER", "ACCOUNTANT")
                        .requestMatchers("/api/acct/**").hasRole("ACCOUNTANT")
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                        .requestMatchers("/api/security/events/**").hasRole("AUDITOR")
                        .requestMatchers("/h2-console/**").permitAll() // expose H2 console
                        .requestMatchers("/actuator/shutdown").permitAll() // required for tests
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // no session
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}