package com.bright.apollo.transition;

import com.bright.apollo.common.entity.TOboxDeviceConfig;

import java.util.Map;

public interface ThirdPartyTransition {

    TMallDeviceAdapter onbright2TMall();

    TOboxDeviceConfig TMall2Obright();
}
