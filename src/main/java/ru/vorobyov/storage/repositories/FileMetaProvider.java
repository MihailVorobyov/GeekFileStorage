package ru.vorobyov.storage.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.vorobyov.storage.entities.FileMetaDTO;
import ru.vorobyov.storage.repositories.interfaces.IFileMetaProvider;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

@Repository
public class FileMetaProvider implements IFileMetaProvider {
	private static final String GET_FILES_META = "select hash, file_name as filename, file_size as filesize from " +
		"winter_market.file_metadata where sub_type =:subtype";
	
	private static final String GET_FILE_PATH_BY_HASH = "select file_name as filename from winter_market.file_metadata" +
		" where hash =:hash";
	
	private static final String SAVE_FILE_META_DATA = "insert into winter_market.file_metadata (hash, file_name, file_size, sub_type)" +
		" values (:hash, :file_name, :file_size, :subtype)";
	
	@Autowired
	private Sql2o sql2o;
	
//	public FileMetaProvider(Sql2o sql2o) {
//		this.sql2o = sql2o;
//	}
	
	@Override
	public String checkFileExists(String fileHash) {
		try(Connection connection = sql2o.open())  {
			return connection.createQuery(GET_FILE_PATH_BY_HASH, false)
				.addParameter("hash", fileHash)
				.executeScalar(String.class);
		}
	}
	
	@Override
	public void saveFileMeta(UUID fileHash, String fileName, long fileSize, int subType) {
		try(Connection connection = sql2o.open())  {
			connection.getJdbcConnection().setAutoCommit(false);
			connection.setRollbackOnException(true);
			connection.createQuery(SAVE_FILE_META_DATA, false)
				.addParameter("hash", fileHash.toString())
				.addParameter("file_name", fileName)
				.addParameter("file_size", fileSize)
				.addParameter("subtype", subType)
				.executeUpdate();
		} catch (SQLException throwable) {
			throwable.printStackTrace();
		}
	}
	
	@Override
	public Collection<FileMetaDTO> getMetaFiles(int subType) {
		try(Connection connection = sql2o.open())  {
			return connection.createQuery(GET_FILES_META, false)
				.addParameter("subtype", subType)
				.executeAndFetch(FileMetaDTO.class);
		}
	}
}
