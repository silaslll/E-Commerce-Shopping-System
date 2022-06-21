package com.shopme.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AmazonS3UtilTests {

	@Test
	public void testListFolder() {
		String folderName = "category-images/10";
		List<String> listKeys = AmazonS3Util.listFolder(folderName);
		System.out.println("----1111----");
		listKeys.forEach(System.out::println);
		System.out.println("----2222----");
	}

	@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test-upload";
		String fileName = "WechatIMG15121.jpeg";
		String filePath = "/Users/lll/Desktop/" + fileName;

		InputStream inputStream = new FileInputStream(filePath);

		AmazonS3Util.uploadFile(folderName, fileName, inputStream);
	}

	@Test
	public void testDeleteFile() {
		String fileName = "test-upload/WechatIMG15121.jpeg";
		AmazonS3Util.deleteFile(fileName);
	}

	@Test
	public void testRemoveFolder() {
		String folderName = "test-upload";
		AmazonS3Util.removeFolder(folderName);
	}
}