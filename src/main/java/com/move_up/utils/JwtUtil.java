package com.move_up.utils;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JwtUtil {

    private static Logger logger = LoggerFactory.logger(JwtUtil.class);
    private static final String USER = "user";
    private static final String SECRET = "daychinhlachukycuatoi_dungdungnochoviecbathopphapnhehihi";

    public static String generateToken(Object obj) {
        String token = null;
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(USER, obj);
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            JWSSigner signer = new MACSigner(SECRET.getBytes());
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            (logger).error(e.getMessage());
        }
        return token;
    }
    
    public static String getKeyTokenTail(String token) {
    	if (token != null) {
    		StringBuilder res = new StringBuilder("");
    		for (int i=token.length()-1; res.length()<=10 ; i--) {
    			res.append(token.charAt(i));
    		}
    		
    		return res.toString();
    	}
		return null;
    }

}