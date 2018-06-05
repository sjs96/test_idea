#namespace("price")
#define priceOrderBy()
    #if(orderBy=="create_time")
      order by p.create_time
    #else if(orderBy=="create_time desc")
      order by p.create_time DESC
    #else if(orderBy=="update_time")
      order by p.update_time
    #else if(orderBy=="update_time desc")
      order by p.update_time DESC
    #else if(orderBy=="p_price")
      order by p.p_price
    #else if(orderBy=="p_price desc")
      order by p.p_price DESC
    #else
      order by p.create_time
    #end
#end
#define priceFrom()
  select p.*,ifnull(u.ui_name,'')ui_landlord_name from sys_price p
  LEFT JOIN fixed_sys_user u ON p.ui_landlord_id=u.ui_id
  where 1=1
   #if(p_name)
      AND p_name LIKE concat('%',#para(p_name),'%')
    #end
    #if(ui_landlord_id)
      AND p.ui_landlord_id = #para(ui_landlord_id)
    #end
    #if(ui_tenant_id)
      AND u.ui_id = #para(ui_tenant_id)
    #end
    #if(ui_manage_id)
      AND p.ui_landlord_id = (select ui_landlord_id from fixed_sys_user where ui_id = #para(ui_manage_id))
    #end
    #@priceOrderBy()
#end

  #sql("query")
  SELECT * from (#@priceFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@priceFrom())asdfghj
  #end



  #sql("queryById")
  select * from sys_price where p_id=?
  #end

  #sql("delete")
  delete from sys_price where p_id = ?
  #end

  #sql("queryByAll")
  select * from sys_price
  #end
  #sql("queryByLandlord")
  select *,concat(p_name,'(',p_price,')') as p_name_price from sys_price  where  ui_landlord_id=?
  #end

#end