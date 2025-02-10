package com.modsen.passengerservice.aspect;

import com.modsen.cabaggregatorexceptionspringbootstarter.exception.AccessToResourcesDeniedException;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidateAccessToResourcesAspect {

    private final PassengerService passengerService;

    private static final String ROLE_ADMIN_VALUE = "ROLE_ADMIN";
    private static final String EMAIL_CLAIM = "email";
    private static final String DEFAULT_ACCESS_DENIED_MESSAGE = "Access denied";

    @Around("@annotation(com.modsen.passengerservice.aspect.ValidateAccessToResources)")
    public Object hasPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        JwtAuthenticationToken token = (JwtAuthenticationToken) args[args.length - 1];
        Long userId = (Long) args[0];
        if (token.getAuthorities().stream()
                .filter(Objects::nonNull)
                .anyMatch(authority -> authority.getAuthority()
                        .equals(ROLE_ADMIN_VALUE))) {
            return joinPoint.proceed();
        }
        if (!Objects.equals(passengerService.getPassengerById(userId).email(),
                token.getToken().getClaims().get(EMAIL_CLAIM))) {
            throw new AccessToResourcesDeniedException(DEFAULT_ACCESS_DENIED_MESSAGE);
        }
        return joinPoint.proceed();
    }

}
