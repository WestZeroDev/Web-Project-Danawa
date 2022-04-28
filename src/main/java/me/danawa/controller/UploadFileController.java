package me.danawa.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import me.danawa.domain.UploadFile;
import me.danawa.service.UploadFileService;

@Controller
public class UploadFileController {
	private UploadFileService uploadFileService;
	private ResourceLoader resourceLoader;
	
	@Autowired
	public UploadFileController(UploadFileService uploadFileService, ResourceLoader resourceLoader) {
		this.uploadFileService = uploadFileService;
		this.resourceLoader = resourceLoader;
	}
	
	//이미지 업로드
	@PostMapping("/image")
	public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file, HttpSession session) {
		try {
			UploadFile uploadFile = uploadFileService.store(file);
			return ResponseEntity.ok().body("/image/" + uploadFile.getId());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	//이미지 가져오기
	@GetMapping("/image/{fileId}")
	public ResponseEntity<?> serveFile(@PathVariable Long fileId){
		try {
			UploadFile uploadFile = uploadFileService.load(fileId);
			Resource resource = resourceLoader.getResource("file:" + uploadFile.getPath());
			return ResponseEntity.ok().body(resource);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	//이미지 삭제
	@ResponseBody @PostMapping("/image/delete")
	public String deleteFile(@RequestParam(value="src") String src) {
		String[] result = src.split("/image/");
		System.out.println(result[1]);
		Long id = Long.parseLong(result[1]);
		uploadFileService.deleteById(id);
		return "delete success";
	}
	
}
