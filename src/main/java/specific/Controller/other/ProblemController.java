package specific.Controller.other;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.parameter.Parameter;
import fixed.util.Util;
import specific.service.other.ProblemService;
import specific.service.system.MeterService;

import java.util.HashMap;
import java.util.Map;

public class ProblemController  extends Controller {
    static ProblemService service = new ProblemService();

    public void query() {
        Kv kv = Kv.create();
        Util.setParaVal(kv, "p_name", getPara("p_name"));
        SqlPara count = Db.getSqlPara("problem.queryCount", kv);
        SqlPara query = Db.getSqlPara("problem.query", kv);
        renderJson(service.queryByPage(getParaToInt("page", Util.PAGE), getParaToInt("rows", Util.ROWS), count.getSql(), query.getSql(), count.getPara()));
    }


    public void save() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("p_name", getPara("p_name", ""));
        data.put("p_download", getPara("p_download", ""));
        data.put("p_content", getPara("p_content", ""));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("primaryKey", "p_id");
        map.put("id", getPara("p_id"));
        map.put("tableName", "sys_problem");
        map.put("data", data);

        renderJson(service.save(map,getParaToInt("loginuserid",0)));
    }

    @Parameter.Must({"default_id_"})
    public void queryById() {
        renderJson(service.queryById(Db.getSql("problem.queryById"), getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete() {
        renderJson(service.delete(Db.getSql("problem.delete"), getPara("default_id_")));
    }

}