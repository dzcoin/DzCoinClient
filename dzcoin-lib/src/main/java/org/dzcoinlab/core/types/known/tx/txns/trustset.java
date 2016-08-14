package org.dzcoinlab.core.types.known.tx.txns;


import org.dzcoinlab.core.amount;
import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;
import org.dzcoinlab.core.uint.uint32;

public class trustset extends transaction {
    public trustset() {
        super(transactiontype.trustset);
    }

    public uint32 qualityin() {return get(uint32.qualityin);}
    public uint32 qualityout() {return get(uint32.qualityout);}
    public amount limitamount() {return get(amount.limitamount);}
    public void qualityin(uint32 val) {put(field.qualityin, val);}
    public void qualityout(uint32 val) {put(field.qualityout, val);}
    public void limitamount(amount val) {put(field.limitamount, val);}
}


