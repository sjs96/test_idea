package fixed.Service;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseService {
    // 分页
    public Map<String, Object>   queryByPage(int page,int rows,String countSql,String QuerySql,Object... obj){
        Page<Record> list = Db.paginateByFullSql(page,rows,countSql,QuerySql,obj);
        return Util.getResultMapByPage(list);
    }
    // 全部
    public Map<String, Object>  queryByAll(String QuerySql,Object... obj){
        List<Record> list = Db.find(QuerySql,obj);
        return Util.getResultMapByAll(list);
    }
    // 全部
    public Map<String, Object>  queryByAll(SqlPara sql){
        List<Record> list = Db.find(sql);
        return Util.getResultMapByAll(list);
    }
    //　查询单个
    public Map<String, Object>  queryById(String QueryByIdSql,Object... obj){
        Record user = Db.findFirst(QueryByIdSql,obj);
        return Util.getResultMapByID(user);
    }
    //　保存
    public Map<String, Object>  save(Map<String,Object> map ,int loginuserid){
        Map<String,Object> data = (Map<String,Object>) map.get("data");
        String primaryKey = (String) map.get("primaryKey");
        String id = (String) map.get("id");
        String tableName = (String) map.get("tableName");
        if(id==null||id.length()<1){
            return create(data,tableName,primaryKey,loginuserid);
        }
        return update(data,tableName,primaryKey,id,loginuserid);
    }
    //　更新
    public Map<String, Object>  update(Map<String,Object> data,String tableName,String primaryKey,String val,int loginuserid){
        Record user = Db.findById(tableName,primaryKey, val);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            user.set(entry.getKey()+"", entry.getValue());
        }
        user.set("update_user", loginuserid);
        user.set("update_time",Util.getCurrentTime());
        boolean bool = Db.update(tableName,primaryKey, user);
        return Util.getResultMapByEdit(bool);
    }
    //添加
    public Map<String, Object>  create(Map<String,Object> data,String tableName,String primaryKey,int loginuserid){
        Record record = new Record();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            record.set(entry.getKey()+"", entry.getValue());
        }
        record.set("create_time", Util.getCurrentTime());
        data.put("create_user", loginuserid);
        boolean bool = Db.save(tableName, record);
        return Util.getResultMapByAdd(bool);
    }
    //删除
    public Map<String, Object> delete(String Sql,String deleteId,Object... obj){
        String [] id = deleteId.split(",");
        int count = 0;
        for(int i =0;i<id.length;i++){
            count += Db.update(Sql,  id[i]);
        }
        return Util.getResultMapByDel(count);
    }
}
