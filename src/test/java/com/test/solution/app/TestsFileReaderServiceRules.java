package com.test.solution.app;

import com.test.solution.app.service.rules.FileReaderServiceRules;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestsFileReaderServiceRules {

    @Autowired
    private FileReaderServiceRules rules;

    @Test
    public void fileInDirectoryTest() {
        Path rootPath = Paths.get("C:\\application\\data\\in");
        Path filePath = Paths.get("C:\\application\\data\\in\\some_file.dat");

        Assert.assertTrue(rules.fileIsInRootPath(rootPath, filePath));
    }

    @Test
    public void fileOutDirectoryTestOne() {
        Path rootPath = Paths.get("C:\\application\\data\\in");
        Path filePath = Paths.get("C:\\application\\data\\in\\history\\some_file.dat");

        Assert.assertFalse(rules.fileIsInRootPath(rootPath, filePath));
    }

    @Test
    public void fileOutDirectoryTestTwo() {
        Path rootPath = Paths.get("C:\\application\\data\\in");
        Path filePath = Paths.get("C:\\application\\data\\some_file.dat");

        Assert.assertFalse(rules.fileIsInRootPath(rootPath, filePath));
    }
}
