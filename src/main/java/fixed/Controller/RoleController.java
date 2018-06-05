package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.RoleService;
import fixed.Service.UserService;
import fixed.parameter.Parameter;
import fixed.util.Util;

import java.util.HashMap;
import java.util.Map;

public class RoleController  extends Controller {
    static RoleService service = new RoleService();
    static UserService userService = new UserService();

    public void query()  {
        Kv kv = Kv.create();
        Util.setParaVal(kv,"r_name",getPara("r_name"));
        Util.setParaVal(kv,"orderBy",getPara("orderBy"));
        SqlPara count = Db.getSqlPara("role.queryCount",kv);
        SqlPara query = Db.getSqlPara("role.query",kv);
        renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),count.getPara() ));
    }

    public void queryByAll(){
        renderJson(service.queryByAll(Db.getSql("role.queryByAll")));
    }
    public void queryByRole(){
        Record user = Db.findFirst(Db.getSql("user.queryById"),getPara("loginuserid"));
        if(user==null){
            renderJson(Util.getResultMap(Util.state_err,"暂无数据",null));
        }
        int role =userService.getRole(getPara("loginuserid"));
        if(role == userService.role_fd ){
            renderJson(service.queryByAll(Db.getSql("role.queryByFD")));
        }else if(role == userService.role_xtgly  ){
            renderJson(service.queryByAll(Db.getSql("role.queryByXTGLY")));
        }else if( role == userService.role_ejxtgly ){
            renderJson(service.queryByAll(Db.getSql("role.queryByEJXTGLY")));
        }else{
            renderJson(Util.getResultMap(Util.state_err,"暂无数据",null));
        }
    }


    public void save(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("r_name",getPara("r_name","") );

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("primaryKey","r_id");
        map.put("id",getPara("r_id"));
        map.put("tableName","fixed_sys_role");
        map.put("data",data);

        renderJson(service.save(map,getParaToInt("loginuserid",0)));
    }
    @Parameter.Must({"default_id_"})
    public void queryById(){
        renderJson(service.queryById(Db.getSql("role.queryById"),getPara("default_id_")));
    }

    @Parameter.Must({"default_id_"})
    public void delete(){
        renderJson(service.delete(Db.getSql("role.delete"),getPara("default_id_")));
    }

    @Parameter.Must({"dp_id"})
    public void queryByDepart(){
        String dp_id = getPara("dp_id");
        String [] id = dp_id.split(",");
        dp_id = id[id.length-1];
        renderJson(service.queryByAll(Db.getSql("role.queryByDepart"),dp_id));
    }

}

