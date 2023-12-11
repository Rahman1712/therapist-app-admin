package com.ar.therapist.admin.aws;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class CustomAwsCredentialsProvider implements AwsCredentialsProvider {

    private final AwsCredentials credentials;

    public CustomAwsCredentialsProvider(String accessKeyId, String secretAccessKey) {
        this.credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    }

    @Override
    public AwsCredentials resolveCredentials() {
        return credentials;
    }
}
