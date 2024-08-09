package com.humanity.commerce_api.DTOs;

public class ImagesByIdDTO {
    private String fileName;
    private String URL;

    public ImagesByIdDTO() {
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getFileName() {
        return fileName;
    }

    public String getURL() {
        return URL;
    }
}
