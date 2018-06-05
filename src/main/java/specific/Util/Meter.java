package specific.Util;

import com.jfinal.plugin.activerecord.Db;
import fixed.util.RedisUtil;
import fixed.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Meter {

    public static Map<String,Object> getContent(InputStream is){
        try {
            //头部
            byte[] head = new byte[1];
            //功能码
            byte[] function = new byte[4];
            //长度
            byte[] length ;
            //尾部
            byte[] tail = new byte[1];
            //CRC校验
            byte[] crc16 = new byte[2];
            //数据
            byte[] data ;
            is.read(head);
            is.read(function);
            System.out.println("电表数据");
            System.out.println(MeterUtil.get16(function));
            int len = getLength(function);
            length =  new byte[len];
            is.read(length);
            System.out.println(MeterUtil.get16(length));
            data = new byte[(int)MeterUtil.get10(length) - len];
            is.read(data);
            System.out.println(MeterUtil.get16(data));
            is.read(crc16);
            System.out.println(MeterUtil.get16(crc16));
            is.read(tail);
            System.out.println(MeterUtil.get16(tail));
            Map<String,Object> returnMap= new HashMap<>();
            returnMap.put("head",head);
            returnMap.put("function",function);
            returnMap.put("length",length);
            returnMap.put("data",data);
            returnMap.put("crc16",crc16);
            returnMap.put("tail",tail);
            return returnMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备no
     * @param data
     * @return
     */
    public static Map<String,Object> getMeterNo(byte[] data){
        Map<String,Object> map = new HashMap<String,Object>();
        byte[] meterNo = new byte[6];
        byte[] newData = new byte[data.length-6];
        for (int i = 0; i < data.length; i++) {
            if(i<6){
                meterNo[i]=data[i];
            }else{
                newData[i-6]=data[i];
            }
        }
        map.put("meterNo",meterNo);
        map.put("data",newData);
        return map;
    }
    public static Map<String,Object> getData(Map<String,Object> map){

        byte[] data = (byte[])map.get("meterData");
        byte[] function = (byte[])map.get("function");
        byte[] meterNo = (byte[])map.get("meterNo");
        String meterNo_ = MeterUtil.get16(meterNo);
        Map<String,Object> map2222 = new HashMap<String,Object>();
        if(data.length>0){
            if(data.length>2){
                getData2(function,data,meterNo);
            }else{
                System.out.println("电表返回数据有问题");
                System.out.println(MeterUtil.get16(data));
                System.out.println(MeterUtil.get16(meterNo));
                System.out.println(MeterUtil.get16(function));

                String msg = MeterUtil.get16(data);
                String str1 = msg.substring(0,2);
                int a1 = Integer.parseInt(str1,16);
                int a2 = Integer.parseInt("8f",16);
                String msg_state =Integer.toHexString(a1 & a2);
                System.out.println(msg_state);
                if("80".equals(msg_state)||"86".equals(msg_state)){
                    set(meterNo_);
                }else if("81".equals(msg_state)){
                    Db.update(Db.getSql("meter.setError"),  81,"CRC校验码错误",meterNo_.substring(1));
                }else if("82".equals(msg_state)){
                    Db.update(Db.getSql("meter.setError"),  82,"不支持的主类或子类",meterNo_.substring(1));
                }else if("83".equals(msg_state)){
                    Db.update(Db.getSql("meter.setError"),  83,"修改密钥1参数错误",meterNo_.substring(1));
                }else if("84".equals(msg_state)){
                    Db.update(Db.getSql("meter.setError"),  84,"充值超限额",meterNo_.substring(1));
                }else if("85".equals(msg_state)){
                    Db.update(Db.getSql("meter.setError"),  85,"命令太陈旧",meterNo_.substring(1));
                }
            }
        }else{
            set(meterNo_);
        }
        return map2222;
    }
    //更新数据库
    public static void set(String meterNo_){
        Db.update(Db.getSql("meter.register2"),  meterNo_.substring(1));
        Db.update(Db.getSql("meter.register1"),  meterNo_.substring(1));
        Db.update(Db.getSql("order.rechargeState"),  meterNo_.substring(1));
        Db.update(Db.getSql("meter.setTokenGLEmpty"),  meterNo_.substring(1));
        Db.update(Db.getSql("meter.setTokenQDEmpty"),  meterNo_.substring(1));
        Db.update(Db.getSql("meter.setTokenKGFDEmpty"),  meterNo_.substring(1));
        Db.update(Db.getSql("meter.setTokenKGFKEmpty"),  meterNo_.substring(1));
    }
    public static long getLength(byte function,byte val){
        int str = (int)MeterUtil.getInt(MeterUtil.get16(val));
        String type = MeterUtil.get2(function).substring(4);
        long length = 0;
        if("1110".equals(type)){
            length = 2 <<(str-1);
        }else{
            System.out.println("getLength"+str);
            if(str>8){
                str=str-8;
            }
            switch (str){
                case 0:{length=0;};break;
                case 1:{length=1;};break;
                case 2:{length=2;};break;
                case 3:{length=3;};break;
                case 4:{length=4;};break;
                case 5:{length=6;};break;
                case 6:{length=8;};break;
            }
        }
        return length;
    }
    public static Map<String,Object> getData2(byte[] function,byte[] data,byte[] meterNo){
        Map<String,Object> map = new HashMap<String,Object>();
        long length = getLength(function[0],data[0]);
        byte[] variable = new byte[2];
        byte[] variableDate = new byte[(int)length];

        int newData_length = data.length-(int)length-2;
        byte[] newData = new byte[newData_length];
        for(int i=0;i<data.length;i++){
            if(i<2){
                variable[i]=data[i];
            }else if(i < (length+2) ){
                variableDate[i-2]=data[i];
            }else{
                newData[i-(int)length-2]=data[i];
            }
        }
        getMeterVal(MeterUtil.get16(variable),variableDate,meterNo);
        if(newData.length>0){
            getData2(function,newData,meterNo);
        }
        map.put("variable",variable);
        map.put("variableDate",variableDate);
        map.put("data",newData);
        return map;
    }
    public static void getMeterVal(String key,byte[] data,byte[] meterNo){
        String meter = MeterUtil.get16(meterNo);
        switch (key)
        {
            case "040c":{

                Map<String,Object> map = new HashMap<>();
                map.put("total_electricity",MeterUtil.getDouble(MeterUtil.get16(Arrays.copyOfRange(data,0,4)))/10);
                map.put("surplus_electricity",MeterUtil.getDouble(MeterUtil.get16(Arrays.copyOfRange(data,4,8)))/10);
                map.put("current",MeterUtil.getDouble(MeterUtil.get16(Arrays.copyOfRange(data,8,11)))/100);
                map.put("voltage",MeterUtil.getDouble(MeterUtil.get16(Arrays.copyOfRange(data,11,13)))/100);
                String s =  MeterUtil.get2(Arrays.copyOfRange(data,13,14));
                if("1".equals(s.substring(0,1))){
                    map.put("state",1);
                }else{
                    map.put("state",0);
                }
                Map<String,Object> meterMap =  RedisUtil.getMeter(meter);
                map.put("m_id",meterMap.get("m_id"));

                RedisUtil.setValue(RedisUtil.Current,meter,map,60*14);
                System.out.println("瞬时数据");
                System.out.println("总电量："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,0,4))));
                System.out.println("剩余电量；"+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,4,8))));
                System.out.println("电流："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,8,11))));
                System.out.println("电压："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,11,13))));
                System.out.println("继电器状态："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,13,14))));
                System.out.println("继电器状态："+MeterUtil.get2(Arrays.copyOfRange(data,13,14)));
                try{

                    Db.update(Db.getSql("meter.setElectricity"),MeterUtil.getDouble(MeterUtil.get16(Arrays.copyOfRange(data,4,8)))/10, meter.substring(1));
                }catch (Exception e){
                    Util.SOP(e);
                }
            }
            ;break;
            case "0460":{
                System.out.println("主动查询");
                System.out.println("电压："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/10);
                RedisUtil.setValue(RedisUtil.dianya,meter,map,20);
            }
            ;break;
            case "0462":{
                System.out.println("主动查询");
                System.out.println("电流："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/1000);
                RedisUtil.setValue(RedisUtil.dianliu,meter,map,20);
            }
            ;break;
            case "0463":{
                System.out.println("主动查询");
                System.out.println("有功功率："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/1000);
                RedisUtil.setValue(RedisUtil.yougonggonglv,meter,map,20);
            }
            ;break;
            case "0464":{
                System.out.println("主动查询");
                System.out.println("视在功率："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/1000);
                RedisUtil.setValue(RedisUtil.shizaigonglv,meter,map,20);
            }
            ;break;
            case "0465":{
                System.out.println("主动查询");
                System.out.println("无功功率："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/1000);
                RedisUtil.setValue(RedisUtil.wugonggonglv,meter,map,20);
            }
            ;break;
            case "0266":{
                System.out.println("主动查询");
                System.out.println("功率因素："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/1000);
                RedisUtil.setValue(RedisUtil.gonglvyinshu,meter,map,20);
            }
            ;break;
            case "0267":{
                System.out.println("主动查询");
                System.out.println("频率："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/100);
                RedisUtil.setValue(RedisUtil.pinlv,meter,map,20);
            }
            ;break;
            case "0468":{
                System.out.println("主动查询");
                System.out.println("有功总电量："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/100);
                RedisUtil.setValue(RedisUtil.yougongzongdianliang,meter,map,20);
            }
            ;break;
            case "0103":{
                System.out.println("恢复出厂设置");
                System.out.println("有功总电量："+MeterUtil.get16(data));
            }
            ;break;
            case "0c74":{
                System.out.println("主动查询");
                System.out.println("剩余电量："+MeterUtil.getInt(MeterUtil.get16(data)));
                Map<String,Object> map = new HashMap<>();
                map.put("value",MeterUtil.getDouble(MeterUtil.get16(data))/10);
                RedisUtil.setValue(RedisUtil.shengyudianliang,meter,map,20);
                Db.update(Db.getSql("meter.setElectricity"),MeterUtil.getDouble(MeterUtil.get16(data))/10, meter.substring(1));
            }
            ;break;
            case "0400":{
                System.out.println("平均数据");
                System.out.println("总电量："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,0,4))));
                System.out.println("剩余电量；"+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,4,8))));
                System.out.println("电流："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,8,11))));
                System.out.println("电压："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,11,13))));
                System.out.println("继电器状态："+MeterUtil.getInt(MeterUtil.get16(Arrays.copyOfRange(data,13,14))));
                System.out.println("继电器状态："+MeterUtil.get2(Arrays.copyOfRange(data,13,14)));
            }
            ;break;
            case "03f0":{
               System.out.println("版本号："+MeterUtil.get16(data));
            }
            ;break;
        }
    }
    public static int getLength(byte[] data){
        String sb = MeterUtil.get2(data);
        return Integer.parseInt(sb.substring(28,29))+1;
    }
    /**
     * 连接服务器后第一条数据
     * @param is
     */
    public static String getCurrent(InputStream is) {
        Map<String,Object> dataMap = getContent(is);
        byte[] data = (byte[])dataMap.get("data");
        byte[] meterNo = new byte[6];
        byte[] newData = new byte[data.length-6];
        Map<String,Object> map = getMeterNo(data);
        meterNo = (byte[])map.get("meterNo");
        newData = (byte[])map.get("data");
        dataMap.put("meterNo",meterNo);
        dataMap.put("meterData",newData);
        getData(dataMap);
        return MeterUtil.get16(meterNo);
    }


}
