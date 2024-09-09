package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.data.File;
import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFile(Long fileId);

    @Select("SELECT fileName FROM FILES WHERE  fileId = #{fileId}")
    String getFileName(Long fileId);

    @Select("SELECT * FROM FILES WHERE fileId=#{fileId}")
    File getFileUpload(long fileId);

    @Select("SELECT * FROM FILES WHERE userId=#{userId}")
    List<File> getFilesByUserId(Long userId);

    @Insert("INSERT INTO FILES (filename,contenttype,filesize,filedata,userid) VALUES(#{fileName}, #{contenttype}, #{filesize}, #{filedata}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);


    @Update("UPDATE FILES SET filename=#{fileName}, contenttype=#{contenttype}, filesize=#{filesize}, filedata=#{filedata}, userid=#{userId} WHERE fileId=#{fileId}")
    void updateFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId=#{fileId}")
    void deleteFile(Long fileId);

    @Select("SELECT * FROM FILES WHERE fileName={fileName}")
    File getFileByName(String fileName);
}


