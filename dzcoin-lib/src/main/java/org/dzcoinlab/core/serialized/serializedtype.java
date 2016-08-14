package org.dzcoinlab.core.serialized;

public interface serializedtype {
    object tojson();
    byte[] tobytes();
    string tohex();
    void tobytessink(bytessink to);
}


