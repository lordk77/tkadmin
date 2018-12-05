package io.ticketcoin.config;

import java.util.Properties;

import io.ticketcoin.rest.integration.stripe.StripeService;

public class Configuration {
	
	private static Properties properties = new Properties();
	private static String DEFAULT_CONFIG_FILENAME = "config.properties";
	
	
	public static String ETH_NODE = "ETH_NODE";
	public static String TICKETCOIN_CORE_ADDRESS = "TICKETCOIN_CORE_ADDRESS";
		
	public static String CTO_PRIVATE_KEY = "CTO_PRIVATE_KEY";
	public static String TX_DETAIL_URL = "TX_DETAIL_URL";

	

			
			
		
    static {
        try {
            properties.load(StripeService.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILENAME));
            
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } 
    }
    
    
    public static String getProperty(String propertyName)
    {
    	return properties.getProperty(propertyName);
    }
    

}
