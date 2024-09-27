package com.egg.biblioteca;

import com.egg.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            // Permite acceso a los recursos estáticos en "/videos/**"
            .antMatchers("/videos/**").permitAll()
            // Acceso a recursos estáticos como CSS, JS, e imágenes
            .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
            // Rutas protegidas por rol ADMIN
            .antMatchers("/admin/**").hasRole("ADMIN")
            // Permite acceso a todas las demás rutas
            .antMatchers("/**").permitAll()
            .and()
        .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/logincheck")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/inicio")
            .permitAll()
            .and()
        .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll()
            .and()
        .csrf()
            .disable(); // Considera habilitar CSRF en producción
}
 }