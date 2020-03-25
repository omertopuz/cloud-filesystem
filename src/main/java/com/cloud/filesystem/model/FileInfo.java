package com.cloud.filesystem.model;

import java.util.UUID;

public class FileInfo {
    protected String fileName;
    protected UUID fileMetaDataId;
    protected int folderId;
    private String category;
    private String keyField;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UUID getFileMetaDataId() {
        return fileMetaDataId;
    }

    public void setFileMetaDataId(UUID fileMetaDataId) {
        this.fileMetaDataId = fileMetaDataId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }
}
