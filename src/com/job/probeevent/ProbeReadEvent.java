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


@XmlRootElement
public class ProbeReadEvent extends ProbeEvent {
    float temperature;
    
    public ProbeReadEvent() {
        this(NO_PROBE_ID, Float.NaN, null);
    }
    
    public ProbeReadEvent(String  probeId, float temperature) {
        this(probeId, temperature, new Date());
    }
    
    public ProbeReadEvent(String  probeId, float temperature, Date time) {
        super(probeId, time);
        this.temperature  = temperature;
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

    @XmlElement
    public float getTemperature() {
        return temperature;
    }
    
    protected void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    
    @Override
    public void fire(ProbeEventListener listener) {
        listener.probeRead(this);
    }

    @Override
    public String toString() {
        return "Probe  "+source+" read at "+time+" : "+temperature+"°C";
    }

    public boolean equals(Object object) {
        return super.equals(object)
            && object instanceof ProbeReadEvent 
                && ((ProbeReadEvent)object).getTemperature()==temperature;
    }
}
