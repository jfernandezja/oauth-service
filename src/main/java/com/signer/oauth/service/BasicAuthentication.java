package com.signer.oauth.service;

import java.util.Base64;

public class BasicAuthentication {
	public static class AuthInfo {
		private String name;
		private String password;
		
		private AuthInfo(String name, String password) {
			super();
			this.name = name;
			this.password = password;
		}
		
		public String getName() {
			return name;
		}
		public String getPassword() {
			return password;
		}
		
	}
	
	public static AuthInfo parse(String header) {
		if(header == null || !header.startsWith("Basic"))
			return null;
		
		String[] parts = header.split(" ");
		if(parts.length != 2) 
			return null;
		
		String auth = new String(Base64.getDecoder().decode(parts[1].getBytes()));
		if(auth == null || !auth.contains(":"))
			return null;
		
		String[] authParts = auth.split(":");
		if(authParts.length != 2) 
			return null;
		
		return new AuthInfo(authParts[0], authParts[1]);
	}
}
