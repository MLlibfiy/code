
#创建多个版本的表
如果插入数据超过了最大版本数，最老的数据会被删除掉
create 'User', {NAME => 'info', VERSIONS => 5}

#指定列族和版本数量查询
scan 'User' ,{NAME => 'info', VERSIONS => 5}


# TTL  数据过期时间，秒，针对每一个单元格
create 'User', {NAME => 'info', TTL => 5}


