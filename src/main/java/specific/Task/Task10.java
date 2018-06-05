package specific.Task;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.Service.BaseService;
import fixed.util.RedisUtil;
import fixed.util.Util;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import specific.Util.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task10 implements Job {
    final static int cz = 1;
    final static int zc = 2;
    final static int gl = 3;
    final static int qdl = 4;
    final static int qd = 5;
    final static int kgfd = 6;
    final static int kgfk = 7;
    BaseService service = new BaseService();
    static int num = 100000;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        OutputStream os = null;
        System.out.println("10秒循环开始");
        System.out.println("readyEmpty");
        readyEmpty();
        System.out.println("readyToken");
        readyToken();
        System.out.println("register");
        register();
        System.out.println("rechargeToken");
        rechargeToken();
        System.out.println("recharge");
        recharge();
        System.out.println("glToken");
        glToken();
        System.out.println("qdlToken");
        qdlToken();
        System.out.println("qdToken");
        qdToken();
        System.out.println("gl");
        gl();
        System.out.println("qdl");
        qdl();
        System.out.println("qd");
        qd();


        //  get111()


        System.out.println("10秒循环结束");


    }

    public void get111() {

        double total = (Runtime.getRuntime().totalMemory()) / (1024.0 * 1024);
        double max = (Runtime.getRuntime().maxMemory()) / (1024.0 * 1024);
        double free = (Runtime.getRuntime().freeMemory()) / (1024.0 * 1024);
        System.out.println("Java 虚拟机试图使用的最大内存量(当前JVM的最大可用内存) maxMemory(): " + max + "MB<br/>");
        System.out.println("Java 虚拟机中的内存总量(当前JVM占用的内存总数) totalMemory(): " + total + "MB<br/>");
        System.out.println("Java 虚拟机中的空闲内存量(当前JVM空闲内存) freeMemory(): " + free + "MB<br/>");
        System.out.println("因为JVM只有在需要内存时才占用物理内存使用，所以freeMemory()的值一般情况下都很小，<br/>" +
                "而JVM实际可用内存并不等于freeMemory()，而应该等于 maxMemory() - totalMemory() + freeMemory()。<br/>");
        System.out.println("JVM实际可用内存: " + (max - total + free) + "MB<br/>");
    }

    //获取注册tonken
    public void readyToken() {
        if (TokenSocketServer.getOutputStream() == null) {

            return;
        }
        List<Record> ready1 = Db.find(Db.getSql("meter.queryByReady1"));
        for (Record record : ready1) {
            String EnString2 = getMsg(record, zc);
            System.out.println(EnString2);
            try {
                TokenSocketServer.getOutputStream().write(EnString2.getBytes());
                TokenSocketServer.getOutputStream().flush();
                System.out.println("正在创建");
            } catch (IOException e) {
                Util.SOP(e);
                e.printStackTrace();
            }
        }
    }


    //清空数据
    public void readyEmpty() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("meter.queryByReady2"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in2 = ins.register("0" + m_no);//046004620C74
                System.out.println(MeterUtil.get16(in2));
                SocketServer.getOutputStream(m_no).write(in2);
                SocketServer.getOutputStream(m_no).flush();
            }
        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }
    }

    //注册
    public void register() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("meter.queryByReady3"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                String m_token1 = record.get("m_token1");
                String m_token2 = record.get("m_token2");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in3 = ins.setToken("0" + m_no, m_token1, m_token2);
                SocketServer.getOutputStream(m_no).write(in3);
                SocketServer.getOutputStream(m_no).flush();
            }
        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }


    //获取充值token
    public void rechargeToken() {
        try {
            if (TokenSocketServer.getOutputStream() == null) {
                return;
            }
            List<Record> ready1 = Db.find(Db.getSql("order.queryByRecharge"));
            for (Record record : ready1) {
                String EnString2 = getMsg(record, cz);
                try {
                    TokenSocketServer.getOutputStream().write(EnString2.getBytes());
                    TokenSocketServer.getOutputStream().flush();
                    Map<String, Object> map = new HashMap<>();
                    String o_id = "" + record.getInt("o_id");
                    String create_user = "" + record.getInt("create_user");
                    String m_no = "" + record.get("m_no");
                    map.put("o_id", o_id);
                    map.put("create_user", create_user);
                    RedisUtil.setValue(RedisUtil.TC, "" + num + m_no, map, 60 * 60 * 24);
                    System.out.println("正在创建" + EnString2);
                    System.out.println("RedisUtil.TC：" + RedisUtil.TC + "" + num + m_no);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }

    //获取功率token
    public void glToken() {
        try {
            if (TokenSocketServer.getOutputStream() == null) {

                return;
            }
            List<Record> ready1 = Db.find(Db.getSql("meter.queryByGL1"));
            for (Record record : ready1) {
                String EnString2 = getMsg(record, gl);
                try {
                    TokenSocketServer.getOutputStream().write(EnString2.getBytes());
                    TokenSocketServer.getOutputStream().flush();
                    Map<String, Object> map = new HashMap<>();
                    String m_no = "" + record.get("m_no");
                    map.put("m_no", m_no);
                    map.put("type", "gl");
                    RedisUtil.setValue(RedisUtil.TM, "" + num + m_no, map, 60 * 60 * 24);
                    System.out.println("正在创建");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Util.SOP(e);
            e.printStackTrace();
        }
    }
    //获取功率token
    public void kgFdToken() {
        try {
            if (TokenSocketServer.getOutputStream() == null) {

                return;
            }
            List<Record> ready1 = Db.find(Db.getSql("meter.queryByKgFd1"));
            for (Record record : ready1) {
                String EnString2 = getMsg(record,kgfd );
                try {
                    TokenSocketServer.getOutputStream().write(EnString2.getBytes());
                    TokenSocketServer.getOutputStream().flush();
                    Map<String, Object> map = new HashMap<>();
                    String m_no = "" + record.get("m_no");
                    map.put("m_no", m_no);
                    map.put("type", "kgfd");
                    RedisUtil.setValue(RedisUtil.TM, "" + num + m_no, map, 60 * 60 * 24);
                    System.out.println("正在创建");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Util.SOP(e);
            e.printStackTrace();
        }
    }
    //获取功率token
    public void kgFkToken() {
        try {
            if (TokenSocketServer.getOutputStream() == null) {

                return;
            }
            List<Record> ready1 = Db.find(Db.getSql("meter.queryByKgFk1"));
            for (Record record : ready1) {
                String EnString2 = getMsg(record, kgfk);
                try {
                    TokenSocketServer.getOutputStream().write(EnString2.getBytes());
                    TokenSocketServer.getOutputStream().flush();
                    Map<String, Object> map = new HashMap<>();
                    String m_no = "" + record.get("m_no");
                    map.put("m_no", m_no);
                    map.put("type", "kgfk");
                    RedisUtil.setValue(RedisUtil.TM, "" + num + m_no, map, 60 * 60 * 24);
                    System.out.println("正在创建");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Util.SOP(e);
            e.printStackTrace();
        }
    }
    //获取清电量token
    public void qdlToken() {
        try {
            if (TokenSocketServer.getOutputStream() == null) {
                return;
            }
            System.out.println(Db.getSql("meter.queryByQDL1"));
            List<Record> ready1 = Db.find(Db.getSql("meter.queryByQDL1"));
            System.out.println("ready1" + ready1.toString());
            for (Record record : ready1) {
                System.out.println("1");
                String EnString2 = getMsg(record, qdl);
                System.out.println("EnString2" + EnString2);
                try {
                    System.out.println("2");
                    TokenSocketServer.getOutputStream().write(EnString2.getBytes());
                    System.out.println("3");
                    TokenSocketServer.getOutputStream().flush();
                    System.out.println("4");
                    Map<String, Object> map = new HashMap<>();
                    String o_id = "" + record.getInt("o_id");
                    System.out.println("5");
                    String m_id = "" + record.getInt("m_id");
                    System.out.println("6");
                    String m_no = "" + record.get("m_no");
                    System.out.println("7");
                    map.put("m_id", m_id);
                    map.put("m_no", m_no);
                    map.put("o_id", o_id);
                    map.put("type", "qdl");
                    System.out.println("8");
                    RedisUtil.setValue(RedisUtil.TM, "" + num + m_no, map, 60 * 60 * 24);
                    System.out.println("正在创建");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }

    //获取窃电token
    public void qdToken() {
        try {
            if (TokenSocketServer.getOutputStream() == null) {

                return;
            }
            List<Record> ready1 = Db.find(Db.getSql("meter.queryByQD1"));
            for (Record record : ready1) {
                String EnString2 = getMsg(record, qd);
                try {
                    TokenSocketServer.getOutputStream().write(EnString2.getBytes());
                    TokenSocketServer.getOutputStream().flush();
                    Map<String, Object> map = new HashMap<>();
                    String m_id = "" + record.getInt("m_id");
                    String m_no = "" + record.get("m_no");
                    map.put("m_id", m_id);
                    map.put("m_no", m_no);
                    map.put("type", "qd");
                    RedisUtil.setValue(RedisUtil.TM, "" + num + m_no, map, 60 * 60 * 24);
                    System.out.println("正在创建");
                    System.out.println("key1:" + RedisUtil.TM + "" + num + m_no);
                } catch (IOException e) {
                    Util.SOP(e);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }

    //充值
    public void recharge() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("order.queryByRecharge2"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                String o_token = record.get("o_token");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in3 = ins.setToken("0" + m_no, o_token);
                System.out.println(MeterUtil.get16(in3));

                SocketServer.getOutputStream(m_no).write(in3);
                SocketServer.getOutputStream(m_no).flush();
            }

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }

    //充值
    public void gl() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("meter.queryByGL2"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                String o_token = record.get("m_token_gl");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in3 = ins.setToken("0" + m_no, o_token);
                System.out.println(MeterUtil.get16(in3));

                SocketServer.getOutputStream(m_no).write(in3);
                SocketServer.getOutputStream(m_no).flush();
            }

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }
    //充值
    public void kgfd() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("meter.queryByKgFd2"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                String o_token = record.get("m_token_kg_fd");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in3 = ins.setToken("0" + m_no, o_token);
                System.out.println(MeterUtil.get16(in3));

                SocketServer.getOutputStream(m_no).write(in3);
                SocketServer.getOutputStream(m_no).flush();
            }

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }
    //充值
    public void kgfk() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("meter.queryByKgFk2"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                String o_token = record.get("m_token_kg_fk");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in3 = ins.setToken("0" + m_no, o_token);
                System.out.println(MeterUtil.get16(in3));

                SocketServer.getOutputStream(m_no).write(in3);
                SocketServer.getOutputStream(m_no).flush();
            }

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }

    //清电量
    public void qdl() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("meter.queryByQDL2"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                String o_token = record.get("o_token");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in3 = ins.setToken("0" + m_no, o_token);
                System.out.println(MeterUtil.get16(in3));

                SocketServer.getOutputStream(m_no).write(in3);
                SocketServer.getOutputStream(m_no).flush();
            }

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }

    //充值
    public void qd() {
        try {

            List<Record> ready1 = Db.find(Db.getSql("meter.queryByQD2"));
            for (Record record : ready1) {
                String m_no = record.get("m_no");
                String o_token = record.get("m_token_qd");
                if (SocketServer.getOutputStream(m_no) == null) {
                    return;
                }
                Instructions ins = new Instructions();
                byte[] in3 = ins.setToken("0" + m_no, o_token);
                System.out.println(MeterUtil.get16(in3));

                SocketServer.getOutputStream(m_no).write(in3);
                SocketServer.getOutputStream(m_no).flush();
            }

        } catch (IOException e) {
            Util.SOP(e);
            e.printStackTrace();
        }

    }


    public static String getMsg(Record record, int type) {
        String msg = "";
        try {
            num = num + 1;
            if (num > 999999) {
                num = 100000;
            }
            String m_no = record.get("m_no");
            String ui_kmf_password_check = record.get("ui_kmf_password_check");
            String ui_kmf_password = record.get("ui_kmf_password");
            String ui_kmf_sgc = record.get("ui_kmf_sgc");
            String ui_kmf_no = record.get("ui_kmf_no");
            String m_power = "";
            String m_kg_fd = "";
            String m_kg_fk = "";
            String p_electric = "";

            try {
                m_power = "" + (int) (record.getDouble("m_power") * 1);
            } catch (Exception e) {
                Util.SOP(e, "m_power");
            }
            try {
                m_kg_fd = "" + (int) (record.getDouble("m_kg_fd") * 1);
            } catch (Exception e) {
                Util.SOP(e, "m_kg_fd");
            }
            try {
                m_kg_fk = "" + (int) (record.getDouble("m_kg_fk") * 1);
            } catch (Exception e) {
                Util.SOP(e, "m_kg_fk");
            }
            try {
                p_electric = "" + (int) (record.getDouble("p_electric") * 10);
            } catch (Exception e) {
                Util.SOP(e, "p_electric");
            }


            for (int i = p_electric.length(); i < 8; i++) {
                p_electric = "0" + p_electric;
            }
            for (int i = m_power.length(); i < 8; i++) {
                m_power = "0" + m_power;
            }
            for (int i = m_kg_fd.length(); i < 8; i++) {
                m_kg_fd = "0" + m_kg_fd;
            }
            for (int i = m_kg_fk.length(); i < 8; i++) {
                m_kg_fk = "0" + m_kg_fk;
            }

            if (m_no == null || m_no.length() != 11
                    || ui_kmf_password_check == null || ui_kmf_password_check.length() != 4
                    || ui_kmf_password == null || ui_kmf_password.length() != 16
                    || ui_kmf_sgc == null || ui_kmf_sgc.length() != 6
                    || ui_kmf_no == null || ui_kmf_no.length() != 2) {
                System.out.println(m_no + "房东kmf文件有问题");
                System.out.println(m_no);
                System.out.println(ui_kmf_password_check);
                System.out.println(ui_kmf_password);
                System.out.println(ui_kmf_sgc);
                System.out.println(ui_kmf_no);
                return msg;
            }

            switch (type) {
                case zc:
                    msg = "SM?GT" + num + m_no + ui_kmf_sgc + ui_kmf_no + ui_kmf_password + ui_kmf_password_check;
                    break;
                case cz:
                    msg = "SM?TC" + num + m_no + ui_kmf_sgc + "00" + p_electric + ui_kmf_no + ui_kmf_password + ui_kmf_password_check;
                    break;
                case gl:
                    msg = "SM?TM" + num + m_no + ui_kmf_sgc + "00" + m_power + ui_kmf_no + ui_kmf_password + ui_kmf_password_check;
                    break;
                case qdl:
                    msg = "SM?TM" + num + m_no + ui_kmf_sgc + "01" + m_power + ui_kmf_no + ui_kmf_password + ui_kmf_password_check;
                    break;
                case qd:
                    msg = "SM?TM" + num + m_no + ui_kmf_sgc + "05" + m_power + ui_kmf_no + ui_kmf_password + ui_kmf_password_check;
                    break;
                case kgfk:
                    msg = "SM?TM" + num + m_no + ui_kmf_sgc + "08" + m_kg_fk + ui_kmf_no + ui_kmf_password + ui_kmf_password_check;
                    break;
                case kgfd:
                    msg = "SM?TM" + num + m_no + ui_kmf_sgc + "09" + m_kg_fd + ui_kmf_no + ui_kmf_password + ui_kmf_password_check;
                    break;

            }

            if (msg.length() > 0) {
                byte[] b = msg.getBytes();
                String crc = msg + CRC16Util.getCRC_ibm(b);
                msg = AES.Encrypt(crc);
            }
        } catch (Exception e) {
            Util.SOP(e);
        }

        return msg;
    }
}
