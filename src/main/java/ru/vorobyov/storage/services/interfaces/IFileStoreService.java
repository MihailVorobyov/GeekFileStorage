package ru.vorobyov.storage.services.interfaces;

import ru.vorobyov.storage.entities.FileMetaDTO;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.UUID;

public interface IFileStoreService {
    String storeFile(byte[] content, String fileName, long fileSize, int subFileType) throws IOException, NoSuchAlgorithmException;

    byte[] getFile(UUID md5) throws IOException;

    Collection<FileMetaDTO> getMetaFiles(int subtype);
    
    void deleteFile(String md5, String fileName, int subtype);
}
