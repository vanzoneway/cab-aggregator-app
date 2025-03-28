package com.modsen.notificationservice.service.impl;

import com.modsen.notificationservice.constants.AppConstants;
import com.modsen.notificationservice.dto.ride.RideResponseDto;
import com.modsen.notificationservice.dto.statistic.GeneralUserStatisticDto;
import com.modsen.notificationservice.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    @Override
    public void sendUserStatisticOnEmail(GeneralUserStatisticDto generalUserStatisticDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setTo(generalUserStatisticDto.user().email());
            mimeMessageHelper.setSubject(AppConstants.MAIL_SUBJECT);
            mimeMessageHelper.setText(buildHtmlGreetingText(generalUserStatisticDto)
                    .orElseThrow(() -> new IOException(AppConstants.NULL_HTML_STRING_EXCEPTION_MESSAGE)), true);

            String htmlReport = buildReportAsStringForPdf(generalUserStatisticDto);
            ByteArrayOutputStream pdfStream = generatePdf(htmlReport);
            mimeMessageHelper.addAttachment(AppConstants.REPORT_FILE_NAME,
                    new ByteArrayDataSource(pdfStream.toByteArray(), AppConstants.PDF_ATTACHMENT_TYPE));
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Optional<String> buildHtmlGreetingText(GeneralUserStatisticDto generalUserStatisticDto) throws IOException {
        String template = new String(FileCopyUtils.copyToByteArray(
                resourceLoader.getResource(AppConstants.GREETING_TEXT_CLASSPATH).getInputStream()),
                StandardCharsets.UTF_8);
        return Optional.of(template.replace("{{fullName}}", generalUserStatisticDto.user().firstName()
                + " " + generalUserStatisticDto.user().lastName()));
    }

    private String buildReportAsStringForPdf(GeneralUserStatisticDto generalUserStatisticDto) throws IOException {
        String template = new String(FileCopyUtils.copyToByteArray(
                resourceLoader.getResource(AppConstants.REPORT_TEXT_CLASSPATH).getInputStream()),
                StandardCharsets.UTF_8);
        String ridesHtml = generalUserStatisticDto.rideStatistic().rides().stream()
                .map(this::buildRideRow)
                .collect(Collectors.joining());
        return template.replace("{{firstName}}", generalUserStatisticDto.user().firstName())
                .replace("{{lastName}}", generalUserStatisticDto.user().lastName())
                .replace("{{userType}}", generalUserStatisticDto.userType())
                .replace("{{averageRating}}", String.valueOf(generalUserStatisticDto.user().averageRating()))
                .replace("{{rides}}", ridesHtml)
                .replace("{{averageRideCost}}",
                        String.valueOf(generalUserStatisticDto.rideStatistic().averageCost()));
    }

    private String buildRideRow(RideResponseDto ride) {
        return "<tr>"
                + "<td>" + ride.id() + "</td>"
                + "<td>" + ride.driverId() + "</td>"
                + "<td>" + ride.passengerId() + "</td>"
                + "<td>" + ride.departureAddress() + "</td>"
                + "<td>" + ride.destinationAddress() + "</td>"
                + "<td>" + ride.rideStatus() + "</td>"
                + "<td>" + ride.orderDateTime() + "</td>"
                + "<td>" + ride.cost() + "</td>"
                + "</tr>";
    }

    private ByteArrayOutputStream generatePdf(String htmlContent) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream;
    }

}