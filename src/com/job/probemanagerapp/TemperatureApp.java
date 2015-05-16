/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanagerapp;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author joel
 */
public class TemperatureApp {
    private static final String CHAMBRE_FROIDE = "Chambre froide";
    private static final String PETIT_CONGELO = "Petit congélateur";
    private static final String GRAND_CONGELO = "Grand congélateur";
    
    
    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {        Collection<TemperatureReader> readers = null;
        ProbeManager pm = ProbeManager.getInstance();
        pm.addProbeListener(new ProbeListener() {

        @Override
        public void probeConnected(String probeId) { 
            System.out.println("ProbeConnected "+probeId);
        }

        @Override
        public void probeDisonnected(String probeId) {
            System.out.println("ProbeDisconnected "+probeId);
        }

        @Override
        public void probeTemberatureRead(String probeId, float newTemperature) {
            System.out.println("Probe  "+probeId+" : "+newTemperature+"°C");
        }

        @Override
        public void probeReadingFailed(String probeId, Throwable t) {
            System.out.println("ProbeReadingFailed  "+t);
        }
    });

        Timer timer = new Timer("ProbeTimer");
        Random random = new Random();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    pm.checkProbes();
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }, 1000, 5000);

    }

}
