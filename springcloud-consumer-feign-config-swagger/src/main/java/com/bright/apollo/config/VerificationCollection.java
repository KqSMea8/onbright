package com.bright.apollo.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VerificationCollection {

	public static boolean useSet(String[] arr,String targetValue){
	    Set<String> set=new HashSet<String>(Arrays.asList(arr));
	    return set.contains(targetValue);
	}
}