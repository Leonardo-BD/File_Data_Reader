package com.test.solution.app.service.rules;

import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FileReaderServiceRules {

    public boolean fileIsInRootPath(Path rootPath, Path filePath) {
        int rootPathStrLength = rootPath.toString().length();
        int fileNameStrLength = filePath.getFileName().toString().length();

        return  rootPathStrLength + fileNameStrLength + 1 == filePath.toString().length(); //+1 is a dir separator (/)
    }
}
