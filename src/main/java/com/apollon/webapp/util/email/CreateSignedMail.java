package com.apollon.webapp.util.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class CreateSignedMail {
	
	 //
    // certificate serial number seed.
    //
    static int  serialNo = 1;

    static AuthorityKeyIdentifier createAuthorityKeyId(
        PublicKey pub) 
        throws IOException
    {
        ByteArrayInputStream bIn = new ByteArrayInputStream(pub.getEncoded());
        SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(
            (ASN1Sequence)new ASN1InputStream(bIn).readObject());

        return new AuthorityKeyIdentifier(info);
    }

    static SubjectKeyIdentifier createSubjectKeyId(
        PublicKey pub) 
        throws IOException
    {
        ByteArrayInputStream bIn = new ByteArrayInputStream(pub.getEncoded());

        SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(
            (ASN1Sequence)new ASN1InputStream(bIn).readObject());

        return new SubjectKeyIdentifier(info);
    }

    /**
     * create a basic X509 certificate from the given keys
     */
    public static X509Certificate makeCertificate(
        KeyPair subKP,
        String  subDN,
        KeyPair issKP,
        String  issDN) 
        throws GeneralSecurityException, IOException
    {
        X509Name   xName   = new X509Name(subDN);
        PublicKey  subPub  = subKP.getPublic();
        PrivateKey issPriv = issKP.getPrivate();
        PublicKey  issPub  = issKP.getPublic();
        
        X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
        
        v3CertGen.setSerialNumber(BigInteger.valueOf(serialNo++));
        v3CertGen.setIssuerDN(new X509Name(issDN));
        v3CertGen.setNotBefore(new Date(System.currentTimeMillis()));
        v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 100)));
        v3CertGen.setSubjectDN(new X509Name(subDN));
        v3CertGen.setPublicKey(subPub);
        v3CertGen.setSignatureAlgorithm("MD5WithRSAEncryption");

        v3CertGen.addExtension(
            X509Extensions.SubjectKeyIdentifier,
            false,
            createSubjectKeyId(subPub));

        v3CertGen.addExtension(
            X509Extensions.AuthorityKeyIdentifier,
            false,
            createAuthorityKeyId(issPub));

        return  v3CertGen.generateX509Certificate(issPriv); //originale deprecato //v3CertGen.generate(issPriv);
    }


}
