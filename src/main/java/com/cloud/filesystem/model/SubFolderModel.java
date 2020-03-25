package com.cloud.filesystem.model;

import java.util.List;

public class SubFolderModel {
    List<FolderInfo> folderList;

    public List<FolderInfo> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<FolderInfo> folderList) {
        this.folderList = folderList;
    }
}
