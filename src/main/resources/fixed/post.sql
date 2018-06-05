#namespace("post")
#define postOrderBy()
    #if(orderBy=="po_no")
      order by po_no
    #else if(orderBy=="po_no desc")
      order by po_no DESC
    #else if(orderBy=="po_name")
      order by po_name
    #else if(orderBy=="po_name desc")
      order by po_name DESC
    #else
      order by j.create_time
    #end
  #end
  #define postFrom()
 select * from fixed_sys_post where 1 = 1
    #if(po_name)
      AND po_name LIKE concat('%',#para(po_name),'%')
    #end
      #@postOrderBy()
  #end

  #sql("query")
  SELECT * from (#@postFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@postFrom())asdfghj
  #end


  #sql("queryById")
  select * from fixed_sys_post where po_id=?
  #end

  #sql("delete")
  delete from fixed_sys_post where po_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_sys_post
  #end

  #sql("queryByDepart")
  select po_name,p.po_id from (select * from fixed_sys_depart_post where dp_id =?) dp
  left join fixed_sys_post p on p.po_id=dp.po_id
  #end

#end