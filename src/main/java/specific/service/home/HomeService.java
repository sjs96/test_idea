package specific.service.home;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.Service.BaseService;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeService extends BaseService {
    public Map<String, Object> queryByHome(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryBy1",Db.findFirst(Db.getSql("home.queryBy1")));
        map.put("queryBy2",Db.findFirst(Db.getSql("home.queryBy2")));
        map.put("queryBy3",Db.findFirst(Db.getSql("home.queryBy3")));
        map.put("queryBy4",Db.findFirst(Db.getSql("home.queryBy4")));

        map.put("queryByMonth", Db.find(Db.getSql("home.queryByMonth")));


        return Util.getResultMap(Util.state_ok,"首页数据",map);

    }
    public Map<String, Object> queryByHome2(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryUserByMonth",Db.find(Db.getSql("home.queryUserByMonth")));
        map.put("queryUserByYear",Db.find(Db.getSql("home.queryUserByYear")));


        return Util.getResultMap(Util.state_ok,"首页数据",map);


    }
    public Map<String, Object> queryByHome3(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryOrderByMonth",Db.find(Db.getSql("home.queryOrderByMonth")));
        map.put("queryOrderByYear",Db.find(Db.getSql("home.queryOrderByYear")));


        return Util.getResultMap(Util.state_ok,"首页数据",map);

    }
}
