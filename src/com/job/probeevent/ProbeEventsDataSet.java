/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent;

import com.job.probeevent.ProbeConnectionEvent;
import com.job.probeevent.ProbeFailedEvent;
import com.job.probeevent.ProbeReadEvent;
import com.job.probeevent.ProbeEvent;
import com.job.probeevent.ProbeDisconnectionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProbeEventsDataSet {

    private List<ProbeEvent> events;

    @XmlElements({
        @XmlElement(name="probeConnectionEvent", type=ProbeConnectionEvent.class),
        @XmlElement(name="probeDisconnectionEvent", type=ProbeDisconnectionEvent.class),
        @XmlElement(name="probeReadEvent", type=ProbeReadEvent.class),
        @XmlElement(name="probeFailedEvent", type=ProbeFailedEvent.class)
    })
    
    public synchronized void addEvent(ProbeEvent event) {
       if(events==null) {
           events = new ArrayList<>();
       }
       events.add(event);
    }

    public synchronized List<ProbeEvent> getEvents() {
       return events;
    }

    public synchronized void setEvents(List<ProbeEvent> someEvents) {
        this.events = someEvents;
    }


}
