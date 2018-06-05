package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.NoticeService;
import fixed.Service.RoleService;
import fixed.parameter.Parameter;
import fixed.util.Util;

import java.util.HashMap;
import java.util.Map;

public class NoticeController extends Controller {
    static NoticeService service = new NoticeService();


    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        SqlPara count = Db.getSqlPara("notice.queryCount",kv);
        SqlPara query = Db.getSqlPara("notice.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),count.getPara() ));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("n_type",getPara("n_type","") );
        data.put("n_content",getPara("n_content","") );
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","n_id");
        map.put("id",getPara("n_id"));
        map.put("tableName","fixed_sys_notice");
        map.put("data",data);

        renderJson(service.save(map,getParaToInt("loginuserid",0)));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("notice.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete(Db.getSql("notice.delete"),getPara("default_id_")));
    }


}

