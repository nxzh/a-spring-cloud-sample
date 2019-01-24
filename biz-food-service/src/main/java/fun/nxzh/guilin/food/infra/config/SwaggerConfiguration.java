/*
 * Copyright 2016-2017 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see http://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fun.nxzh.guilin.food.infra.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Springfox Swagger configuration.
 *
 * <p>Warning! When having a lot of REST endpoints, Springfox can become a performance issue. In
 * that case, you can use a specific Spring profile for this class, so that only front-end
 * developers have access to the Swagger view.
 */
@Configuration
@ConditionalOnProperty(prefix = "application", name = "swagger.enable")
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

  private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

  private final ApplicationProperties applicationProperties;

  public SwaggerConfiguration(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  /**
   * Springfox configuration for the API Swagger docs.
   *
   * @return the Swagger Springfox configuration
   */
  @Bean
  public Docket swaggerSpringfoxApiDocket() {
    log.debug("Starting Swagger");
    StopWatch watch = new StopWatch();
    watch.start();
    Contact contact =
        new Contact(
            applicationProperties.getSwagger().getContactName(),
            applicationProperties.getSwagger().getContactUrl(),
            applicationProperties.getSwagger().getContactEmail());

    ApiInfo apiInfo =
        new ApiInfo(
            applicationProperties.getSwagger().getTitle(),
            applicationProperties.getSwagger().getDescription(),
            applicationProperties.getSwagger().getVersion(),
            applicationProperties.getSwagger().getTermsOfServiceUrl(),
            contact,
            applicationProperties.getSwagger().getLicense(),
            applicationProperties.getSwagger().getLicenseUrl(),
            new ArrayList<>());

    Docket docket =
        new Docket(DocumentationType.SWAGGER_2)
            .host(applicationProperties.getSwagger().getHost())
            .protocols(
                new HashSet<>(Arrays.asList(applicationProperties.getSwagger().getProtocols())))
            .apiInfo(apiInfo)
            .forCodeGeneration(true)
            .directModelSubstitute(java.nio.ByteBuffer.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .select()
            .paths(regex(applicationProperties.getSwagger().getDefaultIncludePattern()))
            .build();
    watch.stop();
    log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
    return docket;
  }

  /**
   * Springfox configuration f or the management endpoints (actuator) Swagger docs.
   *
   * @param appName the application name
   * @param managementContextPath the path to access management endpoints
   * @param appVersion the application version
   * @return the Swagger Springfox configuration
   */
  @Bean
  public Docket swaggerSpringfoxManagementDocket(
      @Value("${spring.application.name}") String appName,
      @Value("${management.endpoints.web.base-path}") String managementContextPath,
      @Value("${info.project.version}") String appVersion) {

    String host = applicationProperties.getSwagger().getHost();
    String[] protocols = applicationProperties.getSwagger().getProtocols();

    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(
            new ApiInfo(
                appName + " management API",
                "Management endpoints documentation",
                appVersion,
                "",
                ApiInfo.DEFAULT_CONTACT,
                "",
                "",
                new ArrayList<VendorExtension>()))
        .groupName("management")
        .host(host)
        .protocols(new HashSet<>(Arrays.asList(protocols)))
        .forCodeGeneration(true)
        .directModelSubstitute(java.nio.ByteBuffer.class, String.class)
        .genericModelSubstitutes(ResponseEntity.class)
        .select()
        .paths(regex(managementContextPath + ".*"))
        .build();
  }
}
