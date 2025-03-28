package com.modsen.driverservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;

@Validated
@Tag(name = "statistic-report-operations", description = """
        The endpoints contained here are intended for operations related to triggering the sending of statistic reports.
        These reports contain ride statistics for drivers and are sent to the notification service.

        It is important to note that JWT authorization is used here: only ROLE_ADMIN can trigger the sending of reports.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface StatisticReportOperations {

    @Operation(summary = "Triggers the sending of statistic reports",
            description = """
                    Triggers the process of sending statistic reports for all active drivers. 
                    The reports include ride statistics and are sent to the notification service.

                    This operation is typically scheduled to run automatically (e.g., monthly), 
                    but it can also be triggered manually by an ADMIN.

                    Example request:
                    POST /api/v1/drivers/notifications

                    Example response:
                    - Status Code: 200 (OK)
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistic reports sent successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint")
    })
    void triggerSendingReports();

}
