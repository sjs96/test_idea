package specific.service.system;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.Service.BaseService;
import fixed.Service.UserService;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeterService extends BaseService{
    static UserService service = new UserService();
    public Map<String, Object> save(Map<String,Object> map,int loginuserid ){
        Map<String, Object> data = (Map<String, Object>)map.get("data");
        if(checkNo(data.get("m_no")+"")){
            return super.save(map,loginuserid);
        }else{
            return Util.getResultMap(Util.state_err,"电表输入有误",null);
        }
    }
    public String isAvailable(Object m_id,Object m_no){
        String msg ="";
        List<Record> list = Db.find(Db.getSql("meter.isAvailable"),m_id,m_id,m_id,m_id,m_id,m_id,m_id,m_id,m_id,m_id,m_id);
        if (list!=null&&list.size()>0){
            for(Record record :list){
                if(msg.length()>0){
                    msg+=",";
                }else{
                    msg =m_no+":";
                }
                msg+=record.getStr("msg");
            }
        }
        return msg;
    }
    public Map<String, Object> setGL(List<Map<String,Object>> list ){
        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object m_id =  map.get("m_id");
            Object m_no =  map.get("m_no");
            Object m_power =  map.get("m_power");
            String myMsg= isAvailable(m_id,m_no);
            if(myMsg.length()<1){
                int  i = Db.update(Db.getSql("meter.setGL"),  m_power,m_id);

                if(i>0){
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
            return Util.getResultMap(Util.state_ok,"成功:"+success+",失败:"+error+"。"+msg,null);
        }else{
            return Util.getResultMap(Util.state_err,"操作失败."+msg,null);
        }
    }
    public Map<String, Object> removeError(List<Map<String,Object>> list ){
        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object m_id =  map.get("m_id");
            Object m_no =  map.get("m_no");
            int  i = Db.update(Db.getSql("meter.removeError"),  m_id);;

            if(i>0){
                success++;
            }else{
                error++;
            }
        }
        if(success>0){
            return Util.getResultMap(Util.state_ok,"成功:"+success+",失败:"+error+"。"+msg,null);
        }else{
            return Util.getResultMap(Util.state_err,"操作失败."+msg,null);
        }
    }
    public Map<String, Object> setKG(List<Map<String,Object>> list,int loginuserid ){
        int role =service.getRole(loginuserid);

        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object m_id =  map.get("m_id");
            Object m_no =  map.get("m_no");
            Object start =  map.get("start");
            String myMsg= isAvailable(m_id,m_no);
            if(myMsg.length()<1){
                int  i = 0;
                if( role == UserService.role_fd||role == UserService.role_fdgly){
                    i = Db.update(Db.getSql("meter.setKGFD"),  start,  start,m_id);
                }else{
                    i = Db.update(Db.getSql("meter.setKGFK"),  start,m_id);
                }

                if(i>0){
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
            return Util.getResultMap(Util.state_ok,"成功:"+success+",失败:"+error+"。"+msg,null);
        }else{
            return Util.getResultMap(Util.state_err,"操作失败."+msg,null);
        }
    }
    public Map<String, Object> setQD(List<Map<String,Object>> list ){
        int success = 0;
        int error = 0;
        String msg = "";
        for(Map<String,Object> map :list ){
            if(msg.length()>0){
                msg+=",";
            }
            Object m_id =  map.get("m_id");
            Object m_no =  map.get("m_no");
            String myMsg= isAvailable(m_id,m_no);
            if(myMsg.length()<1){
                String m_no_type= (""+m_no).substring(0,2);
                if("43".equals(m_no_type)){
                    error++;
                    myMsg=m_no+":更新失败,WIFI表没有窃电功能";
                }else{
                    int  i = Db.update(Db.getSql("meter.setQD"),  m_id);;

                    if(i>0){
                        success++;
                    }else{
                        error++;
                        myMsg=m_no+":更新失败";
                    }
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
    public  boolean  checkNo(String aaa){
        if(aaa==null || aaa.length()!=11){
            return false;
        }
        String newStr = aaa.substring(0,10);
        byte [] a= new byte[10];
        for(int i=0;i<newStr.length();i++){
            a[i]=Byte.valueOf(newStr.substring(i,i+1));
        }

        int buff =0;
        byte [] temp= new byte[5];
        for(int i=0;i<5;i++){
            temp[i] = (byte) (a[i*2+1]*2);
            buff+=a[i*2];
            buff=buff+temp[i]/10+temp[i]%10;
        }
        buff=buff%10;
        if(0==buff){
            System.out.println(buff);
            newStr = newStr+buff;
        }else{
            System.out.println(10-buff);
            newStr = newStr+(10-buff);
        }
        if(newStr.equals(aaa)){
            return true;
        }
        return false;
    }
    public Map<String, Object> queryByMe(String QuerySql, Object... obj){
        List<Record> list = Db.find(QuerySql,obj);
        List<Integer> newList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            newList.add(Integer.parseInt(record.get("m_id")+""));
        }
        if(newList!=null&&newList.size()>0){
            return Util.getResultMap(Util.state_ok,"有数据",newList);
        }
        return Util.getResultMap(Util.state_err_bypage,"暂无数据",newList);

    }
}
