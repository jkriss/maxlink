
package maxlink;
public class LinkConstants {
	// used to have same settings as net.maxhole for easy testing
	// public static final String MAXGROUP = "224.74.74.74";
	
	public static final String OBJECTNAME = "jk.link";
	public static final String MAXGROUP = "224.74.74.75";
	public static final int MAXPORT = 7474;
	public static final byte TIMETOLIVE = (byte)1;
	public static final byte LOCAL_TIMETOLIVE = (byte)0;
	public int port = MAXPORT;
	public String ip = MAXGROUP;
	
	public static final String MAX_SUFFIX = "-max";
	public static final String OBJECT_SUFFIX = "-obj";
	public static final String NEW_INLET_MESSAGE = "new-inlet";
	public static final String NEW_OUTLET_MESSAGE = "new-outlet";
	public static final String WHO_QUERY_MESSAGE = "who";
	public static final String IO_QUERY_MESSAGE = "io-query";
	//public static final String INLET_INFO_MESSAGE = "inlet-info";
	//public static final String OUTLET_INFO_MESSAGE = "outlet-info";
	public static final String ANNOUNCE_APPLET_MESSAGE = "applet-announce";
	
	public static final int MAX_INLETS = 32;
	public static final int MAX_OUTLETS = 32;
}
