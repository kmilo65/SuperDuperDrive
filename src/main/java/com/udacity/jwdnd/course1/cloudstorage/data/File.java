package com.udacity.jwdnd.course1.cloudstorage.data;

public class File {

    private Long fileId;
    private String fileName;
    private String contenttype;
    private Long filesize;
    private Long userId;


    private byte[] filedata;


    public File() {}

    public File(Long fileId, String fileName, String contenttype, Long filesize, Long userId, byte[] filedata) {
        this.fileId=fileId;
        this.fileName = fileName;
        this.contenttype = contenttype;
        this.filesize = filesize;
        this.userId = userId;
        this.filedata = filedata;
    }

    public Long getFileId() { return fileId; }

    public String getFileName() { return fileName; }

    public String getContenttype() { return contenttype; }

    public Long getFilesize() { return filesize; }

    public Long getUserid() { return userId; }

    public byte[] getFiledata() { return filedata; }

    public void setFileId(Long fileId) { this.fileId = fileId; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public void setContenttype(String contenttype) { this.contenttype = contenttype; }

    public void setFilesize(Long filesize) { this.filesize = filesize; }

    public void setUserid(Long userId) { this.userId = userId; }

    public void setFiledata(byte[] filedata) { this.filedata = filedata; }
}
