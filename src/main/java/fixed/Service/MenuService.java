package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuService extends BaseService {
    public Map<String, Object>  queryById(String QueryByIdSql,Object... obj){
        Record user = Db.findFirst(QueryByIdSql,obj);
        List<Record> list = Db.find(Db.getSql("menu.queryAttribute"),obj);
        user.set("jurisdiction",list);
        return Util.getResultMapByID(user);
    }
    public Map<String, Object>  save(Map<String,Object> map , List<Map<String,Object>> ids  ,int loginuserid){
        String id= (String) map.get("id");

        synchronized(this) {
            // 保存
            Map<String, Object> resultMap = super.save(map,loginuserid);
            if(id==null||id.length()<1){
                //获取刚添加的id
                Record user = Db.findFirst(Db.getSql("menu.queryByNew"));
                id = ""+user.get("menu_id");
            }else{
                //删除原来职务所有数据
                Db.update(Db.getSql("menu.deleteAttribute"),  id);
            }

            //保存职务数据
            if(id!=null&&id.length()>0){
                for(int i=0;i<ids.size();i++){
                    Map<String,Object> newMap = ids.get(i);
                    Record record = new Record();
                    record.set("m_id", id);
                    record.set("a_attribute", newMap.get("a_attribute"));
                    record.set("a_name", newMap.get("a_name"));
                    Db.save("fixed_sys_menu_attribute", record);
                }
            }

            return resultMap;
        }
    }

    //获取树
    public Map<String,Object> getMenuTree(String FirstSql,String SecondSql,Object... obj){
        List<Record> FirstList = Db.find(FirstSql);
        List<Record> SecondList = Db.find(SecondSql,obj);
        List<Map<String,Object>> list = setTree(FirstList,SecondList);
        if(list!=null&&list.size()>0){
            return Util.getResultMap(Util.state_ok,"有数据",list);
        }
        return Util.getResultMap(Util.state_err,"暂无数据",list);
    }
    public  List<Map<String,Object>> setTree ( List<Record> FirstList, List<Record> SecondList){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for(int i =0;i<FirstList.size();i++){
            Map<String,Object> newMap = FirstList.get(i).getColumns();
            List<Map<String,Object>> childrenList = new ArrayList<Map<String, Object>>();
            for(int l =0;l<SecondList.size();l++){
                if(newMap.get("menu_id").equals(SecondList.get(l).get("menu_parent_id"))){
                    childrenList.add(SecondList.get(l).getColumns());
                }
            }
            if(childrenList.size()>0){
                newMap.put("children",childrenList);
            }
            list.add(newMap);
        }
        return list;
    }
}
