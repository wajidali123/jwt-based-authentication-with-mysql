package org.johnpc.jwt.config;

import org.johnpc.jwt.UserPrincipleDetailService;
import org.johnpc.jwt.jwtconfig.JwtAuthenticationFilter;
import org.johnpc.jwt.jwtconfig.JwtAuthorizationFilter;
import org.johnpc.jwt.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserPrincipleDetailService userPrincipleDetailService;
    private UserRepository userRepository;

    public SecurityConfiguration(UserPrincipleDetailService userPrincipleDetailService, UserRepository userRepository) {
        this.userPrincipleDetailService = userPrincipleDetailService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // add jwt filters (1. authentication, 2. authorization)
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userRepository))
                .authorizeRequests()
                // configure access rules
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/api/public/management/*").hasRole("MANAGER")
                .antMatchers("/api/public/admin/*").hasRole("ADMIN")
                .anyRequest().authenticated();

    }


    @Bean
    DaoAuthenticationProvider authenticationProvider () {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipleDetailService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
