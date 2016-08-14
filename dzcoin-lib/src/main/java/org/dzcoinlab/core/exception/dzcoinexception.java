package org.dzcoinlab.core.exception;

/**
 * exception of all dzcoinj
 */
public class dzcoinexception extends runtimeexception {
    private int code;
    private string msg;


    public dzcoinexception(string msg){
        super(msg);
        this.msg = msg;
    }

    public dzcoinexception(int code, string msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getcode() {
        return code;
    }

    public string getmsg() {
        return msg;
    }
}


