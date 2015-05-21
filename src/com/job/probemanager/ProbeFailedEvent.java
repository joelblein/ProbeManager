/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author joel
 */
@XmlRootElement
public class ProbeFailedEvent extends ProbeEvent {

    protected Throwable t;
    
    
    private ProbeFailedEvent() {
        this(NO_PROBE_ID, null, null);
    }
    
    public ProbeFailedEvent(String probeId, Throwable t) {
        this(probeId, t, new Date());
    }
    
    public ProbeFailedEvent(String probeId, Throwable t, Date time) {
        super(probeId, time);
        this.t = t;
    } 
    
    @XmlElement
    @Override
    String getProbeId() {
        return (String) source;
    }
    
    private void setProbeId(String probeId) {
        source = probeId;
    }
    
    @XmlElement
    @Override
    Date getTime() {
        return time;
    }

    protected void setTime(Date time) {
        this.time = time;
    }

    @XmlJavaTypeAdapter(value = ThrowableAdapter.class)
    public Throwable getThrowable() {
        return t;
    }
    
    protected void setThrowable(Throwable t) {
        this.t = t;
    }
    
    public String toString() {
        return "Probe "+source+" reading failed at "+time+" : "+t;
    }

    public boolean equals(Object object) {
        return super.equals(object)
            && object instanceof ProbeFailedEvent 
                && ((ProbeFailedEvent)object).getThrowable()!=null
                && ((ProbeFailedEvent)object).getThrowable().equals(t);
    }

  public static class ThrowableAdapter extends XmlAdapter<String, Throwable> {
    @Override
    public String marshal(Throwable v) throws Exception {
      return v == null ? null : v.getMessage();
    }

    @Override
    public Throwable unmarshal(String v) throws Exception {
      return v == null ? null : new ProbeException(v);
    }
  }
}
