﻿package org.ripple.bouncycastle.x509;

import java.io.bytearrayinputstream;
import java.io.ioexception;
import java.math.biginteger;
import java.security.generalsecurityexception;
import java.security.invalidkeyexception;
import java.security.nosuchalgorithmexception;
import java.security.nosuchproviderexception;
import java.security.privatekey;
import java.security.publickey;
import java.security.securerandom;
import java.security.signatureexception;
import java.security.cert.certificateencodingexception;
import java.security.cert.certificateparsingexception;
import java.security.cert.x509certificate;
import java.util.date;
import java.util.iterator;

import javax.security.auth.x500.x500principal;

import org.ripple.bouncycastle.asn1.asn1encodablevector;
import org.ripple.bouncycastle.asn1.asn1inputstream;
import org.ripple.bouncycastle.asn1.asn1integer;
import org.ripple.bouncycastle.asn1.asn1sequence;
import org.ripple.bouncycastle.asn1.derbitstring;
import org.ripple.bouncycastle.asn1.derobjectidentifier;
import org.ripple.bouncycastle.asn1.dersequence;
import org.ripple.bouncycastle.asn1.x509.algorithmidentifier;
import org.ripple.bouncycastle.asn1.x509.certificate;
import org.ripple.bouncycastle.asn1.x509.subjectpublickeyinfo;
import org.ripple.bouncycastle.asn1.x509.tbscertificate;
import org.ripple.bouncycastle.asn1.x509.time;
import org.ripple.bouncycastle.asn1.x509.v1tbscertificategenerator;
import org.ripple.bouncycastle.asn1.x509.x509name;
import org.ripple.bouncycastle.jce.x509principal;
import org.ripple.bouncycastle.jce.provider.x509certificateobject;

/**
 * class to produce an x.509 version 1 certificate.
 * @deprecated use org.bouncycastle.cert.x509v1certificatebuilder.
 */
public class x509v1certificategenerator
{
    private v1tbscertificategenerator   tbsgen;
    private derobjectidentifier         sigoid;
    private algorithmidentifier         sigalgid;
    private string                      signaturealgorithm;

    public x509v1certificategenerator()
    {
        tbsgen = new v1tbscertificategenerator();
    }

    /**
     * reset the generator
     */
    public void reset()
    {
        tbsgen = new v1tbscertificategenerator();
    }

    /**
     * set the serial number for the certificate.
     */
    public void setserialnumber(
        biginteger      serialnumber)
    {
        if (serialnumber.compareto(biginteger.zero) <= 0)
        {
            throw new illegalargumentexception("serial number must be a positive integer");
        }
        
        tbsgen.setserialnumber(new asn1integer(serialnumber));
    }

    /**
     * set the issuer distinguished name - the issuer is the entity whose private key is used to sign the
     * certificate.
     */
    public void setissuerdn(
        x500principal   issuer)
    {
        try
        {
            tbsgen.setissuer(new x509principal(issuer.getencoded()));
        }
        catch (ioexception e)
        {
            throw new illegalargumentexception("can't process principal: " + e);
        }
    }
    
    /**
     * set the issuer distinguished name - the issuer is the entity whose private key is used to sign the
     * certificate.
     */
    public void setissuerdn(
        x509name   issuer)
    {
        tbsgen.setissuer(issuer);
    }

    public void setnotbefore(
        date    date)
    {
        tbsgen.setstartdate(new time(date));
    }

    public void setnotafter(
        date    date)
    {
        tbsgen.setenddate(new time(date));
    }

    /**
     * set the subject distinguished name. the subject describes the entity associated with the public key.
     */
    public void setsubjectdn(
        x500principal   subject)
    {
        try
        {
            tbsgen.setsubject(new x509principal(subject.getencoded()));
        }
        catch (ioexception e)
        {
            throw new illegalargumentexception("can't process principal: " + e);
        }
    }
    
    /**
     * set the subject distinguished name. the subject describes the entity associated with the public key.
     */
    public void setsubjectdn(
        x509name   subject)
    {
        tbsgen.setsubject(subject);
    }

    public void setpublickey(
        publickey       key)
    {
        try
        {
            tbsgen.setsubjectpublickeyinfo(new subjectpublickeyinfo((asn1sequence)new asn1inputstream(
                                new bytearrayinputstream(key.getencoded())).readobject()));
        }
        catch (exception e)
        {
            throw new illegalargumentexception("unable to process key - " + e.tostring());
        }
    }

