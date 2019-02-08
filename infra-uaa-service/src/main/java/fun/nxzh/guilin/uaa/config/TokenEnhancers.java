package fun.nxzh.guilin.uaa.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

public class TokenEnhancers {
  @Component
  static class UserIdEnhancer implements TokenEnhancer {

    private void putUserInformation(OAuth2AccessToken accessToken, String userId) {
      final Map<String, Object> additionalInfo = new HashMap<>();
      additionalInfo.put("user_id", userId);

      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    }

    @Override
    public OAuth2AccessToken enhance(
        OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        org.springframework.security.core.userdetails.User loginDetails =
            (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String userId = "UNKNOWN";
        if (loginDetails.getUsername().equals("user1")) {
          userId = "12345";
        } else if (loginDetails.getUsername().equals("user2")) {
          userId = "23456";
        }
        putUserInformation(accessToken, userId);
      }
      return accessToken;
    }
  }
}
