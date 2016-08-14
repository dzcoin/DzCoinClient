package org.dzcoinlab.core.types.known.sle;


import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.hash.hash256;
import org.dzcoinlab.core.serialized.enums.ledgerentrytype;
import org.dzcoinlab.core.uint.uint32;

// this class has a previoustxnid and previoustxnlgrseq
abstract public class threadedledgerentry extends ledgerentry {
    public threadedledgerentry(ledgerentrytype type) {
        super(type);
    }
    public uint32 previoustxnlgrseq() {return get(uint32.previoustxnlgrseq);}
    public hash256 previoustxnid() {return get(hash256.previoustxnid);}
    public void previoustxnlgrseq(uint32 val) {put(field.previoustxnlgrseq, val);}
    public void previoustxnid(hash256 val) {put(field.previoustxnid, val);}
}


