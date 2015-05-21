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
public class XmlEncoder implements ProbeEncoder {
    
    @Override
    public byte[] encode(ProbeEvent probeEvent) throws JAXBException {
        JAXBContext jaxbcontext = JAXBContext.newInstance(ProbeConnectionEvent.class,
                ProbeDisconnectionEvent.class,
                ProbeFailedEvent.class,
                ProbeReadEvent.class);
        Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        jaxbMarshaller.marshal(probeEvent, System.out);     
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(probeEvent, os);
        return os.toByteArray();
    }

    @Override
    public ProbeEvent decode(byte data[]) throws JAXBException {
        JAXBContext jaxbcontext = JAXBContext.newInstance(ProbeConnectionEvent.class,
                ProbeDisconnectionEvent.class,
                ProbeFailedEvent.class,
                ProbeReadEvent.class);
        Unmarshaller unmarshaller = jaxbcontext.createUnmarshaller();
        return (ProbeEvent)unmarshaller.unmarshal(new ByteArrayInputStream(data));
    }
    

}
