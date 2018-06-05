package fixed.test;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.ExcelUtil;
import fixed.util.JSONSerializer;
import fixed.websocket.MsgBean;
import fixed.util.RedisUtil;
import fixed.websocket.NotifyBean;
import fixed.websocket.SignOutBean;
import fixed.websocket.WebSocket;
import specific.Task.Task10;
import specific.Util.MeterUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test extends Controller {

    public static void main(String[] args) {
        save();
       /*
        String str = "968f";
        String str1 = str.substring(0,2);
int a = 0;
        int a1 = Integer.parseInt(str1,16);
        int a2 = Integer.parseInt("8f",16);

       a = a1 & a2;
System.out.println(a);
System.out.println(Integer.toHexString(a));
        Instructions ins= new Instructions();
        Instructions ins2= new Instructions();
        byte [] in =  ins.realTimeData("43000000620","046004620463046404650266026704680C74");
        byte [] in2 = ins2.realTimeData("43000551903","046004620463046404650266026704680C74");

        System.out.println("-----------");
        System.out.println(MeterUtil.get16(in));
        System.out.println(MeterUtil.get16(in2));
        System.out.println("-----------");

        String DeString = "SM!TC6007274400000001601CRC0";
        String num =  DeString.substring(22,24);*/
       // Db.update(Db.getSql("meter.setToken"), "123456", "123456","43000000620");
     /*   String  tStringBuf="SM!GT60088844000000016001518321941858519133457717125276974344511";
        String tStringBuf2="SM!GT6007274400000001600";
 ///       String DeString = AES.Decrypt(tStringBuf.toString());
        String str =  tStringBuf.substring(0,5);
        String no =  tStringBuf.substring(11,22);
        String meterNo1 = tStringBuf.substring(24,44);
        String meterNo2 = tStringBuf.substring(44,64);
        System.out.println(str);
        System.out.println(no);
        System.out.println(meterNo1);
        System.out.println(meterNo2);
        System.out.println(tStringBuf2.length());
        Instructions ins= new Instructions();
        ins.register("43000000513");

        System.out.println(MeterUtil.Hex16ToByte( "681271010611043000000620064150538090A00103AADA5A16"));*/


    }



    public void query(){
        List<Record> ready1 = Db.find(Db.getSql("meter.queryByGL1"));
        for(Record record :ready1) {
            String EnString2 = Task10.getMsg(record, 3);
        }
        String ui_id = getPara("ui_id");
        String token =  getPara("token");
        NotifyBean bean = new NotifyBean(ui_id,token, "充值成功","token:",null);
        WebSocket.eventListenner.clientCache.send(bean);
    }
    public void query5(){
        String ui_id = getPara("ui_id");
        String token =  getPara("token");
        SignOutBean bean = new SignOutBean(ui_id,token, "长时间");
        WebSocket.eventListenner.clientCache.send(bean);
    }

    public void query2(){
        List<Record> ready1 = Db.find(Db.getSql("meter.queryByReady1"));
        for(Record record :ready1){
            String ui_kmf_password_check = record.get("ui_kmf_password_check");
            String ui_kmf_password = record.get("ui_kmf_password");
            String ui_kmf_sgc = record.get("ui_kmf_sgc");
            String ui_kmf_no = record.get("ui_kmf_no");
            String m_id = "";
            String m_power ="";
            String p_electric ="";
            String o_id ="";
            try {
                m_id = ""+record.getInt("m_id");
            }catch (Exception e){
            }
            try {
                 m_power = ""+(int)(record.getDouble("m_power")*1);
            }catch (Exception e){
            }
            try {
                p_electric = ""+(int)(record.getDouble("p_electric")*10);
            }catch (Exception e){
            }
            try {
                o_id = ""+record.getInt("o_id");
            }catch (Exception e){
            }

        }

/*

        Db.update(Db.getSql("meter.setToken"), "123456", "123456","43000000620");
        String msg =  getPara("msg");
        String s = "SM?TM"+msg;
        byte [] b = s.getBytes();
        String crc = s+CRC16Util.getCRC_ibm(b);
        System.out.println("744444444444444444444444444444444444444");
        System.out.println(crc);
        Socket socket = TokenSocketServer.SocketMap;
        OutputStream os= null;
      //  String msg1 = "SM?GT60072744000000016345678960A9B5069982DEAB2C33F";
       // String crc2 =  CRC16Util.calcCrc16(msg1.getBytes());
        String EnString2 = AES.Encrypt(crc);
        System.out.println(EnString2);
        try {
            os = socket.getOutputStream();
            os.write(EnString2.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

/*
            String msg =  getPara("msg");
       System.out.println(msg.getBytes().toString());
        byte[] newDate  = msg.getBytes();
        System.out.println(MeterUtil.get16(newDate));





     //   System.out.println(MeterUtil.Hex16ToByte(msg));
        Socket socket = TokenSocketServer.SocketMap;
        OutputStream os= null;
        try {
            os = socket.getOutputStream();
            os.write(newDate);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        renderJson("555");
    }
    public void query3(){
        renderJson(RedisUtil.getCurrentALL());
    }

    public static void save(){
        File file = new File("C:\\\\Users\\\\10259\\\\Desktop\\\\a.xls");
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> valMap = new HashMap<>();
        ExcelUtil.setMapping(list,"编号","bh",ExcelUtil.STRING);
        ExcelUtil.setMapping(list,"id","id",ExcelUtil.STRING);
        List<Map<String, Object>> newlist = ExcelUtil.readExecl(file, list, valMap);
        System.out.println(newlist);
    }
    public void save2(){
        String setting = getPara("setting");
        Map<String,Object> valMap  = JSONSerializer.deserialize(setting,Map.class);

        File file = new File("C:\\\\Users\\\\10259\\\\Desktop\\\\a.xls");
        List<Map<String,Object>> list = new ArrayList<>();
        List<Map<String, Object>> newlist = ExcelUtil.readExecl(file, list, valMap);
        System.out.println(newlist);
    }
  /*
  导出execl
  public  void execl(){
        List<Map<String, Object>> valist= new ArrayList<Map<String, Object>>();

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("number", "1");
        map1.put("community", "测试社区");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("number", "2");
        map2.put("community", "测试社区2");
        valist.add(map1);
        valist.add(map2);

        List<Map<String, Object>> keyList= new ArrayList<Map<String, Object>>();
        Map<String, Object> map11 = new HashMap<String, Object>();
        map11.put("number", "bh");
        map11.put("community", "社区");
        keyList.add(map11);
        File file= ExcelUtil.toExecl("nh",valist,keyList,"aaa");
        renderFile(file);
    }
导入execl
    public  void excel(){
        File file = new File("E:\\工作资料\\车务段\\新典库解读.xls");
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        ExcelUtil.setMapping(list, "序号", "xh", ExcelUtil.INT);
        ExcelUtil.setMapping(list, "专业系统", "zyxt", ExcelUtil.STRING);
        ExcelUtil.setMapping(list, "安全风险项", "aqfxx", ExcelUtil.STRING);
        List<Map<String,Object>> newList = ExcelUtil.readExecl(file, list, null);
        renderJson(newList);
    }
    导出word
    public  void word(){

        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        map1.put("number", "1");
        map1.put("community", "测试社区");
        map1.put("address", "位置");
        map1.put("Inspection", "检查组");
        map1.put("time", "2018-01-10");
        map1.put("problem", "问题");
        map1.put("pic1", "无");
        map1.put("time2", "2018-01-11");
        map1.put("opinion", "好");
        map1.put("time3", "2018-01-12");
        map1.put("pic2", "无");

        map2.put("number", "1");
        map2.put("community", "测试社区");
        map2.put("address", "位置");
        map2.put("Inspection", "检查组");
        map2.put("time", "2018-01-10");
        map2.put("problem", "问题");
        map2.put("pic1", "无");
        map2.put("time2", "2018-01-11");
        map2.put("opinion", "好");
        map2.put("time3", "2018-01-12");
        map2.put("pic2", "无");
        lists.add(map1);
        lists.add(map2);

        Map<String, Object> dataMap = new HashMap<String, Object>();

        dataMap.put("name", "测试社区");
        dataMap.put("month", "1月");
        dataMap.put("lists", lists);
        System.out.println(getSession().getServletContext().getRealPath(""));
        File file= WordUtil.downloadWord(dataMap,getSession().getServletContext().getRealPath("")+"WEB-INF/classes/word/","test4.ftl","E:/临时备份/18.3.15/合同协议.doc");
        renderFile(file);
    }*/


}
