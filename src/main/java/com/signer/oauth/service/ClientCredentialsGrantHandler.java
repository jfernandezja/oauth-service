package com.signer.oauth.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.signer.jwslib.BadParamException;
import com.signer.jwslib.JWTGenerator;
import com.signer.oauth.service.BasicAuthentication.AuthInfo;
import com.signer.oauth.service.impl.JsonConfiguration.ClientInfo;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

public class ClientCredentialsGrantHandler {
	private static final String CCG_TYPE = "client_credentials";
	private static final String GRANT_TYPE_PARAM = "grant_type=" + CCG_TYPE;
	private static final String AUTH_HEADER = "Authorization";
	
	private Configuration configuration;
	private JWTGenerator generator;
	
	public ClientCredentialsGrantHandler(Configuration configuration) throws Exception {
		this.configuration = configuration;
		this.generator = new JWTGenerator(configuration.getJwsKey(), configuration.getId());
	}
	
	public boolean applies(String request) {
		return request != null && request.equals(GRANT_TYPE_PARAM);
		
	}
	
	public void authorize(HttpServerRequest request, HttpServerResponse response) {
		response.putHeader("content-type", "application/json");
		String authHeader = request.getHeader(AUTH_HEADER);
		AuthInfo authInfo = BasicAuthentication.parse(authHeader);
		if(authInfo == null) {
			JsonObject json = new JsonObject().put("error", "Incorrect Basic auth header");
			response.setStatusCode(400).end(json.toString());
			return;
		}
		
		ClientInfo clientInfo = configuration.getClients().get(authInfo.getName());
		if(clientInfo == null) {
			JsonObject json = new JsonObject().put("error", "Authentication error");
			response.setStatusCode(401).end(json.toString());
			return;
		}
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			JsonObject json = new JsonObject().put("error", "Server error: Hashing algorithm");
			response.setStatusCode(500).end(json.toString());
			return;
		}
		String toDigest = clientInfo.getSeed() + authInfo.getPassword();
		byte[] encodedhash = digest.digest(toDigest.getBytes());
		if(!Base64.getEncoder().encodeToString(encodedhash).equals(clientInfo.getAuthInfo())) {
			JsonObject json = new JsonObject().put("error", "Authentication error");
			response.setStatusCode(401).end(json.toString());
			return;
		}
		
		try {
			JsonObject json = new JsonObject().put("access_token", generator.build(authInfo.getName())).put("token_type", "SignedJWS");
			response.end(json.toString());
		} catch(BadParamException ex)
		{
			JsonObject json = new JsonObject().put("error", "Error generating JWT");
			response.setStatusCode(500).end(json.toString());
			return;
		}
	} 
}
