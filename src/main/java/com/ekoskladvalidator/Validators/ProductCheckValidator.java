package com.ekoskladvalidator.Validators;

import com.ekoskladvalidator.ExProductCheck.ExProductCheckService;
import com.ekoskladvalidator.ExProductCheck.ExcelWriter;
import com.ekoskladvalidator.ExProductCheck.ProductCheck;
import com.ekoskladvalidator.out.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public final class ProductCheckValidator {

    @Value("${eko.techMail}")
    private String techMail;

    @Value("${eko.report}")
    private String filePath;

    private final EmailService emailService;

    private final ExProductCheckService exProductCheckService;

    private final ExcelWriter excelWriter;


    public ProductCheckValidator(EmailService emailService, ExProductCheckService exProductCheckService, ExcelWriter excelWriter) {
        this.emailService = emailService;
        this.exProductCheckService = exProductCheckService;
        this.excelWriter = excelWriter;
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "Europe/Moscow")
    public void genereateReport() throws IOException, MessagingException {
        LocalDate today = LocalDate.now();
        List<ProductCheck> productChecks = exProductCheckService.checkProducts();
        excelWriter.writeToExcel(productChecks, filePath);
        emailService.sendEmailWithAttachment(
                techMail,
                "Выгрузка 404 " + today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth(),
                "",
                filePath);
    }
}
