package com.example.daixiankade.pdademo.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by daixiankade on 2018/9/5.
 */

public class SerialPortUtil {

    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private ReceiveThread mReceiveThread = null;
    private boolean isStart = false;


    //打开串口
    public void openSerialPort() {
        try {     ///dev/ttyMT3   原本：/dev/ttyS0
            serialPort = new SerialPort(new File("/dev/ttyMT3"), 9600, 0);
            //调用对象SerialPort方法，获取串口中"读和写"的数据流

            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            isStart = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
//        getSerialPort();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public SerialPort getSerialPort() {
//        if (mReceiveThread == null) {
//
//            mReceiveThread = new ReceiveThread();
//        }
//        mReceiveThread.start();
        return serialPort;
    }


    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isStart) {
                if (inputStream == null) {
                    return;
                }
                byte[] readData = new byte[1024];
                try {
                    int size = inputStream.read(readData);
                    if (size > 0) {
//                        String readString = DataUtils.ByteArrToHex(readData, 0, size);
//                        EventBus.getDefault().post(readString);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
