package org.dzcoinlab.core.types.known.tx.signed;


import org.dzcoinlab.core.amount;
import org.dzcoinlab.core.variablelength;
import org.dzcoinlab.core.hash.halfsha512;
import org.dzcoinlab.core.hash.hash256;
import org.dzcoinlab.core.hash.prefixes.hashprefix;
import org.dzcoinlab.core.serialized.byteslist;
import org.dzcoinlab.core.serialized.multisink;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;
import org.dzcoinlab.core.uint.uint32;
import org.dzcoinlab.crypto.ecdsa.ikeypair;

public class signedtransaction {
    public transaction txn;
    public hash256 hash;
    public hash256 signinghash;
    public hash256 previoussigninghash;
    public string  tx_blob;

    public signedtransaction(transaction txn){
        this.txn = txn;
    }

    public void prepare(ikeypair keypair, amount fee, uint32 sequence, uint32 lastledgersequence) {
        variablelength pubkey = new variablelength(keypair.pubbytes());

        // this won't always be specified
        if (lastledgersequence != null) {
            txn.put(uint32.lastledgersequence, lastledgersequence);
        }
        txn.put(uint32.sequence, sequence);
        txn.put(amount.fee, fee);
        txn.put(variablelength.signingpubkey, pubkey);

        if (transaction.canonical_flag_deployed) {
            txn.setcanonicalsignatureflag();
        }

        signinghash = txn.signinghash();
        if (previoussigninghash != null && signinghash.equals(previoussigninghash)) {
            return;
        }
        try {
            variablelength signature = new variablelength(keypair.sign(signinghash.bytes()));
            txn.put(variablelength.txnsignature, signature);

            byteslist blob = new byteslist();
            halfsha512 id = halfsha512.prefixed256(hashprefix.transactionid);

            txn.tobytessink(new multisink(blob, id));
            tx_blob = blob.byteshex();
            hash = id.finish();
        } catch (exception e) {
            // electric paranoia
            previoussigninghash = null;
            throw new runtimeexception(e);
        } /*else {*/
            previoussigninghash = signinghash;
        // }
    }

    public transactiontype transactiontype() {
        return txn.transactiontype();
    }
}


