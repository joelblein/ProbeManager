/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

/**
 *
 * @author joel
 */
public class ProbeFailedEvent extends ProbeEvent {
    Throwable t;
    public ProbeFailedEvent(String probeId, Throwable t) {
        super(probeId);
        this.t = t;
    }
    
    public Throwable getThrowable() {
        return t;
    }
    
    public String toString() {
        return "Probe "+source+" reading failed : "+t;
    }
}
