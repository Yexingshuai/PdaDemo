//package com.example.daixiankade.pdademo.service;
//
//import android.serialport.SerialPort;
//import android.util.Log;
//
//import com.traciing.coldchain.transport.collect.TConstans;
//import com.traciing.coldchain.transport.collect.TLog;
//import com.traciing.coldchain.transport.collect.bean.WatchInfo;
//import com.traciing.coldchain.transport.collect.listener.TmpReadListener;
//import com.traciing.coldchain.transport.collect.listener.TmpWriteListener;
//import com.traciing.coldchain.transport.collect.model.BoxModel;
//import com.traciing.coldchain.transport.collect.util.ByteUtils;
//import com.traciing.coldchain.transport.collect.util.CRC16;
//import com.traciing.coldchain.transport.collect.util.DateUtils;
//import com.traciing.coldchain.transport.collect.util.GsonUtils;
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.List;
//public class UartService {
//
//    private static final String TAG = "UartService";
//
//    private static final String SERIAL_PORT = "/dev/ttyMT3";
//    private static final String POWER_PATH = "/sys/class/misc/mtgpio/pin";
//    private static final int BRAUD = 115200;
//    private static final int OPERATION_COUNT = 3;
//
//    private static final int SERIAL_READ_COUNT = 1024 * 10;
//
//    private static final int SYNC_TYPE_READ = 0;
//    private static final int SYNC_TYPE_WRITE = 1;
//    private static final int SYNC_TYPE_STATE = 2;
//    private final Object objLock = new Object();
//
//    private int fd;
//    private SerialPort mSerialPort;
////    private DeviceControl DevCtrl;
//
//    private DecimalFormat df1 = new DecimalFormat("0.00");
//    private boolean isStarted;
//
//    private static final UartService uartService = new UartService();
//
//    public static UartService getInstance() {
//        return uartService;
//    }
//
//    private UartService() {
//        mSerialPort = new SerialPort();
//    }
//
//    /**
//     * @return int
//     */
//    public int openPort() {
//
//        try {
//
//            mSerialPort.OpenSerial(SERIAL_PORT, BRAUD);
//
//            fd = mSerialPort.getFd();
//            Log.d(TAG, "打开串口成功：" + fd);
//
//        } catch (Exception e) {
//            Log.e(TAG, "打开串口异常，尝试关闭重启！", e);
//            return fd = -1;
//        }
//
//        try {
//
////            DevCtrl = new DeviceControl(POWER_PATH);
////            DevCtrl.PowerOnDevice();
//
//            Log.d(TAG, "上电成功！" + fd);
//
//            return fd;
//        } catch (Exception e) {
//            Log.e(TAG, "上电异常：" + e.getMessage(), e);
//
//            try {
//                if (mSerialPort != null)
//                    mSerialPort.CloseSerial(fd);
//            } catch (Exception ee) {
//                Log.e(TAG, "关闭串口异常：" + ee.getMessage(), ee);
//            }
//
//        }
//        return fd = -1;
//    }
//
//    /**
//     *
//     */
//    public void closePort() {
//        try {
//            mSerialPort.CloseSerial(fd);
//            fd = 0;
//
//            Log.d(TAG, "关闭串口成功：" + fd);
//        } catch (Exception e) {
//            Log.e(TAG, "关闭串口异常：" + e.getMessage(), e);
//        }
//
//        try {
//            DevCtrl.PowerOffDevice();
//            Log.d(TAG, "下电成功！");
//        } catch (Exception e) {
//            Log.e(TAG, "下电异常：" + e.getMessage(), e);
//        }
//    }
//
//    public boolean isOpen() {
//        return fd > 0;
//    }
//
//    public boolean isStarted() {
//        return isStarted;
//    }
//
//    public boolean makeSureTimeZigbeeTry() {
//
//        for (int i = 0; i < OPERATION_COUNT; i++) {
//
//            if (makeSureTimeZigbee())
//                return true;
//
//            Log.d(TAG, "校时失败，" + (i + 1) + " 次，重新尝试！");
//
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 启动模块并且设定时钟 2016.8.29 carl
//     * <p>
//     * 数值       描述
//     * 1
//     * 0x55     模块请求帧标志
//     * 0x55
//     * 2
//     * 0x09     长度低字节 3-5 项的字节数
//     * 0x00     长度高字节
//     * 3
//     * 0x84     命令码（启动模块并且设定时钟）
//     * 4
//     * 0x00     秒
//     * 0x00     分
//     * 0x00     时
//     * 0x00     日
//     * 0x00     月
//     * 0x00     年
//     * 5
//     * 0x00 CRC16 低字节
//     * 0x00 CRC16 高字节
//     * <p> =============================
//     * 数值 描述
//     * 1
//     * 0xAA     模块返回帧标志
//     * 0xAA
//     * 2
//     * 0x04     长度低字节 3-5 项的
//     * 0x00     长度低字节
//     * 3
//     * 0x84     命令码（启动模块和设定时钟
//     * 4
//     * 0x01     0 失败,1 成功
//     * 5
//     * 88       CRC16 低字节
//     * AD       CRC16 高字节
//     *
//     * @return boolean
//     */
//    public boolean makeSureTimeZigbee() {
//
//        Calendar c = DateUtils.getNowCalendar();
//
//        byte[] order = new byte[13];
//        order[0] = (byte) 0x55;
//        order[1] = (byte) 0x55;
//        order[2] = (byte) 0x09;
//        order[3] = (byte) 0x00;
//        order[4] = (byte) 0x84;
//        order[5] = ((byte) c.get(Calendar.SECOND));
//        order[6] = (byte) c.get(Calendar.MINUTE);
//        order[7] = (byte) c.get(Calendar.HOUR_OF_DAY);
//        order[8] = (byte) c.get(Calendar.DATE);
//        order[9] = (byte) (c.get(Calendar.MONTH) + 1);
//        order[10] = (byte) (c.get(Calendar.YEAR) - 2000);
//
//        // 设置校验码 order 预留2字节 校验码使用
//        CRC16.builderCrcData(order);
//
//        try {
//            log(order, "时间校准发送数据");
//
//            mSerialPort.WriteSerialByte(fd, order);
//
//            byte[] readBytes = mSerialPort.ReadSerial(fd, SERIAL_READ_COUNT);
//
//            if (verifyCrcData(readBytes)) {
//                log(readBytes, "时间校准返回包通过 " + DateUtils.calendarToString(c));
//                return isStarted = true;
//            }
//
//            log(readBytes, "时间校准返回包未通过");
//            return false;
//        } catch (Exception e) {
//            Log.e(TAG, "时间校准指令出现异常:" + e.getMessage(), e);
//        }
//
//        return false;
//    }
//
//    /**
//     * 接收模块停止工作
//     * 数值 描述
//     * 1
//     * 0x55     模块请求帧标志
//     * 0x55
//     * 2
//     * 0x03     长度低字节 3-4 项的字节数
//     * 0x00     长度高字节
//     * 3
//     * 0x85     命令码（停止模块工作）
//     * 4
//     * 0x00     CRC16 低字节
//     * 0x00     CRC16 高字节
//     * <p>
//     * <p> =============================
//     * 数值       描述
//     * 0xAA     模块返回帧标志
//     * 0xAA
//     * 0x04     长度低字节  3-5 项的字节数
//     * 0x00     长度低字节
//     * 0x85     命令码（停止模块工作）
//     * 0x01     0 失败,1 成功
//     * 88       CRC16 低字节
//     * AD       CRC16 高字节
//     *
//     * @return boolean
//     */
//    public boolean stopReadZigbee() {
//
//        byte[] order = new byte[7];
//        order[0] = (byte) 0x55;
//        order[1] = (byte) 0x55;
//        order[2] = (byte) 0x03;
//        order[3] = (byte) 0x00;
//        order[4] = (byte) 0x85;
//
//        // 设置校验码 order 预留2字节 校验码使用
//        CRC16.builderCrcData(order);
//
//        try {
//
//            log(order, "模块停止发送数据");
//
//            mSerialPort.WriteSerialByte(fd, order);
//
//            byte[] readBytes = mSerialPort.ReadSerial(fd, SERIAL_READ_COUNT);
//
//            if (verifyCrcData(readBytes)) {
//                log(readBytes, "模块停止返回包通过");
//                isStarted = false;
//                return true;
//            }
//
//            log(readBytes, "模块停止返回包未通过");
//        } catch (Exception e) {
//            Log.e(TAG, "模块停止指令出现异常:" + e.getMessage(), e);
//        }
//
//        return false;
//    }
//
//    public void stopReadZigbeeTry() {
//
//        taskTry(new OperationUarTaskTry() {
//            @Override
//            public boolean taskTry() {
//                return stopReadZigbee();
//            }
//        });
//    }
//
//    /**
//     * 读取当前通道状态
//     */
//    public void readTemNumAndStatus() {
//        byte[] order = new byte[7];
//        order[0] = (byte) 0x55;
//        order[1] = (byte) 0x55;
//        order[2] = (byte) 0x03;
//        order[3] = (byte) 0x00;
//        order[4] = (byte) 0x83;
//
//        // 设置校验码 order 预留2字节 校验码使用
//        CRC16.builderCrcData(order);
//
//        try {
//
//            log(order, "读取当前设置发送数据");
//
//            mSerialPort.WriteSerialByte(fd, order);
//
//            byte[] readBytes = mSerialPort.ReadSerial(fd, SERIAL_READ_COUNT);
//
//            if (!verifyCrcData(readBytes)) {
//                log(readBytes, "读取当前设置返回包未通过");
//                return;
//            }
//
//            Log.d(TAG, "读取当前设置返回包通过!");
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
//                Log.d(TAG, "传感器ID：" + temID + "返回状态:" + currentMes);
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "读取设置指令出现异常:" + e.getMessage(), e);
//        }
//    }
//
//    private boolean wirteTemNumAndStatusTry(List<BoxModel> boxes, TmpWriteListener listener) {
//        for (int i = 0; i < OPERATION_COUNT; i++) {
//
//            if (wirteTemNumAndStatus(boxes, listener))
//                return true;
//
//            Log.d(TAG, "写通道失败，" + (i + 1) + " 次，重新尝试！");
//
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 写通道表
//     * 0 标准链接，1 快速链接，2 历史记录，3 重新启动。
//     * 序号   数值  描述
//     * 1
//     * 0x55     模块请求帧标志
//     * 0x55
//     * 2
//     * 0x07     长度低字节 3-N 项的字节数
//     * 0x00     长度高字节
//     * 3
//     * 0x82     命令码（写通道表）
//     * 4
//     * 0x90     传感器 ID
//     * 0x01
//     * 0x00     操作码：（取值范围：0,1,2,3） 0 标准链接，1 快速链接，2 历史记录，3 重新启动。
//     * 0x00     接收信号强度 RSSI 值，发送时不使用=0。
//     * 0x00     Value[0]
//     * 0x00     Value[1]
//     * 0x00     Value[2]
//     * 0x00     Value[3]
//     * 每个通道占用 8 字节，重复。
//     * N
//     * 88 CRC16 低字节
//     * AD CRC16 高字节
//     * <p>============================
//     * 1
//     * 0xAA     模块返回帧标志
//     * 0xAA
//     * 2
//     * 0x04     长度低字节 3-5 项的字节数
//     * 0x00     长度低字节
//     * 3
//     * 0x82     命令码（写通道表）
//     * 4
//     * 0x01     0 失败,1 成功
//     * 5
//     * 88       CRC16 低字节
//     * AD       CRC16 高字节
//     *
//     * @param boxes    boxes
//     * @param listener listener
//     * @return boolean
//     */
//    private boolean wirteTemNumAndStatus(List<BoxModel> boxes, TmpWriteListener listener) {
//
//        int temNumCount = boxes.size();        //用来存放当前已经录入当前车号的表号的数量
//        int byteNum = temNumCount * 8 + 3;
//
//        int begin = 0;
//
//        Log.d(TAG, "通道设置参数计算:temNumCount:" + temNumCount + ";byteNum:" + byteNum);
//
//        byte[] order = new byte[temNumCount * 8 + 7];
//
//        order[begin++] = 0x55;
//        order[begin++] = 0x55;
//        order[begin++] = ByteUtils.low8Bit(byteNum);
//        order[begin++] = ByteUtils.high8Bit(byteNum);
//        order[begin++] = (byte) 0x82;
//
//        for (int i = 0; i < temNumCount; i++) {
//
//            BoxModel box = boxes.get(i);
//            int transducerNo = Integer.parseInt(box.getTransducerNo());
//            byte controlValue = 0x00;
//
//            if (box.getIsReset() == TConstans.BOX_ISREST_NEED)
//                controlValue = 0x03;
//
//            order[begin++] = ByteUtils.low8Bit(transducerNo);
//            order[begin++] = ByteUtils.high8Bit(transducerNo);
//            order[begin++] = controlValue;
//            order[begin++] = (byte) 0x00;
//            order[begin++] = (byte) 0x00;
//            order[begin++] = (byte) 0x00;
//            order[begin++] = (byte) 0x00;
//            order[begin++] = (byte) 0x00;
//
//            if (listener != null)
//                listener.onMessage(box);
//        }
//
//        // 设置校验码
//        CRC16.builderCrcData(order);
//
//        try {
//            log(order, "下发通道表发送数据");
//
//            mSerialPort.WriteSerialByte(fd, order);
//
//            byte[] readBytes = mSerialPort.ReadSerial(fd, SERIAL_READ_COUNT);
//
//            if (verifyCrcData(readBytes)) {
//                log(readBytes, "下发通道表返回包通过");
//                return true;
//            }
//
//            log(readBytes, "下发通道表返回包未通过");
//        } catch (Exception e) {
//            Log.e(TAG, "下发通道指令出现异常:" + e.getMessage(), e);
//        }
//
//        return false;
//
//    }
//
//    public boolean clearTemtureZigbeeTry() {
//
//        for (int i = 0; i < OPERATION_COUNT; i++) {
//
//            if (clearTemtureZigbee())
//                return true;
//
//            Log.d(TAG, "清空读取之前的数据，" + (i + 1) + " 次，重新尝试！");
//
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 清空读取之前的数据
//     *
//     * @return boolean
//     */
//    public boolean clearTemtureZigbee() {
//
//        byte[] order = new byte[7];
//        order[0] = (byte) 0x55;
//        order[1] = (byte) 0x55;
//        order[2] = (byte) 0x03;
//        order[3] = (byte) 0x00;
//        order[4] = (byte) 0x82;
//
//        // 设置校验码 order 预留2字节 校验码使用
//        CRC16.builderCrcData(order);
//
//        try {
//            log(order, "清空指令发送数据");
//
//            mSerialPort.WriteSerialByte(fd, order);
//
//            byte[] readBytes = mSerialPort.ReadSerial(fd, SERIAL_READ_COUNT);
//
//            if (verifyCrcData(readBytes)) {
//                log(readBytes, "清空指令返回包通过");
//                return true;
//            }
//
//            log(readBytes, "清空指令返回包未通过");
//        } catch (Exception e) {
//            Log.e(TAG, "清空指令出现异常:" + e.getMessage(), e);
//        }
//
//        return false;
//    }
//
//    /**
//     * 读取记录
//     *
//     * @param listener listener
//     */
//    private void readTemptureZigbee(TmpReadListener listener) {
//
//        byte[] order = new byte[7];
//        order[0] = (byte) 0x55;
//        order[1] = (byte) 0x55;
//        order[2] = (byte) 0x03;
//        order[3] = (byte) 0x00;
//        order[4] = (byte) 0x81;
//
//        // 设置校验码 order 预留2字节 校验码使用
//        CRC16.builderCrcData(order);
//
//        try {
//            // log(order, "读取记录发送数据");
//
//            mSerialPort.WriteSerialByte(fd, order);
//
//            byte[] readBytes = mSerialPort.ReadSerial(fd, SERIAL_READ_COUNT);
//            log(readBytes, "读取记录返回包数据");
//
//            if (!CRC16.verifyCrcData(readBytes)) {
//                Log.d(TAG, "读取记录返回包校验未通过");
//                return;
//            }
//
//            // 主机确认包：（接收数据正确，发送数据到模块）
//            byte[] orderSure = new byte[7];
//            orderSure[0] = (byte) 0x55;
//            orderSure[1] = (byte) 0x55;
//            orderSure[2] = (byte) 0x03;
//            orderSure[3] = (byte) 0x00;
//            orderSure[4] = (byte) 0x80;
//
//            // 设置校验码 orderSure 预留2字节 校验码使用
//            CRC16.builderCrcData(orderSure);
//
//            mSerialPort.WriteSerialByte(fd, orderSure);
//
//            int bodyLen = readBytes.length - 7;
//
//            if (bodyLen == 0)
//                return;
//
//            // debug(count, listener);
//            // aa aa 0f 00 81 (03 00 9e 01 ff 1c b4 2c 16 0c 07 11) 12 b7
//
//            for (int i = 5; i < bodyLen; i += 12) {
//
//                int temID = ByteUtils.bytesToInt(readBytes, i, 2);// 0, 1
//                double temValue = ByteUtils.bytesToDouble(readBytes, i + 2, 2);// 2, 3
//                int ssi = ByteUtils.bytesToInt(readBytes, i + 4, 1);
//
//                final double x = (ssi * 1.0 / 255 * 100 + 0.0) / 100 * 100;
//                double powerdata = ByteUtils.bytesToInt(readBytes, i + 5, 1) * 0.1;
//
//                //大于60秒为实时数据，小于60为历史数据，字节 做 ‘与’7f运算可得到真实值
//                int second = ByteUtils.bytesToInt(readBytes, i + 6, 1);
//                int min = ByteUtils.bytesToInt(readBytes, i + 7, 1);
//                int hour = ByteUtils.bytesToInt(readBytes, i + 8, 1);
//                int day = ByteUtils.bytesToInt(readBytes, i + 9, 1);
//                int month = ByteUtils.bytesToInt(readBytes, i + 10, 1);
//                int year = ByteUtils.bytesToInt(readBytes, i + 11, 1);
//
//                String time_receive = "20" + year + "-" + ByteUtils.addZero(month) + "-" + ByteUtils.addZero(day)
//                        + " " + ByteUtils.addZero(hour) + ":" + ByteUtils.addZero(min) + ":00";
//
//                WatchInfo watchInfo = new WatchInfo();
//                watchInfo.setTransducerNo(String.valueOf(temID));
//                watchInfo.setValue(Double.parseDouble(df1.format(temValue)));
//                watchInfo.setSignal(Double.parseDouble(df1.format(x)));
//                watchInfo.setElectricPower(Double.parseDouble(df1.format(powerdata)));
//                watchInfo.setAccessTime(time_receive);
//                watchInfo.setIsHistory(second);
//
//                Log.d(TAG, "处理前的数据：" + GsonUtils.toJson(watchInfo));
//
//                listener.onMessage(watchInfo);
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "读取数据出现异常:" + e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 防止读写并发几种解决策略，1：对串口的操作使用队列消息来防止并发，2：对串口操作（仅读写）使用锁来防止并发，3：使用共享变量控制
//     * <p>
//     * 读的场景是1秒一次采集数据，
//     * 写的场景是巡检，提交订单，
//     * 考虑到读写并发场景微小，但是同一个串口又不能同时操作，所以采用锁机制
//     *
//     * @param type          type
//     * @param readListener  readListener
//     * @param boxes         boxes
//     * @param writeListener writeListener
//     * @return boolean
//     */
//    public boolean syncReadWriteTemptureZigbee(int type, TmpReadListener readListener, List<BoxModel> boxes, TmpWriteListener writeListener) {
//
//        boolean isSuccess = false;
//
//        synchronized (objLock) {
//
//            if (!isOpen())
//                openPort();
//
//            if (type == SYNC_TYPE_READ) {
//
//                if (!isStarted) // 判断是否需要启动并校时模块
//                    makeSureTimeZigbeeTry();
//
//                readTemptureZigbee(readListener);
//            } else if (type == SYNC_TYPE_WRITE) {
//
//                stopReadZigbeeTry();//每次写通道前需要停止采集模块
//
//                isSuccess = wirteTemNumAndStatusTry(boxes, writeListener);
//            } else
//                Log.d(TAG, "syncReadWriteTemptureZigbee type " + type);
//
//        }
//
//        return isSuccess;
//    }
//
//    public void syncReadTemptureZigbee(TmpReadListener readListener) {
//        syncReadWriteTemptureZigbee(SYNC_TYPE_READ, readListener, null, null);
//    }
//
//    public boolean syncWriteTemptureZigbeeTry(List<BoxModel> boxes, TmpWriteListener writeListener) {
//        return syncReadWriteTemptureZigbee(SYNC_TYPE_WRITE, null, boxes, writeListener);
//    }
//
//    /**
//     * 命令返回判断
//     *
//     * @param src byte[]
//     * @return boolean
//     */
//    private static boolean verifyCrcData(byte[] src) {
//        return CRC16.verifyCrcData(src) && src[5] == 1;
//    }
//
//    /**
//     * 日志
//     *
//     * @param datas datas
//     * @param msg   msg
//     */
//    private static void log(byte[] datas, String msg) {
//
//        if (datas == null) {
//            Log.d(TAG, "datas is null " + msg);
//            return;
//        }
//
//        StringBuilder sBuffer = new StringBuilder();
//
//        sBuffer.append(msg).append("长度为：").append(datas.length).append("  ");
//        sBuffer.append("数据为：");
//
//        for (Byte data : datas)
//            sBuffer.append(ByteUtils.toHexString(data)).append(" ");
//
//        TLog.d(TAG, sBuffer.toString());
//    }
//
//    private void debug(int count, TmpReadListener listener) {
//
//        DecimalFormat df1 = new DecimalFormat("0.00");
//
//        WatchInfo watchInfo = new WatchInfo();
//        watchInfo.setTransducerNo(String.valueOf(1));
//        watchInfo.setValue(Double.parseDouble(df1.format(33.3)));
//        watchInfo.setSignal(Double.parseDouble(df1.format(33.3)));
//        watchInfo.setElectricPower(Double.parseDouble(df1.format(12.1)));
//        watchInfo.setAccessTime(DateUtils.getCurrentTime());
//
//        Log.d(TAG, "debug 处理前的数据：" + GsonUtils.toJson(watchInfo));
//
//        listener.onMessage(watchInfo);
//
//        watchInfo = new WatchInfo();
//        watchInfo.setTransducerNo(String.valueOf(2));
//        watchInfo.setValue(Double.parseDouble(df1.format(33.3)));
//        watchInfo.setSignal(Double.parseDouble(df1.format(33.3)));
//        watchInfo.setElectricPower(Double.parseDouble(df1.format(12.1)));
//        watchInfo.setAccessTime(DateUtils.getCurrentTime());
//
//        Log.d(TAG, "debug 处理前的数据：" + GsonUtils.toJson(watchInfo));
//
//        listener.onMessage(watchInfo);
//    }
//
//    private boolean taskTry(OperationUarTaskTry taskTry) {
//        for (int i = 0; i < OPERATION_COUNT; i++) {
//
//            if (taskTry.taskTry())
//                return true;
//
//            Log.d(TAG, "操作失败，" + (i + 1) + " 次，重新尝试！");
//
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    private interface OperationUarTaskTry {
//        public boolean taskTry();
//    }
//}
