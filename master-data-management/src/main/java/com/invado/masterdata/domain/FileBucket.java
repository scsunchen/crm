package com.invado.masterdata.domain;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Nikola on 26/12/2015.
 */
public class FileBucket {
    MultipartFile file;

    String description;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
