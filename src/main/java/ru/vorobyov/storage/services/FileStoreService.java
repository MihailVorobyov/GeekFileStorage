package ru.vorobyov.storage.services;


import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vorobyov.storage.entities.FileMetaDTO;
import ru.vorobyov.storage.repositories.interfaces.IFileMetaProvider;
import ru.vorobyov.storage.repositories.interfaces.IFileSystemProvider;
import ru.vorobyov.storage.services.interfaces.IFileStoreService;
import ru.vorobyov.storage.utils.FileNameUtil;
import ru.vorobyov.storage.utils.HashHelper;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class FileStoreService implements IFileStoreService {

    @Autowired
    IFileSystemProvider systemProvider;

    @Autowired
    IFileMetaProvider fileMetaProvider;

    @Override
    public String storeFile(byte[] content, String fileName, long fileSize, int subFileType) throws IOException, NoSuchAlgorithmException {
        final UUID md5 = HashHelper.getMd5Hash(content);

        String name = fileMetaProvider.checkFileExists(md5);

        if (name == null) {
            fileMetaProvider.saveFileMeta(md5, fileName, fileSize, subFileType);
            name = systemProvider.storeFile(content, md5, fileName);
        } else {
            if (!name.equals(fileName)) {
                fileMetaProvider.saveFileMeta(md5, fileName, fileSize, subFileType);
                name = FileNameUtil.getFullFileName(fileName, md5);
            } else {
                FileMetaDTO fileMetaDTO = fileMetaProvider.findEquals(md5, fileName, subFileType);
                if (fileMetaDTO == null) {
                    fileMetaProvider.saveFileMeta(md5, fileName, fileSize, subFileType);
                } else
                name = FileNameUtil.getFullFileName(fileName, md5);
            }
        }
        
        return name;
    }

    @Override
    public byte[] getFile(UUID md5) throws IOException {
       String filename = fileMetaProvider.checkFileExists(md5);
       String ext = FilenameUtils.getExtension(filename);
       String fullFileName = md5.toString() + "." + ext;
       return systemProvider.getFile(fullFileName);
    }

    @Override
    public Collection<FileMetaDTO> getMetaFiles(int subtype) {
        return fileMetaProvider.getMetaFiles(subtype);
    }
    
    @Override
    public void deleteFile(String md5, String fileName, int subtype) {
        try {
            fileMetaProvider.deleteFile(md5, fileName, subtype);
            List<FileMetaDTO> fileMetaDTOList = new ArrayList<>(fileMetaProvider.getFilesByHash(md5));
            if (fileMetaDTOList.isEmpty()) {
                String name = FileNameUtil.getFullFileName(fileName, UUID.fromString(md5));
                systemProvider.deleteFile(name);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
