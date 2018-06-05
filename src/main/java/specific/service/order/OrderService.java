package specific.service.order;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.BaseService;
import fixed.util.Util;

import java.util.HashMap;
import java.util.Map;

public class OrderService extends BaseService {

    public Map<String, Object> queryTotal(SqlPara sql, SqlPara sql2){
        Map<String, Object> map = new HashMap<String, Object>();
        Record total = Db.findFirst(sql);
        Record total2 = Db.findFirst(sql2);
        map.put("add_money",total.get("money"));
        map.put("add_electricity",total.get("electricity"));
        map.put("del_money",total2.get("money"));
        map.put("del_electricity",total2.get("electricity"));
        return Util.getResultMap(Util.state_ok,"首页数据",map);

    }
}
