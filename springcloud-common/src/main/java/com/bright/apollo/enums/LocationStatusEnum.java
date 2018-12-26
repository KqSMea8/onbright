package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月20日  
 *@Version:1.1.0  
 */
public enum LocationStatusEnum {

    TREE(0),
    CHECK(1),
    CLEAN(2),
    REPAIR(3);

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    private LocationStatusEnum(Integer status) {
        this.status = status;
    }

}
