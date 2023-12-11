package com.ar.therapist.admin.aws;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

//@Configuration
public class AwsAppConfig {

	private Gson gson = new Gson();
	
	@Bean
	public DataSource dataSource() {
		AwsSecrets secrets = getSecret();
		return DataSourceBuilder
				.create()
				.driverClassName("com.mysql.cj.jdbc.Driver")
				.url("jdbc:" + secrets.getEngine() + "://"+ secrets.getHost() + ":" + secrets.getPort() + "/db_admins")
				.username(secrets.getUsername())
				.password(secrets.getUsername())
				.build();
	}
	
	private AwsSecrets getSecret() {

//	    String secretName = "rahman-db-credential";
	    String secretName = "mydatabase-credential";
	    Region region = Region.of("ap-south-1");

	    SecretsManagerClient client = SecretsManagerClient.builder()
	            .region(region)
//	            .credentialsProvider(DefaultCredentialsProvider.create())
	            .credentialsProvider(new CustomAwsCredentialsProvider(
	                    "AKIAR3ZUU7DXBGCLH5QD", "UfMfNf7c+WtgmkwN0C9IAV/Gfq4k6Stbe2li0xKi"))
	            .build();

	    GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
	            .secretId(secretName)
	            .build();

	    GetSecretValueResponse getSecretValueResponse;

	    try {
	        getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
	    } catch (Exception e) {
	        throw e;
	    }

	    String secret = getSecretValueResponse.secretString();
	    System.err.println(secret);

	    return gson.fromJson(secret, AwsSecrets.class);
	}
}
