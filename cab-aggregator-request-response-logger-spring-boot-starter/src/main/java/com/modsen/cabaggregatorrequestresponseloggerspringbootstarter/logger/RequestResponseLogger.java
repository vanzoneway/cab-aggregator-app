package com.modsen.cabaggregatorrequestresponseloggerspringbootstarter.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Component
@Aspect
public class RequestResponseLogger {

    private final ObjectMapper objectMapper;

    private static final String REQUEST_LOGGING_MESSAGE = """
                   \s
            Request:
            Method: {}
            URI: {}
            Headers: {}
            Body: {}
           \s""";

    private static final String RESPONSE_LOGGING_MESSAGE = """
                   \s
            Response:
            Method: {}
            URI: {}
            Body: {}
            Time Taken: {} ms
           \s""";

    private static final String ERROR_SERIALIZING_JSON_MESSAGE = "Error serializing object to JSON";
    private static final String ERROR_SERIALIZING_REQUEST_BODY_MESSAGE = "Error serializing request body";

    @Around("@within(org.springframework.web.bind.annotation.RestController) && execution(* *(..))")
    public Object logHttpRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestBody = getRequestBody(joinPoint);
        log.info(REQUEST_LOGGING_MESSAGE,
                request.getMethod(),
                getRequestURIWithQueryString(request),
                getHeaders(request),
                requestBody
        );
        return getResponse(joinPoint, request);
    }

    @Around("@within(org.springframework.web.bind.annotation.RestControllerAdvice) && execution(* *(..))")
    public Object logHttpRequestResponseIfGlobalExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return getResponse(joinPoint, request);
    }

    private String getRequestURIWithQueryString(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            return requestURI + "?" + queryString;
        }
        return requestURI;
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder result = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            result.append(headerName).append(": ").append(headerValue).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    private Object getResponse(ProceedingJoinPoint joinPoint, HttpServletRequest request)
            throws Throwable {
        long startTime = System.currentTimeMillis();
        long endTime;
        Object response;
        response = joinPoint.proceed();
        endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String responseBody = convertObjectToJson(response);
        log.info(RESPONSE_LOGGING_MESSAGE,
                request.getMethod(),
                getRequestURIWithQueryString(request),
                responseBody,
                duration
        );
        return response;
    }

    private String getRequestBody(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (method.getParameterAnnotations()[i] != null) {
                for (Annotation annotation : method.getParameterAnnotations()[i]) {
                    if (annotation instanceof RequestBody) {
                        try {
                            return convertObjectToJson(args[i]);
                        } catch (Exception e) {
                            log.error(ERROR_SERIALIZING_REQUEST_BODY_MESSAGE, e);
                        }
                    }
                }
            }
        }
        return "";
    }

    private String convertObjectToJson(Object object) {
        if (object == null)
            return "";
        try {
            if (object instanceof @SuppressWarnings("rawtypes") ResponseEntity re
                    && re.getBody() instanceof InputStreamResource) {
                return "";
            }
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            return result.substring(0, result.length() - 1);
        } catch (Exception e) {
            log.error(ERROR_SERIALIZING_JSON_MESSAGE, e);
            return ERROR_SERIALIZING_JSON_MESSAGE;
        }
    }

}