package com.cloud.filesystem.model;

import java.util.List;
import java.util.UUID;

public class FileDeleteRequest {
    private List<UUID> fileMetaDataIdList;

    public List<UUID> getFileMetaDataIdList() {
        return fileMetaDataIdList;
    }

    public void setFileMetaDataIdList(List<UUID> fileMetaDataIdList) {
        this.fileMetaDataIdList = fileMetaDataIdList;
    }
}
