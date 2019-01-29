package fun.nxzh.guilin.uaa.config;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
        User.withUsername("user1").password(passwordEncoder().encode("111"))
            .authorities(UserAuthoritiesConstants.USER)
            .build());
    userDetailsManager.createUser(
        User.withUsername("user2").password(passwordEncoder().encode("111"))
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
    http
        .headers().frameOptions().sameOrigin()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .antMatchers("/js/**").permitAll()
        .antMatchers("/css/**").permitAll()
        .antMatchers("/v2/api-docs/**").permitAll()
        .antMatchers("/swagger-resources/configuration/ui").permitAll()
        .antMatchers("/swagger-resources/**").permitAll()
        .antMatchers("/webjars/springfox-swagger-ui/**").permitAll()
        .antMatchers("/swagger-ui.html").permitAll()
        .anyRequest().authenticated()
        .and().formLogin().loginPage("/login").permitAll();
  }

  /**
   * Authorization server setup.
   */
  @EnableAuthorizationServer
  @Import(SecurityProblemSupport.class)
  public static class AuthorizationServerConfiguration
      extends AuthorizationServerConfigurerAdapter {

    /**
     * Default access token validity time is 2 hours.
     */
    private static final Integer DEFAULT_ACCESS_TOKEN_VALIDITY_SECS = 7200;

    /**
     * Default refresh token validity time is 2 days.
     */
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
      clients.inMemory().withClient("mobile")
          .secret(passwordEncoder.encode("mobile"))
          .authorities(ClientAuthoritiesConstants.TRUSTED_CLIENT)
          .scopes(ClientScopesConstants.ALL)
          .autoApprove(true)
          .redirectUris("/uaa/welcome")
          .authorizedGrantTypes(
              OAuth2ClientGrantTypeConstants.AUTHORIZATION_CODE,
              OAuth2ClientGrantTypeConstants.IMPLICIT,
              OAuth2ClientGrantTypeConstants.REFRESH_TOKEN,
              OAuth2ClientGrantTypeConstants.PASSWORD
          )
          .accessTokenValiditySeconds(DEFAULT_ACCESS_TOKEN_VALIDITY_SECS)
          .refreshTokenValiditySeconds(DEFAULT_REFRESH_TOKEN_VALIDITY_SECS);
    }

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
     * keys
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
      KeyPair keyPair = new KeyStoreKeyFactory(
          new ClassPathResource(applicationProperties.getKeyStore().getName()),
          applicationProperties.getKeyStore().getPassword().toCharArray())
          .getKeyPair(applicationProperties.getKeyStore().getAlias());
      converter.setKeyPair(keyPair);
      return converter;
    }
  }
}
