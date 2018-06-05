package specific.Util;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.websocket.MsgBean;
import fixed.util.RedisUtil;
import fixed.util.Util;
import fixed.websocket.NotifyBean;
import fixed.websocket.WebSocket;
import specific.Task.Task10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class TokenSocketServerReceiveThread extends Thread {
    // 和本线程相关的Socket
    InputStream is = null;
    OutputStream os = null;
    Socket socket = null;

    public TokenSocketServerReceiveThread(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }

    //线程执行的操作，响应客户端的请求
    public void run(){
        while(true){
            try {
                System.out.println("MeterUtil.get16(data)");
                StringBuffer tStringBuf=new StringBuffer ();
                byte[] date = new byte[1000];
                int size = is.read(date);
                char[] strDate=new char[size];
                for(int i=0;i<size;i++){
                    strDate[i]=(char)date[i];
                }

                tStringBuf.append(strDate);

                String DeString = AES.Decrypt(tStringBuf.toString());
                System.out.println("DeString-----------------------"+DeString);
                String str =  DeString.substring(0,5);

                if("SM!GT".equals(str)){
                    error(DeString);
                    setGT(DeString);

                }else if("SM!TM".equals(str)){
                    error(DeString);
                    setTM(DeString);
                }else if("SM!TC".equals(str)){
                    error(DeString);
                    setTC(DeString);
                }else{
                    String returnStr = "SM?backHeart";
                    String EnString = AES.Encrypt(returnStr);
                    os.write(EnString.getBytes());
                    os.flush();
                    System.out.println("返回："+EnString);
                    System.out.println("返回2："+EnString.getBytes());
                }



                System.out.println("a:"+date);
                System.out.println("b:"+strDate);
                System.out.println("c:"+tStringBuf.toString());
                System.out.println("长度:"+size);
                System.out.println(DeString);

            } catch (IOException e) {
                e.printStackTrace();
            }
            // Meter.getCurrent(is);
        }
    }
    public void setGT(String DeString){
        if(DeString.length()<64){
            return ;
        }
        String meterNo =  DeString.substring(11,22);
        String token1 = DeString.substring(24,44);
        String token2 = DeString.substring(44,64);
        System.out.println("---------------");
        System.out.println(token1);
        System.out.println(token2);
        System.out.println(meterNo);
        System.out.println("---------------");
        Db.update(Db.getSql("meter.setToken"), token1,token2,meterNo);
    }
    public void setTC(String DeString){
        if(DeString.length()<44){
            return ;
        }
        String num =  DeString.substring(5,11);
        String meterNo =  DeString.substring(11,22);
        System.out.println(num);
        System.out.println("RedisUtil.TC2："+RedisUtil.TC+""+num+meterNo);
        Map<String,Object> map  =RedisUtil.getValue(RedisUtil.TC,""+num+meterNo);
        System.out.println(map.toString());
        if (map.containsKey("o_id")&&(""+map.get("o_id")).length()>0) {
            String token = DeString.substring(24,44);
            String o_id = ""+map.get("o_id");
            System.out.println("---------------");
            System.out.println("setTC");
            System.out.println(token);
            System.out.println(o_id);
            System.out.println("---------------");
            Db.update(Db.getSql("order.setToken"), token,o_id);
            Db.update(Db.getSql("meter.setMeterMoney"), o_id,o_id,o_id);
            Record newInfo = Db.findFirst(Db.getSql("order.getOrderInfo"), o_id);
            try{
                NotifyBean bean = new NotifyBean(""+map.get("create_user"),"", "充值成功","token:"+token,newInfo.getColumns());
                WebSocket.eventListenner.clientCache.send(bean);
            }catch (Exception e){
                Util.SOP(e);
            }

        }

    }
    public void setTM(String DeString){
        if(DeString.length()<44){
            return ;
        }
        String num =  DeString.substring(5,11);
        String meterNo =  DeString.substring(11,22);
        System.out.println(num);
        Map<String,Object> map  =RedisUtil.getValue(RedisUtil.TM,""+num+meterNo);
        System.out.println(map.toString());
        if (map.containsKey("type")&&(""+map.get("type")).length()>0) {
            String token = DeString.substring(24,44);
            String m_no = ""+map.get("m_no");

            String type = ""+map.get("type");
            System.out.println("---------------");
            System.out.println("key1:"+RedisUtil.TM+""+num+meterNo);
            System.out.println("setTM");
            System.out.println(token);
            System.out.println(m_no);
            System.out.println("---------------");
            if("gl".equals(type)){
                Db.update(Db.getSql("meter.setTokenGL"), token,m_no);
            }else if("qdl".equals(type)){
                int o_id = Integer.parseInt(""+map.get("o_id"));
                Db.update(Db.getSql("meter.setTokenQDL"), token,o_id);
                Db.update(Db.getSql("meter.empty"),m_no);
            }else if("qd".equals(type)){
                Db.update(Db.getSql("meter.setTokenQD"), token,m_no);
            }else if("kgfd".equals(type)){
                Db.update(Db.getSql("meter.setTokenKGFD"), token,m_no);
            }else if("kgfk".equals(type)){
                Db.update(Db.getSql("meter.setTokenKGFK"), token,m_no);
            }
        }

    }
    public void error(String DeString){
        System.out.println(DeString);
        String num =  DeString.substring(22,24);
        switch (num)
        {
            case "00":System.out.print("正确");break;
            case "11":System.out.print("CRC 校验错误（服务器发送给客户端的命令）");break;
            case "01":System.out.print("CRC 校验错误");break;
            case "02":System.out.print("无效命令");break;
            case "03":System.out.print("数据错误或超出允许设置范围");break;
            case "04":System.out.print("数据密钥校验错误");break;
            case "05":System.out.print("ID 检查错误");break;
            case "06":System.out.print("父密钥校验错误");break;
            case "07":System.out.print("工作密钥校验错误");break;
            case "08":System.out.print("密钥地址号无效");break;
            case "0F":System.out.print("模块已过期");break;
            case "0f":System.out.print("模块已过期");break;
            default:System.out.print("未知");break;
        }
    }
    public byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }
}
