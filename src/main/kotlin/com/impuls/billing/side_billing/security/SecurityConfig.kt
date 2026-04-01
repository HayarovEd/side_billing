package com.impuls.billing.side_billing.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    // Публичные эндпоинты (без авторизации)
                    .requestMatchers(
                        "/",           // Ваш эндпоинт
                        "/auth/**",    // Все эндпоинты аутентификации
                    )
                    .permitAll()
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs",
                        "/swagger-resources/**",
                        "/webjars/**"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .exceptionHandling { configurer ->
                configurer
                    .authenticationEntryPoint { request, response, authException ->
                        println("=== AuthenticationEntryPoint ===")
                        println("Path: ${request.servletPath}")
                        println("Method: ${request.method}")
                        println("Auth Exception: ${authException.message}")
                        println("Authorization header: ${request.getHeader("Authorization")}")

                        response.status = HttpServletResponse.SC_UNAUTHORIZED
                        response.contentType = "application/json"
                        response.writer.write("""{"error": "Unauthorized: ${authException.message}"}""")
                    }
            }
            .securityContext { securityContext ->
                securityContext.requireExplicitSave(false)  // Автоматически сохранять контекст
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    /*  @Bean
      fun passwordEncoder(): PasswordEncoder {
          return BCryptPasswordEncoder()
      }*/
}