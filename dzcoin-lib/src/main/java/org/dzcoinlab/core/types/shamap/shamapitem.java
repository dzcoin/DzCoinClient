package org.dzcoinlab.core.types.shamap;


import org.dzcoinlab.core.hash.prefixes.prefix;
import org.dzcoinlab.core.serialized.bytessink;

abstract public class shamapitem<t> {
    abstract void tobytessink(bytessink sink);
    public abstract shamapitem<t> copy();
    public abstract prefix hashprefix();
}


