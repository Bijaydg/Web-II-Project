package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService CustomUserDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        AuthenticationManagerBuilder authBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
//        authBuilder.userDetailsService(CustomUserDetailsService).passwordEncoder(encoder());
//        return authBuilder.build();
//    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.requestMatchers("/SM/**").authenticated()
                    .anyRequest().permitAll())
                    .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/tasks/")
                        .failureUrl("/login?error=true")
                        .permitAll())
                    .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return httpSecurity.build();
    }
}

//package com.example.demo.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@Enable
//public class SecurityConfig {
//	@Autowired
//	
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//    AuthenticationManage authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http
//                .getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//
//        return authenticationManagerBuilder.build();
//    }
//
//	@Bean
//	SecurityFilterChai securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable())
//				.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").authenticated().anyRequest().permitAll())
//				.formLogin(formLogin -> formLogin.loginPage("/login").loginProcessingUrl("/login")
//						.defaultSuccessUrl("/admin/").failureUrl("/login?error=true").permitAll()
//				).logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
//				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
//
//		return http.build();
//	}
//}