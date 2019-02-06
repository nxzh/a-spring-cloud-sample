package fun.nxzh.guilin.facade.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@Order(90)
@Import(SecurityProblemSupport.class)
public class ActuatorSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private SecurityProblemSupport problemSupport;

  public ActuatorSecurityConfiguration(SecurityProblemSupport securityProblemSupport) {
    this.problemSupport = securityProblemSupport;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.exceptionHandling()
        .authenticationEntryPoint(problemSupport)
        .accessDeniedHandler(problemSupport)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
        .anyRequest().hasAuthority(UserAuthoritiesConstants.SYSTEM)
        .and()
        .httpBasic();
  }
}
