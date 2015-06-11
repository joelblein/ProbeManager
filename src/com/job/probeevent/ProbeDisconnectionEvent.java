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
public class ProbeDisconnectionEvent extends ProbeConnectionEvent {

    private ProbeDisconnectionEvent() {
        this(NO_PROBE_ID);
    } 
    
    public ProbeDisconnectionEvent(String probeId) {
        super(probeId);
    }
    
    public ProbeDisconnectionEvent(String probeId, Date time) {
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
    public void fire(ProbeEventListener listener) {
        listener.probeDisonnected(this);
    }

    @Override
    public String toString() {
        return "Probe "+source+" diconnected at "+time;
    }
}
