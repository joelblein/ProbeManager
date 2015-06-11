/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probevent.mqtt.app;
import com.job.probeevent.mqtt.ProbeEventMqttPublisher;
import com.job.probeevent.ProbeManager;
import com.job.probeevent.ProbeEventXmlEncoder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author joel
 */
public class ProbeEventApp {
    
    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws Exception {
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

        pm.addProbeListener(new ProbeEventStdoLogger());
        
        ProbeEventMqttPublisher mqttLogger = new ProbeEventMqttPublisher(mqttBroketIP, hostname, new ProbeEventXmlEncoder());
        pm.addProbeListener(mqttLogger);

        try {
            mqttLogger.start(); // se connecte au serveur mqtt
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
            }, 1000, 60*1000);        
        
            new Object().wait(); // keep alive...
        } finally {
            mqttLogger.stop(); // se délogue du serveur mqtt
        }
    }
}
