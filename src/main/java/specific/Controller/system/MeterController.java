package specific.Controller.system;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.MD5Util;
import fixed.util.RedisUtil;
import fixed.util.Util;
import com.jfinal.core.Controller;
import specific.service.system.MeterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeterController extends Controller {
    static MeterService service = new MeterService();

    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "m_no", getPara("m_no"));
        Util.setParaVal(kv, "ui_name", getPara("ui_name"));
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        Util.setParaVal(kv,"ui_landlord_id",getPara("ui_landlord_id"));
        Util.setParaVal(kv,"ui_tenant_id",getPara("ui_tenant_id"));
        Util.setParaVal(kv,"ui_manage_id",getPara("ui_manage_id"));
        SqlPara count = Db.getSqlPara("meter.queryCount", kv);
        SqlPara query = Db.getSqlPara("meter.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), count.getPara()));
    }
     public void setGL() {
        List<Map<String, Object>> list = JSONSerializer.deserialize(getPara("m_id"),List.class);
        renderJson(service.setGL(list));
    }
    public void setKG() {
        List<Map<String, Object>> list = JSONSerializer.deserialize(getPara("m_id"),List.class);
        renderJson(service.setKG(list,getParaToInt("loginuserid",0)));
    }

    public void setQD() {
        List<Map<String, Object>> list = JSONSerializer.deserialize(getPara("m_id"),List.class);
        renderJson(service.setQD(list));
    }

    public void save() {
        System.out.println(getPara("ui_landlord_id",""));
        Record user = Db.findFirst(Db.getSql("user.isComplete"),getPara("ui_landlord_id",""));
        if(user==null){
            renderJson(Util.getResultMap(Util.state_err,"房东没有上传kmf文件",null));
            return ;
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("m_no", getPara("m_no", ""));
        data.put("p_id", getParaToInt("p_id", 0));
        data.put("ui_id", getParaToInt("ui_id", 0));
        data.put("m_remarks", getPara("m_remarks", ""));
        data.put("m_address", getPara("m_address", ""));
        data.put("ui_landlord_id", getParaToInt("ui_landlord_id",0) );
        data.put("m_electricity_notice", getPara("m_electricity_notice", ""));
        if(getPara("m_id")==null||getPara("m_id").length()<1){
            user = Db.findFirst(Db.getSql("meter.isExistence"),getPara("m_no",""));
            if(user!=null){

                renderJson(Util.getResultMap(Util.state_err,"电表已存在",null));
                return;
            }
        }


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "m_id");
        map.put("id", getPara("m_id"));
        map.put("tableName", "sys_meter");
        map.put("data", data);

        renderJson(service.save(map,getParaToInt("loginuserid",0)));
    }

    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("meter.queryById"), getPara("default_id_")));
    }
    @Parameter.Must({"m_id"})
    public void removeError(){
        List<Map<String, Object>> list = JSONSerializer.deserialize(getPara("m_id"),List.class);
        renderJson(service.removeError(list));

    }

    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete(Db.getSql("meter.delete"), getPara("default_id_")));
    }
    public void statistics() {
        Record user = new Record();
        user.set("a","aaa");
        user.set("b","222");
        user.set("c","测试");
        renderJson( Util.getResultMapByID(user));
    }
    @Parameter.Must({"m_no"})
    public void realTimeData() {
        Map<String,Object> map = new HashMap<>();
        map.put("m_no",getPara("m_no", ""));
        boolean first = getParaToBoolean("first");
        RedisUtil.setValue(RedisUtil.Query,getPara("m_no", ""),map,20);
        Map<String,Object> dianya =  RedisUtil.getValue(RedisUtil.dianya,getPara("m_no", ""));
        Map<String,Object> dianliu =  RedisUtil.getValue(RedisUtil.dianliu,getPara("m_no", ""));
        Map<String,Object> yougonggonglv =  RedisUtil.getValue(RedisUtil.yougonggonglv,getPara("m_no", ""));
        Map<String,Object> shizaigonglv =  RedisUtil.getValue(RedisUtil.shizaigonglv,getPara("m_no", ""));
        Map<String,Object> wugonggonglv =  RedisUtil.getValue(RedisUtil.wugonggonglv,getPara("m_no", ""));
        Map<String,Object> gonglvyinshu =  RedisUtil.getValue(RedisUtil.gonglvyinshu,getPara("m_no", ""));
        Map<String,Object> pinlv =  RedisUtil.getValue(RedisUtil.pinlv,getPara("m_no", ""));
        Map<String,Object> yougongzongdianliang =  RedisUtil.getValue(RedisUtil.yougongzongdianliang,getPara("m_no", ""));
        Map<String,Object> shengyudianliang =  RedisUtil.getValue(RedisUtil.shengyudianliang,getPara("m_no", ""));

        Map<String,Object> returnMap = new HashMap<>();
        if(dianya!=null && dianya.size()>0){
            returnMap.put("dianya",dianya.get("value"));
        }
        if(dianliu!=null && dianliu.size()>0){
            returnMap.put("dianliu",dianliu.get("value"));
        }
        if(yougonggonglv!=null && yougonggonglv.size()>0){
            returnMap.put("yougonggonglv",yougonggonglv.get("value"));
        }

        if(shizaigonglv!=null && shizaigonglv.size()>0){
            returnMap.put("shizaigonglv",shizaigonglv.get("value"));
        }
        if(wugonggonglv!=null && wugonggonglv.size()>0){
            returnMap.put("wugonggonglv",wugonggonglv.get("value"));
        }
        if(gonglvyinshu!=null && gonglvyinshu.size()>0){
            returnMap.put("gonglvyinshu",gonglvyinshu.get("value"));
        }
        if(pinlv!=null && pinlv.size()>0){
            returnMap.put("pinlv",pinlv.get("value"));
        }
        if(yougongzongdianliang!=null && yougongzongdianliang.size()>0){
            returnMap.put("yougongzongdianliang",yougongzongdianliang.get("value"));
        }
        if(shengyudianliang!=null && shengyudianliang.size()>0){
            returnMap.put("shengyudianliang",shengyudianliang.get("value"));
        }
        if(returnMap!=null && returnMap.size()>0){
            returnMap.put("datetime",Util.getCurrentTime());
            renderJson(Util.getResultMap(Util.state_ok,"成功",returnMap));
        }else{
            if(first){
                renderJson(Util.getResultMap(Util.state_ok,"",null));
                return ;
            }
            returnMap.put("dianya","暂无");
            returnMap.put("dianliu","暂无");
            returnMap.put("yougonggonglv","暂无");
            returnMap.put("shizaigonglv","暂无");
            returnMap.put("wugonggonglv","暂无");
            returnMap.put("gonglvyinshu","暂无");
            returnMap.put("pinlv","暂无");
            returnMap.put("yougongzongdianliang","暂无");
            returnMap.put("shengyudianliang","暂无");
            returnMap.put("datetime",Util.getCurrentTime());
            Map<String,Object> newMap = RedisUtil.getValue(RedisUtil.lixian,getPara("m_no", ""));
            int num = 1;
            if(newMap==null||!newMap.containsKey("num")){
                renderJson(Util.getResultMap(Util.state_ok,"",returnMap));
                newMap=new HashMap<>();
                newMap.put("num",num);
            }else{
                num = Integer.parseInt(""+newMap.get("num"));
                num++;
                newMap.put("num",num);
                if(num>5){
                    renderJson(Util.getResultMap(Util.state_err,"电表离线，请检查网络",returnMap));
                }else{
                    renderJson(Util.getResultMap(Util.state_ok,"",returnMap));
                }
            }
            RedisUtil.setValue(RedisUtil.lixian,getPara("m_no", ""),newMap,30);
        }

    }
    public void queryByFree(){
        Map<String,Object> map = service.queryByAll(Db.getSql("meter.queryByFree"), getParaToInt("ui_landlord_id",0), getParaToInt("ui_id",0));
        renderJson(map);
    }
    public void queryByMe(){
        Map<String,Object> map =service.queryByMe(Db.getSql("meter.queryByMe"), getParaToInt("ui_id",0));
        renderJson(map);
    }
}