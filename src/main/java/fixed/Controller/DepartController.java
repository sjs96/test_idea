package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.DepartService;
import fixed.parameter.Parameter;
import fixed.util.Util;

import java.util.HashMap;
import java.util.Map;


public class DepartController extends Controller {
    static DepartService service = new DepartService();
/*   @Jurisdiction.Role("jurisdictionjurisdicjurisdiction")
    @Parameter.Must({"loginuserid", "page"})*/
    public void query(){
        Kv kv = Kv.create();
        Util.setParaVal(kv,"dp_name",getPara("dp_name"));
        Util.setParaVal(kv,"dp_tel",getPara("dp_tel"));
        Util.setParaVal(kv,"dp_topid",getPara("dp_topid"));
        SqlPara count = Db.getSqlPara("depart.queryCount",kv);
        SqlPara query = Db.getSqlPara("depart.query",kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),count.getPara() ));
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("dp_name",getPara("dp_name","") );
        data.put("dp_tel",getPara("dp_tel",""));
        data.put("dp_topid",getPara("dp_topid",""));
        data.put("dp_sort",getPara("dp_sort",""));
        data.put("dp_note",getPara("dp_note",""));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","dp_id");
        map.put("id",getPara("dp_id"));
        map.put("tableName","fixed_sys_depart");
        map.put("data",data);

        renderJson(service.save(map,getPara("po_ids",""),getParaToInt("loginuserid",0)));
    }

    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("depart.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete(Db.getSql("depart.delete"),getPara("default_id_")));
    }

    public void getTree(){
        renderJson(service.getTree(Db.getSql("depart.queryByAll")));
    }

    public void queryByPost(){
        renderJson(service.queryByPost(Db.getSql("depart.queryByPost"),getPara("dp_id")));
    }




}
