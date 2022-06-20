package com.shopme.common;

public class Constants {
	public static final String S3_BASE_URI;

	static {
		String bucketName = "";

		S3_BASE_URI = bucketName == null ? "" : "https://lhy-shopme-images.s3.amazonaws.com";
	}
}