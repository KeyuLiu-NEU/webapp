package com.cloud;

import com.cloud.config.FileStorageProperties;
import com.cloud.config.dataSourceConfig;
import com.cloud.service.FileStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import software.amazon.awssdk.regions.Region;

import java.io.IOException;


@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class CloudNativeWebAppApplication {

	public static void main(String[] args) throws IOException {
		dataSourceConfig.username = "root";
		dataSourceConfig.password = "liukeyu521";
		dataSourceConfig.hostname = "csye6225-f20.cp8alz277zmq.us-east-1.rds.amazonaws.com";
		FileStorageService.S3_BUCKET_NAME="webapps31";
//		FileStorageService.region= Region.US_EAST_2;
//		if(System.getenv("Region").equals("us_east_2")) FileStorageService.region= Region.US_EAST_2;
//		else if(System.getenv("Region").equals("us_west_1")) FileStorageService.region= Region.US_WEST_1;
//		else if(System.getenv("Region").equals("us_west_2")) FileStorageService.region= Region.US_WEST_2;
		//Runtime.getRuntime().exec(  new String [] {"sudo mysql", "create database db_cloud;", "create user 'springuser'@'%' identified by 'ThePassword';", "grant all on db_cloud.* to 'springuser'@'%';"} );
		SpringApplication.run(CloudNativeWebAppApplication.class, args);
	}


}
