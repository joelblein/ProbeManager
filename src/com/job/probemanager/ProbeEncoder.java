/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author joel
 */
public interface ProbeEncoder {

    public byte[] encode(ProbeEvent probeEvent) throws JAXBException;
    public ProbeEvent decode(byte data[]) throws JAXBException;
    
}
