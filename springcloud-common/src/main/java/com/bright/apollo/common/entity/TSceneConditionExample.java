package com.bright.apollo.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TSceneConditionExample implements Serializable{
    /**  
	 *   
	 */
	private static final long serialVersionUID = 4282311822931191911L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public TSceneConditionExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSceneNumberIsNull() {
            addCriterion("scene_number is null");
            return (Criteria) this;
        }

        public Criteria andSceneNumberIsNotNull() {
            addCriterion("scene_number is not null");
            return (Criteria) this;
        }

        public Criteria andSceneNumberEqualTo(Integer value) {
            addCriterion("scene_number =", value, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberNotEqualTo(Integer value) {
            addCriterion("scene_number <>", value, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberGreaterThan(Integer value) {
            addCriterion("scene_number >", value, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("scene_number >=", value, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberLessThan(Integer value) {
            addCriterion("scene_number <", value, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberLessThanOrEqualTo(Integer value) {
            addCriterion("scene_number <=", value, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberIn(List<Integer> values) {
            addCriterion("scene_number in", values, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberNotIn(List<Integer> values) {
            addCriterion("scene_number not in", values, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberBetween(Integer value1, Integer value2) {
            addCriterion("scene_number between", value1, value2, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSceneNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("scene_number not between", value1, value2, "sceneNumber");
            return (Criteria) this;
        }

        public Criteria andSerialidIsNull() {
            addCriterion("serialId is null");
            return (Criteria) this;
        }

        public Criteria andSerialidIsNotNull() {
            addCriterion("serialId is not null");
            return (Criteria) this;
        }

        public Criteria andSerialidEqualTo(String value) {
            addCriterion("serialId =", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidNotEqualTo(String value) {
            addCriterion("serialId <>", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidGreaterThan(String value) {
            addCriterion("serialId >", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidGreaterThanOrEqualTo(String value) {
            addCriterion("serialId >=", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidLessThan(String value) {
            addCriterion("serialId <", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidLessThanOrEqualTo(String value) {
            addCriterion("serialId <=", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidLike(String value) {
            addCriterion("serialId like", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidNotLike(String value) {
            addCriterion("serialId not like", value, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidIn(List<String> values) {
            addCriterion("serialId in", values, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidNotIn(List<String> values) {
            addCriterion("serialId not in", values, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidBetween(String value1, String value2) {
            addCriterion("serialId between", value1, value2, "serialid");
            return (Criteria) this;
        }

        public Criteria andSerialidNotBetween(String value1, String value2) {
            addCriterion("serialId not between", value1, value2, "serialid");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeIsNull() {
            addCriterion("last_op_time is null");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeIsNotNull() {
            addCriterion("last_op_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeEqualTo(Date value) {
            addCriterion("last_op_time =", value, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeNotEqualTo(Date value) {
            addCriterion("last_op_time <>", value, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeGreaterThan(Date value) {
            addCriterion("last_op_time >", value, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_op_time >=", value, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeLessThan(Date value) {
            addCriterion("last_op_time <", value, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_op_time <=", value, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeIn(List<Date> values) {
            addCriterion("last_op_time in", values, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeNotIn(List<Date> values) {
            addCriterion("last_op_time not in", values, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeBetween(Date value1, Date value2) {
            addCriterion("last_op_time between", value1, value2, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andLastOpTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_op_time not between", value1, value2, "lastOpTime");
            return (Criteria) this;
        }

        public Criteria andCondIsNull() {
            addCriterion("cond is null");
            return (Criteria) this;
        }

        public Criteria andCondIsNotNull() {
            addCriterion("cond is not null");
            return (Criteria) this;
        }

        public Criteria andCondEqualTo(String value) {
            addCriterion("cond =", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondNotEqualTo(String value) {
            addCriterion("cond <>", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondGreaterThan(String value) {
            addCriterion("cond >", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondGreaterThanOrEqualTo(String value) {
            addCriterion("cond >=", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondLessThan(String value) {
            addCriterion("cond <", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondLessThanOrEqualTo(String value) {
            addCriterion("cond <=", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondLike(String value) {
            addCriterion("cond like", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondNotLike(String value) {
            addCriterion("cond not like", value, "cond");
            return (Criteria) this;
        }

        public Criteria andCondIn(List<String> values) {
            addCriterion("cond in", values, "cond");
            return (Criteria) this;
        }

        public Criteria andCondNotIn(List<String> values) {
            addCriterion("cond not in", values, "cond");
            return (Criteria) this;
        }

        public Criteria andCondBetween(String value1, String value2) {
            addCriterion("cond between", value1, value2, "cond");
            return (Criteria) this;
        }

        public Criteria andCondNotBetween(String value1, String value2) {
            addCriterion("cond not between", value1, value2, "cond");
            return (Criteria) this;
        }

        public Criteria andConditionGroupIsNull() {
            addCriterion("condition_group is null");
            return (Criteria) this;
        }

        public Criteria andConditionGroupIsNotNull() {
            addCriterion("condition_group is not null");
            return (Criteria) this;
        }

        public Criteria andConditionGroupEqualTo(Integer value) {
            addCriterion("condition_group =", value, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupNotEqualTo(Integer value) {
            addCriterion("condition_group <>", value, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupGreaterThan(Integer value) {
            addCriterion("condition_group >", value, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupGreaterThanOrEqualTo(Integer value) {
            addCriterion("condition_group >=", value, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupLessThan(Integer value) {
            addCriterion("condition_group <", value, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupLessThanOrEqualTo(Integer value) {
            addCriterion("condition_group <=", value, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupIn(List<Integer> values) {
            addCriterion("condition_group in", values, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupNotIn(List<Integer> values) {
            addCriterion("condition_group not in", values, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupBetween(Integer value1, Integer value2) {
            addCriterion("condition_group between", value1, value2, "conditionGroup");
            return (Criteria) this;
        }

        public Criteria andConditionGroupNotBetween(Integer value1, Integer value2) {
            addCriterion("condition_group not between", value1, value2, "conditionGroup");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_scene_condition
     *
     * @mbg.generated do_not_delete_during_merge Mon Mar 19 16:11:25 CST 2018
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_scene_condition
     *
     * @mbg.generated Mon Mar 19 16:11:25 CST 2018
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}