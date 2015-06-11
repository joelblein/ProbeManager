/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

import com.job.probeevent.ProbeEventXmlEncoder;
import com.job.probeevent.ProbeConnectionEvent;
import com.job.probeevent.ProbeEvent;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joel
 */
public class XmlEncoderTest {
    
    public XmlEncoderTest() {
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
    public void testEncodeDecode() throws Exception {
        ProbeEvent pe = new ProbeConnectionEvent("testId", new Date());
        ProbeEventXmlEncoder encoder = new ProbeEventXmlEncoder();
        assertEquals(pe, encoder.decode(encoder.encode(pe)));
    }
}
