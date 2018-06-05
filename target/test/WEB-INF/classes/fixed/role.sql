#namespace("role")
#define roleOrderBy()
    #if(orderBy=="999999")
    #else
      order by r.create_time
    #end
  #end
  #define roleFrom()
   select * from fixed_sys_role r where 1 = 1
  #if(r_name)
      AND r_name LIKE concat('%',#para(r_name),'%')
    #end
      #@roleOrderBy()
  #end

  #sql("query")
  SELECT * from (#@roleFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@roleFrom())asdfghj
  #end


  #sql("queryById")
  select * from fixed_sys_role where r_id=?
  #end

  #sql("delete")
  delete from fixed_sys_role where r_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_sys_role
  #end
  #sql("queryByFD")
  select * from fixed_sys_role  WHERE 1=1
  and  r_id !=(select b_role1 from fixed_sys_base  limit 1)
  and  r_id !=(select b_role2 from fixed_sys_base  limit 1)
  and  r_id !=(select b_role3 from fixed_sys_base  limit 1)
  #end
  #sql("queryByXTGLY")
  select * from fixed_sys_role  WHERE 1=1
  #end
  #sql("queryByEJXTGLY")
  select * from fixed_sys_role  WHERE 1=1
  and  r_id !=(select b_role1 from fixed_sys_base  limit 1)
  and  r_id !=(select b_role2 from fixed_sys_base  limit 1)
  #end
  #sql("queryByName")
  select * from fixed_sys_role where r_name=?
  #end

#end