/*
 * Confidential property of Traciing.
 * Copyright 2017 Jianggq jgq2012@gmail.com
 *
 * Traciing. All rights reserved.
 *
 * Unpublished rights reserved under Chinese copyright laws.
 *
 * This software contains confidential and trade secret information of
 * Traciing. Use, duplication or disclosure of the software and
 * documentation by any individual, organization or government must be
 * granted written agreement from Traciing.
 *
 * Traciing. BeiJing China http://www.traciing.com
 */

package com.example.daixiankade.pdademo.util;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;


/**
 * ByteUtils Created by Jianggq on 2017/7/5 9:55.
 * <p>
 * 对于正数（00000001）原码来说，首位表示符号位，反码 补码都是本身
 * 对于负数（100000001）原码来说，反码是对原码除了符号位之外作取反运算即（111111110），补码是对反码作+1运算即（111111111）
 * <p>
 * “~”运算符在c、c++、Java、c#中都有，要弄懂这个运算符的计算方法，首先必须明白二进制数在内存中的存放形式，二进制数在内存中是以补码的形式存放的。
 * 另外正数和负数的补码不一样，正数的补码，反码都是其本身，既：
 * <p>
 * 正数9（二进制为：1001）在内存中存储为01001，必须补上符号位（开头的0为符号位）。
 * <p>
 * 补码为01001
 * <p>
 * 反码为01001，其中前面加的0是符号位，负数的符号位用1表示
 * 负数-1（二进制为：0001）在内存中存储为10001，开头的1为符号位，在内存中存放为，11111（负数的补码是：符号位不变，其余各位求反，末位加1）既得到11111。
 * 补码为11111
 * 反码为11110
 * <p>
 * 负数的补码是：符号位不变，其余各位求反，末位加1 ，既-1的补码为11111
 * 反码是：符号位为1，其余各位求反，但末位不加1 ，既-1的反码为11110
 * 也就是说,反码末位加上1就是补码
 * <p>
 * ------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * 弄懂了上述情况后，如何计算就好办了
 * 假设有一个数~9，计算步骤如下，9的二进制为：1001
 * 其补码为01001
 * 对其取反10110（“~”运算符取反后得到这个数），现在需要换成二进制原码用来输出，既先减1，然后取反得11010，符号位为1是负数，既9使用了按位运算符“~”后得到-10。
 * 原码是一种计算机中对数字的二进制定点表示方法。原码表示法在数值前面增加了一位符号位（即最高位为符号位）：正数该位为0，负数该位为1（0有两种表示：+0和-0），其余位表示数值的大小。
 * <p>
 * 机器数
 * <p>
 * 一个数在计算机中的二进制表示形式，叫做这个数的机器数，机器数是带符号的，在计算机中用一个数的最高位存放符号，正数为0，负数为1，
 * 比如，十进制中的+3，假设计算机字长为8位，转换成二进制就是0000 0011，如果是-3，就是1000 0011.那么，这里0000 0011和1000 0011就是机器数，
 * <p>
 * 真值
 * <p>
 * 因为第一位为符号位，所以机器数的形式值就不等于真正的数值，例如上面的有符号数1000 0011，其最高位1代表负，其真正数值是-3,而不是形式值131（1000 0011转换成10进制等于131），
 * 所以为了区别起见，将带符号的机器数对应的真正数值称为机器数的真值。例如：0000 0001的真值=+000 0001=+1,1000 0001的真值=-000 0001=-1
 * <p>
 * 原码：
 * <p>
 * 原码就是符号位加上真值的绝对值，即用第一位表示符号，其余位表示值，比如如果是8位二进制，[+1]原=0000 0001.[-1]原=1000 0001.因为第一位是符号位，
 * 所以8位二进制的取值范围就是：[1111 1111,0111 1111]即[-127,127]，原码是人脑最容易理解和计算的表示方式。
 * <p>
 * 反码：
 * <p>
 * 反码的表示方法是:正数的反码是其本身，负数的反码是在其原码的基础上，符号位不变，其余各个位取反，[+1]=[0000 0001]原= [0000 0001]反，[-1]=[1000 0001]原=[1111 1110]反。
 * 可见如果一个反码表示的是负数，人脑无法直观的看出来它的数值，通常要将其转换成原码再计算。
 * <p>
 * 补码：
 * <p>
 * 补码的表示方法是：正整数的二进制补码与其二进制原码相同，负整数的二进制补码，先求与该负数相对应的正整数的二进制代码，然后所有位取反加1，不够位数时左边补1，
 * 例如，[+1]=[0000 0001]原=[0000 0001]反=[0000 0001]补，[-1]=[1000 0001]原=[1111 1110]反=[1111 1111]补，对于负数，补码表示方式也是人脑无法直观看出其数值的，通常也需要转换成原码再计算其数值。
 * <p>
 * 为什么要使用原码反码补码，
 * <p>
 * 现在我们知道了计算机可以用原码 反码 补码这三种编码方式表示一个数，对于正数因为三种编码方式都相同，没有什么好解释的，但是对于负数，负数的原码反码补码是完全不同的，
 * 既然原码才是被人脑直接识别并用于计算方式，那么为什么还要用反码和补码呢，首先，因为人脑可以知道原码的第一位是符号位，在计算的时候，我们会根据符号位，选择对真值区域的加减，
 * 但是对于计算机，加减乘除已经是最基础的运算，要设计的尽量简单，计算机辨别符号位显然会让计算机的基础电路设计变得十分复杂，于是人们想出了将符号位也参与运算的方法，
 * 我们知道，根据运算法则减去一个正数等于加上一个负数，即：1-1=1+（-1）=0；所以机器可以只有加法而没有减法，这样计算机运算的设计就更简单了，
 * 那么如果用原码计算，1-1=1+(-1)=[0000 0001]原+[1000 0001]原=[1000 0010]原=-2.如果用原码计算，让符号位也参与运算，显然对于减法来说，结果是不正确的，
 * 这也就是为什么计算机内部不用原码表示一个数，为了解决原码做减法的问题出现了反码，
 * 如果用反码计算减法，1-1=1+(-1)= [0000 0001]原+ [1000 0001]原=[0000 0001]反+[1111 1110反]=[1111 1111]反=[1000 0000]原=-0，发现用反码计算减法，结果的真值部分是正确的，
 * 而唯一的问题其实出现在0这个特殊的数值上，虽然人们理解上+0和-0是一样的，但是0带符号是没有任何意义的，而且会有[0000 0000]原和[1000 0000]原两个编码表示0；于是补码的出现，
 * 解决了0的符号以及两个编码的问题：1-1=1+(-1)=[0000 0001]原+[1000 0001]原=[0000 0001]补+[1111 1111]补=[0000 0000]补=[0000 0000]原，这样0用[0000 0000]表示，
 * 而以前出现的问题-0则不存在了，而且可以用[1000 0000]表示-128；(-1) + (-127) = [1000 0001]原 + [1111 1111]原 = [1111 1111]补 + [1000 0001]补 = [1000 0000]补。-1-127的结果应该是-128,
 * 在用补码运算的结果中, [1000 0000]补 就是-128. 但是注意因为实际上是使用以前的-0的补码来表示-128, 所以-128并没有原码和反码表示.(对-128的补码表示[1000 0000]补算出来的原码是[0000 0000]原, 这是不正确的)。
 * 使用补码, 不仅仅修复了0的符号以及存在两个编码的问题, 而且还能够多表示一个最低数. 这就是为什么8位二进制, 使用原码或反码表示的范围为[-127, +127], 而使用补码表示的范围为[-128, 127].因为机器使用补码,
 * 所以对于编程中常用到的32位int类型, 可以表示范围是: [-231, 231-1] 因为第一位表示的是符号位.而使用补码表示时又可以多保存一个最小值。
 * <p>
 * =============================================
 * 当将-127赋值给a[0]时候，a[0]作为一个byte类型，其计算机存储的补码是10000001（8位）。
 * <p>
 * 将a[0]
 * 作为int类型向控制台输出的时候，jvm作了一个补位的处理，因为int类型是32位所以补位后的补码就是1111111111111111111111111
 * 10000001（32位），这个32位二进制补码表示的也是-127.
 * <p>
 * 发现没有，虽然byte->int计算机背后存储的二进制补码由10000001（8位）转化成了1111111111111111111111111
 * 10000001（32位）很显然这两个补码表示的十进制数字依然是相同的。
 * <p>
 * 但是我做byte->int的转化 所有时候都只是为了保持 十进制的一致性吗？
 * <p>
 * 不一定吧？好比我们拿到的文件流转成byte数组，难道我们关心的是byte数组的十进制的值是多少吗？我们关心的是其背后二进制存储的补码吧
 * <p>
 * 所以大家应该能猜到为什么byte类型的数字要&0xff再赋值给int类型，其本质原因就是想保持二进制补码的一致性。
 * <p>
 * 当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样。这样做的目的就是为了保证二进制数据的一致性。
 * <p>
 * 当然拉，保证了二进制数据性的同时，如果二进制被当作byte和int来解读，其10进制的值必然是不同的，因为符号位位置已经发生了变化。
 * <p>
 * ============================================= 象例2中，int c = a[0]&0xff;
 * a[0]&0xff=1111111111111111111111111
 * 10000001&11111111=000000000000000000000000 10000001 ，这个值算一下就是129，
 * <p>
 * 所以c的输出的值就是129。有人问为什么上面的式子中a[0]不是8位而是32位，因为当系统检测到byte可能会转化成int或者说byte与int类型进行运算的时候，就会将byte的内存空间高位补1（也就是按符号位补位）扩充到32位，再参与运算。上面的0xff其实是int类型的字面量值，所以可以说byte与int进行运算。
 * <p>
 * http://blog.csdn.net/sunnyfans/article/details/8286906
 * <p>
 * Big endian means that the most significant byte of any multibyte data field is stored at the lowest memory address, which is also the address of the larger field.
 * 大端模式，是指数据的高字节保存在内存的低地址中，而数据的低字节保存在内存的高地址中，这样的存储模式有点儿类似于把数据当作字符串顺序处理：地址由小向大增加，而数据从高位往低位放；这和我们的阅读习惯一致。
 * Little endian means that the least significant byte of any multibyte data field is stored at the lowest memory address, which is also the address of the larger field.
 * 小端模式，是指数据的高字节保存在内存的高地址中，而数据的低字节保存在内存的低地址中，这种存储模式将地址的高低和数据位权有效地结合起来，高地址部分权值高，低地址部分权值低。
 */
