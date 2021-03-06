package com.bright.apollo.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.dao.device.mapper.TOboxDeviceConfigMapper;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.service.OboxDeviceConfigService;

@Service
public class OboxDeviceConfigServiceImpl  implements OboxDeviceConfigService {
	private static final Logger logger = LoggerFactory.getLogger(OboxDeviceConfigServiceImpl.class);
    public OboxDeviceConfigServiceImpl(){
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
    	logger.info("===deleteTOboxDeviceConfig id:"+id);
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
    	logger.info("===deleteTOboxDeviceConfigByOboxId oboxId:"+oboxId);
        odcMapper.deleteTOboxDeviceConfigByOboxId(oboxId);
    }

    @Override
    public void deleteTOboxDeviceConfigByOboxIdAndNodeAddress(int oboxId, String nodeAddress) {
    	logger.info("===deleteTOboxDeviceConfigByOboxIdAndNodeAddress oboxId:"+oboxId+"===nodeAddress:"+nodeAddress);
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

    @Override
    public List<TOboxDeviceConfig> getOboxDeviceConfigByUserId(Integer userId) {
        return odcMapper.getOboxDeviceConfigByUserId(userId);
    }

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxDeviceConfigService#getOboxDeviceConfigByOboxSerialId(java.lang.String)  
	 */
	@Override
	public List<TOboxDeviceConfig> getOboxDeviceConfigByOboxSerialId(String oboxSerialId) {
		  
		return odcMapper.getOboxDeviceConfigByOboxSerialId(oboxSerialId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxDeviceConfigService#getDeviceTypeByUser(java.lang.Integer)  
	 */
	@Override
	public List<TOboxDeviceConfig> getDeviceTypeByUser(Integer userId) {
		  
		return odcMapper.getDeviceTypeByUser(userId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxDeviceConfigService#getDevciesByUserIdAndType(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public List<TOboxDeviceConfig> getDevciesByUserIdAndType(Integer userId, String deviceType) {
		  
		return odcMapper.getDevciesByUserIdAndType(userId,deviceType);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxDeviceConfigService#getDeviceByUserAndSerialId(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public TOboxDeviceConfig getDeviceByUserAndSerialId(Integer userId, String serialID) {
		 
		return odcMapper.getDeviceByUserAndSerialId(userId,serialID);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxDeviceConfigService#deleteTOboxDeviceConfigByOboxSerialId(java.lang.String)  
	 */
	@Override
	public void deleteTOboxDeviceConfigByOboxSerialId(String serialId) {
		logger.info("===deleteTOboxDeviceConfigByOboxSerialId serialId:"+serialId);
		odcMapper.deleteTOboxDeviceConfigByOboxSerialId(serialId);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxDeviceConfigService#deleteTOboxDeviceConfigById(java.lang.Integer)  
	 */
	@Override
	public void deleteTOboxDeviceConfigById(Integer id) {
		logger.info("===deleteTOboxDeviceConfigById id:"+id);
		odcMapper.deleteTOboxDeviceConfigById(id);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxDeviceConfigService#queryDeviceByGroupId(java.lang.Integer)  
	 */
	@Override
	public List<TOboxDeviceConfig> queryDeviceByGroupId(Integer groupId) {
 		return odcMapper.queryDeviceByGroupId(groupId);
	}


}
