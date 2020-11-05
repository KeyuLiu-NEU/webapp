package com.cloud.config;


import com.cloud.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private CustomAuthenticationProvider authProvider;
    @Autowired
    private UserDetailsService userService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
            //auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
             auth.authenticationProvider(authProvider);
            /*
            auth
                    .inMemoryAuthentication()
                    .withUser("tom").password(bCryptPasswordEncoder().encode("123")).roles("User");
            */
            //auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().ignoringAntMatchers("/v1/user");
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionFixation().none().and()
                .csrf().disable();


        http
                .authorizeRequests()
                .antMatchers("/v1/user/self").permitAll()
                .antMatchers("/v1/question/").permitAll()
               .antMatchers("/v1/question/*").permitAll().
                antMatchers("/v1/questions").permitAll()
                .antMatchers("/v1/question/{id}/answer").permitAll()
            .and()
                .httpBasic();


    }
}
