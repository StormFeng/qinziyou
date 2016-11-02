package com.midian.qzy.utils;

import com.midian.qzy.api.QzyApiClient;
import com.midian.qzy.app.MAppContext;

import midian.baselib.app.AppContext;


/**
 * app工具
 * 
 * @author MIDIAN
 * 
 */
public class AppUtil {
	public static MAppContext getMAppContext(AppContext ac) {
		return (MAppContext) ac;
	}
	
	public static QzyApiClient getPpApiClient(AppContext ac) {
		return ac.api.getApiClient(QzyApiClient.class);
	}
}
