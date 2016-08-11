package com.jy.xinlangweibo.constant;

public interface URL_Constant {
//	https://api.weibo.com/2/statuses/home_timeline.json
	String BASE_URL = "https://api.weibo.com/2/";
	String STATUES_HOMETIMELINE_URL = BASE_URL+"statuses/home_timeline.json";
	String SHOWCOMMENTS_URL = BASE_URL+"comments/show.json";
	String UPDATE_URL = BASE_URL+"statuses/update.json";
	String ip_to_geo_URL = BASE_URL+"location/geo/ip_to_geo.json";
	String place_nearby_timeline_URL= BASE_URL+"place/nearby_timeline.json";
	
}
