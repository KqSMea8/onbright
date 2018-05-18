package com.bright.apollo.bean;

import com.bright.apollo.common.entity.TScene;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SceneDTO extends TScene {
    public SceneDTO(TScene scene) {
        setOboxSerialId(scene.getOboxSerialId());
        setSceneNumber(scene.getSceneNumber());
        setOboxSceneNumber(scene.getOboxSceneNumber());
        setMsgAlter(scene.getMsgAlter());
        setSceneGroup(scene.getSceneGroup());
        setSceneName(scene.getSceneName());
        setSceneType(scene.getSceneType());
        setSceneStatus(scene.getSceneStatus());
    }


    /**
     * scene config
     */
    @Expose
    @SerializedName("actions")
    private List<SceneActionDTO> actions;

    @Expose
    @SerializedName("conditions")
    private List<List<SceneConditionDTO>> conditions;

    public List<SceneActionDTO> getActions() {
        return actions;
    }

    public void setActions(List<SceneActionDTO> actions) {
        this.actions = actions;
    }

    public List<List<SceneConditionDTO>> getConditions() {
        return conditions;
    }

    public void setConditions(List<List<SceneConditionDTO>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "SceneDTO [actions=" + actions + ", conditions=" + conditions
                + "]";
    }
}
