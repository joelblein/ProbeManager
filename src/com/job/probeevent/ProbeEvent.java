/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent;

import java.util.Date;
import java.util.EventObject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joel
 */
public abstract class ProbeEvent extends EventObject {
    protected static String NO_PROBE_ID = "No probe Id";
    protected Date time;

    private ProbeEvent() {
        this(NO_PROBE_ID);
    }
    
    public ProbeEvent(String probeId) {
        this(probeId, new Date());
    }
    
    public ProbeEvent(String probeId, Date time) {
        super(probeId);
        this.time = time;
    }
    
    public String getProbeId() {
        return (String) source;
    }
    
    public Date getTime() {
        return time;
    }

    public abstract void fire(ProbeEventListener listener);
    
    @Override
    public String toString() {
        return "Probe "+source+" event at "+time;
    }

    public boolean equals(Object object) {
        return object!=null
                && object instanceof ProbeEvent 
                && ((EventObject)object).getSource()!=null
                && ((EventObject)object).getSource().equals(source)
                && ((ProbeEvent)object).getTime()!=null
                && ((ProbeEvent)object).getTime().equals(time);
    }
}
