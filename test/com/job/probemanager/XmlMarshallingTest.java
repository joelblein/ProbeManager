/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.swing.text.View;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author joel
 */
public class XmlMarshallingTest {
    
    public XmlMarshallingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void marshallingUnmarshalling() throws Exception {
        JAXBContext jaxbcontext = JAXBContext.newInstance(ProbeEvent.class);
        Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
       
        marshallAndLog(new ProbeConnectionEvent("testId"));
        marshallAndLog(new ProbeDisconnectionEvent("testId"));
        marshallAndLog(new ProbeReadEvent("testId", 18.2f));
        try {
            throw new ProbeException("Test Exception");
        } catch(Exception e) {
            marshallAndLog(new ProbeFailedEvent("testId", e));
        }
    }
    
    private void marshallAndLog(Object jaxbObject) throws Exception {
        JAXBContext jaxbcontext = JAXBContext.newInstance(jaxbObject.getClass());
        Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        jaxbMarshaller.marshal(jaxbObject, System.out);     
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(jaxbObject, os);
        
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        jaxbcontext = JAXBContext.newInstance(ProbeEvent.class);
        Unmarshaller unmarshaller = jaxbcontext.createUnmarshaller();
        Object readObject = unmarshaller.unmarshal(is);

        if(!jaxbObject.equals(readObject)) {
            throw new Exception("Objects are not equals ! ("+jaxbObject+" / "+readObject+")");
        }
    }

    /*
    Map<String, Object> jaxbConfig = new HashMap<String, Object>(); 
// initialize our custom reader
TransientAnnotationReader reader = new TransientAnnotationReader();
try {
	reader.addTransientField(Throwable.class.getDeclaredField("stackTrace"));
	reader.addTransientMethod(Throwable.class.getDeclaredMethod("getStackTrace"));
} catch (SecurityException e) {
	throw new RuntimeException(e);
} catch (NoSuchMethodException e) {
	throw new RuntimeException(e);
} catch (NoSuchFieldException e) {
	throw new RuntimeException(e);
}
jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader); 

JAXBContext jc = JAXBContext.newInstance(new Class[] {jaxbObj.getClass()},jaxbConfig);
    */
}
