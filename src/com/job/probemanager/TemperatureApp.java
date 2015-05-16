/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author joel
 */
public class TemperatureApp {
    
    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        if(args.length<1) {
            throw new RuntimeException("Needs MQTTBroker ip as an argument");
        }
        
        String hostname = "Unknown";
        String mqttBroketIP = args[0];
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }
        
        ProbeManager pm = ProbeManager.getInstance();
        ProbeMQTTLogger mqttLogger = new ProbeMQTTLogger(mqttBroketIP, hostname);
        pm.addProbeListener(mqttLogger);
        pm.addProbeListener(new ProbeLogger());

        try {
            mqttLogger.start();
            Timer timer = new Timer("ProbeTimer");
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
        } finally {
            mqttLogger.stop();
        }
    }

}
