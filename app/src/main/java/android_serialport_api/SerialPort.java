package android_serialport_api;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

import com.example.daixiankade.pdademo.MainActivity;
import com.example.daixiankade.pdademo.MyApp;

public class SerialPort {
    private static final String TAG = "SerialPort";

    /*
    * Do not remove or rename the field mFd: it is used by native method
    * close();
    */
    private int fdx = -1;

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private static Context context;

    public SerialPort() {
        // openport_easy(dev,brd);
    }

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        System.out.println("device======" + device.getAbsolutePath());
        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        //开启串口，传入物理地址、波特率、flags值
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        } else {
            Log.e("mfd---------------",mFd+"");
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public SerialPort(Context context) {
        this.context = context;
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    public void CloseSerial(int fd) {
        closeport(fd);
    }

    //-------------------记录仪

    public int WriteSerialByte(int fd, byte[] str) {
//        return writeport(fd, str);
        return write(fd,str);
    }


    public byte[] ReadSerial(int fd, int len) throws Exception {
        return readport(fd, len, 50);
    }

    /**
     * 打开串口
     * native int openport(String port,int brd,int bit,int stop,int crc)
     *
     * @param port String
     * @param brd  int
     * @throws Exception Exception
     */
    public void OpenSerial(String port, int brd) throws Exception {

        fdx = openport_easy(port, brd);
//        fdx = nopen_esay(port, brd);

        if (fdx < 0)
            throw new Exception("native open returns null");
    }

    public int getFd() {
        return fdx;
    }


    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    private native void closeport(int fd);

//    private native int writeport(int fd, byte[] buf);
    private native int write(int fd, byte[] buf);

    private native byte[] readport(int fd, int count, int delay);

    // JNI
    private native int openport_easy(String port, int brd);
//    private native int nopen_esay(String port, int brd);

    static {


//        System.load("/storage/emulated/0/libserial_port.so");
//        System.load("/data/app-lib/com.example.daixiankade.pdademo/armeabi/libserial_port.so");

     //serial_port
        System.loadLibrary("serial_port");
//        System.loadLibrary("serial_port");
//        System.load("libserial_port");
//        String s=MyApp.getContext().getApplicationInfo().nativeLibraryDir;

//        System.load(MyApp.getContext().getApplicationInfo().nativeLibraryDir+"/libserial_port.so");


//        File dir = MyApp.getContext().getDir("jniLibs", Context.MODE_PRIVATE);
//        File[] currFiles = null;
//        currFiles = dir.listFiles();
//        for (int i = 0; i < currFiles.length; i++) {
//            Log.e("qqqqq",currFiles[i].getAbsolutePath());
//            System.load(currFiles[i].getAbsolutePath());
//        }

    }
}
