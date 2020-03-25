package com.cloud.filesystem.model;

import java.util.UUID;

public class GetFileData extends FileInfo {
    private byte[] blobData;


    public GetFileData(byte[] blobData, String fileName, UUID fileMetaDataId) {
        this.blobData = blobData;
        this.fileName = fileName;
        this.fileMetaDataId = fileMetaDataId;
    }

    public GetFileData() {
    }

    public byte[] getBlobData() {
        return blobData;
    }

    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
}
