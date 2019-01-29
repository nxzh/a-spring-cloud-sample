package fun.nxzh.guilin.uaa.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserResource {
  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize(
      "#oauth2.hasScope(T(fun.nxzh.guilin.uaa.config.ClientScopesConstants).ALL) "
          + "and hasAuthority(T(fun.nxzh.guilin.uaa.config.UserAuthoritiesConstants).USER)")
  public ResponseEntity<?> me() {
    SecurityContext securityContext = SecurityContextHolder.getContext();

    Optional<String> uname =
        Optional.ofNullable(securityContext.getAuthentication())
            .map(
                authentication -> {
                  if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    return springSecurityUser.getUsername();
                  } else if (authentication.getPrincipal() instanceof String) {
                    return (String) authentication.getPrincipal();
                  }
                  return null;
                });
    Map<String, String> info = new HashMap<>();
    info.put("username", uname.get());
    return ResponseEntity.ok(info);
  }
}
