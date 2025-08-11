package hexlet.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/task_statuses/**").permitAll()
                .requestMatchers("/api/task_statuses/**").authenticated()   // POST/PUT/DELETE
                .anyRequest().permitAll()
        );

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}