package com.bright.apollo.dao.obox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

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
	List<TObox> queryOboxByUserId(Integer userId, int pageStart, int pageEnd);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	int queryCountOboxByUserId(Integer userId);

	@Select("select * from t_obox where obox_serial_id = #{oboxSerialId} ")
	TObox queryOboxsByOboxSerialId(String oboxSerialId) throws Exception;

	@Update("update t_obox set obox_name = #{oboxName},\n" +
			"obox_serial_id = #{oboxSerialId },\n" +
			"obox_version = {obox_version },\n" +
			"last_op_time = #{lastOpTime},\n" +
			"obox_status = #{oboxStatus},\n" +
			"obox_ip = #{oboxIp}  where  obox_serial_id = #{oboxSerialId} ")
	void updateObox(TObox obox);

	@Select("select tb.* from t_device_channel tdc\n" +
			"inner join t_obox tb on tdc.obox_id=tb.obox_id\n" +
			"where tdc.device_id=#{deviceId}\n" +
			"order by signal_intensity desc\n" +
			"limit 1,1 ")
	TObox queryOboxsByDeviceChannelId(int deviceId) throws Exception;

	@Delete("delete from t_obox where id = #{id} ")
	void deleteOboxById(TObox obox);

	@Select("select * from t_obox where id = #{id}")
	TObox queryOboxsById(int id);

	@Select("select * from t_obox tob\n" +
			"inner join t_obox_device_config todc on tob.id=todc.obox_id\n" +
			"inner join t_device_channel tdc on tdc.device_id=todc.id\n" +
			"where todc.id =#{oboxDeviceId} and tob.id = #{oboxId} ")
	List<TObox> getOboxsByDeviceChannel(int oboxDeviceId,int oboxId);

}