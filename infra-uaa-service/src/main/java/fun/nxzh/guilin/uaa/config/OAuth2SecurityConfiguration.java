package fun.nxzh.guilin.uaa.config;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
public class OAuth2SecurityConfiguration {

  /** Resource server setup. */
  @Configuration
  @Order(70)
  @EnableResourceServer
  @Import(SecurityProblemSupport.class)
  public static class ResourceServerConfiguration implements ResourceServerConfigurer {

    private final TokenStore tokenStore;
    private static final String RESOURCE_ID = "uaa";
    private SecurityProblemSupport problemSupport;

    public ResourceServerConfiguration(
        TokenStore tokenStore, SecurityProblemSupport problemSupport) {
      this.tokenStore = tokenStore;
      this.problemSupport = problemSupport;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.exceptionHandling()
          .authenticationEntryPoint(problemSupport)
          .accessDeniedHandler(problemSupport)
          .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .requestMatchers()
          .antMatchers("/api/**", "/v2/**")
          .and()
          .authorizeRequests()
          .antMatchers("/v2/**")
          .permitAll()
          .antMatchers("/api/**")
          .authenticated()
          .and()
          .httpBasic()
          .disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
      resources
          .resourceId(RESOURCE_ID)
          .tokenStore(tokenStore)
          .accessDeniedHandler(problemSupport)
          .authenticationEntryPoint(problemSupport);
    }
  }

  @Configuration
  @Order(80)
  class JwkSetEndpointConfiguration extends AuthorizationServerSecurityConfiguration {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      super.configure(http);
      http.requestMatchers()
          .mvcMatchers("/.well-known/jwks.json")
          .and()
          .authorizeRequests()
          .mvcMatchers("/.well-known/jwks.json")
          .permitAll();
    }
  }

  /** Authorization server setup. */
  @Configuration
  @EnableAuthorizationServer
  @Import({SecurityProblemSupport.class, AuthorizationServerSecurityConfiguration.class})
  public static class AuthorizationServerConfiguration implements AuthorizationServerConfigurer {

    /** Default access token validity time is 2 hours. */
    private static final Integer DEFAULT_ACCESS_TOKEN_VALIDITY_SECS = 7200;

    /** Default refresh token validity time is 2 days. */
    private static final Integer DEFAULT_REFRESH_TOKEN_VALIDITY_SECS = 172_800;

    private ApplicationContext applicationContext;

    private final ApplicationProperties applicationProperties;

    private SecurityProblemSupport problemSupport;

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    public AuthorizationServerConfiguration(
        ApplicationContext applicationContext,
        ApplicationProperties applicationProperties,
        SecurityProblemSupport problemSupport,
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
      this.applicationContext = applicationContext;
      this.applicationProperties = applicationProperties;
      this.problemSupport = problemSupport;
      this.authenticationManager = authenticationManager;
      this.userDetailsService = userDetailsService;
      this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer authServer) throws Exception {
      // TODO: use builder to limit the access;
      authServer
          .realm("AUTH-SERVER")
          .accessDeniedHandler(problemSupport)
          .authenticationEntryPoint(problemSupport)
          .tokenKeyAccess(
              "isAnonymous() || hasAuthority('" + ClientAuthoritiesConstants.TRUSTED_CLIENT + "')")
          .checkTokenAccess("hasAuthority('" + ClientAuthoritiesConstants.TRUSTED_CLIENT + "')");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients
          .inMemory()
          .withClient("internal")
          .secret(passwordEncoder.encode("internal"))
          .authorities(ClientAuthoritiesConstants.TRUSTED_CLIENT)
          .scopes(ClientScopesConstants.ALL)
          .authorizedGrantTypes(OAuth2ClientGrantTypeConstants.CLIENT_CREDENTIALS)
          .and()
          .withClient("trust")
          .secret(passwordEncoder.encode("trust"))
          .authorities(ClientAuthoritiesConstants.TRUSTED_CLIENT)
          .scopes(ClientScopesConstants.ALL)
          .autoApprove(false)
          .redirectUris("/uaa/welcome")
          .authorizedGrantTypes(
              OAuth2ClientGrantTypeConstants.AUTHORIZATION_CODE,
              OAuth2ClientGrantTypeConstants.IMPLICIT,
              OAuth2ClientGrantTypeConstants.REFRESH_TOKEN,
              OAuth2ClientGrantTypeConstants.PASSWORD)
          .accessTokenValiditySeconds(DEFAULT_ACCESS_TOKEN_VALIDITY_SECS)
          .refreshTokenValiditySeconds(DEFAULT_REFRESH_TOKEN_VALIDITY_SECS)
          .and()
          .withClient("apidoc")
          .secret(passwordEncoder.encode("apidoc"))
          .authorities(ClientAuthoritiesConstants.TRUSTED_CLIENT)
          .scopes(ClientScopesConstants.ALL)
          .autoApprove(true)
          .authorizedGrantTypes(OAuth2ClientGrantTypeConstants.PASSWORD)
          .accessTokenValiditySeconds(DEFAULT_ACCESS_TOKEN_VALIDITY_SECS)
          .refreshTokenValiditySeconds(DEFAULT_REFRESH_TOKEN_VALIDITY_SECS)
          .and()
          .withClient("untrust")
          .secret(passwordEncoder.encode("untrust"))
          .authorities(ClientAuthoritiesConstants.UNTRUSTED_CLIENT)
          .scopes(ClientScopesConstants.OPEN_ID)
          .autoApprove(false)
          .redirectUris("https://www.github.com")
          .authorizedGrantTypes(
              OAuth2ClientGrantTypeConstants.AUTHORIZATION_CODE,
              OAuth2ClientGrantTypeConstants.IMPLICIT,
              OAuth2ClientGrantTypeConstants.REFRESH_TOKEN)
          .accessTokenValiditySeconds(DEFAULT_ACCESS_TOKEN_VALIDITY_SECS)
          .refreshTokenValiditySeconds(DEFAULT_REFRESH_TOKEN_VALIDITY_SECS);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      // pick up all  TokenEnhancers incl. those defined in the application
      // this avoids changes to this class if an application wants to add its own to the chain
      Collection<TokenEnhancer> tokenEnhancers =
          applicationContext.getBeansOfType(TokenEnhancer.class).values();
      TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
      tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
      endpoints
          .authenticationManager(authenticationManager) // allow password
          .userDetailsService(userDetailsService) // allow refresh_token
          .tokenStore(tokenStore())
          .tokenEnhancer(tokenEnhancerChain)
          .reuseRefreshTokens(false); // don't reuse or we will run into session inactivity timeouts
    }

    /**
     * Apply the token converter (and enhancer) for token store.
     *
     * @return the JwtTokenStore managing the tokens.
     */
    @Bean
    public JwtTokenStore tokenStore() {
      return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * This bean generates an token enhancer, which manages the exchange between JWT acces tokens
     * and Authentication in both directions.
     *
     * @return an access token converter configured with the authorization server's public/private
     *     keys
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
      converter.setKeyPair(keyPair());
      return converter;
    }

    @Bean
    public KeyPair keyPair() {
      return new KeyStoreKeyFactory(
              new ClassPathResource(applicationProperties.getKeyStore().getName()),
              applicationProperties.getKeyStore().getPassword().toCharArray())
          .getKeyPair(applicationProperties.getKeyStore().getAlias());
    }
  }
}
