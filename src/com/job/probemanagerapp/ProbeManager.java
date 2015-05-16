/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanagerapp;

import java.io.*;
import java.util.*;
import static com.job.probemanagerapp.ProbeManager.PROBE_FILE_PREFIX;

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
                fireProbeTemperatureChanged(probe, readProbe(probe));
            } catch (Throwable t) {
                fireProbeReadingFailed(probe, t);
            }
        });
    
        probes = foundProbes;
        return foundProbes;        
    }
    
    private final Collection<ProbeListener> probeListeners = new ArrayList<>();

    public void addProbeListener(ProbeListener listener) {
        probeListeners.add(listener);
    }
 
    public void removeProbeListener(ProbeListener listener) {
        probeListeners.remove(listener);
    }
 
    public ProbeListener[] getProbeListeners() {
        return probeListeners.toArray(new ProbeListener[0]);
    } 

    private void fireProbeConnected(String probeId) {
       probeListeners.forEach((pl) -> {pl.probeConnected(probeId);});
    }
    private void fireProbeDisonnected(String probeId) {
       probeListeners.forEach((pl) -> {pl.probeDisonnected(probeId);});
    }
    private void fireProbeTemperatureChanged(String probeId, float newTemperature) {
       probeListeners.forEach((pl) -> {pl.probeTemberatureRead(probeId,  newTemperature);});
    }
    private void fireProbeReadingFailed(String probeId, Throwable t) {
       probeListeners.forEach((pl) -> {pl.probeReadingFailed(probeId, t);});
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
        ReleveTemperature rt = null;
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
                                rt = new ReleveTemperature(new Date(), tempC);
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

// This lter selects subdirs with name beginning with 28-
// Kernel module gives each 1-wire temp sensor name starting 
// with 28-
class DirectoryFileFilter implements FileFilter
{
  @Override
  public boolean accept(File file) {
    String dir = file.getName();
    String startOfName = dir.substring(0, 3);
    return (file.isDirectory() && startOfName.equals(PROBE_FILE_PREFIX));
  }
}

class ProbeException extends Exception {
    ProbeException(String message) {
        super(message);
    }
    ProbeException(Exception e) {
        super(e);
    }
    ProbeException(String message, Exception e) {
        super(message, e);
    }
}