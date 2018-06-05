package fixed.Controller;

import com.jfinal.captcha.CaptchaRender;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import fixed.Service.UserService;
import fixed.parameter.Parameter;
import fixed.util.JSONSerializer;
import fixed.util.MD5Util;
import fixed.util.Util;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController extends Controller{
	static UserService service = new UserService();


	public void query(){
		String dir = getRequest().getServletContext().getRealPath("/");
		System.out.println(dir);
		Kv kv = Kv.create();
		Util.setParaVal(kv,"ui_code",getPara("ui_code"));
		Util.setParaVal(kv,"ui_name",getPara("ui_name"));
		Util.setParaVal(kv,"ui_telno",getPara("ui_telno"));
		Util.setParaVal(kv,"ui_landlord_id",getPara("ui_landlord_id"));
		Util.setParaVal(kv,"ui_tenant_id",getPara("ui_tenant_id"));
		Util.setParaVal(kv,"ui_manage_id",getPara("ui_manage_id"));
		Util.setParaVal(kv,"r_id",getPara("r_id"));
		int my_role = service.getRole(getParaToInt("loginuserid",0));
		if(my_role == UserService.role_xtgly){
			Util.setParaVal(kv,"my_role","role_xtgly");
		}
		if(my_role == UserService.role_ejxtgly){
			Util.setParaVal(kv,"my_role","role_ejxtgly");
		}
		if(my_role == UserService.role_fd){
			Util.setParaVal(kv,"my_role","role_fd");
		}
		if(my_role == UserService.role_fdgly){
			Util.setParaVal(kv,"my_role","role_fdgly");
		}
		if(my_role == UserService.role_fk){
			Util.setParaVal(kv,"my_role","role_fk)");
		}
		SqlPara count = Db.getSqlPara("user.queryCount",kv);
		SqlPara query = Db.getSqlPara("user.query",kv);
		renderJson(service.queryByPage(getParaToInt("page",Util.PAGE), getParaToInt("rows",Util.ROWS),count.getSql() ,query.getSql(),count.getPara() ));
	}
	public void queryById(){
		renderJson(service.queryById(Db.getSql("user.queryById"),getPara("default_id_")));
	}
	public void queryBaseById(){
		renderJson(service.queryById(Db.getSql("user.queryBaseById"),getPara("default_id_")));
	}

	public void save(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("ui_code", getPara("ui_code","") );
		data.put("ui_nickname", getPara("ui_nickname","") );
		data.put("ui_name", getPara("ui_name","") );
		data.put("ui_telno", getPara("ui_telno","") );
		data.put("ui_fictel", getPara("ui_fictel","") );
		data.put("ui_inside", getPara("ui_inside","") );
		data.put("ui_email", getPara("ui_email","") );
		data.put("ui_sort", getPara("ui_sort","") );
		data.put("ui_isuser", getPara("ui_isuser","") );
		data.put("ui_valid", getPara("ui_valid","") );
		data.put("ui_note", getPara("ui_note","") );
		data.put("ui_landlord_id", getParaToInt("ui_landlord_id",0) );
		data.put("ui_kmf_certificate", getPara("ui_kmf_certificate","") );
		data.put("ui_photo", getPara("ui_photo","") );
		if((data.get("ui_kmf_certificate")+"").length()>0){
			List<Map<String, Object>> list = JSONSerializer.deserialize(data.get("ui_kmf_certificate")+"",List.class);
			for (int i = 0;i<list.size();i++){
				Map<String, Object> map = list.get(i);
				String url = ""+map.get("url");
				if("\\api".equals(url.substring(0,4))){
					url = url.substring(4);
				}

				String  str = readToString(getRequest().getServletContext().getRealPath("/")+url);
				if(str.length()>71){
					data.put("ui_kmf_no", str.substring(20,22) );
					data.put("ui_kmf_password", str.substring(22,38) );
					data.put("ui_kmf_password_check", str.substring(38,42));
					data.put("ui_kmf_sgc", str.substring(56,62) );
					System.out.println(str.substring(20,22));
					System.out.println(str.substring(22,38));
					System.out.println(str.substring(38,42));
					System.out.println(str.substring(56,62));
				}
				System.out.println(str);
				System.out.println(str.length());

			}
		}
		data.put("r_id", getParaToInt("r_id",0) );
		if(getPara("ui_id")==null||getPara("ui_id").length()<1){
			data.put("ui_password", MD5Util.MD5(getPara("ui_password","123456")));
			Record user = Db.findFirst(Db.getSql("user.isExistence"),getPara("ui_code",""));
			if(user!=null){

				renderJson(Util.getResultMap(Util.state_err,"帐号已存在",null));
				return;
			}
		}

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("primaryKey","ui_id");
		map.put("id",getPara("ui_id"));
		map.put("tableName","fixed_sys_user");
		map.put("data",data);

		renderJson(service.save(map,getPara("ui_code","") ,getPara("m_ids",""),getParaToInt("loginuserid",0)));
	}
	public void resetPassword(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("ui_password", MD5Util.MD5("123456"));
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("primaryKey","ui_id");
		map.put("id",getPara("ui_id"));
		map.put("tableName","fixed_sys_user");
		map.put("data",data);

		renderJson(service.save(map,getParaToInt("loginuserid",0)));
	}
	@Parameter.Must({"ui_id"})
	public void savePassword(){
		synchronized(this){
			Record user = Db.findFirst(Db.getSql("user.isExistence2"),getPara("ui_code",""),getPara("ui_id"));
			if(user!=null){
				renderJson(Util.getResultMap(Util.state_err,"帐号已存在",null));
				return;
			}

			String ui_password = getPara("ui_password","");
			Map<String,Object> map = null;
			if(ui_password.length()>0){
				map = service.savePassword(getPara("ui_code",""),getPara("ui_telno",""), getPara("ui_name",""),MD5Util.MD5(getPara("ui_password","")),getPara("ui_id"),MD5Util.MD5(getPara("ui_password_old","")));
			}else{
				map = service.saveInfo(getPara("ui_code",""),getPara("ui_telno",""), getPara("ui_name",""),getPara("ui_id"),MD5Util.MD5(getPara("ui_password_old","")));
			}
			renderJson(map);
		}
	}
	@Parameter.Must({"default_id_"})
	public void delete(){
		renderJson(service.delete(Db.getSql("user.delete"),getPara("default_id_")));
	}

	@Parameter.Must({"default_id_"})
	public void unlock(){
		renderJson(service.unlock(getPara("default_id_")));
	}

	@Parameter.Must({"ui_code","ui_password"})
	public void login(){
		renderJson(service.login(Db.getSql("user.login"),getPara("ui_code"), MD5Util.MD5(getPara("ui_password")),getParaToBoolean("isCookie")));
	}
	public void VerifyCode(){
		CaptchaRender r = new CaptchaRender();
		render(r);
	}
	public void queryByAll(){
		renderJson(service.queryByAll(Db.getSql("user.queryByAll")));
	}
	public void queryMeterByLandlord(){
		renderJson(service.queryByAll(Db.getSql("user.queryMeterByLandlord"),getPara("ui_id")));
	}
	public void queryUserByLandlord(){
		renderJson(service.queryByAll(Db.getSql("user.queryUserByLandlord"),getPara("ui_id")));
	}
	public void queryLandlord(){
		renderJson(service.queryByAll(Db.getSql("user.queryLandlord")));
	}
	public void queryTenantByLandlord(){
		renderJson(service.queryByAll(Db.getSql("user.queryTenantByLandlord"),getPara("ui_landlord_id")));
	}
	public void queryManageByLandlord(){
		renderJson(service.queryByAll(Db.getSql("user.queryManageByLandlord"),getPara("ui_landlord_id")));
	}
	public String readToString(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (IOException e) {
			Util.SOP(e);
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			Util.SOP(e);
			e.printStackTrace();
			return null;
		}
	}
}
