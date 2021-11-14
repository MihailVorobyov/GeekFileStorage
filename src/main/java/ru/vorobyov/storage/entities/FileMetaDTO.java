package ru.vorobyov.storage.entities;

import java.io.Serializable;
import java.util.UUID;

public class FileMetaDTO {
    private String hash;

    private String fileName;
    
    private long fileSize;
    
    private int subType;
    
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public void setSubType(int subType) {
        this.subType = subType;
    }
    
    public long getFileSize() {
        return fileSize;
    }
    
    public int getSubType() {
        return subType;
    }
    
    public FileMetaDTO(String fileName) {
        this.fileName = fileName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
