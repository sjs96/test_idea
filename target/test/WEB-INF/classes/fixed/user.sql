#namespace("user")
#define userOrderBy()
    #if(orderBy=="999999")
    #else
      order by u.create_time
    #end
  #end
  #define userFrom()
   select u.*,r.r_name,ifnull(u2.ui_name,'')ui_landlord_name,if(u.m_account_error_time>NOW(),1,0) as error_state,m.meter_num,u3.user_num from fixed_sys_user u
     LEFT JOIN fixed_sys_user u2 ON u.ui_landlord_id=u2.ui_id
     LEFT JOIN fixed_sys_role r on u.r_id= r.r_id
      LEFT JOIN (select ui_landlord_id as meter_landlord_id,count(*)meter_num from sys_meter GROUP BY ui_landlord_id) m on m.meter_landlord_id= u.ui_id
      LEFT JOIN (select ui_landlord_id as user_landlord_id,count(*)user_num from fixed_sys_user GROUP BY ui_landlord_id) u3 on u3.user_landlord_id= u.ui_id
     where 1=1
  #if(ui_code)
      AND u.ui_code LIKE concat('%',#para(ui_code),'%')
    #end
     #if(ui_landlord_id)
      AND u.ui_landlord_id = #para(ui_landlord_id)
    #end
    #if(ui_tenant_id)
      AND u.ui_id = #para(ui_tenant_id)
    #end
    #if(ui_manage_id)
      AND u.ui_landlord_id = (select ui_landlord_id from fixed_sys_user where ui_id = #para(ui_manage_id))
    #end
     #if(ui_name)
      AND u.ui_name LIKE concat('%',#para(ui_name),'%')
    #end
     #if(ui_nickname)
      AND u.ui_nickname LIKE concat('%',#para(ui_nickname),'%')
    #end
     #if(ui_telno)
      AND u.ui_telno LIKE concat('%',#para(ui_telno),'%')
    #end
     #if(r_id)
      AND u.r_id = #para(r_id)
    #end
    #if('role_xtgly'.equals(my_role))
      AND 1=1
    #end
     #if('role_ejxtgly'.equals(my_role))
      AND u.r_id != (select b_role1 from fixed_sys_base limit 1)
      AND u.r_id != (select b_role2 from fixed_sys_base limit 1)
    #end
     #if('role_fd'.equals(my_role))
      AND u.r_id != (select b_role1 from fixed_sys_base limit 1)
      AND u.r_id != (select b_role2 from fixed_sys_base limit 1)
      AND u.r_id != (select b_role3 from fixed_sys_base limit 1)
    #end
     #if('role_fdgly'.equals(my_role))
      AND u.r_id != (select b_role1 from fixed_sys_base limit 1)
      AND u.r_id != (select b_role2 from fixed_sys_base limit 1)
      AND u.r_id != (select b_role3 from fixed_sys_base limit 1)
      AND u.r_id != (select b_role4 from fixed_sys_base limit 1)
    #end
     #if('role_fk'.equals(my_role))
      AND 1=2
    #end
      #@userOrderBy()
  #end

  #sql("query")
  SELECT * from (#@userFrom())asdfghj
  #end
  #sql("queryMeterByLandlord")
  select * from sys_meter where ui_landlord_id = ?
  #end
  #sql("queryUserByLandlord")
  select * from fixed_sys_user where ui_landlord_id = ?
  #end

  #sql("queryCount")
  SELECT count(*) from (#@userFrom())asdfghj
  #end


  #sql("queryById")
  select * from fixed_sys_user where ui_id=?
  #end
  #sql("queryByAll")
  select * from fixed_sys_user
  #end
  #sql("queryLandlord")
  select u.* from fixed_sys_user u LEFT JOIN fixed_sys_role r on u.r_id=r.r_id where r.r_id = (select b_role3 from fixed_sys_base  limit 1)
  #end
  #sql("queryTenantByLandlord")
  select u.* from fixed_sys_user u LEFT JOIN fixed_sys_role r on u.r_id=r.r_id where r.r_id = (select b_role5 from fixed_sys_base limit 1 ) AND  u.ui_landlord_id=?
  #end
  #sql("queryManageByLandlord")
  select u.* from fixed_sys_user u LEFT JOIN fixed_sys_role r on u.r_id=r.r_id where r.r_id = (select b_role4 from fixed_sys_base  limit 1) AND  u.ui_landlord_id=?
  #end
  #sql("delete")
  delete from fixed_sys_user where ui_id = ? and ui_code!='admin'
  #end
  #sql("login")
  select u.*,r.r_name,u2.ui_name as ui_landlord_name,if(u.m_account_error_time>NOW(),1,0) as error_state from fixed_sys_user u
  LEFT JOIN fixed_sys_role r on u.r_id=r.r_id
  LEFT JOIN fixed_sys_user u2 on u.ui_landlord_id=u2.ui_id
  where u.ui_code = ? and u.ui_password = ?
  #end

  #sql("lockout")
  select u.* from fixed_sys_user u
  LEFT JOIN fixed_sys_role r on u.r_id=r.r_id
  LEFT JOIN fixed_sys_user u2 on u.ui_landlord_id=u2.ui_id
  where u.ui_code = ? and u.m_account_error_time>NOW()
  #end
   #sql("unlock")
  UPDATE fixed_sys_user set m_account_error_time = NULL  where ui_id=?
  #end

 #sql("queryByNew")
 select * from fixed_sys_user order by create_time desc
  #end

  #sql("deletePost")
 delete from fixed_sys_user_post where ui_id = ?
  #end

   #sql("queryByPost")
  select * from fixed_sys_user_post where ui_id = ?
  #end
   #sql("isExistence")
  select * from fixed_sys_user where ui_code=?
  #end
  #sql("isExistence2")
  select * from fixed_sys_user where ui_code=? and ui_id!=?
  #end
  #sql("queryBaseById")
  select ui_id,ui_code,ui_name,ui_nickname,ui_telno from fixed_sys_user where ui_id=?
  #end
   #sql("AccountError")
  UPDATE fixed_sys_user set m_account_error_time = date_add(now(), interval 1 hour)  where ui_code=?
  #end
  #sql("savePassword")
  UPDATE fixed_sys_user set ui_code=?,ui_telno = ?,ui_name=?,ui_password=?  where ui_id=? and ui_password=?
  #end
  #sql("saveInfo")
  UPDATE fixed_sys_user set ui_code=?,ui_telno = ?,ui_name=?  where ui_id=? and ui_password=?
  #end
  #sql("isComplete")
  select * from fixed_sys_user where ui_id=? and ui_kmf_password_check is NOT  NULL  and ui_kmf_password is NOT  NULL
  #end
  #sql("isRoleXTGLY")
    select * from fixed_sys_user where ui_id=? and r_id = (select b_role1 from fixed_sys_base limit 1)
  #end
  #sql("isRoleEJXTGLY")
    select * from fixed_sys_user where ui_id=? and r_id = (select b_role2 from fixed_sys_base limit 1)
  #end
  #sql("isRoleFD")
    select * from fixed_sys_user where ui_id=? and r_id = (select b_role3 from fixed_sys_base limit 1)
  #end
  #sql("isRoleFDGLY")
    select * from fixed_sys_user where ui_id=? and r_id = (select b_role4 from fixed_sys_base limit 1)
  #end
  #sql("isRoleFK")
    select * from fixed_sys_user where ui_id=? and r_id = (select b_role5 from fixed_sys_base limit 1)
  #end

#end