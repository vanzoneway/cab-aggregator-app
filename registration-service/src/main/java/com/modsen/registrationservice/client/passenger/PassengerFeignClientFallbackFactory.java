package com.modsen.registrationservice.client.passenger;

import com.modsen.registrationservice.client.CustomFeignClientException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerErrorException;

@Component
public class PassengerFeignClientFallbackFactory implements FallbackFactory<PassengerFeignClient> {

    @Override
    public PassengerFeignClient create(Throwable cause) {
        return (passengerId, acceptLanguage) -> {
            if (cause.getCause() instanceof CustomFeignClientException customFeignClientException) {
                throw customFeignClientException;
            }
            throw new ServerErrorException(cause.getMessage(), cause);
        };
    }

}
