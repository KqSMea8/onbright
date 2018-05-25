package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.dao.device.mapper.TOboxDeviceConfigMapper;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.service.OboxDeviceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OboxDeviceConfigServiceImpl  implements OboxDeviceConfigService {

    public OboxDeviceConfigServiceImpl(){
        System.out.println("=============OboxDeviceConfigServiceImpl=============");
    }
    @Autowired
    private TOboxDeviceConfigMapper odcMapper;

    @Override
    public TOboxDeviceConfig queryOboxConfigByRFAddr(int oboxId, String address) {
        return odcMapper.queryOboxConfigByAddr(oboxId,address);
    }

    @Override
    public void updateTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig) {
        odcMapper.updateTOboxDeviceConfig(tOboxDeviceConfig);
    }

    @Override
    public TOboxDeviceConfig queryDeviceConfigBySerialID(String deviceSerialId) {
        return odcMapper.queryDeviceConfigBySerialID(deviceSerialId);
    }

    @Override
    public void deleteTOboxDeviceConfig(int id) {
        odcMapper.deleteTOboxDeviceConfig(id);
    }

    @Override
    public int addTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig) {
        return odcMapper.addTOboxDeviceConfig(tOboxDeviceConfig);
    }

    @Override
    public List<TOboxDeviceConfig> getOboxDeviceConfigByOboxId(int oboxId) {
        return odcMapper.getOboxDeviceConfigByOboxId(oboxId);
    }

    @Override
    public void deleteTOboxDeviceConfigByOboxId(int oboxId) {
        odcMapper.deleteTOboxDeviceConfigByOboxId(oboxId);
    }

    @Override
    public void deleteTOboxDeviceConfigByOboxIdAndNodeAddress(int oboxId, String nodeAddress) {
        odcMapper.deleteTOboxDeviceConfigByOboxIdAndNodeAddress(oboxId,nodeAddress);
    }

    @Override
    public List<TOboxDeviceConfig> getTOboxDeviceConfigByOboxSerialIdAndGroupAddress(String OboxSerialId, String groupAddress) {
        return odcMapper.getTOboxDeviceConfigByOboxSerialIdAndGroupAddress(OboxSerialId,groupAddress);
    }

    @Override
    public void updateTOboxDeviceConfigStatus(TOboxDeviceConfig tOboxDeviceConfig, String status) {
        if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.sensor.getValue())
                && tOboxDeviceConfig.getDeviceChildType().equals(
                DeviceTypeEnum.sensor_environment.getValue())) {
            if(tOboxDeviceConfig.getDeviceState().length()>12){
                tOboxDeviceConfig.setDeviceState(status.substring(0, status.length()-2)
                        +tOboxDeviceConfig.getDeviceState().substring(12)  );
            }else{
                tOboxDeviceConfig.setDeviceState(status.substring(0, status.length()-2));
            }
            tOboxDeviceConfig.setLastOpTime(new Date());

        }else{
            if (!tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.sensor.getValue())) {
                tOboxDeviceConfig.setDeviceState(status);
            }
        }
        odcMapper.updateTOboxDeviceConfigStatus(tOboxDeviceConfig);
    }

    @Override
    public TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialId(String deviceSerialId) {
        return odcMapper.getTOboxDeviceConfigByDeviceSerialId(deviceSerialId);
    }

    @Override
    public TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialIdAndAddress(String deviceSerialId, String address) {
        return odcMapper.getTOboxDeviceConfigByDeviceSerialIdAndAddress(deviceSerialId,address);
    }

    @Override
    public List<TOboxDeviceConfig> getAllOboxDeviceConfig(String deviceType) {
        return odcMapper.getAllOboxDeviceConfig(deviceType);
    }

    @Override
    public TOboxDeviceConfig getOboxDeviceConfigById(int id) {
        return odcMapper.getOboxDeviceConfigById(id);
    }


}
