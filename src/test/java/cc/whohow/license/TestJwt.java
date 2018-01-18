package cc.whohow.license;

import cc.whohow.license.util.Pem;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class TestJwt {
    @Test
    public void testKeyGenerator() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        System.out.println(new Pem("RSA PUBLIC KEY", keyPair.getPublic().getEncoded()));
        System.out.println(new Pem("RSA PRIVATE KEY", keyPair.getPrivate().getEncoded()));
    }

    @Test
    public void testJwt() throws Exception {
        String publicKey = "-----BEGIN RSA PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAivefXsQcMfAaOE8RJoodTw47ZbDB9661\n" +
                "3CkhvLZRlvbp2tz7E3Qi4t2gebEon4jx1hNjD5H5qPwIgof4yHirUvltd0+H3Hm/Kqn3X8kIBaK3\n" +
                "S6Fvs+dKse7DH77UhW9GKQQZdbzdwRogbwsf3RDm/wloZBkmVqyHn+eTJDhTjU7Rxxqv/QK8GKG9\n" +
                "urWd35A23s1s6q6KMJVwn8anOSwxk3m+JWDmzyWUcw7yhXOG8XexL4YhH75wvZbWwJN3ZsPTY+tD\n" +
                "UuFadlrWbYUxrTUuKJdwngwVXkUdX0fNftu1K9ZGKMEqlKwtiFG5s1fKVJAgUQDhgKbRVCGxQ+To\n" +
                "kP3Q9QIDAQAB\n" +
                "-----END RSA PUBLIC KEY-----";
        String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCK959exBwx8Bo4TxEmih1PDjtl\n" +
                "sMH3rrXcKSG8tlGW9una3PsTdCLi3aB5sSifiPHWE2MPkfmo/AiCh/jIeKtS+W13T4fceb8qqfdf\n" +
                "yQgFordLoW+z50qx7sMfvtSFb0YpBBl1vN3BGiBvCx/dEOb/CWhkGSZWrIef55MkOFONTtHHGq/9\n" +
                "ArwYob26tZ3fkDbezWzqroowlXCfxqc5LDGTeb4lYObPJZRzDvKFc4bxd7EvhiEfvnC9ltbAk3dm\n" +
                "w9Nj60NS4Vp2WtZthTGtNS4ol3CeDBVeRR1fR81+27Ur1kYowSqUrC2IUbmzV8pUkCBRAOGAptFU\n" +
                "IbFD5OiQ/dD1AgMBAAECggEAUd2utVUyhvi7eZCauemDep8XvsByhEcYO6q/hTyitByhh53HC57P\n" +
                "LncBoq8LD3DNhx/Eq9eDDFntaXhQx0jPJQJrym2/y9KtG6UlZfg4EZnaAWQJOiJ3jnrp2DnDWRhE\n" +
                "bMVNel7ZBHkvwjr7enAKpRQVu2JVg5oAC+OQezXzgrqd1hDfdjsvkAKe1WLI2Wq9A86mdLTdI5SA\n" +
                "/RVjro0DdbQfYoECr22sTIAKnOvWvd258Ozm0sUK3XrUTvUcAGfG4XJ2na6ax/wFQPDwgakA1MGt\n" +
                "5vUDn5VupHgEdFPVj03WnaKFvJM+VRk0gZg0pNTJzNqK4y8FWMuA70JdTnfAAQKBgQDD84D1iN/Q\n" +
                "eF3T0fezd4xooj08fhrZ4+j9rNk4ZeCXAwbokUO1DUVh2UDZZQ6GsTc2oatu1o33G6VvuB+vAUdy\n" +
                "aUaBgw+NYPufsC500TPSskHzs6PISCl2sEIZ1gQ120faulkUdwn19jqU6f2kPW1yXdGSDeakv+9J\n" +
                "ZQSXFYiZ9QKBgQC1jbHGSfKBCRHYkCTxU9VmmCY9xPuKXmFOOwcaI0i51+eAcueVX/NSkrie0pYY\n" +
                "5/QllVdoxC1qBTUTysAYq+foZbwLOfumvZa336vrhcuof/i+9CYHkMvhthYWPkU8qwe/6RgtP/Kr\n" +
                "J9ejEgwGEfsCmbhvm/RrwgXjgKUJHzr7AQKBgD6oMmhQ6CBlHLuKvEXqyD79Ac/CExvxQKM9Dgh2\n" +
                "qyYelarwKFJQeAsdyRq6sbZoHFufvo2mlaaadIAQxDDVm979wsnO+r4ecNdTIse/zW5mCai/nHOC\n" +
                "f5+wJLLWOGpwP721MJ7WoZzAq2LKn4xRCBo13blyMHol1YIGhz04aYGxAoGASpr+icBLcfD7L2YU\n" +
                "eEL995ngZRO0VijwjYg+I+SJ9yycyjDtr3ltcOowCbv5yCRRNLyI3dl33BBlvktb4weJVfuwaWCb\n" +
                "Y+qWq9qzOCaUCRsFadrEYemDjq4Xp25RX1vc5GTx9W5I10KZHz7ESB5NasUmgF6y8zEY4GM17miy\n" +
                "AQECgYB0DjgZ12b+zj00mcnJSrp6in707zED8nYgIStNQIRbZBT8a9Sji8cNkpiUnUY5E3OBgl/k\n" +
                "OFYwxvpmfX6CyIHVS0aVK73RvZY2nCgH+Smo/uI3wo0DMnSPJxG2+DbdoINPwvRNqOrK1+VdoScI\n" +
                "rWyIS6quj8O3w1Ip/clNJ9j0FQ==\n" +
                "-----END RSA PRIVATE KEY-----";

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(new Pem(publicKey).getData());
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(new Pem(privateKey).getData());

        String token = Jwts.builder()
                .claim("test", "test")
                .signWith(SignatureAlgorithm.RS512, keyFactory.generatePrivate(pkcs8EncodedKeySpec))
                .compact();
        System.out.println(token);
        System.out.println(Jwts.parser().setSigningKey(keyFactory.generatePublic(x509EncodedKeySpec)).parseClaimsJws(token));
    }
}
