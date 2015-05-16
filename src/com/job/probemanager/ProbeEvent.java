/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

import java.util.EventObject;

/**
 *
 * @author joel
 */
public class ProbeEvent extends EventObject {
    
    public ProbeEvent(String probeId) {
        super(probeId);
    }

    String getProbeId() {
        return (String) source;
    }
}
