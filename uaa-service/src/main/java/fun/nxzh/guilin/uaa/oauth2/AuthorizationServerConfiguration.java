package fun.nxzh.guilin.uaa.oauth2;

import fun.nxzh.guilin.uaa.config.ApplicationProperties;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

/**
 * Authorization server setup.
 */
@Configuration
@EnableAuthorizationServer
@Import(SecurityProblemSupport.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  /**
   * Default access token validity time is 2 hours.
   */
  private static final Integer DEFAULT_ACCESS_TOKEN_VALIDITY_SECS = 7200;

  /**
   * Default refresh token validity time is 2 days.
   */
  private static final Integer DEFAULT_REFRESH_TOKEN_VALIDITY_SECS = 172_800;

  private ApplicationContext applicationContext;

  private SecurityProblemSupport problemSupport;

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  private ApplicationProperties applicationProperties;


  public AuthorizationServerConfiguration(
      ApplicationProperties applicationProperties,
      ApplicationContext applicationContext,
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService,
      SecurityProblemSupport problemSupport) {
    this.applicationProperties = applicationProperties;
    this
    this.userDetailsService = userDetailsService;
    this.applicationContext = applicationContext;
    this.problemSupport = problemSupport;
  }

  /**
   * Use to configure security constraint with Token Endpoint.
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer authServer) throws Exception {
    authServer
        .accessDeniedHandler(problemSupport)
        .authenticationEntryPoint(problemSupport)
        .tokenKeyAccess(
            "isAnonymous() || hasAuthority('" + ClientAuthoritiesConstants.TRUSTED_CLIENT + "')")
        .checkTokenAccess("hasAuthority('" + ClientAuthoritiesConstants.TRUSTED_CLIENT + "')");
  }

  /**
   * Use to configure OAuth Client.
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("genie-mobile")
        .secret("genie-mobile")
        .authorities(ClientAuthoritiesConstants.TRUSTED_CLIENT)
        .scopes(ClientScopesConstants.ALL)
        .autoApprove(true)
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
        .secret("apidoc")
        .authorities(ClientAuthoritiesConstants.TRUSTED_CLIENT)
        .scopes(ClientScopesConstants.ALL)
        .autoApprove(true)
        .authorizedGrantTypes(OAuth2ClientGrantTypeConstants.PASSWORD)
        .accessTokenValiditySeconds(DEFAULT_ACCESS_TOKEN_VALIDITY_SECS)
        .refreshTokenValiditySeconds(DEFAULT_REFRESH_TOKEN_VALIDITY_SECS);
  }

  /**
   * Use to configure the 'authorization' and 'token' endpoints and token services.
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    //pick up all  TokenEnhancers incl. those defined in the application
    //this avoids changes to this class if an application wants to add its own to the chain
    Collection<TokenEnhancer> tokenEnhancers = applicationContext
        .getBeansOfType(TokenEnhancer.class).values();
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
    endpoints
        .authenticationManager(authenticationManager)   // allow password
        .userDetailsService(userDetailsService)         // allow refresh_token
        .tokenStore(tokenStore())
        .tokenEnhancer(tokenEnhancerChain)
        .reuseRefreshTokens(
            false);     //don't reuse or we will run into session inactivity timeouts
  }
}
