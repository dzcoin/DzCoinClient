package org.dzcoinlab.core.types.known.tx.txns;


import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;
import org.dzcoinlab.core.uint.uint32;

public class offercancel extends transaction {
    public offercancel() {
        super(transactiontype.offercancel);
    }
    public uint32 offersequence() {return get(uint32.offersequence);}
    public void offersequence(uint32 val) {put(field.offersequence, val);}

}


