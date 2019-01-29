package fun.nxzh.guilin.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class LoginSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
    userDetailsManager.createUser(
        User.withUsername("system")
            .password(passwordEncoder().encode("sys123"))
            .authorities(UserAuthoritiesConstants.SYSTEM)
            .build());
    userDetailsManager.createUser(
        User.withUsername("user1")
            .password(passwordEncoder().encode("111"))
            .authorities(UserAuthoritiesConstants.USER)
            .build());
    userDetailsManager.createUser(
        User.withUsername("user2")
            .password(passwordEncoder().encode("111"))
            .authorities(UserAuthoritiesConstants.USER)
            .build());
    userDetailsManager.createUser(
        User.withUsername("admin")
            .password(passwordEncoder().encode("admin123"))
            .authorities(UserAuthoritiesConstants.ADMIN)
            .build());
    return userDetailsManager;
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.headers()
        .frameOptions()
        .sameOrigin()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .antMatchers("/js/**")
        .permitAll()
        .antMatchers("/css/**")
        .permitAll()
        .antMatchers("/v2/api-docs/**")
        .permitAll()
        .antMatchers("/swagger-resources/configuration/ui")
        .permitAll()
        .antMatchers("/swagger-resources/**")
        .permitAll()
        .antMatchers("/webjars/springfox-swagger-ui/**")
        .permitAll()
        .antMatchers("/swagger-ui.html")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll();
  }
}
