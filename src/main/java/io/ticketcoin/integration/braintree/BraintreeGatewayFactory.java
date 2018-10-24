package io.ticketcoin.integration.braintree;
import java.util.Map;
import java.util.Properties;

import com.braintreegateway.BraintreeGateway;

public class BraintreeGatewayFactory {
    public static BraintreeGateway fromConfigMapping(Map<String, String> mapping) {
        return new BraintreeGateway(
            mapping.get("BT_ENVIRONMENT"),
            mapping.get("BT_MERCHANT_ID"),
            mapping.get("BT_PUBLIC_KEY"),
            mapping.get("BT_PRIVATE_KEY")
        );
    }

    public static BraintreeGateway fromConfigFile(String configFileName) {
        Properties properties = new Properties();
        try {
            properties.load(BraintreeGatewayFactory.class.getClassLoader().getResourceAsStream(configFileName));
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } 

        return new BraintreeGateway(
            properties.getProperty("BT_ENVIRONMENT"),
            properties.getProperty("BT_MERCHANT_ID"),
            properties.getProperty("BT_PUBLIC_KEY"),
            properties.getProperty("BT_PRIVATE_KEY")
        );
    }
}