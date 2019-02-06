package fun.nxzh.guilin.facade.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignConfiguration {
  @Bean
  public RequestInterceptor requestTokenBearerInterceptor() {
    return requestTemplate -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;

      requestTemplate.header("Authorization", "Bearer " + jwtToken.getToken().getTokenValue());
    };
  }
}