public class ByteUtils {

    /**
     * 获取int 低8位（统称低8位）
     *
     * @param value int
     * @return byte
     */
    public static byte low8Bit(int value) {
        return (byte) (value & 0xff);
    }

    /**
     * 获取 2字节 int 高8位（相对于两个字节byte而言称高8位）
     *
     * @param value int
     * @return byte
     */
    public static byte high8Bit(int value) {
        return (byte) ((value >> 8) & 0xff);
    }

    /**
     * 1 byte 转 hex string
     *
     * @param value byte
     * @return String
     */
    public static String toHexString(byte value) {

        String hex = Integer.toHexString(value & 0xFF);

        return (hex.length() == 1) ? '0' + hex : hex;
    }

    /**
     * byte 转 hex string
     *
     * @param values byte[]
     * @return String
     */
    public static String toHexString(byte[] values) {

        StringBuilder sBuilder = new StringBuilder();

        for (byte value : values)
            sBuilder.append(toHexString(value)).append(" ");

        return sBuilder.toString();
    }

    /**
     * hex tring 转 byte[]
     *
     * @param hex String
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {

        hex = hex.replace(" ", "");

        int len = hex.length() / 2;

        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();

        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

    /**
     * @param c char
     * @return byte
     */
    public static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #bytesToInt2(byte[] src)} 配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param value int 要转换的int值
     * @return byte[]
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占 len 个字节的byte数组，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #bytesToInt(byte[] src)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param value int
     * @param len   int 转换成多长的byte数组，多余高位丢弃
     * @return byte[]
     */
    public static byte[] intToBytes(int value, int len) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        for (int i = 0; i < len; i++) {
            bo.write(value & 0xff);
            value = value >> 8;
        }

