package org.dzcoinlab.core.types.known.tx.result;

import org.dzcoinlab.core.starray;
import org.dzcoinlab.core.stobject;
import org.dzcoinlab.core.serialized.enums.engineresult;
import org.dzcoinlab.core.types.known.sle.ledgerentry;
import org.dzcoinlab.core.uint.uint32;
import org.dzcoinlab.core.uint.uint8;

import java.util.iterator;

public class transactionmeta extends stobject {
    public static boolean istransactionmeta(stobject source) {
        return source.has(uint8.transactionresult) &&
                source.has(starray.affectednodes);
    }

    public engineresult engineresult() {
        return engineresult(this);
    }

    public iterable<affectednode> affectednodes() {
        starray nodes = get(starray.affectednodes);
        final iterator<stobject> iterator = nodes.iterator();
        return new iterable<affectednode>() {
            @override
            public iterator<affectednode> iterator() {
                return iterateaffectednodes(iterator);
            }
        };
    }

    public void walkprevious(ledgerentry.onledgerentry cb) {
        for (affectednode affectednode : affectednodes()) {
            if (affectednode.waspreviousnode()) {
                cb.onobject(affectednode.nodeasprevious());
            }
        }
    }
    public static iterator<affectednode> iterateaffectednodes(final iterator<stobject> iterator) {
        return new iterator<affectednode>() {
            @override
            public boolean hasnext() {
                return iterator.hasnext();
            }

            @override
            public affectednode next() {
                return (affectednode) iterator.next();
            }

            @override
            public void remove() {
                iterator.remove();
            }
        };
    }

    public uint32 transactionindex() {
        return get(uint32.transactionindex);
    }
}


