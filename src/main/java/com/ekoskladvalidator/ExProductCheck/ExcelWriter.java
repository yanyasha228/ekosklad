package com.ekoskladvalidator.ExProductCheck;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
public class ExcelWriter {

    private static String myPromUrl = "https://my.prom.ua/cms/product/edit/%s";

    public void writeToExcel(List<ProductCheck> productChecks, String fileName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Analysis");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {
                    "ID",
                    "Ссылка Prom",
                    "Ссылка поставщик",
                    "Название",
                    "Дата последнего обновления",
                    "MSG"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Populate data rows
            int rowNum = 1;

            for (ProductCheck productCheck : productChecks) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(productCheck.getProduct().getId());
                row.createCell(1).setCellValue(String.format(myPromUrl, productCheck.getProduct().getId()));
                row.createCell(2).setCellValue(Objects.nonNull(productCheck.getProduct().getUrlForValidating()) ? productCheck.getProduct().getUrlForValidating() : "");
                row.createCell(3).setCellValue(Objects.nonNull(productCheck.getProduct().getName()) ? productCheck.getProduct().getName() : "");
                row.createCell(4).setCellValue(Objects.nonNull(productCheck.getProduct().getLastValidationDate()) ? productCheck.getProduct().getLastValidationDate().toLocalDate().toString() : "");
                row.createCell(5).setCellValue(Objects.nonNull(productCheck.getMsg())? productCheck.getMsg() : "");

            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
            }
        }
    }

}