package hexlet.code.config;

import hexlet.code.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider)
            throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.authenticationProvider(authenticationProvider);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers("/", "/index.html", "/favicon.ico").permitAll()
                .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**", "/static/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/users/*").permitAll()

                .requestMatchers(HttpMethod.GET,  "/api/task_statuses").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/task_statuses/*").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/task-statuses").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/task-statuses/*").permitAll()

                .requestMatchers(HttpMethod.GET,  "/api/tasks").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/tasks/*").permitAll()

                .requestMatchers(HttpMethod.GET,  "/api/labels").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/labels/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/labels").permitAll()

                .anyRequest().authenticated()
        );

        http.httpBasic(h -> h.disable());
        http.formLogin(f -> f.disable());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
