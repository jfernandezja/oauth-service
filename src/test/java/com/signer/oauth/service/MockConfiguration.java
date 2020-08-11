package com.signer.oauth.service;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

import com.signer.oauth.service.impl.JsonConfiguration.ClientInfo;

public class MockConfiguration implements Configuration {
	private Map<String, ClientInfo> clients;
	private RSAPrivateKey pvk;
	
	
	public MockConfiguration(Map<String, ClientInfo> clients, RSAPrivateKey pvk) {
		this.clients = clients;
		this.pvk = pvk;
	}

	@Override
	public Map<String, ClientInfo> getClients() {
		return clients;
	}

	@Override
	public int getPort() {
		return 9999;
	}

	@Override
	public RSAPrivateKey getJwsKey() {
		return pvk;
	}

	@Override
	public String getId() {
		return null;
	}

}
