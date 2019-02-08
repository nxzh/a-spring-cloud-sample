package fun.nxzh.guilin.basket.config.security;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private SecurityProblemSupport problemSupport;

  public WebSecurityConfiguration(SecurityProblemSupport problemSupport) {
    this.problemSupport = problemSupport;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.exceptionHandling()
        .authenticationEntryPoint(problemSupport)
        .accessDeniedHandler(problemSupport)
        .and()
        .csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/api/**")
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(grantedAuthoritiesExtractor());
  }

  Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
    return new GrantedAuthoritiesExtractor();
  }

  static class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
      Collection<GrantedAuthority> authoritiesAll = super.extractAuthorities(jwt);
      Collection<String> authorities = (Collection<String>)jwt.getClaims().get("authorities");
      if (authorities != null) {
        authoritiesAll.addAll(authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
      }

      return authoritiesAll;
    }
  }
  //
  //  Converter<Jwt, AbstractAuthenticationToken> customAuthenticationConverter() {
  //    return new CustomAuthenticationConverter();
  //  }
  //
  //  static class CustomAuthenticationConverter
  //      implements Converter<Jwt, AbstractAuthenticationToken> {
  //    public AbstractOAuth2TokenAuthenticationToken convert(Jwt jwt) {
  //      Collection<String> authorities =
  //          (Collection<String>) jwt.getClaims().getOrDefault("authorities", new ArrayList<>());
  //      authorities.addAll((Collection<String>) jwt.getClaims().get("scope"));
  //      Collection<GrantedAuthority> auths =
  //          authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  //      return new CustomJwtAuthenticationToken(jwt, auths);
  //    }
  //  }
  //
  //  static class CustomJwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt>
  // {
  //
  //    public CustomJwtAuthenticationToken(
  //        Jwt token, Collection<? extends GrantedAuthority> authorities) {
  //      super(token, authorities);
  //    }
  //
  //    @Override
  //    public Collection<GrantedAuthority> getAuthorities() {
  //      Jwt jwt = this.getToken();
  //      Collection<String> authorities =
  //          (Collection<String>) jwt.getClaims().getOrDefault("authorities", new ArrayList<>());
  //      authorities.addAll((Collection<String>) jwt.getClaims().get("scope"));
  //      return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  //    }
  //
  //    @Override
  //    public String getName() {
  //      if (this.getPrincipal() instanceof Jwt) {
  //        String username =
  //            (String) ((Jwt) this.getPrincipal()).getClaims().getOrDefault("user_name", "NONE");
  //        String clientId = (String) ((Jwt) this.getPrincipal()).getClaims().get("client_id");
  //        return username + "@" + clientId;
  //      }
  //      return super.getName();
  //    }
  //
  //    @Override
  //    public Map<String, Object> getTokenAttributes() {
  //      return this.getToken().getClaims();
  //    }
  //  }
}
