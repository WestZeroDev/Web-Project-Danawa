package me.danawa.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.UploadFile;
import me.danawa.dao.UploadFileRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadFileService {
	private final UploadFileRepository uploadFileRepository;
	private final Path rootLocation = Paths.get("E:\\spring-workspace\\danawa\\src\\main\\resources\\static\\upload");
	
	public UploadFile store(MultipartFile file) throws Exception {
		//originalName: 예시.jpg
		//savedName: uuid예시.png
		//path: d:/images/uuid예시.jpg
		//size: 4994942
		//date: 2020-02-06
		
		try {
			if(file.isEmpty()) {
				throw new Exception("Failed to store empty file " + file.getOriginalFilename());
			}
			
			String savedName = saveFile(rootLocation.toString(), file);
			UploadFile uploadFile = new UploadFile();
			uploadFile.setOriginalName(file.getOriginalFilename());
			uploadFile.setSavedName(savedName);
			uploadFile.setPath(rootLocation.toString().replace(File.separatorChar, '/') + '/' + savedName);   
			uploadFile.setSize(file.getResource().contentLength());
			uploadFile.setDate(LocalDate.now());
			uploadFileRepository.save(uploadFile);
			return uploadFile;
			
		} catch(IOException e) {
			throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
		}
	}
	
	public UploadFile load(Long fileId) {
		return uploadFileRepository.findById(fileId).get();
	}
	
	public String saveFile(String rootLocation, MultipartFile file) throws IOException {
		File uploadDir = new File(rootLocation);
		if(!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		//savedName 생성
		UUID uuid = UUID.randomUUID();
		String savedName = uuid.toString() + file.getOriginalFilename();
		File saveFile = new File(rootLocation, savedName);
		FileCopyUtils.copy(file.getBytes(), saveFile);
		return savedName;
	}
	
	public void delete(List<Long> fileList) {
		for(Long id : fileList) {
			Optional<UploadFile> result = uploadFileRepository.findById(id);
			if(result.isPresent()) {
				UploadFile uploadFile = result.get();
				File file = new File(uploadFile.getPath());
				if(file.isFile()) {
					file.delete();
				}
				uploadFileRepository.delete(uploadFile);
			}
		}
	}
	
	public void deleteById(long id) {
		Optional<UploadFile> result = uploadFileRepository.findById(id);
		if(result.isPresent()) {
			UploadFile uploadFile = result.get();
			File file = new File(uploadFile.getPath());
			if(file.isFile()) {
				file.delete();
			}
			uploadFileRepository.delete(uploadFile);
		}
	}
	
}
