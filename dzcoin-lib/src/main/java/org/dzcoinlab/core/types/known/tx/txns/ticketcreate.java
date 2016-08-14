package org.dzcoinlab.core.types.known.tx.txns;


import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;

public class ticketcreate extends transaction {
    public ticketcreate() {
        super(transactiontype.ticketcreate);
    }
}


