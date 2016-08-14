package org.dzcoinlab.core.types.known.tx.txns;


import org.dzcoinlab.core.hash.hash256;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;

public class ticketcancel extends transaction {
    public ticketcancel() {
        super(transactiontype.ticketcancel);
    }
    public hash256 ticketid() {
        return get(hash256.ticketid);
    }
    public void ticketid(hash256 id) {
        put(hash256.ticketid, id);
    }
}


