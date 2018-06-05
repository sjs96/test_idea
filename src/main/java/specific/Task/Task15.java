package specific.Task;

import fixed.util.RedisUtil;
import fixed.util.Util;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import specific.Util.Instructions;
import specific.Util.MeterUtil;
import specific.Util.SocketServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

public class Task15 implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String,Object> queryMap  = RedisUtil.getQueryAll();

        for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
            String meter = ""+entry.getValue();
            System.out.println(meter+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
            try {
                System.out.println("this is test job one " + new Date());
                if(SocketServer.getOutputStream(meter.substring(1))==null){
                    return ;
                }
                Instructions ins= new Instructions();
                byte [] in = ins.realTimeData(meter,"046004620463046404650266026704680C74");//046004620C74
                SocketServer.getOutputStream(meter.substring(1)).write(in);
                SocketServer.getOutputStream(meter.substring(1)).flush();



            } catch (IOException e) {
                Util.SOP(e);
                e.printStackTrace();
            }
        }



    }
}
