package org.zhenying.project;

public class s3crendential {
	private static String accessID = "";
	// Please convert all "/"s to %2F. 
	// More details please refer http://wiki.apache.org/hadoop/AmazonS3. 
	private static String secretKey = "";
	
	protected static String getPremission() {
		return accessID + ":" + secretKey + "@"; 
	}

}
