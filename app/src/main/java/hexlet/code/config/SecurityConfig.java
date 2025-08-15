package hexlet.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> res.setStatus(401)));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET,
                        "/",
                        "/index.html",
                        "/static/**",
                        "/assets/**",
                        "/favicon.ico",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/task-statuses/**",
                        "/api/task_statuses/**",
                        "/api/users/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/login", "/login", "/api/users/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/users/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").permitAll()
                .anyRequest().authenticated()
        );

        return http.build();
    }
}
