package org.dzcoinlab.core.types.known.tx.txns;


import org.dzcoinlab.core.amount;
import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;
import org.dzcoinlab.core.uint.uint32;

public class offercreate extends transaction {
    public offercreate() {
        super(transactiontype.offercreate);
    }
    public uint32 expiration() {return get(uint32.expiration);}
    public uint32 offersequence() {return get(uint32.offersequence);}
    public amount takerpays() {return get(amount.takerpays);}
    public amount takergets() {return get(amount.takergets);}
    public void expiration(uint32 val) {put(field.expiration, val);}
    public void offersequence(uint32 val) {put(field.offersequence, val);}
    public void takerpays(amount val) {put(field.takerpays, val);}
    public void takergets(amount val) {put(field.takergets, val);}
}


