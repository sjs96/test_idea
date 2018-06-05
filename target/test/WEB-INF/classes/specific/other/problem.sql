#namespace("problem")
  #define problemOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time
    #end
  #end
  #define problemFrom()
    select * from sys_problem p where 1=1
    #if(p_name)
      AND p_name LIKE concat('%',#para(p_name),'%')
    #end
      #@problemOrderBy()
  #end

  #sql("query")
  SELECT * from (#@problemFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@problemFrom())asdfghj
  #end


  #sql("queryById")
  select * from sys_problem where p_id=?
  #end

  #sql("delete")
  delete from sys_problem where p_id = ?
  #end

  #sql("queryByAll")
  select * from sys_problem
  #end

#end