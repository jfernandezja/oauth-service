package com.signer.oauth.service.impl;

import java.io.File;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.signer.oauth.service.Configuration;

public class JsonConfiguration implements Configuration {
	private static final String CONFIG_FILE = "config.json";
	private static final String PORT_PARAM = "port";
	private static final String JWS_KEY_PARAM = "jws_private_key";
	private static final String CLIENTS_PARAM = "clients_info";
	private static final String AS_NAME_PARAM = "name";

	private static final String CLIENT_NAME_PARAM = "name";
	private static final String CLIENT_AUTH_PARAM = "auth_info";
	private static final String CLIENT_SEED_PARAM = "seed";

	public static class ClientInfo {
		private String authInfo;
		private String seed;

		private ClientInfo(String authInfo, String seed) {
			super();
			this.authInfo = authInfo;
			this.seed = seed;
		}

		public String getAuthInfo() {
			return authInfo;
		}

		public String getSeed() {
			return seed;
		}
	}

	private Map<String, ClientInfo> clients = new HashMap<String, ClientInfo>();
	private int port;
	private RSAPrivateKey jwsKey;
	private String id;

	public JsonConfiguration(String config) throws Exception {
		initialize(config);
	}

	public JsonConfiguration() throws Exception {
		byte[] config = FileUtils.readFileToByteArray(new File(CONFIG_FILE));
		initialize(new String(config));
	}

	public Map<String, ClientInfo> getClients() {
		return clients;
	}

	public int getPort() {
		return port;
	}
	
	public String getId() {
		return id;
	}

	public RSAPrivateKey getJwsKey() {
		return jwsKey;
	}

	private void initialize(String config) throws Exception {
		JSONObject obj = new JSONObject(config);
		initId(obj);
		initPort(obj);
		initJwsKey(obj);
		initClients(obj);
	}
	
	private void initId(JSONObject obj) throws Exception {
		id = obj.getString(AS_NAME_PARAM);
	}

	private void initPort(JSONObject obj) throws Exception {

		port = obj.getInt(PORT_PARAM);
	}

	private void initJwsKey(JSONObject obj) throws Exception {
		String pvk = obj.getString(JWS_KEY_PARAM);
		byte[] encoded = Base64.getDecoder().decode(pvk.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		jwsKey = (RSAPrivateKey) kf.generatePrivate(keySpec);

	}

	private void initClients(JSONObject obj) throws Exception {
		JSONArray clients = obj.getJSONArray(CLIENTS_PARAM);
		for (int i = 0; i < clients.length(); i++) {
			JSONObject client = clients.getJSONObject(i);
			String clientName = client.getString(CLIENT_NAME_PARAM);
			String clientAuth = client.getString(CLIENT_AUTH_PARAM);
			String clientSeed = client.getString(CLIENT_SEED_PARAM);
			this.clients.put(clientName, new ClientInfo(clientAuth, clientSeed));
		}
	}
}
