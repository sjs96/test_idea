#namespace("notice")

#define noticeOrderBy()
  #if(orderBy=="999999")
      #else
        order by n.create_time
      #end
  #end
  #define noticeFrom()
      select * from fixed_sys_notice n where 1=1
      #@noticeOrderBy()
  #end

  #sql("query")
  SELECT * from (#@noticeFrom())asdfghj
  #end

  #sql("queryCount")
  SELECT count(*) from (#@noticeFrom())asdfghj
  #end

  #sql("queryById")
  select * from fixed_sys_notice where n_id=?
  #end

  #sql("delete")
  delete from fixed_sys_notice where n_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_sys_notice
  #end

#end