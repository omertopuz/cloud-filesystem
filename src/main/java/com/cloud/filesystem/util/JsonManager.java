package com.cloud.filesystem.util;

import com.cloud.filesystem.config.JsonObjectMappers;
import com.cloud.filesystem.model.FileInfo;
import com.cloud.filesystem.model.FolderInfo;
import com.cloud.filesystem.model.UserTicket;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class JsonManager {
    @Autowired
    private JsonObjectMappers mappers;

//    public <T> T deserializeObjectByAppYml(String jsonString,Class<T> clazz,String appYmlProperty){
//        final LinkedHashMap<String,Object> lhm;
//        JSONParser jsonParser = new JSONParser(jsonString);
//        List<Method> setters = Arrays.stream(clazz.getDeclaredMethods()).filter(x->x.getName().startsWith("set")).collect(Collectors.toList());
//
//        T deserializedObj = null;
//        try {
//            deserializedObj = (T) Class.forName("com.cloud.filesystem.model.UserTicket").getDeclaredConstructors()[0].newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Map<String,String> objectFields = mappers.getObjectmappers().get(appYmlProperty);
//        try {
//            lhm = jsonParser.parseObject();
//
//            for (int i = 0; i <setters.size() ; i++) {
//                final String name = setters.get(i).getName().replace("set","");
//                String setterName = objectFields.get(name);
//                if (setterName!= null){
//                    Object parameter =lhm.get(setterName);
//                    try {
//                        setters.get(i).invoke(deserializedObj,parameter);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return deserializedObj;
//    }

    public UserTicket getUserTicketObject(String jsonString){
        UserTicket ticket = new UserTicket();
        JSONParser jsonParser = new JSONParser(jsonString);

        LinkedHashMap<String,Object> lhm = null;
        try {
            lhm = jsonParser.parseObject();
            ticket.setUserId(Integer.parseInt(lhm.get(mappers.getObjectmappers().get("userticket").get("userId")).toString()));
            ticket.setUserTitle(lhm.get(mappers.getObjectmappers().get("userticket").get("userTitle")).toString());
            ticket.setTicket(UUID.fromString(lhm.get(mappers.getObjectmappers().get("userticket").get("ticket")).toString()));
        } catch (ParseException e) {

        }
        ticket.setRetrievalDateTime(LocalDateTime.now());

        return ticket;
    }

    public FolderInfo getFolderInfoObject(String jsonString){
        JSONParser jsonParser = new JSONParser(jsonString);
        LinkedHashMap<String,Object> lhm = null;
        try {
            lhm = jsonParser.parseObject();
        } catch (ParseException e) {

        }
        return getFolderInfoObject(lhm);
    }

    public FolderInfo getFolderInfoObject(LinkedHashMap<String,Object> lhm){
        FolderInfo folder = new FolderInfo();
        Object folderId = lhm.get(mappers.getObjectmappers().get("folderinfo").get("folderId"));
        Object folderName = lhm.get(mappers.getObjectmappers().get("folderinfo").get("folderName"));
        Object parentFolderId = lhm.get(mappers.getObjectmappers().get("folderinfo").get("parentFolderId"));
        folder.setFolderId(folderId!= null ? Integer.parseInt(folderId.toString()):0);
        folder.setFolderName(folderName!=null?folderName.toString():null);
        folder.setParentFolderId(parentFolderId!= null ? Integer.parseInt(parentFolderId.toString()):0);
        return folder;
    }

    public List<FolderInfo> getSubFolderList(String jsonString){
        List<FolderInfo> folderList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser(jsonString);

        try {
            LinkedHashMap<String,Object> jsonArray = jsonParser.parseObject();
            jsonArray.forEach((k,v)->{
                if (v instanceof List){
                    List<Object> arr = (List<Object>) v;
                    arr.forEach(item->{
                    LinkedHashMap<String,Object> folderJsonEntity = (LinkedHashMap<String, Object>) item;
                    folderList.add(getFolderInfoObject(folderJsonEntity));
                    });
                }
            });
        } catch (ParseException e) {

        }

        return folderList;
    }

    public List<FileInfo> getFilesInFolderList(String jsonString){
        List<FileInfo> fileList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser(jsonString);

        try {
            LinkedHashMap<String,Object> jsonArray = jsonParser.parseObject();
            jsonArray.forEach((k,v)->{
                if (v instanceof List){
                    List<Object> arr = (List<Object>) v;
                    arr.forEach(item->{
                        LinkedHashMap<String,Object> fileJsonEntity = (LinkedHashMap<String, Object>) item;
                        fileList.add(getFileInfoObject(fileJsonEntity));
                    });
                }
            });
        } catch (ParseException e) {

        }

        return fileList;
    }

    public FileInfo getFileInfoObject(LinkedHashMap<String,Object> lhm){
        FileInfo fileObject = new FileInfo();
        fileObject.setFileMetaDataId(UUID.fromString(lhm.get(mappers.getObjectmappers().get("fileinfo").get("fileMetaDataId")).toString()));
        fileObject.setFileName(lhm.get(mappers.getObjectmappers().get("fileinfo").get("fileName"))!=null?
                lhm.get(mappers.getObjectmappers().get("fileinfo").get("fileName")).toString():null);
        fileObject.setFolderId(lhm.get(mappers.getObjectmappers().get("fileinfo").get("folderId"))!=null ?
                Integer.parseInt(lhm.get(mappers.getObjectmappers().get("fileinfo").get("folderId")).toString()):0);
        fileObject.setCategory(lhm.get(mappers.getObjectmappers().get("fileinfo").get("category"))!=null?
                lhm.get(mappers.getObjectmappers().get("fileinfo").get("category")).toString():null);
        fileObject.setKeyField(lhm.get(mappers.getObjectmappers().get("fileinfo").get("keyField"))!=null?
                lhm.get(mappers.getObjectmappers().get("fileinfo").get("keyField")).toString():null);
        return fileObject;
    }

    public UUID getFileMetaDataId(String jsonString){
        JSONParser jsonParser = new JSONParser(jsonString);
        LinkedHashMap<String,Object> lhm = null;
        String metaDataString = "";
        try {
            lhm = jsonParser.parseObject();
            metaDataString = lhm.get(mappers.getObjectmappers().get("fileMetadaId").get("fieldName")).toString();
        } catch (ParseException e) {

        }
        return UUID.fromString(metaDataString);
    }

    public String buildDivvyDriveFolderParamsJson() {
        StringBuilder sbJson = new StringBuilder();

        Map<String,String> divvyDriveFolderParams = mappers.getObjectmappers().get("divvyDriveFolderParams");

        sbJson.append("{ \"").append(divvyDriveFolderParams.get("username")).append("\":\"").append(mappers.getCredentials().get("username")).append("\",");
        sbJson.append("\"").append(divvyDriveFolderParams.get("usertitle")).append("\":\"").append(mappers.getCredentials().get("usertitle")).append("\",");
        sbJson.append("\"").append(divvyDriveFolderParams.get("userId")).append("\":").append(mappers.getCredentials().get("userId")).append(",");
        sbJson.append("\"").append(divvyDriveFolderParams.get("publicip")).append("\":\"").append(mappers.getPublicip()).append("\",");
        sbJson.append("\"").append(divvyDriveFolderParams.get("macaddress")).append("\":\"").append(mappers.getMacaddress()).append("\",");
        sbJson.append("\"").append(divvyDriveFolderParams.get("machineName")).append("\":\"").append(mappers.getMachineName()).append("\",");
        sbJson.append("\"").append(divvyDriveFolderParams.get("clienttype")).append("\":\"").append(mappers.getClienttype()).append("\"}");

        return sbJson.toString();
    }

    public String buildUserTicketJson(UserTicket userTicket) {
        StringBuilder sbJson = new StringBuilder();

        Map<String,String> userticketParams = mappers.getObjectmappers().get("userticket");

        sbJson.append("{ \"").append(userticketParams.get("userId")).append("\":").append(userTicket.getUserId()).append(",");
        sbJson.append("\"").append(userticketParams.get("userTitle")).append("\":\"").append(userTicket.getUserTitle()).append("\",");
        sbJson.append("\"").append(userticketParams.get("ticket")).append("\":\"").append(userTicket.getTicket()).append("\"}");

        return sbJson.toString();
    }

    public String buildFileMetadataListJson(UserTicket userTicket) {
        StringBuilder sbJson = new StringBuilder();

        Map<String,String> userticketParams = mappers.getObjectmappers().get("userticket");

        sbJson.append("{ \"").append(userticketParams.get("userId")).append("\":").append(userTicket.getUserId()).append(",");
        sbJson.append("\"").append(userticketParams.get("userTitle")).append("\":\"").append(userTicket.getUserTitle()).append("\",");
        sbJson.append("\"").append(userticketParams.get("ticket")).append("\":\"").append(userTicket.getTicket()).append("\"}");

        return sbJson.toString();
    }
}
