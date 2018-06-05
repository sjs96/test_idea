package specific.Util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Instructions {
    /**
     * bit_31
     * 0：由主机发出的命令帧
     * 1：由从机发出的应答帧
     */
    public  String bit_31 = "0";

    /**
     * bit_30
     * 0：从机正确应答
     * 1：从机对异常信息的应答
     */
    public  String bit_30 = "0";

    /**
     * bit_29
     * 0：无后续数据帧
     * 1：有后续数据帧
     */
    public  String bit_29 = "0";

    /**
     * bit_28
     * 0：读
     * 1：写
     */
    public  String bit_28 = "0";

    /**
     * bit_27_24 = 0 (读)
     * 0001：只读数据（RO）
     * 0010：只读数据块（RO）
     * 0011：读后续数据
     * 0100：重读数据
     * 0101：可读可写数据（RW）
     * 0110：可读可写数据块（RW）
     * 1110：特殊数据
     * 1111：特殊数据块
     * Bit28 = 1 (写)
     * 0010：可读可写数据（RW）
     * 1110：特殊数据
     * 注：特殊数据一般为长度超8字节的数据
     */
    public  String bit_27_24 = "0001";

    /**
     * bit_23_20
     * 0111：云服务
     * 0110：上位机
     * 0101：集中器
     * 0100：转换器A
     * 0011：采集器
     * 0010：转换器B
     * 0001：终端(无转发功能)这个是电表
     * 0000：开关类设备
     */
    public  String bit_23_20 = "0110";

    /**
     * bit_19_16
     * 0111：YTL云服务
     * 0110：上位机
     * 0101：集中器
     * 0100：转换器A
     * 0011：采集器
     * 0010：转换器B
     * 0001：终端(无转发功能)
     * 0000：开关类设备
     */
    public  String bit_19_16 = "0001";

    /**
     * bit_15_8
     * 电表   01h
     *  水表   02h
     *  气表   03h
     *  ……
     *  温度表 10h
     *  湿度表 11h
     *  压力表 12h
     */
    public  String bit_15_8 = "00000001";

    /**
     * 未用
     */
    public  String bit_7_4 = "0000";

    /**
     * bit_3
     * 数据长度字节数
     * 0： 1byte(默认)
     * 1： 2byte
     */
    public  String bit_3 = "0";

    /**
     * 00：1byte
     * 01：2byte
     * 10：4byte
     * 11：6byte(默认)
     */
    public  String bit_2_1 = "11";

    /**
     * 未用
     */
    public  String bit_0 = "0";

    /**
     * 头
     */
    public final String x86 = "01101000";

    /**
     * 尾
      */
    public final String x16 = "00010110";

    /**
     * CRC16
     */
    public String crc16 = "0000000000000000";

    /**
     * 设备地址
     */
    public  String device_address = "000001000011000000000000000000000000011000100000";
    /**
     * password_7
     * 0：密码
     * 1：密钥
     *
     */
    public  String password_7 = "";
    /**
     * password_6
     * 空
     */
    public  String password_6 = "";
    /**
     * password_5_0
     * 密码
     */
    public  String password_5_0 = "";

    public  String dy = "0000010001100000";
    //功能码
    public String getFunctionCode(){
        return bit_31+bit_30+bit_29+bit_28+bit_27_24+bit_23_20+bit_19_16+bit_15_8+bit_7_4+bit_3+bit_2_1+bit_0;
    }
    //长度
    public String getLength(){
        String str="";
        if("0".equals(bit_3)){
            str = Integer.toBinaryString(device_address.length()/8+dy.length()/8+1);
            while(str.length()%8!=0){
                str= "0"+str;
            }

        }else if("1".equals(bit_3)){
            str = Integer.toBinaryString(device_address.length()/+dy.length()/8+2);
            while(str.length()%16!=0){
                str= "0"+str;
            }
        }
        return str;
    }

    //读指令
    public String getCrc(){
        String a = getFunctionCode()+getLength()+device_address+dy;
        String crc2 =  CRC16Util.getCRC_modbus(MeterUtil.Hex2ToByte(a));
        return MeterUtil.get2(MeterUtil.Hex16ToByte(crc2));
    }
    public String getReadInstructions(){

        return x86+getFunctionCode()+getLength()+device_address+dy+getCrc()+x16;
    }

    public byte[] realTimeData(String meterNo,String dy2){

        device_address = MeterUtil.get2(MeterUtil.Hex16ToByte(meterNo));
        dy = MeterUtil.get2(MeterUtil.Hex16ToByte(dy2));
        String str  = getReadInstructions();
        return MeterUtil.Hex2ToByte(str);
    }

    public byte[] register(String meterNo){
        String str = "1271010611"+meterNo+"064150538090A00103AA";

        str = "68"+str + CRC16Util.getCRC_modbus(MeterUtil.Hex16ToByte(str))+"16";
        return MeterUtil.Hex16ToByte(str);
    }
    public byte[] setToken(String meterNo ,String token){
        String str = "1e61010620"+meterNo+"064150538090a0140f"+token+"000000000000";

        str = "68"+str + CRC16Util.getCRC_modbus(MeterUtil.Hex16ToByte(str))+"16";
        return MeterUtil.Hex16ToByte(str);
    }

    public byte[] setToken(String meterNo ,String token ,String token2){
        String str = "1e61010632"+meterNo+"064150538090a0140f"+token+"000000000000"+"140f"+token2+"000000000000";

        str = "68"+str + CRC16Util.getCRC_modbus(MeterUtil.Hex16ToByte(str))+"16";
        System.out.println(MeterUtil.get16(MeterUtil.Hex16ToByte(str)));
        return MeterUtil.Hex16ToByte(str);
    }

}
