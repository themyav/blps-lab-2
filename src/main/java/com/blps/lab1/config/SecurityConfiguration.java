package com.blps.lab1.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails normalUser = User.builder()
                .username("gc")
                .password("$2a$12$pLlEDlW7J3LJMLMl0Uv8Xu.NO1TYDvrMmIpoDhpHZ3So65XlsR.Vy")
                .roles("USER")
                .authorities("CAN_PUBLISH")
                .build();
        UserDetails adminUser = User.builder()
                .username("admin")
                .password("$2a$12$4MVGfzHJ2C370at3MTGHdeX6z/kon2X5KbVWZTGfqjWBhj.KnQBuC")
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //тут все ок
        return httpSecurity.authorizeHttpRequests(registry -> {
            registry.requestMatchers("/home").permitAll();
            registry.requestMatchers("/vacancy/publish").hasRole("ADMIN");
            registry.requestMatchers("/vacancy/draft").hasAuthority("CAN_PUBLISH");
            registry.anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults()).formLogin(Customizer.withDefaults()).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}