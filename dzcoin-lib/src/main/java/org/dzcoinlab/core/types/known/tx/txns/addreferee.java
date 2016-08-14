package org.dzcoinlab.core.types.known.tx.txns;

import org.dzcoinlab.core.accountid;
import org.dzcoinlab.core.amount;
import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;

public class addreferee extends transaction {
    public addreferee() {
        super(transactiontype.addreferee);
    }
    public accountid destination() {return get(accountid.destination);}
    public amount amount() {return get(amount.amount);}
    public void amount(amount val) {put(field.amount, val);}
    public void destination(accountid val) {put(field.destination, val);}
}


