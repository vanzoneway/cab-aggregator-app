package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.controller.StatisticReportOperations;
import com.modsen.driverservice.service.StatisticReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/drivers/notifications")
@RequiredArgsConstructor
public class StatisticReportController implements StatisticReportOperations {

    private final StatisticReportService statisticReportService;

    @Override
    @PostMapping
    public void triggerSendingReports() {
        statisticReportService.scheduledSenderStatisticToNotificationService();
    }

}
