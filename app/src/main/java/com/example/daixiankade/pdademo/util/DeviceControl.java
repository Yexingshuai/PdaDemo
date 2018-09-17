package com.example.daixiankade.pdademo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by daixiankade on 2018/9/6.
 */


public class DeviceControl {
    private BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(new File("/sys/class/misc/mtgpio/pin"), false));

    public DeviceControl()
            throws IOException {
    }

    public void DeviceClose()
            throws IOException {
        this.CtrlFile.close();
    }

    public void PowerOffDevice()
            throws IOException {
        this.CtrlFile.write("off");
        this.CtrlFile.flush();
    }

    public void PowerOffMTDevice()
            throws IOException {
        this.CtrlFile.write("-wdout64 0");
        this.CtrlFile.flush();
    }

    public void PowerOnDevice()
            throws IOException {
        this.CtrlFile.write("on");
        this.CtrlFile.flush();
    }

    public void PowerOnMTDevice()
            throws IOException {
        this.CtrlFile.write("-wdout64 1");
        this.CtrlFile.flush();
    }

    public void TriggerOffDevice()
            throws IOException {
        this.CtrlFile.write("trigoff");
        this.CtrlFile.flush();
    }

    public void TriggerOnDevice()
            throws IOException {
        this.CtrlFile.write("trig");
        this.CtrlFile.flush();
    }
}
