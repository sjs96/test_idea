package fixed.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fixed.util.AESUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;


public class SecurityInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		String ctrlKey = inv.getControllerKey();
		//判断是否为手机请求
		if (ctrlKey.contains("api")) {
			String key = inv.getMethodName();
			if (key.contains("login")) {
				inv.invoke();
			} else {
				String sec = inv.getController().getPara("sec");
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("code", -1);
				resultMap.put("data", null);
				resultMap.put("sec", null);
				if (StrKit.notBlank(sec)) {
					try {
						String uinfo = AESUtils.decrypt(AESUtils.SECURITY, sec);
						String[] arr = uinfo.split(";");
						if (StrKit.notBlank(arr) && arr.length > 1) {
							long time = Long.valueOf(arr[1]);
							long cur = new Date().getTime();
							if (cur - time > 30 * 60 * 1000) {
								inv.getController().renderJson(resultMap);
							} else {
								String newSecurity = arr[0] + ";" + cur;
								inv.getController().setAttr("sec", AESUtils.encrypt(AESUtils.SECURITY, newSecurity));
								inv.invoke();
							}
						} else {
							inv.getController().renderJson(resultMap);
						}
					} catch (Exception e) {
						e.printStackTrace();
						inv.getController().renderJson(resultMap);
					}
				} else {
					inv.getController().renderJson(resultMap);
				}
			}
		}else{
			String userid = inv.getController().getSessionAttr("loginuserid");
			if(StrKit.notBlank(userid)){
				inv.invoke();
			}else{
				inv.getController().redirect("/");
			}
		}
	}
}
