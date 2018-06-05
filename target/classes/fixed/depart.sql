#namespace("depart")

  #sql("query")
  select a.*,b.dp_name dp_top_name from fixed_sys_depart a left join fixed_sys_depart b on a.dp_topid = b.dp_id where 1 = 1

    #if(dp_name)
      AND a.dp_name LIKE concat('%',#para(dp_name),'%')
    #end
    #if(dp_tel)
      AND a.dp_tel LIKE concat('%',#para(dp_tel),'%')
    #end
     #if(dp_topid)
      AND a.dp_topid  = #para(dp_topid)
    #end
  #end

  #sql("queryCount")
  select count(*) from fixed_sys_depart  where 1 = 1

    #if(dp_name)
      AND dp_name LIKE concat('%',#para(dp_name),'%')
    #end
    #if(dp_tel)
      AND dp_tel LIKE concat('%',#para(dp_tel),'%')
    #end
     #if(dp_topid)
      AND dp_topid  = #para(dp_topid)
    #end
  #end

  #sql("queryById")
  select * from fixed_sys_depart where dp_id=?
  #end

  #sql("delete")
  delete from fixed_sys_depart where dp_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_sys_depart
  #end

 #sql("queryByNew")
 select * from fixed_sys_depart order by create_time desc
  #end

  #sql("deletePost")
 delete from fixed_sys_depart_post where dp_id = ?
  #end

   #sql("queryByPost")
  select po_id from fixed_sys_depart_post where dp_id = ?
  #end
#end