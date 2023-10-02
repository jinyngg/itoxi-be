package com.itoxi.petnuri.global.component;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileUploadComponent {

    @Value("${file.upload.path.windows}")
    private String fileUploadPathWindows;

    @Value("${file.upload.path.linux}")
    private String fileUploadPathLinux;

    public String getFileUploadPath() {
        String separator = File.separator;
        String fileUploadPath =
                separator.equals("\\") ? fileUploadPathWindows : fileUploadPathLinux;

        createDirectoryIfNotExists(fileUploadPath);
        return fileUploadPath;
    }

    public void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
						    log.info("make directory success!");
            }
        }
    }
}
