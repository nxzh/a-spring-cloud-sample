package fun.nxzh.guilin.facade.config;

/**
 * All the authorities can be assigned to a client in the system. One to one mapping to table
 * authority.
 */
public class ClientAuthoritiesConstants {

  public static String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

  public static String UNTRUSTED_CLIENT = "ROLE_UNTRUSTED_CLIENT";

  /** disallow to new instance. */
  private ClientAuthoritiesConstants() {}
}
