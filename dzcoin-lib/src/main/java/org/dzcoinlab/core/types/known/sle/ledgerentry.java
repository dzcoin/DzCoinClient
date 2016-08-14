package org.dzcoinlab.core.types.known.sle;

import org.dzcoinlab.core.accountid;
import org.dzcoinlab.core.amount;
import org.dzcoinlab.core.stobject;
import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.formats.leformat;
import org.dzcoinlab.core.hash.hash256;
import org.dzcoinlab.core.serialized.enums.ledgerentrytype;
import org.dzcoinlab.core.types.known.sle.entries.accountroot;
import org.dzcoinlab.core.types.known.sle.entries.directorynode;
import org.dzcoinlab.core.types.known.sle.entries.offer;
import org.dzcoinlab.core.types.known.sle.entries.ripplestate;
import org.dzcoinlab.core.uint.uint16;
import org.dzcoinlab.core.uint.uint32;

import java.util.treeset;

public class ledgerentry extends stobject {
    public ledgerentry(ledgerentrytype type) {
        setformat(leformat.formats.get(type));
        put(field.ledgerentrytype, type);
    }

    public ledgerentrytype ledgerentrytype() {return ledgerentrytype(this);}
    public hash256 index() { return get(hash256.index); }
    public uint32 flags() {return get(uint32.flags);}
    public hash256 ledgerindex() {return get(hash256.ledgerindex);}

    public void ledgerentrytype(uint16 val) {put(field.ledgerentrytype, val);}
    public void ledgerentrytype(ledgerentrytype val) {put(field.ledgerentrytype, val);}
    public void flags(uint32 val) {put(field.flags, val);}
    public void ledgerindex(hash256 val) {put(field.ledgerindex, val);}

    public treeset<accountid> owners() {
        treeset<accountid> owners = new treeset<accountid>();

        if (has(field.lowlimit)) {
            owners.add(get(amount.lowlimit).issuer());
        }
        if (has(field.highlimit)) {
            owners.add(get(amount.highlimit).issuer());
        }
        if (has(field.account)) {
            owners.add(get(accountid.account));
        }

        return owners;
    }

    public void index(hash256 index) {
        put(hash256.index, index);
    }

    public void setdefaults() {
        if (flags() == null) {
            flags(new uint32(0));
        }
    }

    public static abstract class onledgerentry {
        public abstract void onoffer(offer of);
        public abstract void ondirectorynode(directorynode dn);
        public abstract void onripplestate(ripplestate rs);
        public abstract void onaccountroot(accountroot ar);
        public abstract void onall(ledgerentry le);

        public void onobject(stobject object) {
            if (object instanceof offer) {
                onoffer(((offer) object));
            } else if (object instanceof accountroot) {
                onaccountroot((accountroot) object);
            } else if (object instanceof directorynode) {
                ondirectorynode((directorynode) object);
            } else if (object instanceof ripplestate) {
                onripplestate((ripplestate) object);
            }
            onall((ledgerentry) object);
        }
    }
}


