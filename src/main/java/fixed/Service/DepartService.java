package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartService extends BaseService{

    public Map<String, Object>  save(Map<String,Object> map ,String id ,int loginuserid){String dp_id= (String) map.get("id");


        synchronized(this) {
            // 保存
            Map<String, Object> resultMap = super.save(map,loginuserid);
            if(dp_id==null||dp_id.length()<1){
                //获取刚添加的id
                Record user = Db.findFirst(Db.getSql("depart.queryByNew"));
                dp_id = ""+user.get("dp_id");
            }else{
                //删除原来职务所有数据
                Db.update(Db.getSql("depart.deletePost"),  dp_id);
            }

            //保存职务数据
            if(id!=null&&id.length()>0){
                String[] ids = id.split(",");
                for (int i = 0; i < ids.length; i++) {
                    Record record = new Record();
                    record.set("dp_id", dp_id);
                    record.set("po_id", ids[i]);
                    Db.save("fixed_sys_depart_post", record);
                }
            }

            return resultMap;
        }
    }

    public Map<String, Object> queryByPost(String QuerySql, Object... obj){
        List<Record> list = Db.find(QuerySql,obj);
        List<Integer> newList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            newList.add(Integer.parseInt(record.get("po_id")+""));
        }
        if(newList!=null&&newList.size()>0){
            return Util.getResultMap(Util.state_ok,"有数据",newList);
        }
        return Util.getResultMap(Util.state_err_bypage,"暂无数据",newList);

    }
    public Map<String, Object> getTree(String QuerySql, Object... obj){
        List<Record> list = Db.find(QuerySql,obj);
        List<Map<String,Object>> newList= listtotee(list,"0");
        if(newList!=null&&newList.size()>0){
            return Util.getResultMap(Util.state_ok,"有数据",newList);
        }
        return Util.getResultMap(Util.state_err_bypage,"暂无数据",newList);
    }
    public List<Map<String,Object>> listtotee(List<Record> list, String topid) {
        List<Map<String,Object>> treelist = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < list.size(); i++) {
            Record depart = list.get(i);
            if (topid.equals(""+depart.get("dp_topid"))) {
                Map<String,Object> tree = new HashMap<String,Object>();
                tree.put("value",""+depart.get("dp_id"));
                tree.put("label",""+depart.get("dp_name"));
                List<Map<String,Object>> children = listtotee(list, ""+depart.get("dp_id"));

                if(children!=null&&children.size()>0){
                    tree.put("children",children);
                }
                treelist.add(tree);
            }
        }
        return treelist;
    }
}
