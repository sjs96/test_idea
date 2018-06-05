package fixed.util;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    //默认值
    public static  int ROWS=10;                                   //分页行数
    public static  int PAGE=1;                                    //分页页数
    public static  int ZERO=0;                                    //默认值

    //返回值状态
    public static final int state_ok = 100;             //正常
    public static final int state_err = 200;            //错误
    public static final int state_err_jurisdiction = 201;            //权限不足
    public static final int state_err_not_logged = 202;            //未登录
    public static final int state_err_bypage = 203;            //分页数据为空
    public static final int state_err_byid = 204;            //查询单个数据
    public static final int state_err_add = 205;            //添加
    public static final int state_err_edit = 206;            //更新
    public static final int state_err_del = 207;            //删除

    // 设置参数
    public static final void setParaVal(Kv kv,String key,String value)  {
        if(value==null||value.length()<1){
            return;
        }
        kv.set(key,value);
    }

    // 获取当前时间
    public static final String  getCurrentTime()  {
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(day);
    }

    // 分页
    public static final Map<String, Object>  getResultMapByPage(Page<Record> page)  {
        if(page.getList()!=null&&page.getList().size()>0){
            return getResultMap(state_ok,"有数据",page);
        }
        return getResultMap(state_err_bypage,"暂无数据",page);
    }
    // 全部
    public static final Map<String, Object>  getResultMapByAll(List<Record> list)  {
        if(list!=null&&list.size()>0){
            return getResultMap(state_ok,"有数据",list);
        }
        return getResultMap(state_err_bypage,"暂无数据",list);
    }
    // 查询单个记录
    public static final Map<String, Object>  getResultMapByID(Record page)  {
        if(page!=null){
            return getResultMap(state_ok,"有数据",page);
        }
        return getResultMap(state_err_byid,"暂无数据",page);
    }
    // 添加
    public static final Map<String, Object>  getResultMapByAdd(boolean bool)  {
        if(bool){
            return getResultMap(state_ok,"添加成功",null);
        }
        return getResultMap(state_err_add,"添加失败",null);
    }
    // 更新
    public static final Map<String, Object>  getResultMapByEdit(boolean bool)  {
        if(bool){
            return getResultMap(state_ok,"更新成功",null);
        }
        return getResultMap(state_err_edit,"更新失败",null);
    }
    // 删除
    public static final Map<String, Object>  getResultMapByDel( int count )  {
        if(count>0){
            return getResultMap(state_ok,"删除成功",null);
        }
        return getResultMap(state_err_del,"删除失败",null);
    }
    //下载
    public static final Map<String, Object>  getResultMapByExecl(Map<String, Object> name)  {
        if(name!=null&&ValueUtile.getInteger("state",name)==100){
            return getResultMap(state_ok,"下载成功",name);
        }
        return getResultMap(state_err,"下载失败",name);
    }
    // 返回值封装
    public static final Map<String, Object>  getResultMap(int state,String msg,Object json)  {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", msg);
        resultMap.put("state", state);
        resultMap.put("json", json);
        return resultMap;
    }
    // 判断状态是否正常  状态值为100 为正常
    public static final boolean isOk(Map<String, Object> resultMap)  {
        boolean state = false;
        if(Integer.parseInt(resultMap.get("state")+"")==100) {
            state = true;
        }
        return state;
    }
    public static void SOP(Exception e){
        System.out.println(e.toString());
    }
    public static void SOP(Exception e,String name){
        System.out.println(name+e.toString());
    }

}
