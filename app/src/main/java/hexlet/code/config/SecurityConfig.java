package hexlet.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/task_statuses/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tasks/**").permitAll()
                .requestMatchers("/api/task_statuses/**").authenticated()
                .requestMatchers("/api/tasks/**").authenticated()
                .requestMatchers("/api/labels/**").authenticated()
                .requestMatchers("/api/users").permitAll()
                .anyRequest().permitAll()
        );

        http.httpBasic(withDefaults());
        return http.build();
    }
}
