#namespace("order")
  #define orderOrderBy()
   #if(orderBy=="o_type")
      order by o_type
    #else if(orderBy=="o_type desc")
      order by o_type DESC
    #else if(orderBy=="ui_name")
      order by ui_name
    #else if(orderBy=="ui_name desc")
      order by ui_name DESC
    #else if(orderBy=="m_no")
      order by m_no
    #else if(orderBy=="m_no desc")
      order by m_no DESC
    #else if(orderBy=="p_total")
      order by p_total
    #else if(orderBy=="p_total desc")
      order by p_total DESC
    #else if(orderBy=="p_money")
      order by p_money
    #else if(orderBy=="p_money desc")
      order by p_money DESC
    #else if(orderBy=="create_time")
      order by create_time
    #else if(orderBy=="create_time desc")
      order by create_time DESC
    #else if(orderBy=="update_time")
      order by update_time
    #else if(orderBy=="update_time desc")
      order by update_time DESC
    #else
      order by o.create_time
    #end
  #end
  #define orderFrom()
    select o.*,convert(o.p_money/o.p_price, decimal(12,2)) as p_total,m.m_no,u.ui_name,ifnull(u2.ui_name,'')ui_landlord_name from sys_order o
    left join sys_meter m on o.m_id= m.m_id
     left join fixed_sys_user u on u.ui_id= m.ui_id
    LEFT JOIN fixed_sys_user u2 ON m.ui_landlord_id=u2.ui_id
   where 1=1
    #if(ui_landlord_id)
      AND u.ui_landlord_id = #para(ui_landlord_id)
    #end
    #if(ui_tenant_id)
      AND u.ui_id = #para(ui_tenant_id)
    #end
    #if(ui_manage_id)
      AND u.ui_landlord_id = (select ui_landlord_id from fixed_sys_user where ui_id = #para(ui_manage_id))
    #end
    #if(time_start)
      AND o.create_time > #para(time_start)
    #end
    #if(time_end)
      AND o.create_time < #para(time_end)
    #end
      #@orderOrderBy()
  #end

  #sql("query")
  SELECT * from (#@orderFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@orderFrom())asdfghj
  #end

  #sql("queryByExecl")
  SELECT *,
   case o_type
    when 1 then '售电'
    when -1 then '清电'
    else '未知'
    end AS o_type_str ,
   case o_state
    when 1 then '未完成'
    when 2 then '生成token'
    when 3 then '完成'
    else '未知'
    end AS o_state_str
   from (#@orderFrom())asdfghj
  #end

  #sql("queryById")
  select * from sys_order where o_id=?
  #end

  #sql("delete")
  delete from sys_order where o_id = ?
  #end

  #sql("queryByAll")
  select * from sys_order
  #end
 #sql("queryTotalByAdd")
  SELECT convert(sum(p_money), decimal(12,2)) as money,convert(sum(p_money*p_price), decimal(12,2)) as electricity from (#@orderFrom())asdfghj where o_type =1
  #end
   #sql("queryTotalByDel")
  SELECT convert(sum(p_money), decimal(12,2)) as money,convert(sum(p_money*p_price), decimal(12,2)) as electricity from (#@orderFrom())asdfghj where o_type =-1
  #end

  #sql("queryByRecharge")
  select o.*,m.m_no,u2.*,convert(o.p_money/o.p_price, decimal(12,2)) p_electric  from sys_order o
  LEFT JOIN sys_meter m on o.m_id= m.m_id
  left join fixed_sys_user u on m.ui_id = u.ui_id
  left join fixed_sys_user u2 on u.ui_landlord_id = u2.ui_id
  where o_token is null AND o_type = 1 AND  o_state=1  and m_error=0
  #end
  #sql("setToken")
  UPDATE sys_order set o_state = 2,o_token = ?,update_time=now() where o_id = ? and o_state = 1
  #end
  #sql("rechargeState")
  UPDATE sys_order set o_state = 3,update_time=now() where m_id = (SELECT m_id from sys_meter where m_no=?) and o_state=2
  #end
  #sql("queryByRecharge2")
  select* from sys_order o
  LEFT JOIN sys_meter m on o.m_id= m.m_id where  o_state=2 AND o_type = 1  and m_error=0
  #end
  #sql("getOrderInfo")
 select o.o_id,o.o_remarks,DATE_FORMAT(o.create_time,"%Y-%m-%d %T") create_time,o.o_token,m.m_no,m.m_electricity,u.ui_name,o.p_price,o.p_money,convert(o.p_money/o.p_price, decimal(12,2)) p_electric from sys_order o
left join sys_meter m on o.m_id=m.m_id
left join fixed_sys_user u on m.ui_id=u.ui_id
where o.o_id=?
  #end

#end