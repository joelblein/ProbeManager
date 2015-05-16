/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;


public class ProbeReadEvent extends ProbeEvent {
    float temperature;
    public ProbeReadEvent(String  probeId, float temperature) {
        super(probeId);
        this.temperature  = temperature;
    }
    
    public float getTemperature() {
        return temperature;
    }
    
    @Override
    public String toString() {
        return "Probe  "+source+" read : "+temperature+"°C";
    }

}
