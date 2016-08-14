package org.dzcoinlab.core.binary;


import org.dzcoinlab.core.serialized.binaryserializer;
import org.dzcoinlab.core.serialized.bytessink;
import org.dzcoinlab.core.serialized.serializedtype;

public class stwriter {
    bytessink sink;
    binaryserializer serializer;
    public stwriter(bytessink bytessink) {
        serializer = new binaryserializer(bytessink);
        sink = bytessink;
    }
    public void write(serializedtype obj) {
        obj.tobytessink(sink);
    }
    public void writevl(serializedtype obj) {
        serializer.addlengthencoded(obj);
    }
}


