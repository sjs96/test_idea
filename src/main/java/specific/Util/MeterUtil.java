package specific.Util;

import java.util.Arrays;

public class MeterUtil {

    public static long getInt(String hex_num){
        long dec_num = Long.parseLong(hex_num, 16);
        return dec_num;
    }
    public static double getDouble(String hex_num){
        long dec_num = Long.parseLong(hex_num, 16);
        return Double.parseDouble(dec_num+"");
    }

    public static String get16(byte[] data){
        String str="";
        for(int i=0;i<data.length;i++){
            String str1 = Long.toHexString(data[i]);
            if(str1.length()==1){
                str1="0"+str1;
            }
            if(str1.length()>2){
                str1=str1.substring(str1.length()-2);
            }
            str += str1;
        }
        return str;
    }
    public static String get16(byte data){
        String str = Long.toHexString(data);
        if(str.length()==1){
            str="0"+str;
        }
        if(str.length()>2){
            str=str.substring(str.length()-2);
        }
        return str;
    }
    public static String get2(byte[] data){
        String str="";
        for(int i=0;i<data.length;i++){
            String str1 =  Long.toBinaryString((data[i] & 0xFF) + 0x100).substring(1);
            str += str1;
        }
        return str;
    }
    public static String get2(byte data){
        String str =  Long.toBinaryString((data & 0xFF) + 0x100).substring(1);

        return str;
    }
    public static long get10(byte [] data){
        return Long.parseLong(Long.valueOf(get2(data),2).toString());
    }
    public static long get10(byte data){
        return Long.parseLong(Long.valueOf(get2(data),2).toString());
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] Hex16ToByte(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Long.parseLong(subStr, 16);
        }

        return bytes;
    }
    /**
     * 将2进制字符串转换为byte[]
     *
     * @param hex2Str
     * @return
     */
    public static byte[] Hex2ToByte(String hex2Str)
    {
        while(hex2Str.length()%8!=0){
            hex2Str="0"+hex2Str;
        }
        byte [] b = new byte[hex2Str.length()/8];
        for(int i = 0;i<hex2Str.length();i+=8){
            b[i/8] = Long.valueOf(hex2Str.substring(i, i+8), 2).byteValue();
        }
        return b;
    }


}
