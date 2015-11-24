delimiter //
create procedure getLastRate(IN table_name varchar(25),IN vos_id varchar(25)) 
begin 
set @sql=concat('select * from (select * from ',table_name,' where vosId="',vos_id,'" and is_available=true and is_success=true and is_correct=true order by send_time desc) abc group by code' );
 prepare stmt from @sql; 
 execute stmt; 
 deallocate prepare stmt; 
 select @sql; 
 end//
 
 
