package com.gzsf.operation.exception;


public class NoServiceFoundException  extends BaseException {
    public NoServiceFoundException (){
        super(202,"No Service Found");
    }
}
