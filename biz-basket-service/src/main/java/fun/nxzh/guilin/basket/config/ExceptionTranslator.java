package fun.nxzh.guilin.basket.config;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationProblem;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures. The
 * error response follows RFC7807 - Problem Details for HTTP APIs
 * (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

  /** Post-process Problem payload to add the message key for front-end if needed */
  @Override
  public ResponseEntity<Problem> process(
      @Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
    if (entity == null || entity.getBody() == null) {
      return entity;
    }
    Problem problem = entity.getBody();
    if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
      return entity;
    }
    ProblemBuilder builder =
        Problem.builder()
            .withType(Problem.DEFAULT_TYPE)
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());

    if (problem instanceof ConstraintViolationProblem) {
      builder
          .with("violations", ((ConstraintViolationProblem) problem).getViolations())
          .with("message", Problem.DEFAULT_TYPE);
      return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    } else {
      builder
          .withCause(((DefaultProblem) problem).getCause())
          .withDetail(problem.getDetail())
          .withInstance(problem.getInstance());
      problem.getParameters().forEach(builder::with);
      if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
        builder.with("message", "error.http." + problem.getStatus().getStatusCode());
      }
      return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }
  }
}
