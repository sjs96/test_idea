#namespace("tableSetting")
#define tableSettingOrderBy()
    #if(orderBy=="999999")
    #else
      order by r.create_time
    #end
  #end
  #define tableSettingFrom()
   select * from fixed_table_setting r where 1 = 1
      #@tableSettingOrderBy()
  #end

  #sql("query")
  SELECT * from (#@tableSettingFrom())asdfghj
  #end

  #sql("queryCount")
  #end


  #sql("queryById")
  select * from fixed_table_setting where table_setting_id=?
  #end

  #sql("delete")
  delete from fixed_table_setting where table_setting_id = ?
  #end
  #sql("deleteModel")
  delete from fixed_table_setting where  ui_id =? and table_setting_model = ?
  #end

  #sql("queryByAll")
  select * from fixed_table_setting
  #end

  #sql("queryByUIID")
  select * from fixed_table_setting where ui_id=?
  #end

#end