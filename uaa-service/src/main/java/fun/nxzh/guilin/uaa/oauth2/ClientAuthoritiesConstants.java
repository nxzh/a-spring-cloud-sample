package fun.nxzh.guilin.uaa.oauth2;

/**
 * All the authorities can be assigned to a client in the system. One to one mapping to table
 * authority.
 */
public class ClientAuthoritiesConstants {

  public static String TRUSTED_CLIENT = "TRUSTED_CLIENT";

  public static String UNTRUSTED_CLIENT = "UNTRUSTED_CLIENT";

  /**
   * disallow to new instance.
   */
  private ClientAuthoritiesConstants() {
  }
}
