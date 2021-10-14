package com.works.configs;

import com.works.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    // sql içinde sorgulama yaparak kullanıcının varlığını ve rolü'nü denetler.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(userService.encoder());
    }

    // rollere göre kullanıcı hangi sayfaya giriş yapacak ise ilgili denetimi yapar.
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http.rememberMe().key("").tokenValiditySeconds(5000);

        http.httpBasic().and().headers().frameOptions().disable().and()
                .authorizeRequests()
                //----------------HTML-PAGES----------------------------------------------------------------------------
                .antMatchers("/admin/**").hasRole("MVC")
                .antMatchers("/rest/**").hasAnyRole("MVC","REST","CUSTOMER")
                //------------------------------------------------------------------------------------------------------
                .antMatchers("/home").permitAll()
                .antMatchers("/gallery").permitAll()
                .antMatchers("/contact/**").permitAll()
                .antMatchers("/about").permitAll()
                .antMatchers("/forgotpassword/**").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/getXDistricts/**").permitAll()
                //----------------MASTER-PAGE---------------------------------------------------------------------------
                .antMatchers("/adminpanel/incadminpanel/header/**").hasRole("MVC")
                .antMatchers("/adminpanel/incadminpanel/layout/**").hasRole("MVC")
                .antMatchers("/adminpanel/incadminpanel/sidebar/**").hasRole("MVC")
                //------------------------------------------------------------------------------------------------------
                .antMatchers("/homepanel/inchome/header/**").permitAll()
                .antMatchers("/homepanel/inchome/layout/**").permitAll()
                .antMatchers("/homepanel/inchome/sidebar/**").permitAll()
                //------------------FRONT-END---------------------------------------------------------------------------
                .antMatchers("/css/**").permitAll()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/gifs/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/uploads/**").permitAll()
                //-----------------ERROR PAGES--------------------------------------------------------------------------
                .antMatchers("/403/**").permitAll()
                .antMatchers("/404/**").permitAll()
                .antMatchers("/405/**").permitAll()
                //------------------------------------------------------------------------------------------------------
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(userService)
                .permitAll();
        http.csrf().disable();
    }
    //Overloading Method
    //For Swagger
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}