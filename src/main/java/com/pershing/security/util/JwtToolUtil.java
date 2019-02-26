package com.pershing.security.util;

import java.util.HashMap;
import java.util.Map;

import org.jose4j.jwk.EcJwkGenerator;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.pershing.security.model.PershingUser;

@Service
public class JwtToolUtil {
	private static PublicJsonWebKey senderJwk;
	private static final String USER_KEY = "User";

	private volatile String senderJwkString;

	@Autowired
	public JwtToolUtil() throws JoseException {

		if (senderJwkString != null) {
			senderJwk = PublicJsonWebKey.Factory.newPublicJwk(senderJwkString);
		} else {
			senderJwk = EcJwkGenerator.generateJwk(EllipticCurves.P256);

			senderJwk.setKeyId("sender's key");

			senderJwkString = senderJwk.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);

		}
	}

	public String generateToken(PershingUser loginData) {
		String jwtString = "";
		try {
			// Create the Claims, which will be the content of the JWT
			JwtClaims claims = new JwtClaims();

			claims.setIssuer("PershingSender");
			claims.setAudience("PershingReceiver");
			// timeout 為11分鐘
			claims.setExpirationTimeMinutesInTheFuture(11);

			// a unique identifier for the token
			claims.setGeneratedJwtId();

			// when the token was issued/created (now)
			claims.setIssuedAtToNow();

			// time before which the token is not yet valid (2 minutes ago)
			claims.setNotBeforeMinutesInThePast(2);
			claims.setSubject("subject");

			Gson gson = new Gson();
			String userJson = gson.toJson(loginData);
			claims.setClaim(USER_KEY, userJson);

			JsonWebSignature jws = new JsonWebSignature();

			jws.setPayload(claims.toJson());

			jws.setKey(senderJwk.getPrivateKey());

			jws.setKeyIdHeaderValue(senderJwk.getKeyId());

			jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);

			jwtString = jws.getCompactSerialization();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jwtString;
	}

	public Map<String, Object> validateToken(String jwtToken) {

		return jwtSecurityTokenHander(jwtToken);
	}

	private Map<String, Object> jwtSecurityTokenHander(String jwtToken) {
		PershingUser user = null;

		boolean isCorrect = true;

		try {

			JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime().setRequireSubject()
					.setExpectedIssuer("PershingSender").setExpectedAudience("PershingReceiver")
					.setVerificationKey(senderJwk.getPublicKey()).build();

			JwtClaims jwtClaims = jwtConsumer.processToClaims(jwtToken);

			if (jwtClaims.hasClaim(USER_KEY)) {
				String userJson = jwtClaims.getClaimValue(USER_KEY).toString();
				Gson gson = new Gson();
				user = gson.fromJson(userJson, PershingUser.class);

			} else {
				throw new Exception("No UserData !!");
			}

		} catch (Exception e) {

			// e.printStackTrace();
			isCorrect = false;
			System.out.println(">>> JWT驗證失敗");
		}

		Map<String, Object> map = new HashMap<>();
		map.put("User", user);
		map.put("isCorrect", isCorrect);
		return map;
	}

}
