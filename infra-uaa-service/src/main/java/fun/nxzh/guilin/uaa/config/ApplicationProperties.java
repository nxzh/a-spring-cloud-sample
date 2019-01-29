package fun.nxzh.guilin.uaa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

  private KeyStore keyStore = new KeyStore();

  private Swagger swagger = new Swagger();

  public KeyStore getKeyStore() {
    return keyStore;
  }

  public Swagger getSwagger() {
    return swagger;
  }


  public static class KeyStore {

    //name of the keystore in the classpath
    private String name = "keystore.jks";
    //password used to access the key
    private String password = "password";
    //name of the alias to fetch
    private String alias = "selfsigned";

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getAlias() {
      return alias;
    }

    public void setAlias(String alias) {
      this.alias = alias;
    }
  }

  public static class Swagger {

    private Boolean enable = false;

    private String title = "Application API";

    private String description = "API documentation";

    private String version = "0.0.1";

    private String termsOfServiceUrl;

    private String contactName;

    private String contactUrl;

    private String contactEmail;

    private String license;

    private String licenseUrl;

    private String defaultIncludePattern = "/api/.*";

    private String host;

    private String[] protocols = {};

    private String accessTokenUri;

    private String oauthClientId;

    private String oauthClientSecret;

    public Boolean getEnable() {
      return enable;
    }

    public void setEnable(Boolean enable) {
      this.enable = enable;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public String getTermsOfServiceUrl() {
      return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
      this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContactName() {
      return contactName;
    }

    public void setContactName(String contactName) {
      this.contactName = contactName;
    }

    public String getContactUrl() {
      return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
      this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
      return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
      this.contactEmail = contactEmail;
    }

    public String getLicense() {
      return license;
    }

    public void setLicense(String license) {
      this.license = license;
    }

    public String getLicenseUrl() {
      return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
      this.licenseUrl = licenseUrl;
    }

    public String getDefaultIncludePattern() {
      return defaultIncludePattern;
    }

    public void setDefaultIncludePattern(String defaultIncludePattern) {
      this.defaultIncludePattern = defaultIncludePattern;
    }

    public String getHost() {
      return host;
    }

    public void setHost(final String host) {
      this.host = host;
    }

    public String[] getProtocols() {
      return protocols;
    }

    public void setProtocols(final String[] protocols) {
      this.protocols = protocols;
    }

    public String getAccessTokenUri() {
      return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
      this.accessTokenUri = accessTokenUri;
    }

    public String getOauthClientId() {
      return oauthClientId;
    }

    public void setOauthClientId(String oauthClientId) {
      this.oauthClientId = oauthClientId;
    }

    public String getOauthClientSecret() {
      return oauthClientSecret;
    }

    public void setOauthClientSecret(String oauthClientSecret) {
      this.oauthClientSecret = oauthClientSecret;
    }
  }
}
