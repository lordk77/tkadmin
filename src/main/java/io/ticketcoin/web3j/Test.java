package io.ticketcoin.web3j;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import io.ticketcoin.config.Configuration;
import io.ticketcoin.utility.UUIDConverter;
import io.ticketcoin.web3j.wrapper.TicketCoinCore;
import io.ticketcoin.web3j.wrapper.TicketCoinCoreUtil;

public class Test {
	
	private static Web3j web3j = Web3j.build(new HttpService(Configuration.getProperty(Configuration.ETH_NODE)));

	//0xf30f04f8919864c198070501be101b16eb3b1f09
	
	public static void main(String[] args) 
	{
		try {
			//Genera uno wallet
//			File outputDir = new File(WalletUtils.getRinkebyKeyDirectory());
//			String fileName = WalletUtils.generateNewWalletFile("burubu",outputDir);
			
			
			ContractGasProvider gasProvider = new StaticGasProvider(Convert.toWei("8", Unit.GWEI).toBigInteger(),new BigInteger("3000000"));
			
			String fileName = WalletUtils.getRinkebyKeyDirectory() +"/UTC--2018-11-10T14-33-51.48000000Z--10a94cb649026b89ed1c28e3600bb49de17f2825.json";
			
			//loads credential
			Credentials credentials = WalletUtils.loadCredentials("burubu",fileName);
			System.out.println(credentials.getAddress());

			 
			//Checks balance
			System.out.println("Balance: " + getETHBalance(credentials.getAddress()).getBalance());			


			
			//enroll ticket
			TicketCoinCore tc = TicketCoinCore.load(Configuration.getProperty(Configuration.TICKETCOIN_CORE_ADDRESS), web3j, credentials, 
					gasProvider);
			
			
			
			


	        String value =  tc.getCTO().send();
			System.out.println("--> : "  + value);

			/*
			 * b1140e5a-2558-4223-af2f-ed5a8adb8e69
			 * */
			UUID tckUUID = UUID.randomUUID();
			TicketCoinCoreUtil tccu = new TicketCoinCoreUtil(credentials, web3j, gasProvider);
			CompletableFuture<EthSendTransaction> cp = tccu.enrollTicket(
					UUIDConverter.convertToBigInteger(tckUUID),
					BigInteger.ZERO, //_organizationUUID
					BigInteger.ZERO, //_priceCapGwai
					BigInteger.ZERO, //_validFrom
					BigInteger.ZERO, //_validUntil
					BigInteger.ONE, //_allowedTransfers
					BigInteger.ZERO, //_transferRule
					BigInteger.ZERO, //_ticketState
					"0x10a94cb649026b89ed1c28e3600bb49de17f2825"//_owner
					);
			
			String transactionHash =  cp.get().getResult();
			System.out.println("tx --> : "  + transactionHash);


			tccu.startTxListener(web3j, transactionHash);
	
			
			
	        List<BigInteger> result =  tc.tokensOfOwner(credentials.getAddress()).send();
	        for (BigInteger _tokenID : result) {
	        	try {
				System.out.println("--> : "  + UUIDConverter.convertFromBigInteger(tc.getTicketUUID(_tokenID).send()).toString());
	        	}
	        	catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	  public static void testEthGetTransactionByHash(String txHash) throws Exception {
	        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txHash).send();
	        Transaction transaction = ethTransaction.getTransaction().get();
	        System.out.println(transaction.getFrom());
	    }
	  
	  
	  public static EthGetBalance getETHBalance(String address) throws Exception {
	        return  web3j.ethGetBalance(
	                address, DefaultBlockParameter.valueOf("latest")).send();
	        
	  }
	  
	  
	  
	  
	  
	  
	   
	  
	  
	  
	  
	  
	  
}
