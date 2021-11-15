package ru.vorobyov.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vorobyov.storage.services.interfaces.IFileStoreService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Controller
@CrossOrigin
public class FileController {
	
	@Autowired
	IFileStoreService fileStoreService;
	
	@PostMapping("/storefile")
	public ResponseEntity<String> uploadFile(
		@RequestParam("file") MultipartFile file,
		@RequestParam("subtype") int subType
	) throws IOException, NoSuchAlgorithmException {
		
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is Empty");
		}
		
		String name = fileStoreService.storeFile(file.getBytes(), file.getOriginalFilename(), file.getSize(), subType);
		return ResponseEntity.ok(name);
	}
	
	@GetMapping("/getfile")
	public ResponseEntity<Resource> downloadFile(@RequestParam("hash") UUID hash) throws IOException {
		byte[] array = fileStoreService.getFile(hash);
		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(new ByteArrayResource(array));
	}
	
	@GetMapping("/getfiles")
	public ResponseEntity<?> getFiles(@RequestParam("subtype") int subtype) throws IOException {
		return ResponseEntity.ok(fileStoreService.getMetaFiles(subtype));
	}
	
	@DeleteMapping("/deletefile")
	public ResponseEntity<?> deleteFile(@RequestParam("hash") String md5,
										@RequestParam("fileName") String fileName,
	                                    @RequestParam("subtype") int subtype) {
		fileStoreService.deleteFile(md5, fileName, subtype);
		return ResponseEntity.ok(String.format("File %s deleted", fileName));
	}
}
