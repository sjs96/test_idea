#namespace("meter")

#define meterOrderBy()
    #if(orderBy=="ui_name")
      order by u.ui_name
    #else if(orderBy=="ui_name desc")
      order by u.ui_name DESC
    #else if(orderBy=="m_no")
      order by m_no
    #else if(orderBy=="m_no desc")
      order by m_no DESC
    #else if(orderBy=="m_electricity")
      order by m_electricity
    #else if(orderBy=="m_electricity desc")
      order by m_electricity DESC
    #else
      order by  m.create_time
    #end
#end
#define meterFrom()
  select m.*,p.p_name,u.ui_name,p.p_price,ifnull(u2.ui_name,'')ui_landlord_name,concat(p.p_name,'(',p.p_price,')') as p_name_price,oo.order_num,if(DATE_ADD(online_time, interval 360 minute)>now(),if(order_num>0,-3,if(m_error=0,m_state,-4)),-2) as meter_state
   from sys_meter m
  left join fixed_sys_user u on m.ui_id=u.ui_id
  LEFT JOIN fixed_sys_user u2 ON m.ui_landlord_id=u2.ui_id
  left join sys_price p on m.p_id=p.p_id
  LEFT JOIN(select m_id,count(*)order_num from sys_order where o_state=1 group by m_id)oo on oo.m_id=m.m_id
  where 1=1
    #if(m_no)
      AND m.m_no LIKE concat('%',#para(m_no),'%')
    #end
    #if(ui_landlord_id)
      AND m.ui_landlord_id = #para(ui_landlord_id)
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
    #@meterOrderBy()
#end

  #sql("query")
  SELECT * from (#@meterFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@meterFrom())asdfghj
  #end

  #sql("queryById")
      select * from sys_meter m
      left join sys_price p on m.p_id=p.p_id
      where m_id=?
  #end


  #sql("delete")
  delete from sys_meter where m_id = ?
  #end

  #sql("queryByAll")
  select * from sys_meter
  #end
  #sql("queryByFree")
  select * from sys_meter where ui_landlord_id=? and (ui_id is Null or ui_id=0 or ui_id=?)
  #end
  #sql("queryByMe")
  select * from sys_meter where ui_id=? AND ui_id !=0
  #end
 #sql("isExistence")
  select * from sys_meter where m_no=?
  #end
 #sql("queryByReady1")
    select m.*,u2.* from sys_meter m
    left join fixed_sys_user u on m.ui_id = u.ui_id
    left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token1 is null OR m_token2 is null and m_error=0
  #end
 #sql("queryByReady2")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_state=0 and m_error=0
  #end
 #sql("queryByReady3")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_state=1 AND m_token1 is not null AND m_token2 is not null and m_error=0
  #end
 #sql("setUser")
UPDATE sys_meter set ui_id = null where ui_id=?
  #end
 #sql("setUser2")
UPDATE sys_meter set ui_id = ? where m_id=?
  #end
   #sql("register2")
UPDATE sys_meter set m_state = 2 where m_no = ? and m_state=1
  #end
 #sql("register1")
 UPDATE sys_meter set m_state = 1 where m_no = ? and m_state=0
  #end
 #sql("setToken")
  UPDATE sys_meter set m_token1 = ?,m_token2=? where m_no = ?
  #end
 #sql("setElectricity")
  UPDATE sys_meter set m_electricity = ? ,online_time = now() where m_no = ?
  #end

  #sql("queryByGL1")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_gl ='start' and m_error=0
  #end
    #sql("queryByKgFd1")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fd ='start' and m_error=0
  #end
    #sql("queryByKgFk1")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fk ='start' and m_error=0
  #end
  #sql("queryByQDL1")
  select o.*,m.m_no,u2.*,convert(o.p_money/o.p_price, decimal(12,2)) p_electric  from sys_order o
  LEFT JOIN sys_meter m on o.m_id= m.m_id
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id
  where o_type = -1 AND o_state=1 and m_error=0
  #end
  #sql("queryByQD1")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_qd ='start' and m_error=0
  #end
  #sql("queryByKgfd1")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fd ='start' and m_error=0
  #end
  #sql("queryByKgfk1")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fk ='start' and m_error=0
  #end
  #sql("queryByGL2")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_gl !='start' AND m_token_gl is not null and m_error=0
  #end
  #sql("queryByKgFd2")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fd !='start' AND m_token_kg_fk is not null and m_error=0
  #end
  #sql("queryByKgFk2")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fk !='start' AND m_token_kg_fk is not null and m_error=0
  #end
  #sql("queryByQD2")
  select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_qd !='start' AND m_token_qd is not null and m_error=0
  #end
   #sql("queryByKgfd2")
    select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fd !='start' AND m_token_kg_fd is not null and m_error=0
  #end
  #sql("queryByKgfk2")
   select m.*,u2.* from sys_meter m
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_token_kg_fk !='start' AND m_token_kg_fk is not null and m_error=0
  #end
  #sql("queryByQDL2")
    select o.*,m.m_no,u2.*,convert(o.p_money/o.p_price, decimal(12,2)) p_electric  from sys_order o
  LEFT JOIN sys_meter m on o.m_id= m.m_id
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id
  where o_type = -1  AND o_state=2 and m_error=0
  #end
 #sql("setTokenGL")
  UPDATE sys_meter set m_token_gl = ? where m_no = ?
  #end
 #sql("setTokenKGFD")
  UPDATE sys_meter set m_token_kg_fd = ? where m_no = ?
  #end
   #sql("setTokenKGFK")
  UPDATE sys_meter set m_token_kg_fk = ? where m_no = ?
  #end
   #sql("setTokenQD")
  UPDATE sys_meter set m_token_qd = ? where m_no = ?
  #end
   #sql("setTokenKgfd")
  UPDATE sys_meter set m_token_kg_fd = ?,m_kg_fd=? where m_no = ?
  #end
   #sql("setTokenKgfk")
  UPDATE sys_meter set m_token_kg_fk = ?,m_kg_fk=? where m_no = ?
  #end
  #sql("setTokenQDL")
  UPDATE sys_order set o_token = ?,o_state=2,update_time=now() where o_id = ? and o_state = 1
  #end
 #sql("setTokenGLEmpty")
  UPDATE sys_meter set m_token_gl = null where m_no = ? AND  m_token_gl !='start' AND m_token_gl is not null
  #end
 #sql("setTokenKGFDEmpty")
  UPDATE sys_meter set m_token_kg_fd = null where m_no = ? AND  m_token_kg_fd !='start' AND m_token_kg_fd is not null
  #end
 #sql("setTokenKGFKEmpty")
  UPDATE sys_meter set m_token_kg_fk = null where m_no = ? AND  m_token_kg_fk !='start' AND m_token_kg_fk is not null
  #end

   #sql("setTokenQDEmpty")
  UPDATE sys_meter set m_token_qd = null  where m_no = ? AND  m_token_qd !='start' AND m_token_qd is not null
  #end
  #sql("setTokenKgfkEmpty")
  UPDATE sys_meter set m_token_kg_fk = null  where m_no = ? AND  m_token_kg_fk !='start' AND m_token_kg_fk is not null
  #end
  #sql("setTokenKgfdEmpty")
  UPDATE sys_meter set m_token_kg_fd = null  where m_no = ? AND  m_token_kg_fd !='start' AND m_token_kg_fd is not null
  #end
  #sql("setGL")
  UPDATE sys_meter set m_token_gl ='start',m_power=? where m_id = ?
  #end
  #sql("setKGFD")
  UPDATE sys_meter set m_token_kg_fd ='start',m_kg_fd=?,m_kg_fk=? where m_id = ?
  #end
  #sql("setKGFK")
  UPDATE sys_meter set m_token_kg_fk ='start',m_kg_fk=? where m_id = ?
  #end

  #sql("setQD")
  UPDATE sys_meter set m_token_qd ='start' where m_id = ?
  #end
  #sql("setMeterMoney")
  update sys_meter set
  m_money=(ifnull(m_money,0)+(select p_money from sys_order where o_id =?)),
  m_electricity=(ifnull(m_electricity,0)+(select p_money/p_price m_electricity from sys_order where o_id = ?))
  where m_id = (select m_id from sys_order where o_id = ?)
  #end

  #sql("isAvailable")
  select "1" as type,"正在设置最大功率门限" as msg from sys_meter where m_token_gl is not null and m_id=?
  Union
  select "2" as type,"正在清除剩余电量" as msg from sys_order where o_state!=3 and m_id=? and o_type=-1
  Union
  select "3" as type,"正在清除窃电状态" as msg from sys_meter where m_token_qd is not null and m_id=?
  Union
  select "4" as type,"正在充值" as msg from sys_order where o_state!=3 and m_id=? and o_type=1
  Union
  select "5" as type,"电表未注册" as msg from sys_meter where (m_state=0 or m_state=1 )and m_id=?
  Union
  select "6" as type,"房东kmf文件异常" as msg from sys_meter m  left join fixed_sys_user u on m.ui_id = u.ui_id left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id where m_id=? and (LENGTH(u2.ui_kmf_password_check)!=4 or LENGTH(u2.ui_kmf_password) !=16 or LENGTH(u2.ui_kmf_sgc) !=6 or LENGTH(u2.ui_kmf_no) !=2 or u2.ui_kmf_password_check is null or u2.ui_kmf_password is null or u2.ui_kmf_sgc is null or u2.ui_kmf_no is null)
  Union
  select "7" as type,"房东正在合闸" as msg from sys_meter where m_token_kg_fd is not null and m_kg_fd=0 and m_id=?
  Union
  select "8" as type,"房东正在开闸" as msg from sys_meter where m_token_kg_fd is not null and m_kg_fd=1 and m_id=?
  Union
  select "9" as type,"房客正在合闸" as msg from sys_meter where m_token_kg_fk is not null and m_kg_fk=0 and m_id=?
  Union
  select "10" as type,"房客正在开闸" as msg from sys_meter where m_token_kg_fk is not null and m_kg_fk=1 and m_id=?
   Union
  select "11" as type,"电表异常" as msg from sys_meter where m_error!=0 and m_id=?
  #end
  #sql("empty")
    update sys_meter set m_money=0,m_electricity=0 where m_no =?
  #end
  #sql("setError")
  UPDATE sys_meter set m_error = ?,m_error_msg=? where m_no = ?
  #end
  #sql("removeError")
  UPDATE sys_meter set m_error = 0,m_error_msg='' where m_id = ?
  #end
#end