package org.dzcoinlab.core.types.shamap;


import org.dzcoinlab.core.stobject;
import org.dzcoinlab.core.hash.prefixes.hashprefix;
import org.dzcoinlab.core.hash.prefixes.prefix;
import org.dzcoinlab.core.serialized.bytessink;
import org.dzcoinlab.core.types.known.sle.ledgerentry;

public class ledgerentryitem extends shamapitem<ledgerentry> {
    public ledgerentryitem(ledgerentry entry) {
        this.entry = entry;
    }

    public ledgerentry entry;

    @override
    void tobytessink(bytessink sink) {
        entry.tobytessink(sink);
    }

    @override
    public shamapitem<ledgerentry> copy() {
        stobject object = stobject.translate.frombytes(entry.tobytes());
        ledgerentry le = (ledgerentry) object;
        // todo: what about other auxiliary (non serialized) fields
        le.index(entry.index());
        return new ledgerentryitem(le);
    }

    @override
    public prefix hashprefix() {
        return hashprefix.leafnode;
    }
}


