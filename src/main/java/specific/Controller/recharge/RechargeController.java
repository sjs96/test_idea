package specific.Controller.recharge;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.Util;
import specific.service.recharge.RechargeService;
import specific.service.system.MeterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeController   extends Controller {
    static RechargeService service = new RechargeService();
    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "m_no", getPara("m_no"));
        Util.setParaVal(kv, "ui_name", getPara("ui_name"));
        Util.setParaVal(kv,"ui_landlord_id",getPara("ui_landlord_id"));
        Util.setParaVal(kv,"ui_tenant_id",getPara("ui_tenant_id"));
        Util.setParaVal(kv,"ui_manage_id",getPara("ui_manage_id"));
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        SqlPara count = Db.getSqlPara("recharge.queryCount", kv);
        SqlPara query = Db.getSqlPara("recharge.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), count.getPara()));
    }


    public void save() {

        List<Map<String, Object>> list = JSONSerializer.deserialize(getPara("m_id"),List.class);
        renderJson(service.recharge(list,getParaToInt("loginuserid",0)));

    }
    public void empty() {
        List<Map<String, Object>> list = JSONSerializer.deserialize(getPara("m_id"),List.class);
        renderJson(service.empty(list));
    }

    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("recharge.queryById"), getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete(Db.getSql("recharge.delete"), getPara("default_id_")));
    }
    public void statistics() {
        Record user = new Record();
        user.set("a","aaa");
        user.set("b","222");
        user.set("c","测试");
        renderJson( Util.getResultMapByID(user));
    }

}