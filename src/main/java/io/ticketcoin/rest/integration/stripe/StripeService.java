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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.net.RequestOptions;

import io.ticketcoin.dashboard.persistence.model.PurchaseOrder;
import io.ticketcoin.dashboard.persistence.model.User;
import io.ticketcoin.dashboard.persistence.service.ChargeService;
import io.ticketcoin.dashboard.persistence.service.GenericService;
import io.ticketcoin.dashboard.persistence.service.PurchaseOrderService;
import io.ticketcoin.dashboard.persistence.service.UserService;
import io.ticketcoin.rest.integration.stripe.dto.EphemeralKeysRequest;
import io.ticketcoin.rest.integration.stripe.model.StripeChargeRequest;
import io.ticketcoin.rest.integration.stripe.model.StripeChargeResponse;
import io.ticketcoin.rest.response.JSONResponseWrapper;


@Path("/stripe")
public class StripeService {
	
	@Context
    private MessageContext mc;

	
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
	@Consumes(MediaType.APPLICATION_JSON)
	public Response charge(StripeChargeRequest chargeRequest) 
	{
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
				chargeParams.put("statement_descriptor", "Ticketcoin");

				
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
	
	
	@POST
	@Path("/ephemeral_keys")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ephemeral_keys(EphemeralKeysRequest ephemeralKeysRequest) 
	{
			
			try 
			{
				String userName = ((OAuthContext)mc.getContext(OAuthContext.class)).getSubject().getLogin();
				EphemeralKey ephemeralKey = createEphemeralKeys(ephemeralKeysRequest, userName);
				
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getSuccessWrapper(ephemeralKey.getRawJson()))).build();

				
			} catch (Exception e) {
				e.printStackTrace();
				return Response.ok(new Gson().toJson(JSONResponseWrapper.getFaultWrapper(e.getMessage()))).build();
			}
	}

	
	
	public EphemeralKey createEphemeralKeys(EphemeralKeysRequest ephemeralKeysRequest, String userName) throws StripeException
	{
		//Check the user
		User user = new UserService().getUser(userName);	
		
		//Register the user to Stripe if not yet registered
		if(StringUtils.isBlank(user.getStripe_identifier()))
		{
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("description", "Customer " + (StringUtils.isNotBlank(user.getEmail()) ? user.getEmail() : user.getUsername()));
			if(ephemeralKeysRequest.getSource()!=null)
				customerParams.put("source", ephemeralKeysRequest.getSource());
			Customer c = Customer.create(customerParams);
			user.setStripe_identifier(c.getId());
			new UserService().saveOrUpdate(user);
		}
		
		//Creates the ephemeralKeys
		Map<String, Object> ephemeralKeyParams = new HashMap<String, Object>();
		ephemeralKeyParams.put("customer", user.getStripe_identifier());
//		ephemeralKeyParams.put("stripe_version", ephemeralKeysRequest.getApi_version());
		
		RequestOptions opt = RequestOptions.builder()
				//.setApiKey(ephemeralKeysRequest.getApi_version())
				.setStripeVersion(ephemeralKeysRequest.getApi_version())
				//.setClientId(user.getStripe_identifier())
				.build();
		
		EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams, opt);
		return ephemeralKey;
	}

	
	

	public MessageContext getMc() {
		return mc;
	}



	public void setMc(MessageContext mc) {
		this.mc = mc;
	}
	
	
	

	
	

}
