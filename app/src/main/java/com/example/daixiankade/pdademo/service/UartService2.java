package com.example.daixiankade.pdademo.service;


import android.util.Log;

import com.example.daixiankade.pdademo.util.DeviceControl;

import java.util.Calendar;

import android_serialport_api.SerialPort;

/**
 * Created by yexing on 2018/9/6.
 */

public class UartService2 {

    public static final String TAG = "UartService---------";
    private static final String SERIAL_PORT = "/dev/ttyMT3";
    private static final String POWER_PATH = "/sys/class/misc/mtgpio/pin";

    private static final UartService2 uartService = new UartService2();

    private static final int BRAUD = 115200;

    private SerialPort mSerialPort;

    private DeviceControl DevCtrl;

    private int fd;

    public static UartService2 getInstance() {
        return uartService;
    }

    public UartService2() {
        mSerialPort = new SerialPort();
    }

    public int openPort() {

        try {

            mSerialPort.OpenSerial(SERIAL_PORT, BRAUD);

            fd = mSerialPort.getFd();
//            TLog.d(TAG, "打开串口成功：" + fd);
            Log.e(TAG, "串口打开成功---");

        } catch (Exception e) {
//            TLog.e(TAG, "打开串口异常，尝试关闭重启！", e);
            Log.e(TAG, "串口打开失败，尝试关闭重启---");
            return fd = -1;
        }

        try {

            DevCtrl = new DeviceControl();
            DevCtrl.PowerOnDevice();

//            TLog.d(TAG, "上电成功！" + fd);
            Log.e(TAG, "上电成功---");

            return fd;
        } catch (Exception e) {
//            TLog.e(TAG, "上电异常：" + e.getMessage(), e);

            try {
                if (mSerialPort != null)
                    mSerialPort.CloseSerial(fd);
            } catch (Exception ee) {
//                TLog.e(TAG, "关闭串口异常：" + ee.getMessage(), ee);
                Log.e(TAG, "关闭串口异常---");
            }

        }
        return fd = -1;
    }

}
