/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent.mqtt;

import com.job.probeevent.ProbeEvent;
import com.job.probeevent.ProbeEventEncoder;
import com.job.probeevent.ProbeEventListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * @author joel */ 
public class ProbeEventMqttReceiver {
    private static final String PERSISTENCE_DIRECTORY = "XXXXXXXXXX";
    String serverURI;
    String clientId;
    ProbeEventEncoder encoder;
    KeptAliveMqttConnection connection;
    ProbeEventListener listener;
    MqttConnectOptions connOpts;
    
    public ProbeEventMqttReceiver(String serverURI, String clientId, ProbeEventEncoder encoder, ProbeEventListener listener) throws MqttException, IllegalArgumentException {
        this.serverURI = serverURI;
        this.clientId = clientId;
        this.encoder = encoder;
        this.listener = listener;
        
        connection = new KeptAliveMqttConnection(serverURI, clientId, new MqttCallback() {
            @Override
            public final void connectionLost(Throwable thrwbl) {
                connection.connectionLost(thrwbl);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mm) throws Exception {
                System.out.println("A ProbeEvent message arrived.");
                ProbeEvent event = null;
                try {
                    event = encoder.decode(mm.getPayload());
                } catch(Throwable t) {
                    System.out.println("Unable to decode event : "+new String(mm.getPayload()));
                    t.printStackTrace(System.out);
                }

                if(event!=null) {
                    try {
                        event.fire(listener);
                    } catch(Throwable t) {
                        System.out.println("Listener couldn't deal with the received event "+t);
                        t.printStackTrace(System.out);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken imdt) {
                // should never happen...
            }
        });
    }
            
    public synchronized void start() throws MqttException {
        connection.start();
    }

    public synchronized void stop() {
        connection.stop();
    }

}
