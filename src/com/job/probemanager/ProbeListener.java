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
public interface ProbeListener {
   public void probeConnected(ProbeConnectionEvent e);
   public void probeDisonnected(ProbeDisconnectionEvent e);
   public void probeRead(ProbeReadEvent e);
   public void probeReadingFailed(ProbeFailedEvent e);
}
