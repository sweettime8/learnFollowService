package com.elcom.follow.constant;

/**
 *
 * @author anhdv
 */
public class Constant {
    
    public static final String SRV_VER = "/v1.0";
    
    /** Validation message */
    public static final String VALIDATION_INVALID_PARAM_VALUE = "Invalid param value";
    public static final String VALIDATION_DATA_NOT_FOUND = "Data not found";
    
    /** Redis keys */
    public static final String FOLLOW_CACHE_LST_BY_PARAMS = "co_learn_follow_lst";
    public static final String FOLLOW_CACHE_GET_STATUS = "co_learn_follow_status";
    
    /** = 1 là đang theo dõi */
    public static final int FOLLOW_STATUS_ACTIVE = 1;
    
    /** = 0 là ngừng theo dõi */
    public static final int FOLLOW_STATUS_INACTIVE = 0;
    
    /** Scheduler tạo partiton tháng tới chạy vào 23h45' ngày cuối tháng */
    public static final String  ADD_PARTITION_NEXT_MONTH = "0 45 23 28-31 * ?";
    //public static final String ADD_PARTITION_NEXT_MONTH = "0 45 23 L * ?";
    
    /** Type safe */
    /*public static final String  CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;
    private static final Config CONFIG;
    
    static {
        Config baseConfig = ConfigFactory.load("constant");
        CONFIG = ConfigFactory.parseFile(new File(CONFIG_DIR + "constant.conf")).withFallback(baseConfig);
        loadConfig();
    }
    
    public static void loadConfig() {
        //SCAN_TIME_FOLLOW_LST_DATA = CONFIG.getString("SCAN_TIME_FOLLOW_LST_DATA");
    }*/
}
