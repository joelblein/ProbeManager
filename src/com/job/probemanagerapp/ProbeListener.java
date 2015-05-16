/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probemanagerapp;

/**
 *
 * @author joel
 */
public interface ProbeListener {
   public void probeConnected(String probe);
   public void probeDisonnected(String probeId);
   public void probeTemberatureRead(String probeId, float newTemperature);
   public void probeReadingFailed(String probeId, Throwable t);
}
