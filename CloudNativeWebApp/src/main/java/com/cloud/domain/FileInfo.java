package com.cloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class FileInfo {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    private String id;
    private String file_name;
    private String url;
    private String upload_date;
    private String fileContentType;
    private String lastAccessTime;
    private String lastModifiedTime;
    private Long size;
    private String creationTime;
    private String Md5Hash;

    @JsonIgnore
    public String getFileContentType() {
        return fileContentType;
    }
    @JsonIgnore
    public String getLastAccessTime() {
        return lastAccessTime;
    }
    @JsonIgnore
    public String getLastModifiedTime() {
        return lastModifiedTime;
    }
    @JsonIgnore
    public Long getSize() {
        return size;
    }
    @JsonIgnore
    public String getCreationTime() {
        return creationTime;
    }
    @JsonIgnore
    public String getMd5Hash() {
        return Md5Hash;
    }

}
