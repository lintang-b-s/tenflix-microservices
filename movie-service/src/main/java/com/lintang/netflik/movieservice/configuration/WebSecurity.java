package com.lintang.netflik.movieservice.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
////
//        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests((authz) -> authz.requestMatchers(HttpMethod.POST, "/api/v1/**")
//                        // .hasAuthority("SCOPE_profile")
//                        .hasRole("admin")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/**")
//                        .hasRole("admin")
//                        // .hasAnyAuthority("ROLE_developer")
//                        // .hasAnyRole("devleoper","user")
//                        .anyRequest().authenticated())
//                .oauth2ResourceServer(
//                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
//
//        return http.build();

         http
                 .cors().disable().csrf().disable().authorizeRequests()
                .antMatchers( "/api/v1/movie-service/movies").hasAnyRole("admin", "user", "default-roles-tenflix")
                 .antMatchers( "/api/v1/movie-service/actors").hasAnyRole("admin", "user", "default-roles-tenflix")
                 .antMatchers( "/api/v1/movie-service/creators").hasAnyRole("admin", "user", "default-roles-tenflix")
                 .antMatchers( "/api/v1/movie-service/upload").hasAnyRole("admin", "user", "default-roles-tenflix")
                 .antMatchers( "/api/v1/movie-service/videos").hasAnyRole("admin", "user", "default-roles-tenflix")

                 .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and().oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
         return;
//                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
