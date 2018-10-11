package com.gzsf.operation.exception;


public class NoCmdToRunException extends BaseException {
    public NoCmdToRunException(){
        super(200,"No CMD To Run");
    }
}
