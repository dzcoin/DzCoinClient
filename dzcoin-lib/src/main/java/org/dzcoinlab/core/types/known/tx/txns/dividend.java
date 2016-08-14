package org.dzcoinlab.core.types.known.tx.txns;

import org.dzcoinlab.core.accountid;
import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;
import org.dzcoinlab.core.uint.uint64;

/**
 * dividend data class
 */
public class dividend extends transaction{
    public dividend() {
        super(transactiontype.dividend);
    }

    public accountid destination() {return get(accountid.destination);}
    public uint64 dividendcoins(){return (uint64)get(field.dividendcoins);}
    public uint64 dividendcoinsvbc(){return (uint64)get(field.dividendcoinsvbc);}
}


