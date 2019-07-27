package com.test.solution.app.service.utils;

import com.test.solution.app.enumerator.InputType;
import com.test.solution.app.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Component
public class FileReaderServiceUtils {

    private final Logger log = LoggerFactory.getLogger(FileReaderServiceUtils.class);
    private static final String MSG_EXCEPTION = "Exception::";

    public void createDirsIfNotExist(String inputPathStr, String outputPathStr, String historyPathStr) throws IOException {
        try {
            Files.createDirectories(Paths.get(inputPathStr));
            Files.createDirectories(Paths.get(outputPathStr));
            Files.createDirectories(Paths.get(historyPathStr));
        } catch (IOException e) {
            log.error("Error while creating /data directories");
            log.error(MSG_EXCEPTION, e);
            throw e;
        }
    }

    public OutputFileModel createOutFileModel(Path filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toFile()), StandardCharsets.UTF_8))) {

            int customerCount = 0;
            int sellerCount = 0;
            String higherValueSaleId = "";
            String worstSeller = "";

            BigDecimal saleLowerValue = null;
            BigDecimal saleHigherValue = new BigDecimal(0);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split("ç");

                if (lineSplit[0].equals(InputType.SELLER.getId())) {
                    sellerCount++;
                } else if (lineSplit[0].equals(InputType.CUSTOMER.getId())) {
                    customerCount++;
                } else if (lineSplit[0].equals(InputType.SALE.getId())) {
                    BigDecimal saleTotalValue = new BigDecimal(0);

                    Sale sale = new Sale(lineSplit[1], new ArrayList<>(), lineSplit[3]);

                    lineSplit[2] = lineSplit[2].replace("[", "").replace("]", "");

                    for (String saleItemStr : lineSplit[2].split(",")) {
                        String[] saleItemSplit = saleItemStr.split("-");

                        SaleItem saleItem = new SaleItem(
                                Long.valueOf(saleItemSplit[0]),
                                new BigDecimal(saleItemSplit[1]),
                                new BigDecimal(saleItemSplit[2])
                        );

                        saleTotalValue = saleTotalValue.add(saleItem.getPrice().multiply(saleItem.getQuantity()));
                    }

                    if (saleTotalValue.compareTo(saleHigherValue) == 1) {
                        saleHigherValue = saleTotalValue;
                        higherValueSaleId = sale.getSaleId();
                    }

                    if (saleLowerValue == null || saleTotalValue.compareTo(saleLowerValue) == -1) {
                        saleLowerValue = saleTotalValue;
                        worstSeller = sale.getSalesmanName();
                    }
                }
            }

            return new OutputFileModel(customerCount, sellerCount, higherValueSaleId, worstSeller);
        } catch (Exception e) {
            log.error("Error occurred during file " + filePath.getFileName().toString() + " processing");
            log.error(MSG_EXCEPTION, e);
            throw e;
        }
    }

    public void writeOutputFile(Path filePath, String outputPathStr, OutputFileModel outputFileModel) throws IOException {
        String outFileContent =
                "File: " +
                filePath.getFileName() +
                "\nTotal custommers: " +
                outputFileModel.getCustomerCount() +
                "\nTotal sellers: " +
                outputFileModel.getSellerCount() +
                "\nHigher value Sale ID: " +
                outputFileModel.getHigherValueSaleId() +
                "\nWorst seller: " +
                outputFileModel.getWorstSeller();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getOutputFileName(filePath.getFileName().toString(), outputPathStr)))) {
            writer.write(outFileContent);

            log.info("File " + filePath.getFileName().toString() + " was successfully outputted to /data/out");
        } catch (Exception e) {
            log.error("Error occurred during file " + filePath.getFileName().toString() + " outputting to /data/out");
            log.error(MSG_EXCEPTION, e);
            throw e;
        }
    }

    private String getOutputFileName(String fileName, String outputPathStr) {
        fileName = fileName.replace(" ", "_");
        String[] fileNameSplit = fileName.split("\\.");

        return outputPathStr + fileNameSplit[0] + ".done.dat";
    }
}
