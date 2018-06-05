package fixed.Controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import fixed.Service.JurisdictionService;
import fixed.Service.MenuService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JurisdictionController extends Controller {
    static JurisdictionService service = new JurisdictionService();

    public void getTree(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("r_id",getParaToInt("r_id",0) );
        data.put("j_id",getParaToInt("j_id",0) );
        renderJson(service.getTree(data ));
    }



    public void getJurisdiction(){
        renderJson(service.queryByAll(Db.getSql("jurisdiction.queryByUser"),getPara("loginuserid","0")));
    }
    public void query(){
        Kv kv = Kv.create();
        Util.setParaVal(kv,"j_name",getPara("j_name"));
        Util.setParaVal(kv,"r_id",getPara("r_id"));
        SqlPara count = Db.getSqlPara("jurisdiction.queryCount",kv);
        SqlPara query = Db.getSqlPara("jurisdiction.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),count.getPara() ));
    }





    public void save(){
        //判断是否存在角色
        Record user = Db.findFirst(Db.getSql("role.queryByName"),getPara("r_name",""));
        String j_id =  getPara("j_id");
        if(user!=null){
            if(j_id==null||j_id.length()==0){
                renderJson(Util.getResultMap(Util.state_err,"角色已存在",null));
                return;
            }
        }else{
            //创建角色
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("r_name",getPara("r_name","") );
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("primaryKey","r_id");
            map.put("id",getPara("r_id"));
            map.put("tableName","fixed_sys_role");
            map.put("data",data);
            service.save(map,getParaToInt("loginuserid",0));
            user = Db.findFirst(Db.getSql("role.queryByName"),getPara("r_name",""));
        }


        if(user!=null){
            Map<String,Object> data2 = new HashMap<String,Object>();
            data2.put("j_name",getPara("j_name","") );
            data2.put("r_id",user.get("r_id"));

            Map<String,Object> map2 = new HashMap<String,Object>();
            map2.put("primaryKey","j_id");
            map2.put("id",getPara("j_id"));
            map2.put("tableName","fixed_sys_jurisdiction");
            map2.put("data",data2);

            List<Map<String,Object>> ids = JSONSerializer.deserialize(getPara("ids"),List.class);
            renderJson(service.save(map2,ids,getParaToInt("loginuserid",0)));
            return;
        }

        renderJson(Util.getResultMap(Util.state_err,"保存失败",null));
    }

    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("jurisdiction.queryById"),getPara("default_id_")));
    }
    @Before(Tx.class)
    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete2(Db.getSql("jurisdiction.delete1"),Db.getSql("jurisdiction.delete"),getPara("default_id_")));
    }

}