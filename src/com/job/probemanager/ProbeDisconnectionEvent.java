/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;


public class ProbeDisconnectionEvent extends ProbeConnectionEvent {

    public ProbeDisconnectionEvent(String probeId) {
        super(probeId);
    }
    
    public String toString() {
        return "Probe "+source+" diconnected";
    }
}
