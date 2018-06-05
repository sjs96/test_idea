package fixed.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import fixed.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JurisdictionService extends BaseService{

    public Map<String, Object>  save(Map<String,Object> map , List<Map<String,Object>> ids  ,int loginuserid){
        String id= (String) map.get("id");

        synchronized(this) {
            // 保存
            Map<String, Object> resultMap = super.save(map,loginuserid);
            if(id==null||id.length()<1){
                //获取刚添加的id
                Record user = Db.findFirst(Db.getSql("jurisdiction.queryByNew"));
                id = ""+user.get("j_id");
            }else{
                //删除原来职务所有数据
                Db.update(Db.getSql("jurisdiction.deleteDetails"),  id);
            }

            //保存职务数据
            if(id!=null&&id.length()>0){
                for(int i=0;i<ids.size();i++){
                    Map<String,Object> newMap = ids.get(i);
                    Record record = new Record();
                    record.set("j_id", id);
                    record.set("m_id", newMap.get("id"));
                    record.set("jd_name", newMap.get("name"));
                    Db.save("fixed_sys_jurisdiction_details", record);
                }
            }

            return resultMap;
        }
    }

    public Map<String, Object> getTree(Map<String,Object> data){
        int r_id = (int)data.get("r_id");
        int j_id = (int)data.get("j_id");
        String QuerySql = Db.getSql("jurisdiction.queryMenuByOne");
        String QuerySql2 = Db.getSql("jurisdiction.queryMenuByTwo");
        Map<String,Object> returnMap = new HashMap<>();

        List<Integer> num = new ArrayList<>();
        List<Map<String,Object>> list = new ArrayList<>();
        List<Record> menu1 = Db.find(QuerySql);
        List<Record> jurisdiction = getJ(j_id);
        for(int i=0;i<menu1.size();i++){
            Record record = menu1.get(i);
            List<Record> menu2 = Db.find(QuerySql2,record.get("menu_id"));
            num.add(menu2.size());
            for(int j=0;j<menu2.size();j++){
                Record record2 = menu2.get(j);
                Map<String,Object> map = new HashMap<>();
                List<Map<String,Object>> operation = new ArrayList<>();
                List<Record> attributeList = Db.find(Db.getSql("menu.queryAttribute"),record2.get("menu_id"));
                for(int k=0;k<attributeList.size();k++){
                    Record recordK = attributeList.get(k);
                    operation.add(setJ(""+recordK.get("a_attribute"),""+recordK.get("a_name"),record2.getInt("menu_id"),jurisdiction));
                }

                Map<String,Object> page = setJ("page",""+record2.get("menu_name"),record2.getInt("menu_id"),jurisdiction);
                /*setJ(operation,"add",record2.getInt("menu_id"),jurisdiction);
                setJ(operation,"edit",record2.getInt("menu_id"),jurisdiction);
                setJ(operation,"delete",record2.getInt("menu_id"),jurisdiction);
                setJ(operation,"query",record2.getInt("menu_id"),jurisdiction);*/
                map.put("module",record.get("menu_name"));
                map.put("id",record2.get("menu_id"));
                map.put("operation",operation);
                map.put("page",page);
                list.add( map);
            }
        }
        returnMap.put("num",num);
        returnMap.put("list",list);

        return Util.getResultMap(Util.state_ok,"暂无数据",returnMap);
    }
    public Map<String,Object> setJ( String name,String label,int id,List<Record> jurisdiction){
        boolean state = false;
        boolean disabled = false;
        for(int j=0;j<jurisdiction.size();j++){
            Record record = jurisdiction.get(j);
            if(record.getInt("m_id")==id&&name.equals(record.get("jd_name"))){
                state=true;
            }
        }


        Map<String,Object> newMap = new HashMap<>();
        newMap.put("state",state);
        newMap.put("disabled",disabled);
        newMap.put("label",label);
        newMap.put("name",name);
        return newMap;
    }
    public List<Record> getJ(int id ){
        return Db.find(Db.getSql("jurisdiction.queryDetails"),id);
    }
    //删除
    public Map<String, Object> delete2(String Sql1,String Sql,String deleteId,Object... obj){
        String [] id = deleteId.split(",");
        int count = 0;
        for(int i =0;i<id.length;i++){
            count += Db.update(Sql1,  id[i]);
            count += Db.update(Sql,  id[i]);
        }
        return Util.getResultMapByDel(count);
    }
}
