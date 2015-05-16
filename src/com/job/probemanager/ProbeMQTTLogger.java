/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * @author joel
 */ 
public class ProbeMQTTLogger implements ProbeListener, MqttCallback {
    String serverURI;
    String clientId;
    MqttClient mqttClient = null;

    public ProbeMQTTLogger(String serverURI, String clientId) {
        this.serverURI = serverURI;
        this.clientId = clientId;
    }
            
    private void logToMQTTServer(String message) throws MqttException {
        synchronized(this) {
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
        }
        mqttClient.publish("cafetiton/temperature", message.getBytes(), 2, true);
    }
    
    public void start() {
    }

    public void stop() {
        try  {
            mqttClient.disconnect();
        } catch (MqttException me) {
            System.out.println("Unable to disconnect from MQTT server : "+me); 
            me.printStackTrace();
        }
        mqttClient = null;
    }

    @Override
    public void probeConnected(ProbeConnectionEvent e){
        String message = e.toString();
        try  {
            logToMQTTServer(message);
        } catch (MqttException me) {
            System.out.println("Unable to publish on MQTT server : "+me); 
            me.printStackTrace();
        }
    }

    @Override
    public void probeDisonnected(ProbeDisconnectionEvent e) {
        String message = e.toString();
        try  {
            logToMQTTServer(message);
        } catch (MqttException me) {
            System.out.println("Unable to publish on MQTT server : "+me); 
            me.printStackTrace();
        }
    }

    @Override
    public void probeRead(ProbeReadEvent e) {
        String message = e.toString();
        try  {
            logToMQTTServer(message);
        } catch (MqttException me) {
            System.out.println("Unable to publish on MQTT server : "+me); 
            me.printStackTrace();
        }
    }

    @Override
    public void probeReadingFailed(ProbeFailedEvent e) {
        String message = e.toString();
        try  {
            logToMQTTServer(message);
        } catch (MqttException me) {
            System.out.println("Unable to publish on MQTT server : "+me); 
            me.printStackTrace();
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
        try  {
            System.out.println("A message "+imdt.getMessage()+" was delivered with response "+imdt);
        } catch (MqttException me) {
            System.out.println("A message was delivered : no more informations"+me); 
        }
    }
}
