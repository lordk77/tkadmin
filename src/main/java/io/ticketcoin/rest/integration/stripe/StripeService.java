package io.ticketcoin.rest.integration.stripe;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.model.Charge;

import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.service.ChargeService;
import io.ticketcoin.dashboard.persistence.service.GenericService;
import io.ticketcoin.dashboard.persistence.service.PurchaseOrderService;
import io.ticketcoin.rest.integration.stripe.model.StripeChargeRequest;
import io.ticketcoin.rest.integration.stripe.model.StripeChargeResponse;
import io.ticketcoin.rest.response.JSONResponseWrapper;


@Path("/stripe")
public class StripeService {
	
	private static Properties properties = new Properties();
	public static String DEFAULT_CONFIG_FILENAME = "config.properties";
	
	
    static {
        try {
            properties.load(StripeService.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILENAME));
             
            Stripe.apiKey = properties.getProperty(properties.getProperty("STRIPE_ENV") + "_" + "PRIVATE_KEY");
            
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } 
    }
    
    public static String getPublicKey()
    {
        return properties.getProperty(properties.getProperty("STRIPE_ENV") + "_" + "PUBLIC_KEY");
    }
    
    
    
	@POST
	@Path("/charge")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response charge(@FormParam("amount") Integer amount, @FormParam("stripeToken") String stripeToken , @FormParam("stripeTokenType") String stripeTokenType, @FormParam("stripeEmail") String stripeEmail, @FormParam("orderUUID") String orderUUID) 
	{
		
			StripeChargeRequest chargeRequest = new StripeChargeRequest();
			chargeRequest.setAmount(amount);
			chargeRequest.setStripeToken(stripeToken);
			chargeRequest.setStripeTokenType(stripeTokenType);
			chargeRequest.setStripeEmail(stripeEmail);
			chargeRequest.setOrderUUID(orderUUID);
			chargeRequest.setRequestDate(new Date());
			
			try 
			{
				
				if(StringUtils.isBlank(chargeRequest.getOrderUUID()))
					throw new Exception("error.missing.data");
				
				
				PurchaseOrder o = new PurchaseOrderService().getOrder(chargeRequest.getOrderUUID());
				
				if(o==null)
					throw new Exception("error.missing.order");
				
				else if (o.getTotalAmount().compareTo(new BigDecimal(chargeRequest.getAmount()).divide(new BigDecimal("100")))>0)
					throw new Exception("error.insuficent.amount");
				
		        chargeRequest.setDescription("Order no." + chargeRequest.getOrderUUID());
		        
		        new GenericService<>(StripeChargeRequest.class).save(chargeRequest);
		        	
				
				Map<String, Object> chargeParams = new HashMap<>();
				chargeParams.put("amount", chargeRequest.getAmount());
				chargeParams.put("currency", "EUR");
				chargeParams.put("description", chargeRequest.getDescription());
				chargeParams.put("source", chargeRequest.getStripeToken());
				Charge charge = new ChargeService().carge(o, chargeParams);
		            
		            
		        StripeChargeResponse response = new StripeChargeResponse(); 
		        
		        response.setStripeId(charge.getId());
		        response.setStatus(charge.getStatus());
		        response.setChargeId(charge.getId());
		        response.setBalance_transaction(charge.getBalanceTransaction());
		        chargeRequest.setResponse(response);
		        
		        new GenericService<>(StripeChargeRequest.class).save(chargeRequest);
		        
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(response))).build();

					
				
				
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
	}
	

}
