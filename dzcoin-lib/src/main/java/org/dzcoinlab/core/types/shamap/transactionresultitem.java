package org.dzcoinlab.core.types.shamap;


import org.dzcoinlab.core.hash.prefixes.hashprefix;
import org.dzcoinlab.core.hash.prefixes.prefix;
import org.dzcoinlab.core.serialized.binaryserializer;
import org.dzcoinlab.core.serialized.bytessink;
import org.dzcoinlab.core.types.known.tx.result.transactionresult;

public class transactionresultitem extends shamapitem<transactionresult> {
    transactionresult result;

    public transactionresultitem(transactionresult result) {
        this.result = result;
    }

    @override
    void tobytessink(bytessink sink) {
        binaryserializer write = new binaryserializer(sink);
        write.addlengthencoded(result.txn);
        write.addlengthencoded(result.meta);
    }

    @override
    public shamapitem<transactionresult> copy() {
        // that's ok right ;) these bad boys are immutable anyway
        return this;
    }

    @override
    public prefix hashprefix() {
        return hashprefix.txnode;
    }
}


