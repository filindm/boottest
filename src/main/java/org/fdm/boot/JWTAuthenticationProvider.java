package org.fdm.boot;


import java.util.Date;
import java.lang.UnsupportedOperationException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;


@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {
    
    private JWSVerifier verifier;
    
    public JWTAuthenticationProvider() {
        this.verifier = new MACVerifier("DB4AEF4719809709E560ED8DE2F9C77B886B963B28BA20E9A8A621BBD4ABA400");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JWTToken jwtToken = (JWTToken) authentication;
        JWT jwt = jwtToken.getJwt();
        
        // Check type of the parsed JOSE object
        if (jwt instanceof PlainJWT) {
            handlePlainToken((PlainJWT) jwt);
        } else if (jwt instanceof SignedJWT) {
            handleSignedToken((SignedJWT) jwt);
        } else if (jwt instanceof EncryptedJWT) {
            handleEncryptedToken((EncryptedJWT) jwt);
        }
        
        Date referenceTime = new Date();
        ReadOnlyJWTClaimsSet claims = jwtToken.getClaims();
        
        Date expirationTime = claims.getExpirationTime();
        if (expirationTime == null || expirationTime.before(referenceTime)) {
            throw new TokenExpiredException("The token is expired");
        }
        
        Date notBeforeTime = claims.getNotBeforeTime();
        if (notBeforeTime == null || notBeforeTime.after(referenceTime)) {
            throw new InvalidTokenException("Not before is after sysdate");
        }
        
        String issuerReference = "my.site.com";
        String issuer = claims.getIssuer();
        if (!issuerReference.equals(issuer)) {
            throw new InvalidTokenException("Invalid issuer");
        }
        
        jwtToken.setAuthenticated(true);
        return jwtToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTToken.class.isAssignableFrom(authentication);
    }
    
    private void handlePlainToken(PlainJWT jwt) {
        throw new InvalidTokenException("Unsecured plain tokens are not supported");
    }
    
    private void handleSignedToken(SignedJWT jwt) {
        try {
            if (!jwt.verify(verifier)) {
                throw new InvalidSignatureException("Signature validation failed");
            }
        } catch (JOSEException e) {
            throw new InvalidSignatureException("Signature validation failed");
        }
    }
    
    private void handleEncryptedToken(EncryptedJWT jwt) {
        throw new UnsupportedOperationException("Unsupported token type");
    }
    
}
