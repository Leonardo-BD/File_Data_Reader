package com.test.solution.app.service.utils;

import com.test.solution.app.enumerator.InputType;
import com.test.solution.app.model.*;
import com.test.solution.app.service.rules.FileReaderServiceRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FileReaderServiceUtils {

    @Autowired
    private FileReaderServiceRules rules;

    @Value("${file-reader-service.worst-salesman-by-sallary}")
    private boolean worstSellerBySallary;

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
            String higherValueSaleId = "";

            BigDecimal saleHigherValue = new BigDecimal(0);

            Set<Seller> sellerList = new HashSet<>();
            Map<String, BigDecimal> sellerTotalSoldMap = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split("ç");

                if (lineSplit[0].equals(InputType.SELLER.getId())) {
                    Seller seller = new Seller(lineSplit[1], lineSplit[2], new BigDecimal(lineSplit[3]));

                    sellerList.add(seller);
                } else if (lineSplit[0].equals(InputType.CUSTOMER.getId())) {
                    customerCount++;
                } else if (lineSplit[0].equals(InputType.SALE.getId())) {
                    Sale sale = new Sale(lineSplit[1], new ArrayList<>(), lineSplit[3], new BigDecimal(0));

                    if (sellerTotalSoldMap.get(sale.getSalesmanName()) == null){
                        sellerTotalSoldMap.put(sale.getSalesmanName(), new BigDecimal(0));
                    }

                    lineSplit[2] = lineSplit[2].replace("[", "").replace("]", "");
                    for (String saleItemStr : lineSplit[2].split(",")) {
                        String[] saleItemSplit = saleItemStr.split("-");

                        SaleItem saleItem = new SaleItem(
                                Long.valueOf(saleItemSplit[0]),
                                new BigDecimal(saleItemSplit[1]),
                                new BigDecimal(saleItemSplit[2])
                        );

                        sale.getSaleItemList().add(saleItem);
                        sale.setTotalValue(sale.getTotalValue().add(saleItem.getPrice().multiply(saleItem.getQuantity())));
                    }

                    if (sale.getTotalValue().compareTo(saleHigherValue) == 1) {
                        saleHigherValue = sale.getTotalValue();
                        higherValueSaleId = sale.getSaleId();
                    }

                    sellerTotalSoldMap.put(sale.getSalesmanName(), sellerTotalSoldMap.get(sale.getSalesmanName()).add(sale.getTotalValue()));
                }
            }

            Seller worstSeller = getWorstSalesmanInTotalSoldMap(sellerList, sellerTotalSoldMap);

            return new OutputFileModel(customerCount, sellerList.size(), higherValueSaleId, worstSeller.getName());
        } catch (Exception e) {
            log.error("Error occurred during file " + filePath.getFileName().toString() + " processing");
            log.error(MSG_EXCEPTION, e);
            throw e;
        }
    }

    public Seller getWorstSalesmanInTotalSoldMap(Set<Seller> sellerList, Map<String, BigDecimal> sellerTotalSoldMap) {
        Seller worstSeller = null;
        BigDecimal worstScore = null;

        for (Seller seller : sellerList) {
            BigDecimal sellerScore = rules.getSellerScore(seller, sellerTotalSoldMap.get(seller.getName()));

            if (worstScore == null || sellerScore.compareTo(worstScore) == -1) {
                worstSeller = seller;
                worstScore = sellerScore;
            }
        }

        return worstSeller;
    }

    public void writeOutputFile(Path filePath, String outputPathStr, OutputFileModel outputFileModel) throws IOException {
        String worstSellerString = worstSellerBySallary ?
                "\nWorst salesman (by salary - sold value): " : "\nWorst salesman (by brute sold value): ";

        String outFileContent =
                "File: " +
                filePath.getFileName() +
                "\nTotal custommers: " +
                outputFileModel.getCustomerCount() +
                "\nTotal sellers: " +
                outputFileModel.getSellerCount() +
                "\nHigher value Sale ID: " +
                outputFileModel.getHigherValueSaleId() +
                worstSellerString +
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

    public String getOutputFileName(String fileName, String outputPathStr) {
        fileName = fileName.replace(" ", "_").replace(".dat", ".done.dat");

        return outputPathStr + fileName;
    }
}
