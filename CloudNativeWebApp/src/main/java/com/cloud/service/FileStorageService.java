package com.cloud.service;


import com.cloud.config.FileStorageProperties;
import com.cloud.domain.FileInfo;
import com.cloud.domain.FileInfoRepository;
import com.cloud.exceptions.FileStorageException;
import com.cloud.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.sync.RequestBody;
import com.timgroup.statsd.StatsDClient;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    @Autowired
    private FileInfoRepository fileInfoRepository;
//    @Autowired
//    private StatsDClient statsDClient;
    public static Region region;
    public static String S3_BUCKET_NAME;
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    S3Client client = S3Client.builder()
            .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
            .region(region)
            .build();

    //both store file into server and metadata to database
    public FileInfo storeFile(MultipartFile file,String userID,String questionID) {
        // Normalize file name


        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileInfo fileInfo=new FileInfo();

        try {

            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            String hashString = new BigInteger(1, digest).toString(16);


            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(userID).resolve(questionID).resolve(fileName);
            File directory = new File(this.fileStorageLocation.resolve(userID).resolve(questionID).toString());
            if(!directory.exists()){

                directory.mkdirs();

            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            BasicFileAttributes view= Files.getFileAttributeView(targetLocation, BasicFileAttributeView.class).readAttributes();
            String fileContentType = file.getContentType();
            fileInfo.setFileContentType(fileContentType);
            fileInfo.setMd5Hash(hashString);
            fileInfo.setFile_name(fileName);
            fileInfo.setUpload_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            fileInfo.setCreationTime(view.creationTime().toString());
            fileInfo.setLastAccessTime(view.lastAccessTime().toString());
            fileInfo.setLastModifiedTime(view.lastModifiedTime().toString());
            fileInfo.setSize(view.size());

//            Long startTime=System.nanoTime();
//            fileInfoRepository.save(fileInfo);
//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime);
//            statsDClient.recordExecutionTime("fileInfoSaveQuery", duration / 1000000);
//
//            String key=fileInfo.getId();
//
//            startTime=System.nanoTime();
//            client.putObject(PutObjectRequest.builder().bucket(S3_BUCKET_NAME).key(key)
//                    .build(), RequestBody.of(file.getBytes()));
//            endTime = System.nanoTime();
//            duration = (endTime - startTime);
//            statsDClient.recordExecutionTime("s3putObject", duration / 1000000);
//
//            fileInfo.setUrl("https://"+S3_BUCKET_NAME+".s3.amazonaws.com/"+key);

            fileInfoRepository.save(fileInfo);
            String key=fileInfo.getId();
            client.putObject(PutObjectRequest.builder().bucket(S3_BUCKET_NAME).key(key)
                    .build(), RequestBody.of(file.getBytes()));
            fileInfo.setUrl("https://"+S3_BUCKET_NAME+".s3.amazonaws.com/"+key);
            //GetObjectResponse getObjectResponse=client.getObject(GetObjectRequest.builder().bucket(S3_BUCKET_NAME).key(key).build(),nul);
            //Map<String,String> metadata=getObjectResponse.metadata();
            //HeadObjectResponse headObjectResponse=client.headObject(HeadObjectRequest.builder().bucket(S3_BUCKET_NAME).key(key).sseCustomerAlgorithm("AES256").build());
            deleteFile(fileInfo,userID,questionID);
            return fileInfo;
        } catch (IOException | NoSuchAlgorithmException ex) {
           throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

    }
//    public void deleteFromS3(FileInfo fileInfo){
//
//        Long startTime=System.nanoTime();
//        client.deleteObject(DeleteObjectRequest.builder().bucket(S3_BUCKET_NAME).key(fileInfo.getId()).build());
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime);
//        statsDClient.recordExecutionTime("s3deleteObject", duration / 1000000);
//    }

    public void deleteFromS3(FileInfo fileInfo){

        client.deleteObject(DeleteObjectRequest.builder().bucket(S3_BUCKET_NAME).key(fileInfo.getId()).build());
    }

    public void deleteFile(FileInfo fileInfo,String userID, String questionID){
        try {

            File file=new File(this.fileStorageLocation.resolve(userID).resolve(questionID).resolve(fileInfo.getFile_name()).toString());
            file.delete();
            file=new File(this.fileStorageLocation.resolve(userID).resolve(questionID).toString());
            file.delete();
            file=new File(this.fileStorageLocation.resolve(userID).toString());
            file.delete();
            file=new File(this.fileStorageLocation.toString());
            file.delete();

        } catch (Exception ex) {
            throw new MyFileNotFoundException("File not found " + fileInfo.getFile_name(), ex);
        }
    }
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
