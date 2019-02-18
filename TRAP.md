# Trap

## Config Server

要将

## Hystrix + Turbine + Rabbit

Client: 

    spring:
      cloud:
        stream:
          bindings:
            hystrixStreamOutput:
              destination: guilin_hystrix
              
    feign:
      hystrix:
        enabled: true
    hystrix:
      shareSecurityContext: true
      
Server:

    spring:
      cloud:
        stream:
          bindings:
            turbineStreamInput:
              destination: guilin_hystrix
              
而且要在 main 方法上加上: `@EnableBinding`

## Sleuth + Zipkin + Rabbit

引入:

      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-sleuth-zipkin</artifactId>
      </dependency>
      <dependency>
          <groupId>org.springframework.amqp</groupId>
          <artifactId>spring-rabbit</artifactId>
      </dependency>
      
spring-boot 2.1.1.RELEASE 会报错, 修改方法, 使用 spring-boot 2.1.0.RELEASE.

