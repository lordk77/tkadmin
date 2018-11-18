package io.ticketcoin.currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class CryptoConverter {
	
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    
    
	private static long lastReadTime = 0;
	
	public static enum CryptoCurrency{
		BTC, ETH,LTC
	}
	
	private static Map<String,CryptoCrossDTO> crossTable = new HashMap<>();
	
	
	public static BigDecimal convert(BigDecimal amount, String fromCurrency, CryptoCurrency toCurrency )
	{
		refreshCrossValues();
		if(crossTable.containsKey(fromCurrency)) 
		{
			if(CryptoCurrency.BTC.equals(toCurrency))
				return amount.multiply(crossTable.get(fromCurrency).getBTC());
			else if(CryptoCurrency.ETH.equals(toCurrency))
				return amount.multiply(crossTable.get(fromCurrency).getETH());
			else if(CryptoCurrency.LTC.equals(toCurrency))
				return amount.multiply(crossTable.get(fromCurrency).getLTC());
			else 
				return  BigDecimal.ZERO;
		}
		else 
			return  BigDecimal.ZERO;

	}
	
	
	private static void refreshCrossValues()
	{

		synchronized (crossTable) {
			
			//update cross every 30 minutes
			if(new Date().getTime() - lastReadTime > 30*60*1000);
			{
			
				try 
				{
					
					
					CryptoCrossDTO crossEUR = getCross("EUR", "BTC,ETH,LTC");
					CryptoCrossDTO crossUSD = getCross("USD", "BTC,ETH,LTC");
		
					crossTable.clear();
					crossTable.put("EUR", crossEUR);
					crossTable.put("USD", crossUSD);
		
					lastReadTime = new Date().getTime();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	
	private static CryptoCrossDTO getCross(String fsym, String tsyms)
	{

		// The following line initializes the HttpGet Object with the URL in order to send a request
        HttpGet get = new HttpGet("https://min-api.cryptocompare.com/data/price?fsym=" + fsym + "&tsyms=" + tsyms);
        CryptoCrossDTO cryptoCrossDTO = null;
        
        try {
            CloseableHttpResponse response =  httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            // the following line converts the JSON Response to an equivalent Java Object
            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));

            // Parsed JSON Objects are accessed according to the JSON resonse's hierarchy, output strings are built
            cryptoCrossDTO= new CryptoCrossDTO();
            cryptoCrossDTO.setBTC(new BigDecimal(exchangeRates.getString("BTC")));
            cryptoCrossDTO.setETH(new BigDecimal(exchangeRates.getString("ETH")));
            cryptoCrossDTO.setLTC(new BigDecimal(exchangeRates.getString("LTC")));
            
            response.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return cryptoCrossDTO;
	}
	
	
	
	
public static void main(String[] args) {
	System.out.println(convert(BigDecimal.ONE, "EUR", CryptoCurrency.ETH));
}
}
