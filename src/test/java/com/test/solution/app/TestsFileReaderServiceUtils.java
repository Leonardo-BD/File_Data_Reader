package com.test.solution.app;

import com.test.solution.app.model.OutputFileModel;
import com.test.solution.app.service.utils.FileReaderServiceUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestsFileReaderServiceUtils {

    @Autowired
    private FileReaderServiceUtils utils;

    @Value("${tests.files.file-one}")
    private String fileOnePathStr;

    @Value("${tests.files.file-two}")
    private String fileTwoPathStr;

    @Value("${file-reader-service.worst-salesman-by-sallary}")
    private boolean worstSellerBySallary;

    @Test
    public void CreateOutFileModelTest() throws IOException {
        OutputFileModel fileOneModelExpected = new OutputFileModel(2, 2, "10", worstSellerBySallary ? "Pedro" : "Paulo");
        Path fileOnePath = Paths.get(fileOnePathStr);

        OutputFileModel fileTwoModelExpected = new OutputFileModel(4, 3, "05", worstSellerBySallary ? "Gustavo" : "Ana");
        Path fileTwoPath = Paths.get(fileTwoPathStr);

        Assert.assertEquals(fileOneModelExpected, utils.createOutFileModel(fileOnePath));
        Assert.assertEquals(fileTwoModelExpected, utils.createOutFileModel(fileTwoPath));
    }

    @Test
    public void getOutputFileNameTest() {
        String fileNameOne = "file one.dat";
        String fileNameTwo = "file.two.dat";
        String outputPathStr = "C:\\application\\data\\output\\";

        String expectedNameFileOne = "C:\\application\\data\\output\\file_one.done.dat";
        String expectedNameFileTwo = "C:\\application\\data\\output\\file.two.done.dat";

        String nonExpectedNameFileOne = "C:\\application\\data\\output\\file one.done.dat";
        String nonExpectedNameFileTwo = "C:\\application\\data\\output\\file.two.dat";

        Assert.assertEquals(expectedNameFileOne, utils.getOutputFileName(fileNameOne, outputPathStr));
        Assert.assertEquals(expectedNameFileTwo, utils.getOutputFileName(fileNameTwo, outputPathStr));

        Assert.assertNotEquals(nonExpectedNameFileOne, utils.getOutputFileName(fileNameOne, outputPathStr));
        Assert.assertNotEquals(nonExpectedNameFileTwo, utils.getOutputFileName(fileNameTwo, outputPathStr));
    }
}
