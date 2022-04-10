package me.danawa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.beans.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

}
