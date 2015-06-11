/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent.mqtt;

import com.job.probeevent.ProbeEventEncoder;
import com.job.probeevent.ProbeConnectionEvent;
import com.job.probeevent.ProbeFailedEvent;
import com.job.probeevent.ProbeEventListener;
import com.job.probeevent.ProbeReadEvent;
import com.job.probeevent.ProbeEvent;
import com.job.probeevent.ProbeDisconnectionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * @author joel
 */ 
public class ProbeEventMqttPublisher implements ProbeEventListener, MqttCallback {
    String serverURI;
    String clientId;
    ProbeEventEncoder encoder;
    KeptAliveMqttConnection connection;

    public ProbeEventMqttPublisher(String serverURI, String clientId, ProbeEventEncoder encoder) throws MqttException {
        this.serverURI = serverURI;
        this.clientId = clientId;
        connection = new KeptAliveMqttConnection(serverURI, clientId, this);
        this.encoder = encoder;
    }
            
    private void sendToMQTTServer(ProbeEvent probeEvent) throws Exception {
        connection.getMqttClient().publish("cafetiton/temperature", encoder.encode(probeEvent), 2, true);
    }
    
    public synchronized void start() throws MqttException {
        connection.start();
    }

    public synchronized void stop() {
        connection.stop();
    }

    @Override
    public void probeConnected(ProbeConnectionEvent e){
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeEventMqttPublisher.class.getName()).log(Level.SEVERE, "Unable to publish connection event ("+e+")on MQTT server", t);
        }
    }

    @Override
    public void probeDisonnected(ProbeDisconnectionEvent e) {
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeEventMqttPublisher.class.getName()).log(Level.SEVERE, "Unable to publish disconnection event ("+e+")on MQTT server", t);
        }
    }

    @Override
    public void probeRead(ProbeReadEvent e) {
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeEventMqttPublisher.class.getName()).log(Level.SEVERE, "Unable to publish probeReadind event ("+e+")on MQTT server", t);
        }
    }

    @Override
    public void probeReadingFailed(ProbeFailedEvent e) {
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeEventMqttPublisher.class.getName()).log(Level.SEVERE, "Unable to publish probeReadindFailed event ("+e+")on MQTT server", t);
        }
    }    

    @Override
    public void connectionLost(Throwable thrwbl) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        System.out.println("A message in topic "+topic+" arrived :"+mm);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }
}
