/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author joel
 */
public class ProbeEventXmlEncoder implements ProbeEventEncoder {
    private static JAXBContext jaxbcontext = null;
    
    private static synchronized JAXBContext getJAXBContext() throws JAXBException {
        if(jaxbcontext==null) {
            jaxbcontext = JAXBContext.newInstance(ProbeConnectionEvent.class,
                    ProbeDisconnectionEvent.class,
                    ProbeFailedEvent.class,
                    ProbeReadEvent.class);
        }
        return jaxbcontext;
    }
    
    public static void encode(ProbeEvent pe, OutputStream os) throws JAXBException {
        Marshaller jaxbMarshaller = getJAXBContext().createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);        
        jaxbMarshaller.marshal(pe, os);
    }
    
    public static ProbeEvent decode(InputStream is) throws JAXBException {
        Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
        return (ProbeEvent)unmarshaller.unmarshal(is);
    }
    
    @Override
    public byte[] encode(ProbeEvent probeEvent) throws JAXBException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        encode(probeEvent, os);
        return os.toByteArray();
    }

    @Override
    public ProbeEvent decode(byte data[]) throws JAXBException {
        return decode(new ByteArrayInputStream(data));
    }
    

}
