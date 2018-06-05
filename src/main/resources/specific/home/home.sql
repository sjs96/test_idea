#namespace("home")

  #sql("queryBy1")
    select count(*) as num  from fixed_sys_user
  #end
 #sql("queryBy2")
    select count(*) as num  from sys_meter
  #end
  #sql("queryBy3")
    select sum(p_price) as num from sys_price
  #end
  #sql("queryBy4")
   select count(*) as num from fixed_sys_user where  to_days(create_time) = to_days(now())
  #end
  #sql("queryByMonth")
   select DATE_FORMAT(create_time,'%Y-%m') as month,count(*)as num from sys_meter GROUP BY DATE_FORMAT(create_time,'%Y-%m')
  #end

 #sql("queryUserByMonth")
   select DATE_FORMAT(create_time,'%Y-%m') as month,count(*) as num from fixed_sys_user GROUP BY DATE_FORMAT(create_time,'%Y-%m')
  #end
 #sql("queryUserByYear")
   select DATE_FORMAT(create_time,'%Y') as month,count(*)as num from fixed_sys_user GROUP BY DATE_FORMAT(create_time,'%Y')
  #end

 #sql("queryOrderByMonth")
   select DATE_FORMAT(create_time,'%Y-%m') as month,count(*)as num from sys_order GROUP BY DATE_FORMAT(create_time,'%Y-%m')
  #end
  #sql("queryOrderByYear")
     select DATE_FORMAT(create_time,'%Y') as month,count(*)as num from sys_order GROUP BY DATE_FORMAT(create_time,'%Y')
  #end
#end