﻿package org.ripple.bouncycastle.jcajce.provider.asymmetric.ec;

import java.io.bytearrayoutputstream;
import java.security.algorithmparameters;
import java.security.invalidalgorithmparameterexception;
import java.security.invalidkeyexception;
import java.security.key;
import java.security.nosuchalgorithmexception;
import java.security.privatekey;
import java.security.publickey;
import java.security.securerandom;
import java.security.spec.algorithmparameterspec;

import javax.crypto.badpaddingexception;
import javax.crypto.cipher;
import javax.crypto.cipherspi;
import javax.crypto.illegalblocksizeexception;
import javax.crypto.nosuchpaddingexception;
import javax.crypto.shortbufferexception;

import org.ripple.bouncycastle.crypto.invalidciphertextexception;
import org.ripple.bouncycastle.crypto.keyencoder;
import org.ripple.bouncycastle.crypto.agreement.ecdhbasicagreement;
import org.ripple.bouncycastle.crypto.digests.sha1digest;
import org.ripple.bouncycastle.crypto.engines.aesengine;
import org.ripple.bouncycastle.crypto.engines.desedeengine;
import org.ripple.bouncycastle.crypto.engines.iesengine;
import org.ripple.bouncycastle.crypto.generators.eckeypairgenerator;
import org.ripple.bouncycastle.crypto.generators.ephemeralkeypairgenerator;
import org.ripple.bouncycastle.crypto.generators.kdf2bytesgenerator;
import org.ripple.bouncycastle.crypto.macs.hmac;
import org.ripple.bouncycastle.crypto.paddings.paddedbufferedblockcipher;
import org.ripple.bouncycastle.crypto.params.asymmetrickeyparameter;
import org.ripple.bouncycastle.crypto.params.ecdomainparameters;
import org.ripple.bouncycastle.crypto.params.eckeygenerationparameters;
import org.ripple.bouncycastle.crypto.params.eckeyparameters;
import org.ripple.bouncycastle.crypto.params.ecpublickeyparameters;
import org.ripple.bouncycastle.crypto.params.iesparameters;
import org.ripple.bouncycastle.crypto.params.ieswithcipherparameters;
import org.ripple.bouncycastle.crypto.parsers.eciespublickeyparser;
import org.ripple.bouncycastle.jcajce.provider.asymmetric.util.ecutil;
import org.ripple.bouncycastle.jcajce.provider.asymmetric.util.iesutil;
import org.ripple.bouncycastle.jce.interfaces.eckey;
import org.ripple.bouncycastle.jce.interfaces.ecprivatekey;
import org.ripple.bouncycastle.jce.interfaces.ecpublickey;
import org.ripple.bouncycastle.jce.interfaces.ieskey;
import org.ripple.bouncycastle.jce.provider.bouncycastleprovider;
import org.ripple.bouncycastle.jce.spec.iesparameterspec;
import org.ripple.bouncycastle.util.strings;


