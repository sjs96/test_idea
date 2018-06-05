package specific.Task;

import com.jfinal.plugin.cron4j.ITask;
import fixed.Service.BaseService;
import fixed.util.JSONSerializer;
import fixed.util.RedisUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Task900 implements Job {
    BaseService service = new BaseService();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String,Object> maps = RedisUtil.getCurrentALL();
        for (Map.Entry<String,Object> entry : maps.entrySet()) {
            Map<String,Object>  meter = (Map<String,Object>)entry.getValue();

            Map<String,Object> data = new HashMap<String,Object>();
            data.put("total_electricity", meter.get("total_electricity") );
            data.put("surplus_electricity", meter.get("surplus_electricity") );
            data.put("current", meter.get("current") );
            data.put("voltage", meter.get("voltage") );
            data.put("state", meter.get("state") );
            data.put("m_id", meter.get("m_id") );
            data.put("m_no",entry.getKey().substring(1));

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("primaryKey","h_id");
            map.put("id","");
            map.put("tableName","sys_history");
            map.put("data",data);
            service.save(map,0);
        }
    }
}

