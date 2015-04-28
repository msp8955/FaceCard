package edu.gatech.cc.cs7470.facecard;

/**
 * Created by miseonpark on 2/24/15.
 */
public class Constants {

    public static final String PACKAGE_NAME = "edu.gatech.cc.cs7470.facecard";

    //url
    public static final String REGISTER_ACCOUNT_URL = "http://ec2-54-68-110-119.us-west-2.compute.amazonaws.com/facecard/setAccount.php";
    public static final String DISCOVER_ACCOUNT_URL = "http://ec2-54-68-110-119.us-west-2.compute.amazonaws.com/facecard/getInfo.php";

    //profile layout settings
    public static final int PROFILE_PIC_SIZE = 100;
    public static final int PROFILE_PIC_RADIUS = 50;

    //shared preferences labels
    public static final String SHARED_PREFERENCES_ALARM = "alarm"; //Background service
    public static final String SHARED_PREFERENCES_ACCOUNT = "account"; //Google+ account
    public static final String SHARED_PREFERENCES_BLUETOOTH = "bluetooth"; //Bluetooth
    public static final String SHARED_PREFERENCES_GLASS = "glass"; //Glass
    public static final String SHARED_PREFERENCES_HAS_GLASS = "has_glass"; //device has glass connected

    //background bluetooth discovery
    public static final int DISCOVERY_INTERVAL = 1000 * 30; //thirty seconds
    public static final int STATE_CONNECTION_STARTED = 0;
    public static final int STATE_CONNECTION_LOST = 1;
    public static final int READY_TO_CONN = 2;
    public static final int SUCCESS_CONNECT = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
}