public class iescipher
    extends cipherspi
{
    private iesengine engine;
    private int state = -1;
    private bytearrayoutputstream buffer = new bytearrayoutputstream();
    private algorithmparameters engineparam = null;
    private iesparameterspec enginespec = null;
    private asymmetrickeyparameter key;
    private securerandom random;
    private boolean dhaesmode = false;
    private asymmetrickeyparameter otherkeyparameter = null;

    public iescipher(iesengine engine)
    {
        this.engine = engine;
    }


    public int enginegetblocksize()
    {
        if (engine.getcipher() != null)
        {
            return engine.getcipher().getblocksize();
        }
        else
        {
            return 0;
        }
    }


    public int enginegetkeysize(key key)
    {
        if (key instanceof eckey)
        {
            return ((eckey)key).getparameters().getcurve().getfieldsize();
        }
        else
        {
            throw new illegalargumentexception("not an ec key");
        }
    }


    public byte[] enginegetiv()
    {
        return null;
    }


    public algorithmparameters enginegetparameters()
    {
        if (engineparam == null && enginespec != null)
        {
            try
            {
                engineparam = algorithmparameters.getinstance("ies", bouncycastleprovider.provider_name);
                engineparam.init(enginespec);
            }
            catch (exception e)
            {
                throw new runtimeexception(e.tostring());
            }
        }

        return engineparam;
    }


    public void enginesetmode(string mode)
        throws nosuchalgorithmexception
    {
        string modename = strings.touppercase(mode);

        if (modename.equals("none"))
        {
            dhaesmode = false;
        }
        else if (modename.equals("dhaes"))
        {
            dhaesmode = true;
        }
        else
        {
            throw new illegalargumentexception("can't support mode " + mode);
        }
    }


    public int enginegetoutputsize(int inputlen)
    {
        int len1, len2, len3;

        len1 = engine.getmac().getmacsize();

        if (key != null)
        {
            len2 = 1 + 2 * (((eckey)key).getparameters().getcurve().getfieldsize() + 7) / 8;
        }
        else
        {
            throw new illegalstateexception("cipher not initialised");
        }

        if (engine.getcipher() == null)
        {
            len3 = inputlen;
        }
        else if (state == cipher.encrypt_mode || state == cipher.wrap_mode)
        {
            len3 = engine.getcipher().getoutputsize(inputlen);
        }
        else if (state == cipher.decrypt_mode || state == cipher.unwrap_mode)
        {
            len3 = engine.getcipher().getoutputsize(inputlen - len1 - len2);
        }
        else
        {
            throw new illegalstateexception("cipher not initialised");
        }

        if (state == cipher.encrypt_mode || state == cipher.wrap_mode)
        {
            return buffer.size() + len1 + len2 + len3;
        }
        else if (state == cipher.decrypt_mode || state == cipher.unwrap_mode)
        {
            return buffer.size() - len1 - len2 + len3;
        }
        else
        {
            throw new illegalstateexception("cipher not initialised");
        }

    }

    public void enginesetpadding(string padding)
        throws nosuchpaddingexception
    {
        string paddingname = strings.touppercase(padding);

        // tdod: make this meaningful...
        if (paddingname.equals("nopadding"))
        {

        }
        else if (paddingname.equals("pkcs5padding") || paddingname.equals("pkcs7padding"))
        {

        }
        else
        {
            throw new nosuchpaddingexception("padding not available with iescipher");
        }
    }


    // initialisation methods

    public void engineinit(
        int opmode,
        key key,
        algorithmparameters params,
        securerandom random)
        throws invalidkeyexception, invalidalgorithmparameterexception
    {
        algorithmparameterspec paramspec = null;

        if (params != null)
        {
            try
            {
                paramspec = params.getparameterspec(iesparameterspec.class);
            }
            catch (exception e)
            {
                throw new invalidalgorithmparameterexception("cannot recognise parameters: " + e.tostring());
            }
        }

        engineparam = params;
        engineinit(opmode, key, paramspec, random);

    }


    public void engineinit(
        int opmode,
        key key,
        algorithmparameterspec enginespec,
        securerandom random)
        throws invalidalgorithmparameterexception, invalidkeyexception
    {
        otherkeyparameter = null;

        // use default parameters (including cipher key size) if none are specified
        if (enginespec == null)
        {
            this.enginespec = iesutil.guessparameterspec(engine);
        }
        else if (enginespec instanceof iesparameterspec)
        {
            this.enginespec = (iesparameterspec)enginespec;
        }
        else
        {
            throw new invalidalgorithmparameterexception("must be passed ies parameters");
        }

        // parse the recipient's key
        if (opmode == cipher.encrypt_mode || opmode == cipher.wrap_mode)
        {
            if (key instanceof ecpublickey)
            {
                this.key = ecutil.generatepublickeyparameter((publickey)key);
            }
            else if (key instanceof ieskey)
            {
                ieskey iekey = (ieskey)key;

                this.key = ecutil.generatepublickeyparameter(iekey.getpublic());
                this.otherkeyparameter = ecutil.generateprivatekeyparameter(iekey.getprivate());
            }
            else
            {
                throw new invalidkeyexception("must be passed recipient's public ec key for encryption");
            }
        }
        else if (opmode == cipher.decrypt_mode || opmode == cipher.unwrap_mode)
        {
            if (key instanceof ecprivatekey)
            {
                this.key = ecutil.generateprivatekeyparameter((privatekey)key);
            }
            else if (key instanceof ieskey)
            {
                ieskey iekey = (ieskey)key;

                this.otherkeyparameter = ecutil.generatepublickeyparameter(iekey.getpublic());
                this.key = ecutil.generateprivatekeyparameter(iekey.getprivate());
            }
            else
            {
                throw new invalidkeyexception("must be passed recipient's private ec key for decryption");
            }
        }
        else
        {
            throw new invalidkeyexception("must be passed ec key");
        }


        this.random = random;
        this.state = opmode;
        buffer.reset();

    }


    public void engineinit(
        int opmode,
        key key,
        securerandom random)
        throws invalidkeyexception
    {
        try
        {
            engineinit(opmode, key, (algorithmparameterspec)null, random);
        }
        catch (invalidalgorithmparameterexception e)
        {
            throw new illegalargumentexception("can't handle supplied parameter spec");
        }

    }


    // update methods - buffer the input

    public byte[] engineupdate(
        byte[] input,
        int inputoffset,
        int inputlen)
    {
        buffer.write(input, inputoffset, inputlen);
        return null;
    }


    public int engineupdate(
        byte[] input,
        int inputoffset,
        int inputlen,
        byte[] output,
        int outputoffset)
    {
        buffer.write(input, inputoffset, inputlen);
        return 0;
    }


    // finalisation methods

    public byte[] enginedofinal(
        byte[] input,
        int inputoffset,
        int inputlen)
        throws illegalblocksizeexception, badpaddingexception
    {
        if (inputlen != 0)
        {
            buffer.write(input, inputoffset, inputlen);
        }

        final byte[] in = buffer.tobytearray();
        buffer.reset();

        // convert parameters for use in iesengine
        iesparameters params = new ieswithcipherparameters(enginespec.getderivationv(),
            enginespec.getencodingv(),
            enginespec.getmackeysize(),
            enginespec.getcipherkeysize());

        final ecdomainparameters ecparams = ((eckeyparameters)key).getparameters();

        final byte[] v;

        if (otherkeyparameter != null)
        {
            try
            {
                if (state == cipher.encrypt_mode || state == cipher.wrap_mode)
                {
                    engine.init(true, otherkeyparameter, key, params);
                }
                else
                {
                    engine.init(false, key, otherkeyparameter, params);
                }
                return engine.processblock(in, 0, in.length);
            }
            catch (exception e)
            {
                throw new badpaddingexception(e.getmessage());
            }
        }

        if (state == cipher.encrypt_mode || state == cipher.wrap_mode)
        {
            // generate the ephemeral key pair
            eckeypairgenerator gen = new eckeypairgenerator();
            gen.init(new eckeygenerationparameters(ecparams, random));

            ephemeralkeypairgenerator kgen = new ephemeralkeypairgenerator(gen, new keyencoder()
            {
                public byte[] getencoded(asymmetrickeyparameter keyparameter)
                {
                    return ((ecpublickeyparameters)keyparameter).getq().getencoded();
                }
            });

            // encrypt the buffer
            try
            {
                engine.init(key, params, kgen);

                return engine.processblock(in, 0, in.length);
            }
            catch (exception e)
            {
                throw new badpaddingexception(e.getmessage());
            }

        }
        else if (state == cipher.decrypt_mode || state == cipher.unwrap_mode)
        {
            // decrypt the buffer
            try
            {
                engine.init(key, params, new eciespublickeyparser(ecparams));

                return engine.processblock(in, 0, in.length);
            }
            catch (invalidciphertextexception e)
            {
                throw new badpaddingexception(e.getmessage());
            }
        }
        else
        {
            throw new illegalstateexception("cipher not initialised");
        }

    }

    public int enginedofinal(
        byte[] input,
        int inputoffset,
        int inputlength,
        byte[] output,
        int outputoffset)
        throws shortbufferexception, illegalblocksizeexception, badpaddingexception
    {

        byte[] buf = enginedofinal(input, inputoffset, inputlength);
        system.arraycopy(buf, 0, output, outputoffset, buf.length);
        return buf.length;
    }


    /**
     * classes that inherit from us
     */

    static public class ecies
        extends iescipher
    {
        public ecies()
        {
            super(new iesengine(new ecdhbasicagreement(),
                new kdf2bytesgenerator(new sha1digest()),
                new hmac(new sha1digest())));
        }
    }

    static public class ecieswithdesede
        extends iescipher
    {
        public ecieswithdesede()
        {
            super(new iesengine(new ecdhbasicagreement(),
                new kdf2bytesgenerator(new sha1digest()),
                new hmac(new sha1digest()),
                new paddedbufferedblockcipher(new desedeengine())));
        }
    }

    static public class ecieswithaes
        extends iescipher
    {
        public ecieswithaes()
        {
            super(new iesengine(new ecdhbasicagreement(),
                new kdf2bytesgenerator(new sha1digest()),
                new hmac(new sha1digest()),
                new paddedbufferedblockcipher(new aesengine())));
        }
    }
}


