package com.test.solution.app.service;

import com.test.solution.app.model.*;
import com.test.solution.app.service.rules.FileReaderServiceRules;
import com.test.solution.app.service.utils.FileReaderServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Leonardo Brum Dorneles
 *
 * File reader service
 *
 * */
@Component
public class FileReaderService {

    @Autowired
    private FileReaderServiceUtils utils;

    @Autowired
    private FileReaderServiceRules rules;

    @Value("${file-reader-service.paths.input}")
    private String inputPathStr;

    @Value("${file-reader-service.paths.output}")
    private String outputPathStr;

    @Value("${file-reader-service.paths.history}")
    private String historyPathStr;

    @Value("${file-reader-service.file.extension}")
    private String fileExtension;

    private List<File> inputFileList = new ArrayList<>();

    private final Logger log = LoggerFactory.getLogger(FileReaderService.class);
    private static final String MSG_EXCEPTION = "Exception::";

    @Scheduled(initialDelayString = "${file-reader-service.initial-delay}", fixedRateString = "${file-reader-service.time-rate}")
    public void inputReader() throws IOException {
        utils.createDirsIfNotExist(inputPathStr, outputPathStr, historyPathStr);

        Path inputPath = Paths.get(inputPathStr);

        try(Stream<Path> paths = Files.walk(inputPath)) {
            paths.filter(fp -> rules.fileIsInRootPath(inputPath, fp) && fp.toString().endsWith(fileExtension)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        OutputFileModel outputFileModel = utils.createOutFileModel(filePath);
                        utils.writeOutputFile(filePath, outputPathStr, outputFileModel);

                        inputFileList.add(filePath.toFile());
                    } catch (IOException ex) {
                        log.error(MSG_EXCEPTION, ex);
                    }
                }
            });
        } catch (Exception e) {
            log.error(MSG_EXCEPTION, e);
            throw e;
        }

        moveReadedFilesToHistory();
    }

    private void moveReadedFilesToHistory() throws IOException {
        try {
            for (File file : inputFileList) {
                Files.move(Paths.get(file.getAbsolutePath()), Paths.get(historyPathStr + file.getName()).toAbsolutePath());
            }

            inputFileList.clear();
        } catch (IOException e) {
            log.error(MSG_EXCEPTION, e);
            throw e;
        }
    }
}
