package org.dzcoinlab.core.types.known.tx.txns;


import org.dzcoinlab.core.accountid;
import org.dzcoinlab.core.amount;
import org.dzcoinlab.core.pathset;
import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.hash.hash256;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;
import org.dzcoinlab.core.uint.uint32;

public class payment extends transaction {
    public payment() {
        super(transactiontype.payment);
    }

    public uint32 destinationtag() {return get(uint32.destinationtag);}
    public hash256 invoiceid() {return get(hash256.invoiceid);}
    public amount amount() {return get(amount.amount);}
    public amount sendmax() {return get(amount.sendmax);}
    public accountid destination() {return get(accountid.destination);}
    public pathset paths() {return get(pathset.paths);}
    public void destinationtag(uint32 val) {put(field.destinationtag, val);}
    public void invoiceid(hash256 val) {put(field.invoiceid, val);}
    public void amount(amount val) {put(field.amount, val);}
    public void sendmax(amount val) {put(field.sendmax, val);}
    public void destination(accountid val) {put(field.destination, val);}
    public void paths(pathset val) {put(field.paths, val);}

}


