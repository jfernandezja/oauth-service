package com.signer.oauth.service;

import org.junit.Assert;
import org.junit.Test;

import com.signer.oauth.service.BasicAuthentication.AuthInfo;

public class BasicAuthenticationTest {
	@Test
	public void incorrectHeaderTypeTest() throws Exception {
		Assert.assertNull(BasicAuthentication.parse("Other AAA"));
	}
	
	@Test
	public void incorrectHeaderFormatTest() throws Exception {
		Assert.assertNull(BasicAuthentication.parse("Basic QUFBQUFBQUEK"));
	}
	
	@Test
	public void validHeaderTest() throws Exception {
		AuthInfo info = BasicAuthentication.parse("Basic cGVwZTpwYXNzCg==");
		Assert.assertNotNull(info);
		Assert.assertEquals("pepe", info.getName());
		Assert.assertEquals("pass\n", info.getPassword());
	}
}
