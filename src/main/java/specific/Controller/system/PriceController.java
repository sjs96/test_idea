package specific.Controller.system;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.Util;
import specific.service.system.PriceService;

import java.util.HashMap;
import java.util.Map;

public class PriceController extends Controller {
    static PriceService service = new PriceService();

    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "p_name", getPara("p_name"));
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        Util.setParaVal(kv,"ui_landlord_id",getPara("ui_landlord_id"));
        Util.setParaVal(kv,"ui_tenant_id",getPara("ui_tenant_id"));
        Util.setParaVal(kv,"ui_manage_id",getPara("ui_manage_id"));
        SqlPara count = Db.getSqlPara("price.queryCount", kv);
        SqlPara query = Db.getSqlPara("price.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), count.getPara()));
    }


    public void save() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("p_name", getPara("p_name", ""));
        data.put("p_price", getPara("p_price", ""));
        data.put("p_remarks", getPara("p_remarks", ""));
        data.put("ui_landlord_id", getParaToInt("ui_landlord_id",0) );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "p_id");
        map.put("id", getPara("p_id"));
        map.put("tableName", "sys_price");
        map.put("data", data);

        renderJson(service.save(map,getParaToInt("loginuserid",0)));
    }

    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("price.queryById"), getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete(Db.getSql("price.delete"), getPara("default_id_")));
    }

    public void queryByAll() {
        renderJson(service.queryByAll(Db.getSql("price.queryByAll")));
    }
    public void queryByLandlord() {
        renderJson(service.queryByAll(Db.getSql("price.queryByLandlord"),getPara("ui_landlord_id")));
    }
}