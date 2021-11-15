package ru.vorobyov.storage.repositories.interfaces;

import ru.vorobyov.storage.entities.FileMetaDTO;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IFileMetaProvider {
    String checkFileExists(UUID fileHash);

    void saveFileMeta(UUID Hash, String fileName, long fileSize, int subType);

    Collection<FileMetaDTO> getMetaFiles(int subType);
    
    FileMetaDTO findEquals(UUID hash, String fileName, int subtype);
    
    List<FileMetaDTO> getFilesByHash(String hash);
    
    void deleteFile(String md5, String fileName, int subtype);
    
}
