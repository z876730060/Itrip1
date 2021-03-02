package com.itrip.dao.itripUser;
import com.itrip.pojo.ItripUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface ItripUserMapper {

	public ItripUser getItripUserById(@Param(value = "id") Long id)throws Exception;

	public List<ItripUser>	getItripUserListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripUserCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripUser(ItripUser itripUser)throws Exception;

	public Integer updateActived(@Param(value = "activated") int activated, @Param(value = "userCode") String userCode) throws  Exception;

	public Integer updateItripUser(ItripUser itripUser)throws Exception;

	public Integer deleteItripUserById(@Param(value = "id") Long id)throws Exception;

	public ItripUser getItripUser(@Param(value = "name") String name, @Param(value = "password") String password)throws Exception;

	public ItripUser isExisted(@Param(value = "name") String name)throws Exception;
}
