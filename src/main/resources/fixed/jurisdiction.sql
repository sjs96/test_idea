#namespace("jurisdiction")

  #define jurisdictionOrderBy()
    #if(orderBy=="999999")
    #else
      order by j.create_time
    #end
  #end
  #define jurisdictionFrom()
   select j.*,r.r_name from fixed_sys_jurisdiction j
   left join  fixed_sys_role r on j.r_id=r.r_id  where 1 = 1
     #if(j_name)
      AND j_name LIKE concat('%',#para(j_name),'%')
    #end
      #@jurisdictionOrderBy()
  #end

  #sql("query")
  SELECT * from (#@jurisdictionFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@jurisdictionFrom())asdfghj
  #end


  #sql("queryById")
   select j.*,r.r_name from fixed_sys_jurisdiction j
   left join  fixed_sys_role r on j.r_id=r.r_id  where j_id=?
  #end

  #sql("delete")
  delete from fixed_sys_jurisdiction where j_id = ?
  #end
  #sql("delete1")
  delete from fixed_sys_role where r_id = (select r_id from fixed_sys_jurisdiction where j_id=?)
  #end

  #sql("queryByAll")
  select * from fixed_sys_jurisdiction
  #end

  #sql("queryMenuByOne")
  select * from fixed_sys_menu where menu_level=1
  #end

  #sql("queryMenuByTwo")
  select * from fixed_sys_menu where menu_parent_id=?
  #end

  #sql("queryByNew")
  select * from fixed_sys_jurisdiction order by create_time desc
  #end
  #sql("deleteDetails")
  delete from fixed_sys_jurisdiction_details where j_id=?
  #end

 #sql("queryDetails")
 select * from fixed_sys_jurisdiction_details where j_id=?
  #end
 #sql("queryByUser")
SELECT jd.jd_name,m.menu_linkurl from fixed_sys_jurisdiction_details jd
INNER JOIN fixed_sys_menu m on jd.m_id = m.menu_id
LEFT JOIN fixed_sys_jurisdiction j on jd.j_id = j.j_id
LEFT JOIN fixed_sys_user u on u.r_id = j.r_id
where u.ui_id=?
  #end

#end