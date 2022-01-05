package projekt.projekt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .antMatchers("/staff").hasAuthority("ROLE_STAFF")
                .antMatchers("/client").hasAuthority("ROLE_CLIENT")
                .antMatchers("/client/*").hasAuthority("ROLE_CLIENT")
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/login").successHandler(authenticationSuccessHandler())
                .and().logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and().csrf().disable();
    }

    @Autowired
    DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){ return new AuthenticationHandler(); }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT name, password, 'true' from login WHERE name=?")
                .authoritiesByUsernameQuery("SELECT name, role, 'true' from login WHERE name=?");
    }
}
