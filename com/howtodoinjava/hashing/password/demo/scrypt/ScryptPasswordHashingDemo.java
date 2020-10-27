package com.howtodoinjava.hashing.password.demo.scrypt;

import com.lambdaworks.crypto.SCryptUtil;

public class ScryptPasswordHashingDemo 
{
	public static void main(String[] args) {
		String originalPassword = "password";
		String generatedSecuredPasswordHash = SCryptUtil.scrypt(originalPassword, 16, 16, 16);
		System.out.println(generatedSecuredPasswordHash);
		
		boolean matched = SCryptUtil.check("password", generatedSecuredPasswordHash);
		System.out.println(matched);
		
		matched = SCryptUtil.check("passwordno", generatedSecuredPasswordHash);
		System.out.println(matched);
	}
}
