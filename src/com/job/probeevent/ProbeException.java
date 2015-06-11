/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent;

/**
 *
 * @author joel
 */
public class ProbeException extends Exception {
    public ProbeException(String message) {
        super(message);
    }
    public ProbeException(Exception e) {
        super(e);
    }
    public ProbeException(String message, Exception e) {
        super(message, e);
    }
    
    @Override
    public boolean equals(Object o) {
        return o!=null
                && o instanceof ProbeException
                && ((ProbeException)o).getMessage()!=null
                && ((ProbeException)o).getMessage().equals(getMessage());
    }

}
