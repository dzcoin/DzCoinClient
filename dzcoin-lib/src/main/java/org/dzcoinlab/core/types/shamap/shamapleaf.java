package org.dzcoinlab.core.types.shamap;


import org.dzcoinlab.core.hash.hash256;
import org.dzcoinlab.core.hash.prefixes.prefix;
import org.dzcoinlab.core.serialized.bytessink;

public class shamapleaf extends shamapnode {
    public hash256 index;
    public shamapitem item;
    public long version = -1;

    protected shamapleaf(hash256 index, shamapitem item) {
        this.index = index;
        this.item = item;
    }

    @override public boolean isleaf() {return true;}
    @override public boolean isinner() {return false;}

    @override
    prefix hashprefix() {
        return item.hashprefix();
    }

    @override
    public void tobytessink(bytessink sink) {
        item.tobytessink(sink);
        index.tobytessink(sink);
    }

    public shamapleaf copy() {
        return new shamapleaf(index, item.copy());
    }
}


