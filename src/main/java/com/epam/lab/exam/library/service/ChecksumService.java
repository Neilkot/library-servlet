package com.epam.lab.exam.library.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class ChecksumService {

	private static final ChecksumService INSTANCE = new ChecksumService();
	
	private final ConfigService configService = ConfigService.getInstance();

	private ChecksumService() {
	}

	public static ChecksumService getInstance() {
		return INSTANCE;
	}
	
	public String makeChecksum(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance(configService.getChecksumAlgorithm());
			byte[] digest = md.digest(value.getBytes());
			return DatatypeConverter.printHexBinary(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Couldn't generate checksum." + e.getMessage(), e);
		}
	}

}
