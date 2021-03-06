/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent;

import com.job.probeevent.ProbeConnectionEvent;
import com.job.probeevent.ProbeFailedEvent;
import com.job.probeevent.ProbeEventListener;
import com.job.probeevent.ProbeReadEvent;
import com.job.probeevent.ProbeDisconnectionEvent;
import java.io.*;
import java.util.*;
import static com.job.probeevent.ProbeManager.PROBE_FILE_PREFIX;

public class ProbeManager {
    // This directory created by 1-wire kernel modules
    static String W1_DIR_PATH = "/sys/bus/w1/devices";
    static String W1_BUSMASTER_DIR = "w1_bus_master1";
    static String W1_BUSMASTER_SLAVES = "w1_master_slaves";
    static String PROBE_FILE_PREFIX = "28-";
    
    static final ProbeManager instance = new ProbeManager();
        
    public static ProbeManager getInstance() {
        return instance;
    }
    
    Collection<String> probes = new ArrayList<>();
    long lastCheck = 0;
 
    public Collection<String> checkProbes() throws ProbeException {
        Collection<String> foundProbes = readProbeIds();

        // Detect the last disconnected probes
        Collection<String> disconnectedProbes = new ArrayList<>(probes);
        disconnectedProbes.removeAll(foundProbes);
        disconnectedProbes.stream().forEach((probe) -> {
            fireProbeDisonnected(probe);
        });

        // Detect the new connected probes
        Collection<String> connectedProbes = new ArrayList<>(foundProbes);
        connectedProbes.removeAll(probes);
        connectedProbes.stream().forEach((probe) -> {
            try {
                fireProbeConnected(probe);
            } catch (Throwable t) {
                fireProbeReadingFailed(probe, t);
            }
        });
        
        // Check temperature changes
        // USE PROBE CONFIG CLASS (RESSOURCE FILE ?)
        foundProbes.stream().forEach((probe) -> {
            try {
                fireProbeRead(probe, readProbe(probe));
            } catch (Throwable t) {
                fireProbeReadingFailed(probe, t);
            }
        });
    
        probes = foundProbes;
        return foundProbes;        
    }
    
    private final Collection<ProbeEventListener> probeListeners = new ArrayList<>();

    public void addProbeListener(ProbeEventListener listener) {
        probeListeners.add(listener);
    }
 
    public void removeProbeListener(ProbeEventListener listener) {
        probeListeners.remove(listener);
    }
 
    public ProbeEventListener[] getProbeListeners() {
        return probeListeners.toArray(new ProbeEventListener[0]);
    } 

    private void fireProbeConnected(String probeId) {
       ProbeConnectionEvent e = new ProbeConnectionEvent(probeId);
       probeListeners.forEach((pl) -> {pl.probeConnected(e);});
    }
    private void fireProbeDisonnected(String probeId) {
       ProbeDisconnectionEvent e = new ProbeDisconnectionEvent(probeId);
       probeListeners.forEach((pl) -> {pl.probeDisonnected(e);});
    }
    private void fireProbeRead(String probeId, float newTemperature) {
       ProbeReadEvent e = new ProbeReadEvent(probeId, newTemperature);
       probeListeners.forEach((pl) -> {pl.probeRead(e);});
    }
    private void fireProbeReadingFailed(String probeId, Throwable t) {
       ProbeFailedEvent e = new ProbeFailedEvent(probeId, t);
       probeListeners.forEach((pl) -> {pl.probeReadingFailed(e);});
    }
    
    public Collection<String> readProbeIds() throws ProbeException {
        Collection<String> foundProbes;
        foundProbes = new ArrayList<>();

        String filePath = W1_DIR_PATH + "/" + W1_BUSMASTER_DIR+"/"+W1_BUSMASTER_SLAVES;
        File f = new File(filePath);
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String output;
            try {
                while((output = br.readLine()) != null) {
                    if(output.startsWith(PROBE_FILE_PREFIX)) {
                        String probeId = output.substring(PROBE_FILE_PREFIX.length());
                        foundProbes.add(probeId);
                    }
                }
            } catch(IOException ioe) {
                throw new ProbeException("I/O Error reading file '"+filePath+"'", ioe);
            }
        }   catch (FileNotFoundException fnfe) {
            throw new ProbeException("File '"+filePath+"' doesn't exist", fnfe);
        }
        return foundProbes;
    }

    public float readProbe(String probeId) throws ProbeException {
        if(probeId!=null) {
            // Device data in w1_slave file
            String filePath = W1_DIR_PATH + "/" + PROBE_FILE_PREFIX+probeId+ "/w1_slave";
            File f = new File(filePath);
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String output;
                try {
                    while((output = br.readLine()) != null) {
                        int idx = output.indexOf("t=");
                        if(idx > -1) {
                            // Temp data (x1000) in 5 chars after t=
                            String tempString = output.substring(idx + 2);
                            try {
                                float tempC = Float.parseFloat(tempString);
                                // Divide by 1000 to get degrees Celsius
                                tempC /= 1000;
                                return tempC;
                            } catch(NumberFormatException nfe) {
                                throw new ProbeException("No parsable temperature in String '"+tempString+"");
                            }
                        }
                    }
                } catch(IOException ioe) {
                    throw new ProbeException("I/O Error reading file '"+filePath+"'", ioe);
                }
            }   catch (FileNotFoundException fnfe) {
                throw new ProbeException("File '"+filePath+"' doesn't exist", fnfe);
            }
        } else {
            throw new ProbeException("Probe id is null");
        }
        throw new ProbeException("Unable to read Probe '"+probeId+"'");
    }


  public static void main(String[] argv) throws Exception {
        Collection<String> probes;
        probes = ProbeManager.getInstance().checkProbes();
        for(String p : probes) {
            float t = ProbeManager.getInstance().readProbe(p);
            System.out.println(p + " " + t);
        }
    } 
}
