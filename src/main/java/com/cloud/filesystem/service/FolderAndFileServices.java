package com.cloud.filesystem.service;

import com.cloud.filesystem.config.JsonObjectMappers;
import com.cloud.filesystem.exception.ConnectionNotEstablishedException;
import com.cloud.filesystem.model.*;
import com.cloud.filesystem.util.FileUtils;
import com.cloud.filesystem.util.JsonManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FolderAndFileServices {

    private static UserTicket userTicket;
    private static int TICKET_VALID_HOURS=12;
    enum Languages{TR,EN ,DE}
    enum OrderField{Adi,Boyutu,DosyaTuru,OlusturmaTarihi}
    enum OrderType {AZ, ZA}

    @Autowired
    private JsonObjectMappers appPropertyMappings;

    @Autowired
    private JsonManager jsonManager;

    private final RestTemplate restTemplate;

    public FolderAndFileServices(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void GetTicket() throws ConnectionNotEstablishedException {
        if (userTicket == null || (userTicket!=null && userTicket.getRetrievalDateTime().plusHours(TICKET_VALID_HOURS).isBefore(LocalDateTime.now()))){
            String postData = (String.format("%s~%s~%s~%s", appPropertyMappings.getCredentials().get("username"), appPropertyMappings.getCredentials().get("password"), jsonManager.buildDivvyDriveFolderParamsJson(), Languages.TR));
            HttpEntity<byte[]> entity = getPostEntity(postData,StandardCharsets.UTF_8);
            ResponseEntity<String> response = restTemplate.postForEntity(appPropertyMappings.getServiceaddress().get("usercheckmethod"), entity, String.class);
            userTicket = jsonManager.getUserTicketObject(response.getBody());
//            userTicket = JsonManager.getUserTicketObject(response.getBody());//new UserTicket(response.getBody());


            if (userTicket.getUserId() <= -1)
            {
                throw new ConnectionNotEstablishedException();
            }

            userTicket.setRetrievalDateTime(LocalDateTime.now());
        }
    }

    public GetFileData GetFileBlobData(UUID fileMetaDataId, String fileName){
        GetFileData result = new GetFileData(null,fileName,fileMetaDataId);
        String ticketString = jsonManager.buildUserTicketJson(userTicket);
        String postData = (String.format("%s~%s~%s~%s~%s~%s~%s~%s~%s~%s",
                ticketString,
                fileMetaDataId,
                appPropertyMappings.getCredentials().get("username"), appPropertyMappings.getCredentials().get("password"),
                userTicket.getUserId(),
                appPropertyMappings.getPublicip(),
                appPropertyMappings.getMacaddress(),
                appPropertyMappings.getMachineName(),
                fileName,
                Languages.TR));
        HttpEntity<byte[]> entity = getPostEntity(postData,StandardCharsets.US_ASCII);
        ResponseEntity<byte[]> response = restTemplate.postForEntity(appPropertyMappings.getServiceaddress().get("downloadfileadress"), entity, byte[].class);
        result.setBlobData(response.getBody());
//        FileUtils.writeBytesToFileClassic(response.getBody(),"downloadedxmlfile.xml");

        return result;
    }

    public FolderInfo GetRootFolderInfo(){
        String ticketString = jsonManager.buildUserTicketJson(userTicket);
        String postData = (String.format("%s~%s",
                ticketString,
                userTicket.getUserId()));
        HttpEntity<byte[]> entity = getPostEntity(postData,StandardCharsets.UTF_8);

        ResponseEntity<String> response = restTemplate.postForEntity(appPropertyMappings.getServiceaddress().get("getmainfolder"), entity, String.class);
        FolderInfo result = jsonManager.getFolderInfoObject(response.getBody());
        return result;
    }

    public SubFolderModel GetSubFolders(int parentFolderId){
        String ticketString = jsonManager.buildUserTicketJson(userTicket);
        String postData = (String.format("%s~%s~%s~%s~%s",
                ticketString,
                userTicket.getUserId(),parentFolderId,0,Integer.MAX_VALUE));
        HttpEntity<byte[]> entity = getPostEntity(postData,StandardCharsets.UTF_8);

        ResponseEntity<String> response = restTemplate.postForEntity(appPropertyMappings.getServiceaddress().get("subfolderlist"), entity, String.class);
        SubFolderModel result = new SubFolderModel();
        result.setFolderList(jsonManager.getSubFolderList(response.getBody()));
        return result;
    }

    public FilesInFolder GetFilesInFolder(int folderId){
        String ticketString = jsonManager.buildUserTicketJson(userTicket);
        String postData = (String.format("%s~%s~%s~%s~%s~%s~%s~%s~%s~%s",
                ticketString,
                userTicket.getUserId(),
                folderId,
                true,
                0,
                Integer.MAX_VALUE,
                false,
                OrderField.Adi,
                OrderType.AZ,
                false));
        HttpEntity<byte[]> entity = getPostEntity(postData,StandardCharsets.UTF_8);
//        FileUtils.writeBytesToFileClassic(entity.getBody(),"fileInfoPost.txt");
        ResponseEntity<String> response = restTemplate.postForEntity(appPropertyMappings.getServiceaddress().get("filelistinfolder"), entity, String.class);
        FilesInFolder result = new FilesInFolder();
        result.setFileInfoList(jsonManager.getFilesInFolderList(response.getBody()));
        result.setFolderId(folderId);
        return result;
    }

    public FileInfo FileUpload(GetFileData fileRequest){
        String ticketString = jsonManager.buildUserTicketJson(userTicket);
        String fileData = FileUtils.byteArrayToHexEncode(fileRequest.getBlobData());

        String postData = (String.format("%s~%s~%s~%s~%s~%s~%s~%s~%s~%s~%s~%s",
                fileRequest.getKeyField(),
                appPropertyMappings.getMachineName(),
                appPropertyMappings.getCredentials().get("username"),
                appPropertyMappings.getCredentials().get("password"),
                userTicket.getUserId(),
                fileRequest.getFileName(),
                appPropertyMappings.getPublicip(),
                fileRequest.getCategory(),
                (fileRequest.getFolderId()+"").toString(),
                appPropertyMappings.getMacaddress(),
                ticketString,
                fileData
        ));
        HttpEntity<byte[]> entity = getPostEntity(postData,StandardCharsets.UTF_8);
        FileUtils.writeBytesToFileClassic(entity.getBody(),"uploadFileInfoPost.txt");
        ResponseEntity<String> response = restTemplate.postForEntity(appPropertyMappings.getServiceaddress().get("uploadfile"), entity, String.class);
        FileInfo result = fileRequest;
        result.setFileMetaDataId(jsonManager.getFileMetaDataId(response.getBody()));
        return result;
    }

    public String FileDelete(List<UUID> fileMetaDataList){
        String ticketString = jsonManager.buildUserTicketJson(userTicket);
        String paramString = jsonManager.buildDivvyDriveFolderParamsJson();
        String fileMetaDataListJson = new StringBuilder("[\"").
                append(String.join("\",\"",fileMetaDataList.stream()
                        .map(x -> x.toString()).collect(Collectors.toList())))
                .append("\"]").toString();

        String postData = (String.format("%s~%s~%s~%s",
                ticketString,
                paramString,
                fileMetaDataListJson,
                Languages.TR
        ));
        HttpEntity<byte[]> entity = getPostEntity(postData,StandardCharsets.UTF_8);
//        FileUtils.writeBytesToFileClassic(entity.getBody(),"fileInfoPost.txt");
        ResponseEntity<String> response = restTemplate.postForEntity(appPropertyMappings.getServiceaddress().get("deletefile"), entity, String.class);
        return response.getBody();
    }

    private HttpEntity<byte[]> getPostEntity(String postData, Charset charset){
        byte[] postDataByte = null;
        postDataByte = postData.getBytes(charset);
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(postDataByte.length);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<byte[]> entity = new HttpEntity<>(postDataByte,headers);
        return entity;
    }
}
