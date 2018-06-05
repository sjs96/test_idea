package fixed.jurisdiction;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import fixed.util.RedisUtil;
import fixed.util.Util;

import java.lang.reflect.Method;
import java.util.Map;

public class JurisdictionUtil {

    public static Map<String, Object> check(Invocation inv) {
        //返回值 默认权限不足
        Map<String, Object> returnMap = Util.getResultMap(Util.state_err_jurisdiction,"权限不足",null);

        Controller controller = inv.getController();
        Method method = inv.getMethod();
        AuthMode mode = new AuthMode(); //鉴权模式对象封装
        getMethodAuthMode(mode, method, controller.getClass());

        //获取token
        String token = controller.getPara("token","");



        //获取用户
        Object user = RedisUtil.getValue(RedisUtil.Login,token);

        if(mode.authCode == ErrorCode.REQ_GUEST){ //允许匿名
            returnMap = Util.getResultMap(Util.state_ok,"允许匿名",null);
        }else if(user != null){ //不允许匿名，且当前已登录

            //测试以下
            Cache bbsCache = Redis.use();
            System.out.println(bbsCache.get(token));
            //测试以上

            switch(mode.authCode){
                case ErrorCode.REQ_ROLE: //根据单个角色进行鉴权
                    if(checkRole(user,mode.authId)) {
                        returnMap = Util.getResultMap(Util.state_ok,"有权限",null);
                    }
                break;
                case ErrorCode.REQ_ROLES: //根据多个角色进行鉴权
                  //  if(checkRole(user,mode.authIds)) authAccept = true;
                    break;
                case ErrorCode.REQ_PERM: //根据单个权限进行鉴权
                 //   if(checkRole(user,mode.authId)) authAccept = true;
                    break;
                case ErrorCode.REQ_PERMS: //根据多个权限进行鉴权
                 //   if(checkRole(user,mode.authIds))authAccept = true;
                    break;
                case ErrorCode.REQ_LOGIN: //根据用户的登录状态进行鉴权
                    returnMap = Util.getResultMap(Util.state_ok,"有权限",null);
                    break;
                default:
                    break;
            }
        }else{
            returnMap = Util.getResultMap(Util.state_err_not_logged,"未登录",null);
        }
        return returnMap;
    }

    /**
     * 优先进行Action层面的鉴权
     * @param method
     * @param ctrl
     */
    private static void getMethodAuthMode(AuthMode mode, Method method, Class<?> ctrl){
        if(method.isAnnotationPresent(Jurisdiction.Role.class)){
            mode.authCode = ErrorCode.REQ_ROLE;
            mode.authId = method.getAnnotation(Jurisdiction.Role.class).value();
        }else if(method.isAnnotationPresent(Jurisdiction.Roles.class)){
            mode.authCode = ErrorCode.REQ_ROLES;
            mode.authIds = method.getAnnotation(Jurisdiction.Roles.class).value();
        }else if(method.isAnnotationPresent(Jurisdiction.Perm.class)){
            mode.authCode = ErrorCode.REQ_PERM;
            mode.authId = method.getAnnotation(Jurisdiction.Perm.class).value();
        }else if(method.isAnnotationPresent(Jurisdiction.Perms.class)){
            mode.authCode = ErrorCode.REQ_PERMS;
            mode.authIds = method.getAnnotation(Jurisdiction.Perms.class).value();
        }else if(method.isAnnotationPresent(Jurisdiction.Logined.class)){
            mode.authCode = ErrorCode.REQ_LOGIN;
        }else if(method.isAnnotationPresent(Jurisdiction.Guest.class)){
            mode.authCode = ErrorCode.REQ_GUEST;
        }else{
            getControllerAuthMode(mode, ctrl);
        }
    }

    /**
     * 进行Controller层面的鉴权，只有当Action未设置时有效
     * @param ctrl
     */
    private static void getControllerAuthMode(AuthMode mode, Class<?> ctrl){
        if(ctrl.isAnnotationPresent(Jurisdiction.Role.class)){
            mode.authCode = ErrorCode.REQ_ROLE;
            mode.authId = ctrl.getAnnotation(Jurisdiction.Role.class).value();
        }else if(ctrl.isAnnotationPresent(Jurisdiction.Roles.class)){
            mode.authCode = ErrorCode.REQ_ROLES;
            mode.authIds = ctrl.getAnnotation(Jurisdiction.Roles.class).value();
        }else if(ctrl.isAnnotationPresent(Jurisdiction.Perm.class)){
            mode.authCode = ErrorCode.REQ_PERM;
            mode.authId = ctrl.getAnnotation(Jurisdiction.Perm.class).value();
        }else if(ctrl.isAnnotationPresent(Jurisdiction.Perms.class)){
            mode.authCode = ErrorCode.REQ_PERMS;
            mode.authIds = ctrl.getAnnotation(Jurisdiction.Perms.class).value();
        }else if(ctrl.isAnnotationPresent(Jurisdiction.Logined.class)){
            mode.authCode = ErrorCode.REQ_LOGIN;
        }else{
            mode.authCode = ErrorCode.REQ_GUEST;
        }
    }


    private static boolean checkRole(Object User, String role){
            return true;
    }

}
