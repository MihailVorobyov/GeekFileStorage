package ru.vorobyov.storage.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.vorobyov.storage.entities.FileMetaDTO;
import ru.vorobyov.storage.repositories.interfaces.IFileMetaProvider;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public class FileMetaProvider implements IFileMetaProvider {
	private static final String GET_FILES_META = "select hash, file_name as fileName, file_size as fileSize, sub_type as subtype from " +
		"winter_market.file_metadata where sub_type =:subtype";
	
	private static final String GET_EQUALS = "select hash, file_name as fileName, file_size as fileSize, sub_type as subtype from " +
		"winter_market.file_metadata where hash =:hash and file_name =:fileName and sub_type =:subtype";
	
	private static final String GET_FILE_PATH_BY_HASH = "select file_name as filename from winter_market.file_metadata" +
		" where hash =:hash";
	
	private static final String SAVE_FILE_META_DATA = "insert into winter_market.file_metadata (hash, file_name, file_size, sub_type)" +
		" values (:hash, :file_name, :file_size, :subtype)";
	
	private static final String DELETE_FILE_METADATA = "DELETE FROM winter_market.file_metadata WHERE hash =:hash AND file_name " +
		"=:fileName AND sub_type =:subtype ";
	
	private static final String GET_FILES_META_BY_HASH = "select hash, file_name as fileName, file_size as fileSize, sub_type as subtype from winter_market.file_metadata" +
		" where hash =:hash";
	
	@Autowired
	private Sql2o sql2o;
	
	@Override
	public FileMetaDTO findEquals(UUID md5, String fileName, int subtype) {
		try(Connection connection = sql2o.open())  {
			return connection.createQuery(GET_EQUALS, false)
				.addParameter("hash", md5.toString())
				.addParameter("fileName", fileName)
				.addParameter("subtype", subtype)
				.executeAndFetchFirst(FileMetaDTO.class);
		}
	}
	
	@Override
	public String checkFileExists(UUID fileHash) {
		try(Connection connection = sql2o.open())  {
			return connection.createQuery(GET_FILE_PATH_BY_HASH, false)
				.addParameter("hash", fileHash.toString())
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
	public List<FileMetaDTO> getMetaFiles(int subType) {
		try(Connection connection = sql2o.open())  {
			return connection.createQuery(GET_FILES_META, false)
				.addParameter("subtype", subType)
				.executeAndFetch(FileMetaDTO.class);
		}
	}
	
	@Override
	public void deleteFile(String fileHash, String fileName, int subtype) {
		try(Connection connection = sql2o.open())  {
			connection.createQuery(DELETE_FILE_METADATA, false)
				.addParameter("hash", fileHash)
				.addParameter("fileName", fileName)
				.addParameter("subtype", subtype)
				.executeUpdate();
		}
	}
	
	@Override
	public List<FileMetaDTO> getFilesByHash(String hash) {
		try(Connection connection = sql2o.open())  {
			return connection.createQuery(GET_FILES_META_BY_HASH, false)
				.addParameter("hash", hash)
				.executeAndFetch(FileMetaDTO.class);
		}
	}
}
