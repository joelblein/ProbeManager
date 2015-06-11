/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probeevent;

/**
 *
 * @author joel
 */
public interface ProbeEventEncoder {

    public byte[] encode(ProbeEvent probeEvent) throws Exception;
    public ProbeEvent decode(byte data[]) throws Exception;
    
}
