package com.scarlatti.lucenedemo;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.util.IOUtils;
import org.junit.Test;

import java.util.List;

/**
 * @author Alessandro Scarlatti
 * @since Saturday, 8/10/2019
 */
public class AwsTest {

    private AmazonS3 buildS3() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("access123", "secret123")))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://docker:9000", Regions.US_EAST_1.getName()))
            .withPathStyleAccessEnabled(true)
            .build();

        return s3;
    }

    @Test
    public void testAws() {
        AmazonS3 s3 = buildS3();
        List<Bucket> buckets = s3.listBuckets();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
        }
    }

    @Test
    public void updateTest1Txt() {
        AmazonS3 s3 = buildS3();
        s3.putObject("bucket1", "thing1.txt", "stuff and things233333334444");
    }

    @Test
    public void downloadTest1Txt() throws Exception {
        AmazonS3 s3 = buildS3();
        String content = IOUtils.toString(s3.getObject("bucket1", "thing1.txt").getObjectContent());
        System.out.println(content);
    }

    @Test
    public void downloadDeep1() throws Exception {
        AmazonS3 s3 = buildS3();
        String content = IOUtils.toString(s3.getObject("bucket1", "dir1/penguin.xml").getObjectContent());
        System.out.println(content);
    }

    @Test
    public void addObjectDeep1() {
        AmazonS3 s3 = buildS3();
        s3.putObject("bucket1", "dir1/penguin.xml", "<Penguin2 />");
    }

    @Test
    public void createBucket1() {
        buildS3().createBucket("bucket1");
    }
}