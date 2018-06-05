#namespace("menu")

#define menuOrderBy()
    #if(orderBy=="999999")
    #else
      order by a.menu_parent_id , menu_order desc
    #end
  #end
  #define menuFrom()
    select a.*,b.menu_name menu_parent_name from fixed_sys_menu a left join fixed_sys_menu b on a.menu_parent_id = b.menu_id where 1 = 1
    #if(menu_parent_id)
      AND a.menu_parent_id = #para(menu_parent_id)
    #end
    #if(menu_name)
      AND a.menu_name LIKE concat('%',#para(menu_name),'%')
    #end
    #if(menu_valid)
      AND a.menu_valid = #para(menu_valid)
    #end
      #@menuOrderBy()
  #end

  #sql("query")
  SELECT * from (#@menuFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@menuFrom())asdfghj
  #end


  #sql("queryById")
  select * from fixed_sys_menu where menu_id=?
  #end

  #sql("delete")
  delete from fixed_sys_menu where menu_id = ?
  #end

  #sql("queryByFirst")
  select * from fixed_sys_menu where menu_level=1 order by menu_order desc
  #end

  #sql("queryBySecond")
  SELECT distinct m.* from fixed_sys_menu m
  LEFT JOIN fixed_sys_jurisdiction_details jd on jd.m_id = m.menu_id
  LEFT JOIN fixed_sys_jurisdiction j on jd.j_id = j.j_id
  LEFT JOIN fixed_sys_user u on u.r_id = j.r_id
  where u.ui_id=? and m.menu_level=2 order by menu_order desc
  #end

  #sql("queryByNew")
  select * from fixed_sys_menu order by create_time desc
  #end
  #sql("deleteAttribute")
  delete from fixed_sys_menu_attribute where m_id=?
  #end

   #sql("queryAttribute")
 select * from fixed_sys_menu_attribute where m_id=?
  #end
#end