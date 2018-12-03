package io.ticketcoin.web3j;

import java.math.BigInteger;
import java.util.Arrays;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import io.ticketcoin.config.Configuration;
import io.ticketcoin.web3j.wrapper.TicketCoinCore;

public class Test {
	
	private static Web3j web3j = Web3j.build(new HttpService(Configuration.getProperty(Configuration.ETH_NODE)));

	
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
			//TicketCoinCore tc = TicketCoinCore.load(Configuration.getProperty(Configuration.TICKETCOIN_CORE_ADDRES), web3j, credentials, 
			//		gasProvider);
			
			
			/*
			TransactionReceipt transactionReceipt = tc.enrollTicket(
					UUIDConverter.convertToBigInteger(UUID.randomUUID()),
					BigInteger.ZERO, //_organizationUUID
					BigInteger.ZERO, //_priceCapGwai
					BigInteger.ZERO, //_validFrom
					BigInteger.ZERO, //_validUntil
					BigInteger.ONE, //_allowedTransfers
					BigInteger.ZERO, //_transferRule
					BigInteger.ZERO, //_ticketState
					"0x10a94cb649026b89ed1c28e3600bb49de17f2825"//_owner
					).send();
			
			List<TransferEventResponse> eventValues = tc.getTransferEvents(transactionReceipt);
*/
			
			//System.out.println(tc.ctoAddress().send());
			
			
			
			
			EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
					credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();

		     BigInteger nonce = ethGetTransactionCount.getTransactionCount();
		     
			RawTransaction rawTransaction  = RawTransaction.createTransaction(
					nonce, Convert.toWei("8", Unit.GWEI).toBigInteger(), new BigInteger("3000000"), 
					Configuration.getProperty(Configuration.TICKETCOIN_CORE_ADDRESS),
					BigInteger.ZERO, 
					FunctionEncoder.encode(new Function(TicketCoinCore.FUNC_CTOADDRESS, 
			                Arrays.<Type>asList(), 
			                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {})))
					);
			
					
					
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);

			/*
			EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
			String transactionHash = ethSendTransaction.getTransactionHash();
			System.out.println(transactionHash);
			System.out.println(ethSendTransaction.getRawResponse());
			
			*/
			
			
			EthCall call = web3j.ethCall(
			
		            org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
		               credentials.getAddress(), Configuration.getProperty(Configuration.TICKETCOIN_CORE_ADDRESS), hexValue),
		            DefaultBlockParameterName.LATEST)
		            .send();
			
			
			//new TicketCoinCoreUtil(credential, web3j, gasProvider)
			
			System.out.println(call.getRawResponse());
			
			
			
			
			/*
			for(TransferEventResponse ter : eventValues)
				System.out.println("Enrolled ticket " + ter.tokenId);*/
			
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
