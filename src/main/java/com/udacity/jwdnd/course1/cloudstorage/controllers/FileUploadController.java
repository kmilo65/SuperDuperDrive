package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.data.File;
import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.dataServices.FileService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping(value="/filesupload")
public class FileUploadController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;


    static int FILE_SIZE=1048576;


    @GetMapping("/showfiles")
    private String files(Authentication authentication, Model model){

        return "redirect:/home";
    }



    //file uploads is handled in controller as multipartFile object;
    @PostMapping("/fileUpload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, File file,Authentication authentication, Model model)  {

       int fileUploadSuccess=0;
       String fileUploadMessage="File not upload. Try Again";

        Long userId = userService.getUser(authentication.getName()).getUserid();

        String fileName=fileUpload.getOriginalFilename();
        if (fileUpload == null || fileUpload.isEmpty()) {

            fileUploadMessage="No file to upload yet..!";
            model.addAttribute("fileUploadSuccess",fileUploadSuccess);
            model.addAttribute("fileUploadMessage",fileUploadMessage);
            return "result";
        }


        if(!fileService.fileExist(fileName,userId)){

            try{
                file.setUserid(userId);
                file.setFileName(fileName);
                file.setFiledata(fileUpload.getBytes());
                file.setContenttype(fileUpload.getContentType());
                file.setFilesize(fileUpload.getSize());
                System.out.println(fileUpload.getSize());

                this.fileService.insertFile(file);
                List<File> files = this.fileService.filesUpload(file.getUserid());
                fileUploadSuccess=1;
                fileUploadMessage="File Succesfully Upload";

                model.addAttribute("files",files);

            }
            catch (Exception ex){

                return "redirect:/error";
            }

        }

        model.addAttribute("fileUploadSuccess",fileUploadSuccess);
        model.addAttribute("fileUploadMessage",fileUploadMessage);

        return "result";
    }


    @GetMapping("/deleteFile/{fileId}")
    public String deleteFile(@PathVariable("fileId") Long fileId,Model model) {
        this.fileService.deleteFile(fileId);
        model.addAttribute("fileUploadSuccess",2);
        return "result";
    }

    @GetMapping("/download")
    public ResponseEntity download(@RequestParam String fileName,Authentication authentication){

        Long userId = userService.getUser(authentication.getName()).getUserid();
        List<File> files = fileService.filesUpload(userId);
        File newFile=new File();

        for ( int i=0; i<files.size();i++){
            if(files.get(i).getFileName().equals(fileName)){
               newFile=files.get(i);
            }
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + newFile.getFileName() + "\"")
                .body(newFile);
    }



}
