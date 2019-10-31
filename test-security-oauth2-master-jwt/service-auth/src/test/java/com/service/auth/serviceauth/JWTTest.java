package com.service.auth.serviceauth;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTTest {

	//生成一个jwt令牌
	@Test
	public void testCreateJwt(){
	//证书文件
	String key_location = "test-jwt.jks";
	//密钥库密码
	String keystore_password = "test123";
	//访问证书路径
	ClassPathResource resource = new ClassPathResource(key_location);
	//密钥工厂
	KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,keystore_password.toCharArray());
	//密钥的密码，此密码和别名要匹配
	String keypassword = "test123";
	//密钥别名
	String alias = "test-jwt";
	//密钥对（密钥和公钥）
	KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypassword.toCharArray());
	//私钥
	RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
	//定义payload信息
	Map<String, Object> tokenMap = new HashMap<String, Object>();
	tokenMap.put("id", "123");
	tokenMap.put("name", "mrt");
	tokenMap.put("roles", "r01,r02");
	tokenMap.put("ext", "1");
	//生成jwt令牌
	Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
	//取出jwt令牌
	String token = jwt.getEncoded();
	System.out.println("token="+token);
	}
	
	//资源服务使用公钥验证jwt的合法性，并对jwt解码
	@Test
	public void testVerify(){
	//jwt令牌
	String token
	//="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJyMDEscjAyIiwibmFtZSI6Im1ydCIsImlkIjoiMTIzIn0.KK7_67N5d1Dthd1PgDHMsbi0UlmjGRcm_XJUUwseJ2eZyJJWoPP2IcEZgAU3tUaaKEHUf9wSRwaDgwhrwfyIcSHbs8oy3zOQEL8j5AOjzBBs7vnRmB7DbSaQD7eJiQVJOXO1QpdmEFgjhc_IBCVTJCVWgZw60IEW1_Lg5tqaLvCiIl26K48pJB5f‐le2zgYMzqR1L2LyTFkq39rG57VOqqSCi3dapsZQd4ctq95SJCXgGdrUDWtD52rp5o6_0uq‐mrbRdRxkrQfsa1j8C5IW2‐T4eUmiN3f9wF9JxUK1__XC1OQkOn‐ZTBCdqwWIygDFbU7sf6KzfHJTm5vfjp6NIA";
	  ="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJyMDEscjAyIiwibmFtZSI6Im1ydCIsImlkIjoiMTIzIn0.DHO1TfIN0pzpWgBXe6EpY4ouzRW1Py8uw6Wv-MxNg8_kwRg-KRa5u1ZFCnDSP_KJu9yMQkxeTycmpO6fp-0C0Jc2ZZkmiULEtocr-QgiMA1uelVxPX26Bb7fH_-8_7CdDz3W_scFfr3iDrbthWCKYUHz-QXqkx-dPXnBantgk4VMW1F4HDhLGvvxgkQ4TFgZ86ac_4OujRco4sJcPC-Zf71x0VczOPLFTgWnJLXgqTIs3j5wWY55u7hcf6cZNoGcxkC7YQ4k1XhT9j9IqtCbnGN8DH0xvHYNkCWao100LOF24k31GEVHj6BPFwmi7hYqmIuHLBKpPx_-CCoTCYT5wA";
	//公钥
	//String publickey = "‐‐‐‐‐BEGIN PUBLIC KEY‐‐‐‐‐MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijyxMdq4S6L1Af1rtB8SjCZHNgsQG8JTfGy55eYvzG0B/E4AudR2prSRBvF7NYPL47scRCNPgLnvbQczBHbBug6uOr78qnWsYxHlW6Aa5dI5NsmOD4DLtSw8eX0hFyK5Fj6ScYOSFBz9cd1nNTvx2+oIv0lJDcpQdQhsfgsEr1ntvWterZt/8r7xNN83gHYuZ6TM5MYvjQNBc5qC7Krs9wM7UoQuL+s0X6RlOib7/mcLn/lFLsLDdYQAZkSDx/6+t+1oHdMarChIPYT1sx9Dwj2j2mvFNDTKKKKAq0cv14Vrhz67Vjmz2yMJePDqUi0JYS2r0iIo7n8vN7s83v5uOQIDAQAB‐‐‐‐‐END PUBLIC KEY‐‐‐‐‐";
	  String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoSx+IoT5SdiXUDHMkhepav5wlFXuA4TLrRUZuekwvISn3/KbktrJC12U3ff54a21fyxeK2l4bR9IFVnqgQ1Vn5uOM+rxcf9qXHhVrTVQw7wrih48bXDh1+DcTnthrx/53tBbF2ixJsdyc6mZFXMSConQr8GpbqcmxMJtGedsrxJreh68zZV5Nb6E7TAoADHAxu/firFCH1cEyFpsym2Rzl0qLRrsrEb0YzDNHx3peIwdKxh/YsldncL2kRge1P2B/drgM5qSvfXTXptP3PeLV9QvZfQv7EaUnv6QS9loMYpfuJwM84jMX5jfyN3fD473PAduaIaQgYF3JZbUUvSVZwIDAQAB-----END PUBLIC KEY-----";
	//校验jwt
	Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
	//获取jwt原始内容
	String claims = jwt.getClaims();
	System.out.println(claims);
	//jwt令牌
	String encoded = jwt.getEncoded();
	System.out.println(encoded);
	}

	
}
