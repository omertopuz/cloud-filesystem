package com.cloud.filesystem.controller;

import com.cloud.filesystem.model.*;
import com.cloud.filesystem.service.FolderAndFileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rest/cloudfilesystem")
public class CloudFileRestController {

    @Autowired
    private FolderAndFileServices service;

    @GetMapping("/filedata")
    public GetFileData filedata(String fileMetaDataId,String fileName){
        GetFileData response = service.GetFileBlobData(UUID.fromString(fileMetaDataId),fileName);
        return response;
    }

    @GetMapping("/rootfolder")
    public FolderInfo rootfolder(){
        FolderInfo response = service.GetRootFolderInfo();
        return response;
    }

    @GetMapping("/subfolder/{folderId}")
    public SubFolderModel subfolder(@PathVariable(value = "folderId") Integer folderId){
        SubFolderModel response = service.GetSubFolders(folderId);
        return response;
    }

    @GetMapping("/folderfiles/{folderId}")
    public FilesInFolder folderFiles(@PathVariable(value = "folderId") Integer folderId){
        FilesInFolder response=  service.GetFilesInFolder(folderId);
        return response;
    }

    @GetMapping("/fileupload")
    public FileInfo fileupload(@RequestBody GetFileData request){
        FileInfo response=  service.FileUpload(request);
        return response;
    }

    @GetMapping("/filedelete")
    public String filedelete(@RequestBody FileDeleteRequest request){
        String response=  service.FileDelete(request.getFileMetaDataIdList());
        return response;
    }
}
