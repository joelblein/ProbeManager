/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * @author joel
 */ 
public class ProbeMqttLogger implements ProbeListener, MqttCallback {
    String serverURI;
    String clientId;
    ProbeEncoder encoder;
    MqttClient mqttClient = null;

    public ProbeMqttLogger(String serverURI, String clientId, ProbeEncoder encoder) {
        this.serverURI = serverURI;
        this.clientId = clientId;
        this.encoder = encoder;
    }
            
    synchronized protected MqttClient getMqttClient() throws MqttException {
        if(mqttClient == null) {
            try  {
                    mqttClient = new MqttClient(serverURI, clientId);
                    mqttClient.setCallback(this);
                    mqttClient.connect();
            } catch (IllegalArgumentException iae) {
                System.out.println("Invalid parameters to connect MQTT server (serverURI="+serverURI+", clientId="+clientId+")"); 
                iae.printStackTrace();
            }    
        }
        return mqttClient;
    }    private void sendToMQTTServer(ProbeEvent probeEvent) throws MqttException, JAXBException {
        mqttClient.publish("cafetiton/temperature", encoder.encode(probeEvent), 2, true);
    }
    
    public synchronized void start() {
        try {
            getMqttClient();
        } catch (MqttException me) {
            System.out.println("Unable to start MQTT server : "+me); 
            me.printStackTrace();
        }    
    }

    public synchronized void stop() {
        try  {
            if(mqttClient!=null) {
                mqttClient.disconnect();
            }
        } catch (MqttException me) {
            System.out.println("Unable to stop MQTT server : "+me); 
            me.printStackTrace();
        } finally {
            mqttClient = null;
        }
    }

    @Override
    public void probeConnected(ProbeConnectionEvent e){
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeMqttLogger.class.getName()).log(Level.SEVERE, "Unable to publish connection event ("+e+")on MQTT server", t);
        }
    }

    @Override
    public void probeDisonnected(ProbeDisconnectionEvent e) {
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeMqttLogger.class.getName()).log(Level.SEVERE, "Unable to publish disconnection event ("+e+")on MQTT server", t);
        }
    }

    @Override
    public void probeRead(ProbeReadEvent e) {
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeMqttLogger.class.getName()).log(Level.SEVERE, "Unable to publish probeReadind event ("+e+")on MQTT server", t);
        }
    }

    @Override
    public void probeReadingFailed(ProbeFailedEvent e) {
        try  {
            sendToMQTTServer(e);
        } catch (Throwable t) {
            Logger.getLogger(ProbeMqttLogger.class.getName()).log(Level.SEVERE, "Unable to publish probeReadindFailed event ("+e+")on MQTT server", t);
        }
    }    

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("Connection to mqtt server lost :"+thrwbl);
        mqttClient = null;
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        System.out.println("A message in topic "+topic+" arrived :"+mm);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        if(imdt.getException()==null) {
            System.out.println("Message with id "+imdt.getMessageId()+" has been delivered successfully.");
        } else {
            System.out.println("Message with id "+imdt.getMessageId()+" failed to be delivered "+imdt.getException());
        }
    }
}
