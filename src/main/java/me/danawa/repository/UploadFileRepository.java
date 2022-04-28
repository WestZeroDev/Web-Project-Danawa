package me.danawa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.domain.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

}
