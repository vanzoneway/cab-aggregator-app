package com.modsen.driverservice.client.ride;

import com.modsen.driverservice.client.CustomFeignClientException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerErrorException;

@Component
public class RideFeignClientFallbackFactory implements FallbackFactory<RideFeignClient> {

    @Override
    public RideFeignClient create(Throwable cause) {
        return (driverId) -> {
            if (cause.getCause() instanceof CustomFeignClientException customFeignClientException) {
                throw customFeignClientException;
            }
            throw new ServerErrorException(cause.getMessage(), cause);
        };
    }

}
