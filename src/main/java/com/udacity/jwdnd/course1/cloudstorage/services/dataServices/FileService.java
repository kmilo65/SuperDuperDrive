package com.udacity.jwdnd.course1.cloudstorage.services.dataServices;

import com.udacity.jwdnd.course1.cloudstorage.data.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;


    public File getFileUpload(Long fileId){ return fileMapper.getFile(fileId); }

    public void insertFile(File file){   fileMapper.insertFile(file); }

    public void deleteFile(Long fileId){ fileMapper.deleteFile(fileId);}

    public List<File> filesUpload(Long userId){ return fileMapper.getFilesByUserId(userId);  }

    public File getFileByName(String fileName){return fileMapper.getFileByName(fileName);}

    public Boolean fileExist(String fileName,Long userId){
            Boolean exists=false;
            List<File> files=filesUpload(userId);
            for(int i=0;i<files.size();i++){
                if(files.get(i).getFileName().equals(fileName)){
                    exists=true;
                }
            }
        return exists;
    }

}
