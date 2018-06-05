package specific.service.recharge;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import fixed.Service.BaseService;
import fixed.util.Util;
import specific.service.system.MeterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeService extends BaseService {
    MeterService service = new MeterService();

    public Map<String, Object> recharge(List<Map<String,Object>> list,int loginuserid){
        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object m_id =  map.get("m_id");
            Object m_no =  map.get("m_no");
            String myMsg= service.isAvailable(m_id,m_no);
            if(myMsg.length()<1){
                Object p_money =  map.get("p_money");
                Object p_price =  map.get("p_price");
                Object o_remarks =  map.get("o_remarks");


                Record record = new Record();
                record.set("p_money", p_money);
                record.set("p_price", p_price);
                record.set("o_remarks", o_remarks);
                record.set("o_type",1);
                record.set("o_state",1);
                record.set("m_id", m_id);
                record.set("create_time", Util.getCurrentTime());
                record.set("create_user", loginuserid);
                boolean bool = Db.save("sys_order", record);
                if(bool){
                    success++;
                }else{
                    error++;
                    myMsg=m_no+":更新失败";
                }
            }else{
                error++;
            }
            msg += myMsg;
        }
        if(success>0){
            return Util.getResultMap(Util.state_ok,"成功:"+success+",失败:"+error+"."+msg,null);
        }else{
            return Util.getResultMap(Util.state_err,"操作失败."+msg,null);
        }
    }





















    public Map<String, Object> empty( List<Map<String,Object>> list){
        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object m_id =  map.get("m_id");
            Object m_no =  map.get("m_no");
            String myMsg= service.isAvailable(m_id,m_no);
            if(myMsg.length()<1){
                Record user = Db.findFirst(Db.getSql("meter.queryById"),m_id);
                Record record = new Record();
                double p_money = 0;
                try {
                    p_money = -user.getDouble("m_money");
                }catch (Exception e){

                }
                record.set("p_money",p_money);
                record.set("p_price", 0);
                record.set("o_remarks", "");
                record.set("o_type", -1);
                record.set("o_state",1);
                record.set("m_id",m_id);
                record.set("create_time", Util.getCurrentTime());
                boolean bool = Db.save("sys_order", record);

                if(bool){
                    success++;
                }else{
                    error++;
                    myMsg=m_no+":更新失败";
                }
            }else{
                error++;
            }
            msg += myMsg;
        }
        if(success>0){
            return Util.getResultMap(Util.state_ok,"成功:"+success+",失败:"+error+"."+msg,null);
        }else{
            return Util.getResultMap(Util.state_err,"操作失败."+msg,null);
        }
    }
}
