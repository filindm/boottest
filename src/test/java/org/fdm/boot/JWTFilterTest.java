package org.fdm.boot;


import java.io.IOException;
import java.util.Date;
import java.time.ZonedDateTime;
import javax.servlet.ServletException;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JOSEException;

import static org.junit.Assert.*;


public class JWTFilterTest {
    
    @Autowired
    private JWTFilter filter;
    
    public MockHttpServletResponse doFilter(MockHttpServletRequest request) throws IOException, ServletException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        filter.doFilter(request, response, chain);
        return response;
    }
    
    @Test
    public void jwtFilterValidTest() throws IOException, ServletException {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "http://somesecuredomain.com");
        
        Date now = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setSubject("alice");
        claimsSet.setIssueTime(now);
        claimsSet.setIssuer("my.site.com");
        claimsSet.setExpirationTime(Date.from(ZonedDateTime.now().plusHours(1).toInstant()));
        claimsSet.setNotBeforeTime(now);
        
        String token = "Bearer " + this.signAndSerializeJWT(claimsSet, "superSecretKey");
        request.addHeader("Authorization", token);
        MockHttpServletResponse response = doFilter(request);
        assertEquals(200, response.getStatus());
    }
    
    private String signAndSerializeJWT(JWTClaimsSet claimsSet, String secret) {
        JWSSigner signer = new MACSigner(secret);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), claimsSet);
        try {
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch(JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }
}
