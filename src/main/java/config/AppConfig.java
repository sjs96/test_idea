package config;

import cn.dreampie.quartz.QuartzPlugin;
import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByActionKeyRegex;
import com.jfinal.plugin.activerecord.tx.TxByActionKeys;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import fixed.Controller.*;
import fixed.interceptor.TokenInterceptor;
import fixed.test.Test;
import fixed.websocket.WebSocket;
import specific.Controller.home.HomeController;
import specific.Controller.order.OrderController;
import specific.Controller.other.ProblemController;
import specific.Controller.recharge.RechargeController;
import specific.Controller.system.MeterController;
import specific.Controller.system.PriceController;
import specific.Task.Task900;
import specific.Util.SocketServer;
import specific.Util.TokenSocketServer;

/**
 * Created by jaer on 2016/12/16.
 */
public class AppConfig extends JFinalConfig{
    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        PropKit.use("a_little_config.txt");
        me.setDevMode(true);
        me.setViewType(ViewType.FREE_MARKER);
        me.setBaseUploadPath(PropKit.get("defaultDir"));
     //   me.setJsonFactory(new MixedJsonFactory());
    }
    public void configEngine(Engine me) {}
    public void configRoute(Routes me) {

        me.add("/user", UserController.class);
        me.add("/depart", DepartController.class);
        me.add("/post", PostController.class);
        me.add("/menu", MenuController.class);
        me.add("/upload", UploadController.class);
        me.add("/meter", MeterController.class);
        me.add("/price", PriceController.class);
        me.add("/execl", ExeclController.class);
        me.add("/home", HomeController.class);
        me.add("/problem", ProblemController.class);
        me.add("/role", RoleController.class);
        me.add("/jurisdiction", JurisdictionController.class);
        me.add("/recharge", RechargeController.class);
        me.add("/order", OrderController.class);
        me.add("/notice", NoticeController.class);
        me.add("/tableSeeting", TableSettingController.class);
        me.add("/test", Test.class);
    }

    public static C3p0Plugin createC3p0Plugin() {
        String jdbcUrl = PropKit.get("jdbcUrl");
        String user = PropKit.get("user");
        String password = PropKit.get("password");
        String driverClassName = PropKit.get("driverClassName");
        return new C3p0Plugin(jdbcUrl,user,password,driverClassName);
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        // 配置C3p0数据库连接池插件
        C3p0Plugin C3p0Plugin = createC3p0Plugin();
        me.add(C3p0Plugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(C3p0Plugin);
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
        arp.addSqlTemplate("fixed.sql");
        arp.addSqlTemplate("specific.sql");

     //   arp.setDialect(new SqlServerDialect());
        arp.setDialect(new MysqlDialect());
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
        me.add(arp);
        //Redis缓存
        RedisPlugin bbsRedis = new RedisPlugin("bbs", "127.0.0.1");
        me.add(bbsRedis);
        //WebSocket 连接
        WebSocket.socket();
        //Socket 连接
        SocketServer.socket();
        //TokenSocket 连接
        TokenSocketServer.socket();

        QuartzPlugin quartzPlugin = new QuartzPlugin();
        quartzPlugin.setJobs("system-quartz.properties");
        me.add(quartzPlugin);
    }
    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new TokenInterceptor());
        me.add(new TxByMethodRegex("(.*save.*|.*update.*)"));
        me.add(new TxByMethods("save", "update"));

        me.add(new TxByActionKeyRegex("/trans.*"));
        me.add(new TxByActionKeys("/tx/save", "/tx/update"));
    }
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("basePath"));
    }

}