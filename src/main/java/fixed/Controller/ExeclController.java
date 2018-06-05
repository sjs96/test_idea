package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.ExeclService;
import fixed.util.ExcelUtil;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import fixed.util.ValueUtile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExeclController extends Controller {
    static ExeclService service = new ExeclService();
    public  void  execl(){

        Map<String, Object> setUpMap = JSONSerializer.deserialize(getPara("setUp_execl_"),Map.class);
        Map<String, Object> paramsMap = JSONSerializer.deserialize(getPara("setUp_execl_params"),Map.class);
        String name = (String)setUpMap.get("name");
        String sql = (String)setUpMap.get("sql");
        List<Map<String, Object>> keyList = (List<Map<String, Object>>)setUpMap.get("list");


        Map<String, Object> map = queryByAll(sql,paramsMap);
        List<Record> records = (List)map.get("json");
        List<Map<String, Object>> valist= new ArrayList<Map<String, Object>>();
        for(int i=0;i<records.size();i++){
            valist.add(records.get(i).getColumns());
        }

        Map<String, Object> fileName= ExcelUtil.toExecl(name,valist,keyList);
        renderJson(Util.getResultMapByExecl(fileName));
    }

    public Map<String, Object> queryByAll( String sql, Map<String, Object> setUpMap){
        Kv kv = Kv.create();
        for (Map.Entry<String, Object> entry : setUpMap.entrySet()) {
            Util.setParaVal(kv,entry.getKey(),entry.getValue()+"");
        }
        SqlPara mysql = Db.getSqlPara(sql,kv);
        return service.queryByAll(mysql);
    }

}
