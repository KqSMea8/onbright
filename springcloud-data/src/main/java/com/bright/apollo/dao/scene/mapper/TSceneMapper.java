package com.bright.apollo.dao.scene.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TScene;

@Mapper
@Component
public interface TSceneMapper {

	/**
	 * @param userId
	 * @param pageStart
	 * @param pageEnd
	 * @return
	 * @Description:
	 */
	List<TScene> querySceneByUserId(Integer userId, int pageStart, int pageEnd);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	int queryCountSceneByUserId(Integer userId);

	@Select("select * from t_scene where obox_serial_id=#{oboxSerialId}")
	@Results(value = { @Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "obox_serial_id", property = "oboxSerialId"),
			@Result(column = "obox_scene_number", property = "oboxSceneNumber"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "scene_status", property = "sceneStatus"),
			@Result(column = "scene_type", property = "sceneType"),
			@Result(column = "msg_alter", property = "msgAlter"), @Result(column = "scene_run", property = "sceneRun"),
			@Result(column = "license", property = "license"), @Result(column = "alter_need", property = "alterNeed"),
			@Result(column = "scene_group", property = "sceneGroup") })
	List<TScene> getSceneByOboxSerialId(@Param("oboxSerialId") String oboxSerialId);

	@Delete("delete from t_scene where scene_number = #{sceneNumber}")
	void deleteSceneBySceneNum(@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene where obox_serial_id = #{oboxSerialId}")
	void deleteSceneByOboxSerialId(@Param("oboxSerialId") String oboxSerialId);

	@Select("select * from t_scene where obox_serial_id = #{oboxSerialId} and scene_number = #{sceneNumber} ")
	TScene getTSceneByOboxSerialIdAndSceneNumber(@Param("oboxSerialId") String oboxSerialId,
			@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene where obox_serial_id = #{oboxSerialId} and obox_scene_number = #{oboxSceneNumber}")
	void deleteSceneByOboxSerialIdAndSceneNum(@Param("oboxSerialId") String oboxSerialId,
			@Param("oboxSceneNumber") int oboxSceneNumber);

	
	@Insert("insert into t_scene (scene_name,\n" + "obox_serial_id,\n" + "obox_scene_number,\n" + "scene_status,\n"
			+ "scene_type,\n" + "msg_alter,\n" + "last_op_time,\n" + "scene_run,\n" + "license,\n" + "alter_need,\n"
			+ "scene_group) values(#{sceneName},#{oboxSerialId}," + "#{oboxSceneNumber},#{sceneStatus},#{sceneType},"
			+ "#{msgAlter},#{lastOpTime},#{sceneRun},#{license}," + "#{alterNeed},#{sceneGroup})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "sceneNumber", before = false, resultType = int.class)
	int addScene(TScene scene);

	@Update("update t_scene set scene_name = #{sceneName},\n" + "obox_serial_id = #{oboxSerialId},\n"
			+ "obox_scene_number = #{oboxSceneNumber},\n" + "scene_status = #{sceneStatus},\n"
			+ "scene_type = #{sceneType},\n" + "msg_alter = #{msgAlter},\n" + "last_op_time = #{lastOpTime},\n"
			+ "scene_run = #{sceneRun},\n" + "license = #{license},\n" + "alter_need = #{alterNeed},\n"
			+ "scene_group = #{sceneGroup}" + " where scene_number = #{sceneNumber}")
	@Options(useGeneratedKeys = true, keyProperty = "scene_number", keyColumn = "scene_number")
	int updateScene(TScene scene);

	@Select("select * from t_scene where scene_number = #{sceneNumber}")
	@Results(value = { @Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "obox_serial_id", property = "oboxSerialId"),
			@Result(column = "obox_scene_number", property = "oboxSceneNumber"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "scene_status", property = "sceneStatus"),
			@Result(column = "scene_type", property = "sceneType"),
			@Result(column = "msg_alter", property = "msgAlter"), @Result(column = "scene_run", property = "sceneRun"),
			@Result(column = "license", property = "license"), @Result(column = "alter_need", property = "alterNeed"),
			@Result(column = "scene_group", property = "sceneGroup") })
	TScene getSceneBySceneNumber(@Param("sceneNumber") int sceneNumber);

	@Select("select * from t_scene")
	List<TScene> getALlScene();

	/**
	 * @param oboxSerialId
	 * @param oboxSceneNumber
	 * @return
	 * @Description:
	 */
	@Select("select * from t_scene where obox_serial_id = #{oboxSerialId} and obox_scene_number=#{oboxSceneNumber}")
	@Results(value = { @Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "obox_serial_id", property = "oboxSerialId"),
			@Result(column = "obox_scene_number", property = "oboxSceneNumber"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "scene_status", property = "sceneStatus"),
			@Result(column = "scene_type", property = "sceneType"),
			@Result(column = "msg_alter", property = "msgAlter"), @Result(column = "scene_run", property = "sceneRun"),
			@Result(column = "license", property = "license"), @Result(column = "alter_need", property = "alterNeed"),
			@Result(column = "scene_group", property = "sceneGroup") })
	TScene getTSceneByOboxSerialIdAndOboxSceneNumber(@Param("oboxSerialId") String oboxSerialId,
			@Param("oboxSceneNumber") Integer oboxSceneNumber);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	@Select("SELECT a.* FROM t_scene a WHERE  EXISTS (SELECT 1 "
			+ "From t_user_scene b WHERE a.scene_number=b.scene_number and b.user_id=#{userId})"
			+ " order by a.scene_number desc")
	@Results(value = { @Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "obox_serial_id", property = "oboxSerialId"),
			@Result(column = "obox_scene_number", property = "oboxSceneNumber"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "scene_status", property = "sceneStatus"),
			@Result(column = "scene_type", property = "sceneType"),
			@Result(column = "msg_alter", property = "msgAlter"), @Result(column = "scene_run", property = "sceneRun"),
			@Result(column = "license", property = "license"), @Result(column = "alter_need", property = "alterNeed"),
			@Result(column = "scene_group", property = "sceneGroup") })
	List<TScene> getSceneByUserId(@Param("userId") Integer userId);

	/**
	 * @param userId
	 * @param start
	 * @param count
	 * @return
	 * @Description:
	 */
	@Select("SELECT a.* FROM t_scene a WHERE  EXISTS (SELECT 1 "
			+ "From t_user_scene b WHERE a.scene_number=b.scene_number and b.user_id=#{userId})"
			+ " order by a.scene_number desc limit #{start},#{count}")
	@Results(value = { @Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "obox_serial_id", property = "oboxSerialId"),
			@Result(column = "obox_scene_number", property = "oboxSceneNumber"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "scene_status", property = "sceneStatus"),
			@Result(column = "scene_type", property = "sceneType"),
			@Result(column = "msg_alter", property = "msgAlter"), @Result(column = "scene_run", property = "sceneRun"),
			@Result(column = "license", property = "license"), @Result(column = "alter_need", property = "alterNeed"),
			@Result(column = "scene_group", property = "sceneGroup") })
	List<TScene> getSceneByUserIdAndPage(@Param("userId") Integer userId, @Param("start") int start,
			@Param("count") int count);

	/**  
	 * @param location
	 * @return  
	 * @Description:  
	 */
	@Select("select a.* "
			+ "from t_scene a where  EXISTS(select 1 from t_location_scene "
			+ "b where a.scene_number=b.scene_number and b.location_id=#{location})")
	@Results(value = { @Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "obox_serial_id", property = "oboxSerialId"),
			@Result(column = "obox_scene_number", property = "oboxSceneNumber"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "scene_status", property = "sceneStatus"),
			@Result(column = "scene_type", property = "sceneType"),
			@Result(column = "msg_alter", property = "msgAlter"), @Result(column = "scene_run", property = "sceneRun"),
			@Result(column = "license", property = "license"), @Result(column = "alter_need", property = "alterNeed"),
			@Result(column = "scene_group", property = "sceneGroup") })
	List<TScene> querySceneByLocation(@Param("location")Integer location);

	/**  
	 * @param sceneNumber
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Select("select a.*from t_scene a where  EXISTS (select 1 from "
			+ "t_user_scene b where a.scene_number=b.scene_number and b.user_id=#{userId} and b.scene_number=#{sceneNumber})")
	@Results(value = { @Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "obox_serial_id", property = "oboxSerialId"),
			@Result(column = "obox_scene_number", property = "oboxSceneNumber"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "scene_status", property = "sceneStatus"),
			@Result(column = "scene_type", property = "sceneType"),
			@Result(column = "msg_alter", property = "msgAlter"), @Result(column = "scene_run", property = "sceneRun"),
			@Result(column = "license", property = "license"), @Result(column = "alter_need", property = "alterNeed"),
			@Result(column = "scene_group", property = "sceneGroup") })
	TScene querySceneBySceneNumberAndUserId(@Param("sceneNumber")Integer sceneNumber, @Param("userId")Integer userId);
}