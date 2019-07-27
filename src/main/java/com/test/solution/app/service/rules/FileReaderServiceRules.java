package com.test.solution.app.service.rules;

import com.test.solution.app.model.Seller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Path;

@Component
public class FileReaderServiceRules {

    @Value("${file-reader-service.worst-salesman-by-sallary}")
    private boolean worstSellerBySallary;

    public boolean fileIsInRootPath(Path rootPath, Path filePath) {
        int rootPathStrLength = rootPath.toString().length();
        int fileNameStrLength = filePath.getFileName().toString().length();

        return  rootPathStrLength + fileNameStrLength + 1 == filePath.toString().length(); //+1 is a dir separator (/)
    }

    public BigDecimal getSellerScore(Seller seller, BigDecimal totalSoldValue) {
        return worstSellerBySallary ? totalSoldValue.subtract(seller.getSalary()) : totalSoldValue;
    }
}
