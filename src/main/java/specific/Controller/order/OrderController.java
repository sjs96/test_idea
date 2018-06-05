package specific.Controller.order;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.Util;
import fixed.websocket.NotifyBean;
import fixed.websocket.WebSocket;
import specific.service.order.OrderService;
import specific.service.system.MeterService;

import java.util.HashMap;
import java.util.Map;

public class OrderController  extends Controller {
    static OrderService service = new OrderService();

    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "o_remarks", getPara("o_remarks"));
        Util.setParaVal(kv,"ui_landlord_id",getPara("ui_landlord_id"));
        Util.setParaVal(kv,"ui_tenant_id",getPara("ui_tenant_id"));
        Util.setParaVal(kv,"ui_manage_id",getPara("ui_manage_id"));
        Util.setParaVal(kv,"time_start",getPara("time_start"));
        Util.setParaVal(kv,"time_end",getPara("time_end"));
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        SqlPara count = Db.getSqlPara("order.queryCount", kv);
        SqlPara query = Db.getSqlPara("order.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), count.getPara()));
    }
    public void queryTotal() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "o_remarks", getPara("o_remarks"));
        Util.setParaVal(kv,"ui_landlord_id",getPara("ui_landlord_id"));
        Util.setParaVal(kv,"ui_tenant_id",getPara("ui_tenant_id"));
        Util.setParaVal(kv,"ui_manage_id",getPara("ui_manage_id"));
        Util.setParaVal(kv,"time_start",getPara("time_start"));
        Util.setParaVal(kv,"time_end",getPara("time_end"));
        Util.setParaVal(kv, "orderBy", getPara("orderBy"));
        SqlPara add = Db.getSqlPara("order.queryTotalByAdd", kv);
        SqlPara del = Db.getSqlPara("order.queryTotalByDel", kv);
        renderJson(service.queryTotal(add,del));
    }

    public void getOrderInfo() {
        System.out.println("getOrderInfo");
        Record newInfo = Db.findFirst(Db.getSql("order.getOrderInfo"), getPara("o_id"));
        System.out.println(newInfo.toString());
        try{
            System.out.println("try");
            NotifyBean bean = new NotifyBean(""+getParaToInt("loginuserid",0),"", "充值成功","token:",newInfo.getColumns());
            WebSocket.eventListenner.clientCache.send(bean);
            System.out.println("try2");
            System.out.println( WebSocket.eventListenner.clientCache.toString());
        }catch (Exception e){
            System.out.println("异常");
            Util.SOP(e);
        }
        renderJson();
    }


    public void save() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("m_id", getPara("m_id", ""));
        data.put("o_remarks", getParaToInt("o_remarks", 0));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "o_id");
        map.put("id", getPara("o_id"));
        map.put("tableName", "sys_order");
        map.put("data", data);

        renderJson(service.save(map,getParaToInt("loginuserid",0)));
    }

    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("order.queryById"), getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete(Db.getSql("order.delete"), getPara("default_id_")));
    }

}