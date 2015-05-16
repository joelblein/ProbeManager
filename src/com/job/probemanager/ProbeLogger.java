/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanager;

/**
 *
 * @author joel
 */
public class ProbeLogger implements ProbeListener {

        @Override
        public void probeConnected(ProbeConnectionEvent e) { 
            System.out.println(e);
        }

        @Override
        public void probeDisonnected(ProbeDisconnectionEvent e) {
            System.out.println(e);
        }

        @Override
        public void probeRead(ProbeReadEvent e) {
            System.out.println(e);
        }

        @Override
        public void probeReadingFailed(ProbeFailedEvent e) {
            System.out.println(e);
            e.getThrowable().printStackTrace(System.out);
        }
    
}
