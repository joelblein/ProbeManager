/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ProbeConnectionEvent extends ProbeEvent {

    private ProbeConnectionEvent() {
        this(NO_PROBE_ID);
    } 
    
    public ProbeConnectionEvent(String probeId) {
        super(probeId);
    } 
    
    public ProbeConnectionEvent(String probeId, Date time) {
        super(probeId, time);
    } 

    @XmlElement
    @Override
    public String getProbeId() {
        return (String) source;
    }
    
    private void setProbeId(String probeId) {
        source = probeId;
    }
    
    @XmlElement
    @Override
    public Date getTime() {
        return time;
    }

    protected void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Probe "+source+" connected at "+time;
    }

    @Override
    public void fire(ProbeEventListener listener) {
        listener.probeConnected(this);
    }
}
