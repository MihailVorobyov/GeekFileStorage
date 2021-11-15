package ru.vorobyov.storage.utils;

import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

public class FileNameUtil {
	
	public static String getFullFileName(String fileName, UUID md5) {
		String fileNameExtension = FilenameUtils.getExtension(fileName);
		return String.format("%s.%s", md5, fileNameExtension);
	}
}
