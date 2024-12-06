package com.modsen.passengerservice.controller.impl;

import com.modsen.passengerservice.aspect.ValidateAccessToResources;
import com.modsen.passengerservice.controller.general.PassengerOperations;
import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.service.PassengerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController implements PassengerOperations {

    private final PassengerService passengerService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerDto createPassenger(@RequestBody @Valid PassengerDto passengerDto) {
        return passengerService.createPassenger(passengerDto);
    }

    @Override
    @GetMapping
    public ListContainerResponseDto<PassengerDto> getPagePassengers(
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit) {
        return passengerService.getPagePassengers(offset, limit);
    }

    @Override
    @DeleteMapping("/{passengerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ValidateAccessToResources
    public void safeDeletePassenger(@PathVariable Long passengerId, JwtAuthenticationToken token) {
        passengerService.safeDeletePassengerById(passengerId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    @PutMapping("/{passengerId}")
    @ValidateAccessToResources
    public PassengerDto updatePassengerById(@PathVariable Long passengerId,
                                            @RequestBody @Valid PassengerDto passengerDto,
                                            JwtAuthenticationToken token) {
        return passengerService.updatePassengerById(passengerId, passengerDto);
    }

    @Override
    @GetMapping("/{passengerId}")
    public PassengerDto getPassengerById(@PathVariable Long passengerId) {
        return passengerService.getPassengerById(passengerId);
    }

}