    /**
     * set the signature algorithm. this can be either a name or an oid, names
     * are treated as case insensitive.
     * 
     * @param signaturealgorithm string representation of the algorithm name.
     */
    public void setsignaturealgorithm(
        string  signaturealgorithm)
    {
        this.signaturealgorithm = signaturealgorithm;

        try
        {
            sigoid = x509util.getalgorithmoid(signaturealgorithm);
        }
        catch (exception e)
        {
            throw new illegalargumentexception("unknown signature type requested");
        }

        sigalgid = x509util.getsigalgid(sigoid, signaturealgorithm);

        tbsgen.setsignature(sigalgid);
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject
     * using the default provider "bc".
     * @deprecated use generate(key, "bc")
     */
    public x509certificate generatex509certificate(
        privatekey      key)
        throws securityexception, signatureexception, invalidkeyexception
    {
        try
        {
            return generatex509certificate(key, "bc", null);
        }
        catch (nosuchproviderexception e)
        {
            throw new securityexception("bc provider not installed!");
        }
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject
     * using the default provider "bc" and the passed in source of randomness
     * @deprecated use generate(key, random, "bc")
     */
    public x509certificate generatex509certificate(
        privatekey      key,
        securerandom    random)
        throws securityexception, signatureexception, invalidkeyexception
    {
        try
        {
            return generatex509certificate(key, "bc", random);
        }
        catch (nosuchproviderexception e)
        {
            throw new securityexception("bc provider not installed!");
        }
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject,
     * using the passed in provider for the signing, and the passed in source
     * of randomness (if required).
     * @deprecated use generate()
     */
    public x509certificate generatex509certificate(
        privatekey      key,
        string          provider)
        throws nosuchproviderexception, securityexception, signatureexception, invalidkeyexception
    {
        return generatex509certificate(key, provider, null);
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject,
     * using the passed in provider for the signing, and the passed in source
     * of randomness (if required).
     * @deprecated use generate()
     */
    public x509certificate generatex509certificate(
        privatekey      key,
        string          provider,
        securerandom    random)
        throws nosuchproviderexception, securityexception, signatureexception, invalidkeyexception
    {
        try
        {
            return generate(key, provider, random);
        }
        catch (nosuchproviderexception e)
        {
            throw e;
        }
        catch (signatureexception e)
        {
            throw e;
        }
        catch (invalidkeyexception e)
        {
            throw e;
        }
        catch (generalsecurityexception e)
        {
            throw new securityexception("exception: " + e);
        }
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject
     * using the default provider.
     * <p>
     * <b>note:</b> this differs from the deprecated method in that the default provider is
     * used - not "bc".
     * </p>
     */
    public x509certificate generate(
        privatekey      key)
        throws certificateencodingexception, illegalstateexception, nosuchalgorithmexception, signatureexception, invalidkeyexception
    {
        return generate(key, (securerandom)null);
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject
     * using the default provider and the passed in source of randomness
     * <p>
     * <b>note:</b> this differs from the deprecated method in that the default provider is
     * used - not "bc".
     * </p>
     */
    public x509certificate generate(
        privatekey      key,
        securerandom    random)
        throws certificateencodingexception, illegalstateexception, nosuchalgorithmexception, signatureexception, invalidkeyexception
    {
        tbscertificate tbscert = tbsgen.generatetbscertificate();
        byte[] signature;

        try
        {
            signature = x509util.calculatesignature(sigoid, signaturealgorithm, key, random, tbscert);
        }
        catch (ioexception e)
        {
            throw new extcertificateencodingexception("exception encoding tbs cert", e);
        }

        return generatejcaobject(tbscert, signature);
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject,
     * using the passed in provider for the signing, and the passed in source
     * of randomness (if required).
     */
    public x509certificate generate(
        privatekey      key,
        string          provider)
        throws certificateencodingexception, illegalstateexception, nosuchproviderexception, nosuchalgorithmexception, signatureexception, invalidkeyexception
    {
        return generate(key, provider, null);
    }

    /**
     * generate an x509 certificate, based on the current issuer and subject,
     * using the passed in provider for the signing, and the passed in source
     * of randomness (if required).
     */
    public x509certificate generate(
        privatekey      key,
        string          provider,
        securerandom    random)
        throws certificateencodingexception, illegalstateexception, nosuchproviderexception, nosuchalgorithmexception, signatureexception, invalidkeyexception
    {
        tbscertificate tbscert = tbsgen.generatetbscertificate();
        byte[] signature;

        try
        {
            signature = x509util.calculatesignature(sigoid, signaturealgorithm, provider, key, random, tbscert);
        }
        catch (ioexception e)
        {
            throw new extcertificateencodingexception("exception encoding tbs cert", e);
        }

        return generatejcaobject(tbscert, signature);
    }

    private x509certificate generatejcaobject(tbscertificate tbscert, byte[] signature)
        throws certificateencodingexception
    {
        asn1encodablevector v = new asn1encodablevector();

        v.add(tbscert);
        v.add(sigalgid);
        v.add(new derbitstring(signature));

        try
        {
            return new x509certificateobject(certificate.getinstance(new dersequence(v)));
        }
        catch (certificateparsingexception e)
        {
            throw new extcertificateencodingexception("exception producing certificate object", e);
        }
    }

    /**
     * return an iterator of the signature names supported by the generator.
     * 
     * @return an iterator containing recognised names.
     */
    public iterator getsignaturealgnames()
    {
        return x509util.getalgnames();
    }
}


