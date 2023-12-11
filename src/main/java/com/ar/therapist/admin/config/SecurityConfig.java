package com.ar.therapist.admin.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;
	
	@Value("${cors.set.allowed.origins}")
	private String[] CROSS_ORIGIN_URLS;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        .cors(Customizer.withDefaults())
		.csrf(csrf -> csrf.disable())
		.formLogin(formLogin -> formLogin.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .exceptionHandling((exceptionHandling) ->
			exceptionHandling
				.authenticationEntryPoint(new RestAuthenticationEntryPoint())
				//.accessDeniedPage("/errors/access-denied")
		)
		.authorizeHttpRequests(ahr ->
			ahr.requestMatchers(
					"/api/v1/auth/**", 
					"/pckart/api/v1/user-to-admin/user/**",
					"/pckart/api/v1/user-to-admin/coupon/**",
					"/pckart/api/v1/user-to-admin/order/**",
					"/api/v1/video/**"
			).permitAll()
			.anyRequest().authenticated()
		)
		.sessionManagement(sm -> 
		     sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    )
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		.logout((logout) -> logout
			.logoutUrl("/api/v1/auth/logout")
			.addLogoutHandler(logoutHandler)
			.logoutSuccessHandler((request, response, authentication) -> 
				SecurityContextHolder.clearContext()
			)
 		)
		;

    return http.build();
	}
    
	
	
//	@Bean
//	public CorsFilter corsFilter() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.setAllowCredentials(true);
//		corsConfiguration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
//		corsConfiguration.setAllowedHeaders(Arrays.asList(
//					"Origin","Access-Control-Allow-Origin", "Content-Type",
//					"Accept","Authorization","Origin, Accept","X-Requested-With",
//					"Access-Control-Request-Method","Access-Control-Request-Headers"
//				));
//		corsConfiguration.setExposedHeaders(Arrays.asList(
//					"Origin","Content-Type","Accept","Authorization",
//					"Access-Control-Allow-Origin","Access-Control-Allow-Origin",
//					"Access-Control-Allow-Credentials"
//				));
//		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//		return new CorsFilter(urlBasedCorsConfigurationSource);
//	}
	
	
	
//	@Bean
//	public SessionRegistry sessionRegistry() {
//		return new SessionRegistryImpl();
//	}
//	@Bean HttpSessionEventPublisher httpSessionEventPublisher() {
//			return new HttpSessionEventPublisher();
//	}
}

