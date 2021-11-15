package ru.vorobyov.storage.entities;

import java.io.Serializable;
import java.util.UUID;

public class FileMetaDTO {
    private UUID hash;

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
    
    public FileMetaDTO(String hash, String fileName, long fileSize, int subType) {
        this.hash = UUID.fromString(hash);
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.subType = subType;
    }
    
    public UUID getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = UUID.fromString(hash);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
