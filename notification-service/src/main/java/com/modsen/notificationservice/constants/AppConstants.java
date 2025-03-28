package com.modsen.notificationservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstants {

    public static final String MAIL_SUBJECT = "User Statistics Report";
    public static final String GREETING_TEXT_CLASSPATH = "classpath:html/greeting_text.html";
    public static final String REPORT_TEXT_CLASSPATH = "classpath:html/statistic_report_for_pdf.html";
    public static final String PDF_ATTACHMENT_TYPE = "application/pdf";
    public static final String REPORT_FILE_NAME = "UserStatisticsReport.pdf";

    //Exception message
    public static final String NULL_HTML_STRING_EXCEPTION_MESSAGE = "Null HTML String";

}