        return bo.toByteArray();
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后(大端))的顺序。
     * <p>
     * {@link #bytesToInt2(byte[] src)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 高位字节(前)    低位字节(后)   =
     * = 11001100      00110011      =
     * ===============================
     *
     * @param value int 要转换的int值
     * @return byte[]
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * @param src byte
     * @return int
     */
    public static int byteToInt(byte src) {
        return src & 0xFF;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #intToBytes(int value)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param src byte[]
     * @return int
     */
    public static int bytesToInt(byte[] src) {
        int value = 0, len = src.length - 1, offset = 0;// offset = 0 8 16 24
        int cover = src[len] < 0 ? -1 : 0;

        for (int i = 0; i < 4; i++, offset += 8)
            value |= ((i <= len ? src[i] : cover) & 0xFF) << offset;

        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #intToBytes(int value)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param src byte[]
     * @param pos int
     * @param len int
     * @return int
     */
    public static int bytesToInt(byte[] src, int pos, int len) {

        // offset = 0 8 16 24
        int value = 0, size = pos + 4, count = pos + len - 1, offset = 0;
        int cover = src[count] < 0 ? -1 : 0;

        for (int i = pos; i < size; i++, offset += 8)
            value |= ((i <= count ? src[i] : cover) & 0xFF) << offset;

        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前(大端))的顺序。
     * <p>
     * {@link #intToBytes2(int value)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 高位字节(前)    低位字节(后)   =
     * = 11001100      00110011      =
     * ===============================
     *
     * @param src byte[]
     * @return int
     */
    public static int bytesToInt2(byte[] src) {

        // offset = 0 8 16 24
        int value = 0, pos = 0, start = src.length - 1, size = src.length - 4, offset = 0;
        int cover = src[start] < 0 ? -1 : 0;

        for (int i = start; i >= size; i--, offset += 8)
            value |= ((i >= pos ? src[i] : cover) & 0xFF) << offset;

        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前(大端))的顺序。
     * <p>
     * {@link #intToBytes2(int value)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 高位字节(前)    低位字节(后)   =
     * = 11001100      00110011      =
     * ===============================
     *
     * @param src byte[]
     * @param len int
     * @return int
     */
    public static int bytesToInt2(byte[] src, int pos, int len) {

        // offset = 0 8 16 24
        int value = 0, start = pos + len - 1, size = pos + (len - 4), offset = 0;
        int cover = src[pos] < 0 ? -1 : 0;

        for (int i = start; i >= size; i--, offset += 8)
            value |= ((i >= pos ? src[i] : cover) & 0xFF) << offset;

        return value;
    }

    /**
     * 将double数值转换为占8个字节的byte数组，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #bytes2Double(byte[] arr)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 高位字节(前)    低位字节(后)   =
     * = 11001100      00110011      =
     * ===============================
     *
     * @param d double
     * @return byte[]
     */
    public static byte[] double2Bytes(double d) {
        return double2Bytes(d, 8);
    }

    /**
     * 将double数值转换为占len个字节的byte数组，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #bytes2Double(byte[] arr)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 高位字节(前)    低位字节(后)   =
     * = 11001100      00110011      =
     * ===============================
     *
     * @param d   double
     * @param len int
     * @return byte[]
     */
    public static byte[] double2Bytes(double d, int len) {
        long value = Double.doubleToRawLongBits(d);

        byte[] byteRet = new byte[len];
        for (int i = 0; i < len; i++)
            byteRet[i] = (byte) ((value >> (8 * i)) & 0xff);

        return byteRet;
    }

    /**
     * byte数组中取double数值，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #double2Bytes(double d)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param arr byte[]
     * @return double
     */
    public static double bytes2Double(byte[] arr) {

        long value = 0;
        int count = arr.length - 1;
        int cover = arr[count] < 0 ? -1 : 0;

        for (int i = 0; i < 8; i++)
            value |= ((long) ((i <= count ? arr[i] : cover) & 0xff)) << (8 * i);

        return Double.longBitsToDouble(value);
    }

    /**
     * 将double数值转换为占2个字节的byte数组，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #bytesToDouble(byte[] src)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param value double
     * @return byte[]
     */
    public static byte[] doubleToBytes(double value) {
        return intToBytes((int) (value * 16), 2);
    }

    /**
     * byte数组中取double数值，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #doubleToBytes(double d)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param src byte[]
     * @return double
     */
    public static double bytesToDouble(byte[] src) {
        return bytesToDouble(src, 0, src.length);
    }

    /**
     * byte数组中取double数值，本方法适用于(低位在前，高位在后(小端))的顺序。
     * <p>
     * {@link #doubleToBytes(double d)}配套使用
     * <p>
     * ===============================
     * = src[0]         src[1]       =
     * = 低位字节(前)    高位字节(后)   =
     * = 00110011      11001100      =
     * ===============================
     *
     * @param src byte[]
     * @param pos int
     * @param len int
     * @return double
     */
    public static double bytesToDouble(byte[] src, int pos, int len) {
        return bytesToInt(src, pos, len) / 16.0;
    }

    /**
     * @param src    byte[]
     * @param srcPos int
     * @param length int
     * @return byte[]
     */
    public static byte[] arraycopy(byte[] src, int srcPos, int length) {

        byte[] bytes = new byte[length];
        System.arraycopy(src, srcPos, bytes, 0, length);

        return bytes;
    }

    /**
     * 补0
     *
     * @param value int
     * @return String
     */
    public static String addZero(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

    public static void main(String[] args) {

        System.out.println("=============小端 int ============");

        int x27 = 27456;
        byte[] xbs = intToBytes(x27);
        byte[] x2bs = intToBytes(x27, 2);

        System.out.println("xbs：" + bytesToInt(xbs));
        System.out.println("x2bs：" + bytesToInt(x2bs));
        System.out.println("x2bs, 0, 2：" + bytesToInt(x2bs, 0, 2));

        System.out.println("=============小端 int 负数 ============");

        int x_27 = -27897;
        byte[] x_bs = intToBytes(x_27);
        byte[] x_2bs = intToBytes(x_27, 2);

        System.out.println("x_bs：" + bytesToInt(x_bs));
        System.out.println("x_2bs：" + bytesToInt(x_2bs));
        System.out.println("x_2bs, 0, 2：" + bytesToInt(x_2bs, 0, 2));

        System.out.println("=============大端 int ============");

        int d27 = 12327;
        byte[] dbs = intToBytes2(d27);

        System.out.println("dbs：" + bytesToInt2(dbs));
        System.out.println("dbs, 0, 4：" + bytesToInt2(dbs, 0, 4));

        System.out.println("=============大端 int 负数 ============");

        int d_27 = -33327;
        byte[] d_bs = intToBytes2(d_27);

        System.out.println("d_bs：" + bytesToInt2(d_bs));
        System.out.println("d_bs, 0, 4：" + bytesToInt2(d_bs, 0, 4));

        System.out.println("=============小端 double ============");

        double d = 527.13;

        byte[] xdbs = doubleToBytes(d);
        // byte[] x2dbs = double2Bytes(d);
        // byte[] x22dbs = double2Bytes(d, 2);

        System.out.println("xdbs：" + bytesToDouble(xdbs));
        System.out.println("xdbs, 0, 2：" + bytesToDouble(xdbs, 0, 2));

        System.out.println("=============小端 double 负数============");

        double fd = -57.134;

        byte[] xfdbs = doubleToBytes(fd);

        System.out.println("xfdbs：" + bytesToDouble(xfdbs));
        System.out.println("xfdbs, 0, 2：" + bytesToDouble(xfdbs, 0, 2));

    }

    public static void main123(String[] args) {

//        byte[] resultBytes = new byte[6];
//        resultBytes[0] = 0x05;
//        resultBytes[1] = 0x03;
//        resultBytes[2] = 0x00;
//        resultBytes[3] = 0x00;
//        resultBytes[4] = 0x00;
//        resultBytes[5] = 0x02;
//
//        long start = 0;
//
//        start = System.currentTimeMillis();
//        byte[] m1 = CRC16.calcCrcData(resultBytes);
//        System.out.println(ByteUtils.toHexString(m1));
//        System.out.println(System.currentTimeMillis() - start);
//
//        start = System.currentTimeMillis();
//        byte[] m2 = com.traciing.coldchain.transport.collect.core.uart.CRC16.ECCsenddata(resultBytes);
//        System.out.println(ByteUtils.toHexString(m2));
//        System.out.println(System.currentTimeMillis() - start);
//
//        System.out.println("" + CRC16.verifyCrcData(m2));
    }

    public static void main55(String[] args) {
        long start = 0;
        // 20 00
        // 07 00 B4 01 DD 01 37 00 26 00
        // 08 00 71 02 DD 02 37 00 26 00
        // 09 00 01 00 DD 00 37 00 26 00
        DecimalFormat deFormat = new DecimalFormat("00.00");
        byte[] readBytes = ByteUtils.hexStringToByte("20000700B401DD013700260008007102DD023700260009000100DD0037002600");

        start = System.currentTimeMillis();
        // 跳过两个字节的报文长度int count = (readLen - 2) / 10;
//        for (int i = 2; i < 32; i += 10) {
//
//            // 传感器温度id 2byte
//            int id = ByteUtils2.byteToInt(readBytes[i]) + ByteUtils2.byteToInt(readBytes[i + 1]) * 256;
//
//            // 温湿度值 2byte [-76 1]
//            double valuett = ByteUtils2.bytesToDouble(readBytes[i + 2], readBytes[i + 3]);
//
//            // 通道信号强度RSSI 1byte 0:表 示 信 号 最 弱 ， 255: 表 示 信 号 最 强 。
//            int rssi = ByteUtils2.byteToInt(readBytes[i + 4]);
//            String rssiStr = deFormat.format((rssi * 1.0 / 255 * 100 + 0.0) / 100 * 100);
//
//            // 传感器类型 1byte 1 0001 温度； 2 0010湿度
//            int type = ByteUtils2.byteToInt(readBytes[i + 5]);
//
//            // 连接计数
//            int connectCount = ByteUtils2.byteToInt(readBytes[i + 6]) + ByteUtils2.byteToInt(readBytes[i + 7]) * 256;
//
//            // 电池电量低指示 1byte
//            double powerdata = ByteUtils2.byteToInt(readBytes[i + 8]) + ByteUtils2.byteToInt(readBytes[i + 9]) * 256;
//            String powerdataStr = deFormat.format(100 * (powerdata * 0.1 - 3.2) / (3.9 - 3.2));
//
//            System.out.println(id + ", " + valuett + ", " + rssiStr + ", " + type + ", " + connectCount + ", " + powerdataStr);
//        }

        System.out.println("===================================" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        //// 07 00 B4 01 DD 01 37 00 26 00
        for (int i = 2; i < 32; i += 10) {

            int id = ByteUtils.bytesToInt(arraycopy(readBytes, i, 2));

            double tvalue = ByteUtils.bytesToDouble(arraycopy(readBytes, i + 2, 2));

            int rssi = ByteUtils.byteToInt(readBytes[i + 4]);
            String rssiStr = deFormat.format((rssi * 1.0 / 255 * 100 + 0.0) / 100 * 100);

            int type = ByteUtils.byteToInt(readBytes[i + 5]);

            // 连接计数
            int connectCount = ByteUtils.byteToInt(readBytes[i + 6]);

            // 电池电量低指示 1byte
            int powerdata = ByteUtils.bytesToInt(arraycopy(readBytes, i + 8, 2));
            String powerdataStr = deFormat.format(100 * (powerdata * 0.1 - 3.2) / (3.9 - 3.2));

            System.out.println(id + ", " + tvalue + ", " + rssiStr + ", " + type + ", " + connectCount + ", " + powerdataStr);
        }

        System.out.println("===================================" + (System.currentTimeMillis() - start));

        byte[] sss = doubleToBytes(27.25);

        System.out.println(toHexString(sss));
        System.out.println(bytesToDouble(sss));
    }

    /**
     * 为何与0xff进行与运算

     在剖析该问题前请看如下代码

     public static String bytes2HexString(byte[] b) {

     String ret = "";

     for (int i = 0; i < b.length; i++) {

     String hex = Integer.toHexString(b[ i ] & 0xFF);

     if (hex.length() == 1) {

     hex = '0' + hex;

     }

     ret += hex.toUpperCase();

     }

     使用以下的语句,就可以区分使用&0xff和不使用的区别了

     System.out.println(Integer.toBinaryString(b & 0xff)); 输出结果:000000000000000000000000 11010110

     System.out.println(Integer.toBinaryString(b)); 输出结果:       111111111111111111111111 11010110

     return ret;

     }

     代码解析:

     注意这里b[ i ] & 0xFF将一个byte和 0xFF进行了与运算。

     b[ i ] & 0xFF运算后得出的仍然是个int,那么为何要和 0xFF进行与运算呢?直接 Integer.toHexString(b[ i ]);

     将byte强转为int不行吗?答案是不行的.

     其原因在于:

     1.byte的大小为8bits而int的大小为32bits

     2.Java的二进制采用的是补码形式

     byte是一个字节保存的,有8个位,即8个0、1。

     8位的第一个位是符号位,

     也就是说0000 0001代表的是数字1

     1000 0000代表的就是-1

     所以正数最大位0111 1111,也就是数字127

     负数最大为1111 1111,也就是数字-128

     上面说的是二进制原码,但是在java中采用的是补码的形式,下面介绍下什么是补码

     1、反码:

     一个数如果是正,则它的反码与原码相同;

     一个数如果是负,则符号位为1,其余各位是对原码取反;

     举个例子:2 二进制吗为 00000010,因为是正数,所以其反码也是 00000010

     如果是-2那么,就要把最高位变为1,其他7位按照其正数的位置取反。

     2、补码:利用溢出,我们可以将减法变成加法:

     对于十进制数,从9得到5可用减法:

     9-4=5    因为4+6=10,我们可以将6作为4的补数

     改写为加法:

     +6=15(去掉高位1,也就是减10)得到5.

     对于十六进制数,从c到5可用减法:

     c-7=5    因为7+9=16 将9作为7的补数

     改写为加法:

     c+9=15(去掉高位1,也就是减16)得到5.

     在计算机中,如果我们用1个字节表示一个数,一个字节有8位,超过8位就进1,在内存中情况为(100000000),进位1被丢弃。

     ⑴一个数为正,则它的原码、反码、补码相同

     ⑵一个数为负,刚符号位为1,其余各位是对原码取反,然后整个数加1

     - 1的原码为                10000001

     - 1的反码为                11111110

     + 1

     - 1的补码为                11111111

     0的原码为                  00000000

     0的反码为                  11111111(正零和负零的反码相同)

     +1

     0的补码为                  100000000(舍掉打头的1,正零和负零的补码相同)

     Integer.toHexString的参数是int,如果不进行&0xff,那么当一个byte会转换成int时,由于int是32位,

     而byte只有8位这时会进行补位,例如补码11111111的十进制数为-1

     转换为int时变为11111111 11111111 11111111 11111111好多1啊,呵呵!

     即0xffffffff但是这个数是不对的,这种补位就会造成误差。

     和0xff相与后,高24比特就会被清0了,结果就对了。

     Java中的一个byte,其范围是-128~127的,而Integer.toHexString的参数本来是int,如果不进行&0xff,

     那么当一个byte会转换成int时,对于负数,会做位扩展,举例来说,一个byte的-1(即0xff),

     会被转换成int的-1(即0xffffffff),那么转化出的结果就不是我们想要的了。

     而0xff默认是整形,所以,一个byte跟0xff相与会先将那个byte转化成整形运算,这样,

     结果中的高的24个比特就总会被清0,于是结果总是我们想要的。

     0xFF (十进制1)

     二进制码:00000000 00000000 00000000 11111111

     与 0xff 做 & 运算会将 byte 值变成 int 类型的值,也将 -128~0 间的负值都转成正值了。

     char c = (char)-1 & 0xFF;

     char d = (char)-1;

     System.out.println((int)c); 255

     System.out.println((int)d); 65535

     java中的數值是int,所以0xFF是int,而byte是有符號數,int亦然,直接由byte升為int,符號自動擴展,

     而進行了& 0xFF後,就把符號問題忽略掉了,將byte以純0/1地引用其內容,所以要0xFF,不是多餘的,

     你用一些Stream讀取文件的byte就知道了,我昨天搞了一天,就不明白為什麼讀出來的數某些byte會

     在移位後錯誤的,就是因為這個原因.

     把number转换为二进制,只取最低的8位(bit)。因为0xff二进制就是1111 1111

     & 运算是,如果对应的两个bit都是1,则那个bit结果为1,否则为0.

     比如 1010 & 1101 = 1000 (二进制)

     由于0xff最低的8位是1,因此number中低8位中的&之后,如果原来是1,结果还是1,原来是0,结果位还

     是0.高于8位的,0xff都是0,所以无论是0还是1,结果都是0.
     */
}
