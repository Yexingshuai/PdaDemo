package com.example.daixiankade.pdademo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daixiankade.pdademo.service.DataUtils;
import com.example.daixiankade.pdademo.service.UartService2;
import com.example.daixiankade.pdademo.util.ByteUtils;
import com.example.daixiankade.pdademo.util.CRC16;
import com.example.daixiankade.pdademo.util.Print;
import com.example.daixiankade.pdademo.util.RootUtil;
import com.example.daixiankade.pdademo.util.SerialPortUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;
import rego.printlib.export.regoPrinter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Print print;
    private EditText et;

    private int fd;
    private static final int SERIAL_READ_COUNT = 1024 * 10;

    private static final String SERIAL_PORT = "/dev/ttyMT3";
    private static final int BRAUD = 115200;
    private SerialPort serialPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.et);
        Button bt = findViewById(R.id.bt);
        bt.setOnClickListener(this);
//        SerialPortFinder serialPortFinder = new SerialPortFinder();
//        String[] allDevices = serialPortFinder.getAllDevices();
//        SerialPortUtil portUtil = new SerialPortUtil();
//        portUtil.openSerialPort();

        try {
//            serialPort = new SerialPort(new File("/dev/ttyMT3"), 115200, 0);
//            serialPort.WriteSerialByte(fd, readTemNumAndStatusaa());

            SerialPort serialPort = new SerialPort(this);
//            serialPort.OpenSerial(SERIAL_PORT, BRAUD);
//            File dir = Environment.getExternalStorageDirectory();
//            File file = new File("/data/app-lib/com.example.daixiankade.pdademo-2");
//            String absolutePath = file.getAbsolutePath();
//            String absolutePath = dir.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        //温度记录仪----
//        UartService2 uartService = UartService2.getInstance();
//        uartService.openPort();


//        try {
//
//            SerialPort serialPort = new SerialPort();
//            serialPort.OpenSerial(SERIAL_PORT, BRAUD);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        if (RootUtil.isDeviceRooted()) {
//            Log.d("wwww", "onCreate: 你的设备可以获取root");
//        } else {
//            Log.d("www", "onCreate: 你的设备可以获取不能获取root");
//        }

//        SerialPortUtil serialPortUtil = new SerialPortUtil();
//        serialPortUtil.openSerialPort();
//        serialPort1 = serialPortUtil.getSerialPort();
//        fd = serialPort1.getFd();
//        OutputStream outputStream = serialPortUtil.getOutputStream();
//
//        readTemNumAndStatus();

//        SerialPort serialPort = new SerialPort();
//        try {
//            serialPort.OpenSerial(SERIAL_PORT, BRAUD);
//            int fd = serialPort.getFd();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //打印功能
//        byte[] cmd = null;
//        regoPrinter printer = new regoPrinter(this);
//        print = Print.InstancePrinter(this);
//        print.printString("fdsafd4444444444444444444444444444444444444444sfdsfsdf");


    }

    private void readTemNumAndStatus() {
        byte[] order = new byte[7];
        order[0] = (byte) 0x55;
        order[1] = (byte) 0x55;
        order[2] = (byte) 0x03;
        order[3] = (byte) 0x00;
        order[4] = (byte) 0x83;

        // 设置校验码 order 预留2字节 校验码使用
        CRC16.builderCrcData(order);

        try {

//            log(order, "读取当前设置发送数据");
            Log.e("----aaa", "读取当前设置发送数据");

//            serialPort.WriteSerialByte(fd, order);
            serialPort.getOutputStream().write(order);
            byte[] bytes = new byte[64];
            int a = serialPort.getInputStream().read(bytes);
            Log.e("----aaa", "sdfsdaf-----" + a);

            if (a > 0) {
                Log.e("----aaa", "sdfsdaf-----" + a);
            } else {
                Log.e("---aaaa-aaa", "sdfsdaf-----" + a);
            }
//            byte[] readBytes = serialPort.ReadSerial(fd, SERIAL_READ_COUNT);


//            if (!verifyCrcData(readBytes)) {
////                log(readBytes, "读取当前设置返回包未通过");
//                return;
//            }
//
////            TLog.d(TAG, "读取当前设置返回包通过!");
//
//            int count = (readBytes.length - 7) / 4;
//            for (int i = 0; i < count; i++) {
//
//                int start = i * 4 + 5;
//                int temID = ByteUtils.bytesToInt(readBytes, start, 2);
//
//                start += 2;
//                int statu_value = ByteUtils.bytesToInt(readBytes, start, 1);
//
//                String currentMes = statu_value == 0 ? "成功" : "失败";
//                Log.d("qqqsdsa", "传感器ID：" + temID + "返回状态:" + currentMes);
//            }

        } catch (Exception e) {
            Log.e("qqqsdsa", "读取设置指令出现异常:" + e.getMessage(), e);
        }
    }

    /**
     * 命令返回判断
     *
     * @param src byte[]
     * @return boolean
     */
    private static boolean verifyCrcData(byte[] src) {
        return CRC16.verifyCrcData(src) && src[5] == 1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt:
                String s = et.getHint().toString();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                print.printString(s);
//                print.printString(et.getText().toString().trim());
                break;
        }
    }

    /**
     * 读取当前通道状态
     */
    public byte[] readTemNumAndStatusaa() {
        byte[] order = new byte[7];
        order[0] = (byte) 0x55;
        order[1] = (byte) 0x55;
        order[2] = (byte) 0x03;
        order[3] = (byte) 0x00;
        order[4] = (byte) 0x83;


        // 设置校验码 order 预留2字节 校验码使用
        CRC16.builderCrcData(order);
        return order;
    }


}
