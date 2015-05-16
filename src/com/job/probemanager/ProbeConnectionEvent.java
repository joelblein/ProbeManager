/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;


public class ProbeConnectionEvent extends ProbeEvent {

    public ProbeConnectionEvent(String probeId) {
        super(probeId);
    } 
    
    @Override
    public String toString() {
        return "Probe "+source+" connected";
    }
}
