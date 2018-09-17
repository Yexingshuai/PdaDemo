package com.example.daixiankade.pdademo.util;

import android.content.Context;
import android.widget.Toast;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import rego.printlib.export.regoPrinter;


public class Print {
    public Context mContext;

    private final static String TAG = "Print";
    private final static String SERIAL_PORT = "/dev/ttyG1";
    //	private final static String POWER_PATH = "/proc/geomobile/lf";
    private final static String POWER_PATH = "/sys/class/misc/mtgpio/pin";
    //    private final static String POWER_PATH = "/proc/driver/as3992";
    private final static int BAUDRATE = 115200;

    // <item>1D UPC-A</item>
    // <item>1D UPC-E</item>
    // <item>1D EAN-13</item>
    // <item>1D EAN-8</item>
    // <item>1D CODE39</item>
    // <item>1D ITF</item>
    // <item>1D CODABAR</item>
    // <item>1D CODE93</item>
    // <item>1D CODE 128</item>
    public static final int _CODE_UPC_A = 0;
    public static final int _CODE_UPC_E = 1;
    public static final int _CODE_EAN13 = 2;
    public static final int _CODE_EAN8 = 3;
    public static final int _CODE_CODE_39 = 4;
    public static final int _CODE_ITF = 5;
    public static final int _CODE_CODABAR = 6;
    public static final int _CODE_93 = 7;
    public static final int _CODE_128 = 8;

    private static Print printer;

    private regoPrinter rgprinter;
    public int state;

    public void connect(String port) {

//		context = (ApplicationContext) mContext.getApplicationContext();
//		context.setObject();
        rgprinter = new regoPrinter(mContext.getApplicationContext());
        try {
            DevCtrl = new DeviceControl();
            DevCtrl.PowerOnDevice();
//            DevCtrl.PrintPowerOnDevice();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		state =rgprinter.CON_ConnectDevices("RG-488",
//				"/dev/ttyG1:9600", 200);
        state = rgprinter.CON_ConnectDevices("RG-E48",
                "/dev/ttyMT1:115200", 200);
        if (state > 0) {
            Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            printer_fd = state;
        } else {
            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    private Print(Context context) {
        System.out.println("开启串口");
        mContext = context;
        connect("/dev/ttyG1:9600");
        System.out.println("开启11串口");
    }


    public static Print InstancePrinter(Context context) {
        if (printer == null) {
            printer = new Print(context);
            System.out.println("new printer");
        }
        return printer;
    }

    private int printer_fd;
    private DeviceControl DevCtrl;

    private int flog = 0;


    public void printString(String data) {
        byte[] cmd;
        try {
            cmd = data.getBytes("GBK");
            rgprinter.ASCII_PrintBuffer(printer_fd, cmd,
                    cmd.length);
            rgprinter.ASCII_PrintBuffer(printer_fd,
                    new byte[]{0x0a}, 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void closePrint() {
//        powerOffPrint();
        printer = null;
        // try {
        // DevCtrl.PowerOffDevice();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

//    public void powerOffPrint() {
//        try {
//            DevCtrl.PrintPowerOffDevice();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public void powerOnPrint() {
        try {
            DevCtrl.PowerOnDevice();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String bystesToStr(byte[] s, int start, int len) {
        // CLogger.Write(s.Length + " start:" + start + " len:" + len);
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + len; i++) {
            sb.append(String.format("%X ", s[i] & 0xff));
            sb.append(" ");
        }
        return sb.toString();
    }

//    private PreDefiniation.TransferMode printmode = PreDefiniation.TransferMode.TM_NONE;

    public void printBitMap(final InputStream bitmaptwo) {
        // SetLateralLongitudinalMagnification(1);
        // ResetFactory();
//		MyBitmap.printBitmap(serialPort, printer_fd);

        rgprinter.CON_PageStart(printer_fd, true, 600, 350);
        int k = rgprinter.DRAW_PrintPicture(printer_fd, bitmaptwo, 0,
                0, 350, 220);
//        rgprinter.CON_PageEnd(printer_fd,
//                printmode.getValue());
//		ResetFactory();
    }

    public void setPrintOverTurn(byte state) {
        // 1B 06 1B 63 01 1B 16
        byte[] printdata = {0x1b, 0x06, 0x1b, 0x63, state, 0x1b, 0x16};
        rgprinter.ASCII_PrintBuffer(printer_fd, printdata,
                printdata.length);
    }

    public void printQRC(byte[] data, int multiple) {
        printQRCMultiple(multiple);
        int len = data.length + 5;
        byte[] printdata = new byte[len];
        printdata[0] = 0x1d;
        printdata[1] = 0x6b;
        printdata[2] = 0x4c;
        printdata[3] = (byte) data.length;
        printdata[len - 1] = 00;
        System.arraycopy(data, 0, printdata, 4, data.length);

        // serialPort.WriteSerialByte(printer_fd, printdata);
        rgprinter.ASCII_PrintBuffer(printer_fd, printdata,
                printdata.length);
        String ss = "";
        for (int i = 0; i < printdata.length; i++) {
            ss += printdata[i] + " ";
        }
        System.out.println("qrc_data" + ss);
        // serialPort.WriteSerialByte(printer_fd, new byte[] { 0x00 });
        // serialPort.WriteSerialByte(printer_fd, new byte[] { 0x0a });
    }

    private void printQRCMultiple(int m) {
        byte[] multiple = new byte[8];
        multiple[0] = 0x1d;
        multiple[1] = 0x28;
        multiple[2] = 0x6b;
        multiple[3] = 0x03;
        multiple[4] = 0x00;
        multiple[5] = 0x31;
        multiple[6] = 0x43;
        multiple[7] = (byte) m;
        // serialPort.WriteSerialByte(printer_fd, multiple);
        rgprinter.ASCII_PrintBuffer(printer_fd, multiple,
                multiple.length);
        ResetFactory();
    }

    private void ResetFactory() {
        // serialPort.WriteSerialByte(printer_fd, new byte[] { 0x1b, 0x40 });
        rgprinter.ASCII_PrintBuffer(printer_fd,
                new byte[]{0x1b, 0x40}, 2);
    }
}
