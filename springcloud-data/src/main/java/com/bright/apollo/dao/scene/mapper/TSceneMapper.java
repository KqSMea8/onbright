package com.bright.apollo.dao.scene.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TSceneMapper extends BaseMapper<TScene, TSceneExample, Integer> {

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
	List<TScene> getSceneByOboxSerialId(@Param("oboxSerialId") String oboxSerialId);

	@Delete("delete from t_scene where scene_number = #{sceneNumber}")
	void deleteSceneBySceneNum(@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene where obox_serial_id = #{oboxSerialId}")
	void deleteSceneByOboxSerialId(@Param("oboxSerialId") String oboxSerialId);

	@Select("select * from t_scene where obox_serial_id = #{oboxSerialId} and scene_number = #{sceneNumber} ")
	TScene getTSceneByOboxSerialIdAndSceneNumber(@Param("oboxSerialId") String oboxSerialId,@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene where obox_serial_id = #{oboxSerialId} and obox_scene_number = #{oboxSceneNumber}")
	void deleteSceneByOboxSerialIdAndSceneNum(@Param("oboxSerialId") String oboxSerialId,@Param("oboxSceneNumber") int oboxSceneNumber);

	@Insert("insert into t_scene (scene_name,\n" +
			"obox_serial_id,\n" +
			"obox_scene_number,\n" +
			"scene_status,\n" +
			"scene_type,\n" +
			"msg_alter,\n" +
			"last_op_time,\n" +
			"scene_run,\n" +
			"license,\n" +
			"alter_need,\n" +
			"scene_group) values(#{sceneName},#{oboxSerialId}," +
			"#{oboxSceneNumber},#{sceneStatus},#{sceneType}," +
			"#{msgAlter},#{lastOpTime},#{sceneRun},#{license}," +
			"#{alterNeed},#{sceneGroup})")
	@Options(useGeneratedKeys=true, keyProperty="scene_number", keyColumn="scene_number")
	int addScene(TScene scene);

	@Update("update t_scene set scene_name = #{sceneName},\n" +
			"obox_serial_id = #{oboxSerialId},\n" +
			"obox_scene_number = #{oboxSceneNumber},\n" +
			"scene_status = #{sceneStatus},\n" +
			"scene_type = #{sceneType},\n" +
			"msg_alter = #{msgAlter},\n" +
			"last_op_time = #{lastOpTime},\n" +
			"scene_run = #{sceneRun},\n" +
			"license = #{license},\n" +
			"alter_need = #{alterNeed},\n" +
			"scene_group = #{sceneGroup}" +
			"where scene_number = #{sceneNumber}")
	@Options(useGeneratedKeys=true, keyProperty="scene_number", keyColumn="scene_number")
	int updateScene(TScene scene);

	@Select("select * from t_scene where obox_scene_number = #{sceneNumber}")
	TScene getSceneBySceneNumber(@Param("sceneNumber") int sceneNumber);

	@Select("select * from t_scene")
	List<TScene> getALlScene();
}