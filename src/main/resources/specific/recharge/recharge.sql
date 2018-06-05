#namespace("recharge")


#define rechargeOrderBy()
    #if(orderBy=="m_electricity")
      order by m.m_electricity
    #else if(orderBy=="m_electricity desc")
      order by m.m_electricity DESC
    #else
      order by m.create_time
    #end
#end
#define rechargeFrom()
   select m.*,p.p_name,u.ui_name,p.p_price,ifnull(u2.ui_name,'')ui_landlord_name from sys_meter m
    left join fixed_sys_user u on m.ui_id=u.ui_id
  LEFT JOIN fixed_sys_user u2 ON m.ui_landlord_id=u2.ui_id
  left join sys_price p on m.p_id=p.p_id
 where 1=1
    #if(m_no)
      AND m.m_no LIKE concat('%',#para(m_no),'%')
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
    #@rechargeOrderBy()
#end

  #sql("query")
  SELECT * from (#@rechargeFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@rechargeFrom())asdfghj
  #end


  #sql("queryById")
  select * from sys_meter m left join sys_price p on m.p_id=p.p_id where m_id=?
  #end

  #sql("delete")
  delete from sys_meter where m_id = ?
  #end

  #sql("queryByAll")
  select * from sys_meter
  #end

#end