package com.bright.apollo.dao.obox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;

@Mapper
@Component
public interface TOboxMapper extends BaseMapper<TObox, TOboxExample, Integer> {
	
	
  
	/**  
	 * @param userId
	 * @param pageStart
	 * @param pageEnd
	 * @return  
	 * @Description:  
	 */
	List<TObox> queryOboxByUserId(@Param("userId") Integer userId,@Param("pageStart") int pageStart,@Param("pageEnd") int pageEnd);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	int queryCountOboxByUserId(Integer userId);
	@Results(value = {
            @Result(column="id"   ,property="id"),
            @Result(column="obox_name" ,  property="oboxName"),
            @Result(column="obox_pwd" ,  property="oboxPwd"),
            @Result(column="obox_version" ,  property="oboxVersion"),
            @Result(column="last_op_time" ,  property="lastOpTime"),
            @Result(column="obox_status" ,  property="oboxStatus"),
            @Result(column="license"  , property="license"),
            @Result(column="obox_ip" ,  property="oboxIp" ),
            @Result(column="obox_addr"  , property="oboxAddr" ),
            @Result(column="obox_person"  , property="oboxPerson"),
            @Result(column="obox_activate" ,  property="oboxActivate"),
            @Result(column="obox_control" ,  property="oboxControl"),
            @Result(column="obox_serial_id" ,  property="oboxSerialId")
    })
	@Select("select * from t_obox where obox_serial_id = #{oboxSerialId} ")
	TObox queryOboxsByOboxSerialId(@Param("oboxSerialId") String oboxSerialId) throws Exception;

	@Update("update t_obox set obox_name = #{oboxName},\n" +
			"obox_serial_id = #{oboxSerialId },\n" +
			"obox_version = #{oboxVersion },\n" +
			"obox_status = #{oboxStatus}  where  obox_serial_id = #{oboxSerialId} ")
	void updateObox(TObox obox);
	@Results(value = {
            @Result(column="id"   ,property="oboxId"),
            @Result(column="obox_name" ,  property="oboxName"),
            @Result(column="obox_pwd" ,  property="oboxPwd"),
            @Result(column="obox_version" ,  property="oboxVersion"),
            @Result(column="last_op_time" ,  property="lastOpTime"),
            @Result(column="obox_status" ,  property="oboxStatus"),
            @Result(column="license"  , property="license"),
            @Result(column="obox_ip" ,  property="oboxIp" ),
            @Result(column="obox_addr"  , property="oboxAddr" ),
            @Result(column="obox_person"  , property="oboxPerson"),
            @Result(column="obox_activate" ,  property="oboxActivate"),
            @Result(column="obox_control" ,  property="oboxControl")
    })
	@Select("select tb.* from t_device_channel tdc\n" +
			"inner join t_obox tb on tdc.obox_id=tb.obox_id\n" +
			"where tdc.device_id=#{deviceId}\n" +
			"order by signal_intensity desc\n" +
			"limit 1,1 ")
	TObox queryOboxsByDeviceChannelId(@Param("deviceId") int deviceId) throws Exception;

	@Delete("delete from t_obox where id = #{id} ")
	void deleteOboxById(TObox obox);

	@Select("select * from t_obox where id = #{id}")
	TObox queryOboxsById(@Param("id") int id);

	@Select("select * from t_obox tob\n" +
			"inner join t_obox_device_config todc on tob.id=todc.obox_id\n" +
			"inner join t_device_channel tdc on tdc.device_id=todc.id\n" +
			"where todc.id =#{oboxDeviceId} and tob.id = #{oboxId} ")
	List<TObox> getOboxsByDeviceChannel(@Param("oboxDeviceId") int oboxDeviceId,@Param("oboxId") int oboxId);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Results(value = {
            @Result(column="obox_id"   ,property="oboxId"),
            @Result(column="obox_name" ,  property="oboxName"),
            @Result(column="obox_pwd" ,  property="oboxPwd"),
            @Result(column="obox_version" ,  property="oboxVersion"),
            @Result(column="last_op_time" ,  property="lastOpTime"),
            @Result(column="obox_status" ,  property="oboxStatus"),
            @Result(column="license"  , property="license"),
            @Result(column="obox_ip" ,  property="oboxIp" ),
            @Result(column="obox_addr"  , property="oboxAddr" ),
            @Result(column="obox_person"  , property="oboxPerson"),
            @Result(column="obox_activate" ,  property="oboxActivate"),
            @Result(column="obox_control" ,  property="oboxControl"),
            @Result(column="obox_serial_id" ,  property="oboxSerialId")
    })
	@Select(" select * from t_obox todc " +
			" inner join t_user_obox tud on todc.obox_serial_id = tud.obox_serial_id" +
			" inner join t_user tu on tu.id = tud.user_id" +
			" where tud.user_id = #{userId}")
	List<TObox> getOboxByUserId(@Param("userId") Integer userId);

	/**  
	 * @param obox
	 * @return  
	 * @Description:  
	 */
	 @Insert("insert into t_obox(obox_name,\n" +
				"obox_serial_id,\n" +
				"obox_version,\n" +
				"obox_status,\n" +
				"obox_ip) values(#{oboxName},#{oboxSerialId},#{oboxVersion},#{oboxStatus},#{oboxIp})")
	@Options(useGeneratedKeys=true, keyProperty="oboxId", keyColumn="id")
	int addObox(TObox obox);

}