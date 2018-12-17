package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TUserAliDev;
import com.bright.apollo.dao.device.mapper.AliDeviceMapper;
import com.bright.apollo.service.AliDeviceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliDeviceServiceImpl implements AliDeviceService {

    @Autowired
    private AliDeviceMapper mapper;


    @Override
    public TAliDevice getAliDeviceBySerializeId(String oboxSerialId) {
        return mapper.getAliDeviceBySerializeId(oboxSerialId);
    }

    @Override
    public TAliDeviceUS getAliUSDeviceBySerializeId(String oboxSerialId) {
        return mapper.getAliUSDeviceBySerializeId(oboxSerialId);
    }

    @Override
    public TAliDevTimer getAliDevTimerByTimerId(int id) {
        return mapper.getAliDevTimerByTimerId(id);
    }

    @Override
    public void deleteAliDevTimerBySerializeIdAndType(String deviceSerialId, int isCountdown) {
        mapper.deleteAliDevTimerBySerializeIdAndType(deviceSerialId,isCountdown);
    }

    @Override
    public TAliDevice getAliDeviceByProductKeyAndDeviceName(String productKey, String deviceName) {
        return mapper.getAliDeviceByProductKeyAndDeviceName(productKey,deviceName);
    }

    @Override
    public TAliDeviceUS getAliUSDeviceByProductKeyAndDeviceName(String productKey, String deviceName) {
        return mapper.getAliUSDeviceByProductKeyAndDeviceName(productKey,deviceName);
    }

    @Override
    public void updateAliDevice(TAliDevice aliDevice) {
        mapper.updateAliDevice(aliDevice);
    }

    @Override
    public void updateAliUSDevice(TAliDeviceUS aliDevice) {
        mapper.updateAliUSDevice(aliDevice);
    }


	@Override
	public List<TAliDeviceUS>  getAliUSDeviceByProductKeyAndDeviceSerialId(String productKey, String deviceSerialId) {
		 
		return mapper.getAliUSDeviceByProductKeyAndDeviceSerialId(productKey, deviceSerialId);
	}


	@Override
	public int addAliDevUS(TAliDeviceUS tAliDeviceUS) {
		  
		return mapper.addAliDevUS(tAliDeviceUS);
	}

	@Override
	public int addAliDev(TAliDevice tAliDevice) {
		if(tAliDevice==null){
			return 0;
		}
		tAliDevice.setProductKey(tAliDevice.getProductKey()==null?"":tAliDevice.getProductKey());
		tAliDevice.setDeviceName(tAliDevice.getDeviceName()==null?"":tAliDevice.getDeviceName());
		tAliDevice.setOboxSerialId(tAliDevice.getOboxSerialId()==null?"available":tAliDevice.getOboxSerialId());
		tAliDevice.setOffline(tAliDevice.getOffline()==null?0:tAliDevice.getOffline());
		return mapper.addAliDev(tAliDevice);
	}

	@Override
	public List<TAliDevice> getAliDeviceByProductKey(String productKey) {
		return mapper.getAliDeviceByProductKey(productKey);
	}

	@Override
	public List<TAliDevTimer> getAliDevTimerByDeviceSerialId(String oboxSerialId) {
		return mapper.getAliDevTimerByDeviceSerialId(oboxSerialId);
	}

	@Override
	public TAliDevTimer getAliDevTimerByDeviceSerialIdAndCountDown(String oboxSerialId) {
		return mapper.getAliDevTimerByDeviceSerialIdAndCountDown(oboxSerialId);
	}

	@Override
	public TAliDevTimer getAliDevTimerByIdAndDeviceId(String oboxSerialId, Integer id) {
		return mapper.getAliDevTimerByIdAndDeviceId(oboxSerialId,id);
	}

	@Override
	public TAliDevTimer getAliDevTimerById(int id) {
		return mapper.getAliDevTimerByDId(id);
	}

	@Override
	public void deleteAliDevTimerById(Integer id) {
		mapper.deleteAliDevTimerById(id);
	}

	@Override
	public int addAliDevTimer(TAliDevTimer aliDevTimer) {
		return mapper.addAliDevTimer(aliDevTimer);
	}

	@Override
	public void updateAliDevTimer(TAliDevTimer aliDevTimer) {
		mapper.updateAliDevTimer(aliDevTimer);
	}

	@Override
	public void deleteAliDeviceUser(String oboxSerialId) {
		mapper.deleteAliDeviceUser(oboxSerialId);
	}

	@Override
	public int addAliDevUser(TUserAliDev userAliDev) {
		return mapper.addAliDevUser(userAliDev);
	}


	@Override
	public List<TAliDevice> getAliDeviceByProductKeyAndDeviceSerialId(String productKey, String oboxSerialId) {
		return mapper.getAliDeviceByProductKeyAndDeviceSerialId(productKey,oboxSerialId);
	}
}
