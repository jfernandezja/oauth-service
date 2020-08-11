package com.signer.oauth.service;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

import com.signer.oauth.service.impl.JsonConfiguration.ClientInfo;

public interface Configuration {
	public Map<String, ClientInfo> getClients();
	public int getPort();
	public RSAPrivateKey getJwsKey();
	public String getId();
}
