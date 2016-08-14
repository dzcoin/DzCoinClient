package org.dzcoinlab.core.types.known.tx.txns;


import org.dzcoinlab.core.variablelength;
import org.dzcoinlab.core.fields.field;
import org.dzcoinlab.core.hash.hash128;
import org.dzcoinlab.core.hash.hash256;
import org.dzcoinlab.core.serialized.enums.transactiontype;
import org.dzcoinlab.core.types.known.tx.transaction;
import org.dzcoinlab.core.uint.uint32;

public class accountset extends transaction {
    public accountset() {
        super(transactiontype.accountset);
    }
    public uint32 transferrate() {return get(uint32.transferrate);}
    public uint32 walletsize() {return get(uint32.walletsize);}
    public uint32 setflag() {return get(uint32.setflag);}
    public uint32 clearflag() {return get(uint32.clearflag);}
    public hash128 emailhash() {return get(hash128.emailhash);}
    public hash256 walletlocator() {return get(hash256.walletlocator);}
    public variablelength messagekey() {return get(variablelength.messagekey);}
    public variablelength domain() {return get(variablelength.domain);}
    public void transferrate(uint32 val) {put(field.transferrate, val);}
    public void walletsize(uint32 val) {put(field.walletsize, val);}
    public void setflag(uint32 val) {put(field.setflag, val);}
    public void clearflag(uint32 val) {put(field.clearflag, val);}
    public void emailhash(hash128 val) {put(field.emailhash, val);}
    public void walletlocator(hash256 val) {put(field.walletlocator, val);}
    public void messagekey(variablelength val) {put(field.messagekey, val);}
    public void domain(variablelength val) {put(field.domain, val);}

}


