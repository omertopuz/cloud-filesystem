package com.cloud.filesystem.config;

import com.cloud.filesystem.model.FolderInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import java.util.Map;

@Component
@ConfigurationProperties(prefix = "restserver")
public class JsonObjectMappers {
    private Map<String,Map<String,String>> objectmappers;
    private Map<String,String> serviceaddress;
    private Map<String,String> credentials;
//    private Map<String, Map<String,String>> mainfolders;

    private Map<String, FolderInfo> mainfolders;

    private String domainname;
    private String publicip;
    private String macaddress;
    private String machineName;
    private String clienttype;


    public Map<String, Map<String, String>> getObjectmappers() {
        return objectmappers;
    }

    public void setObjectmappers(Map<String, Map<String, String>> objectmappers) {
        this.objectmappers = objectmappers;
    }

    public Map<String, String> getServiceaddress() {
        return serviceaddress;
    }

    public void setServiceaddress(Map<String, String> serviceaddress) {
        this.serviceaddress = serviceaddress;
    }

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public void setCredentials(Map<String, String> credentials) {
        this.credentials = credentials;
    }

    public Map<String, FolderInfo> getMainfolders() {
        return mainfolders;
    }

    public void setMainfolders(Map<String, FolderInfo> mainfolders) {
        this.mainfolders = mainfolders;
    }

    public String getDomainname() {
        return domainname;
    }

    public void setDomainname(String domainname) {
        this.domainname = domainname;
    }

    public String getPublicip() {
        return publicip;
    }

    public void setPublicip(String publicip) {
        this.publicip = publicip;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }
}
