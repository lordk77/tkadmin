package io.ticketcoin.web3j.wrapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;

import io.ticketcoin.config.Configuration;

public class TicketCoinCoreUtil {
	

	private Credentials credential;
	private Web3j web3j;
	private ContractGasProvider gasProvider;
	
	public TicketCoinCoreUtil(Credentials credential, Web3j web3j, ContractGasProvider gasProvider)
	{
		this.credential = credential;
		this.web3j = web3j;
		this.gasProvider = gasProvider;
	}
	
    
	 public CompletableFuture<EthSendTransaction> enrollTicket(BigInteger _ticketUUID, BigInteger _organizationUUID, BigInteger _priceCapGwai, BigInteger _validFrom, BigInteger _validUntil, BigInteger _allowedTransfers, BigInteger _transferRule, BigInteger _ticketState, String _owner) throws IOException, InterruptedException, ExecutionException {
	        final Function function = new Function(
	                TicketCoinCore.FUNC_ENROLLTICKET, 
	                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_ticketUUID), 
	                new org.web3j.abi.datatypes.generated.Uint256(_organizationUUID), 
	                new org.web3j.abi.datatypes.generated.Uint256(_priceCapGwai), 
	                new org.web3j.abi.datatypes.generated.Uint256(_validFrom), 
	                new org.web3j.abi.datatypes.generated.Uint256(_validUntil), 
	                new org.web3j.abi.datatypes.generated.Int256(_allowedTransfers), 
	                new org.web3j.abi.datatypes.generated.Uint256(_transferRule), 
	                new org.web3j.abi.datatypes.generated.Uint256(_ticketState), 
	                new org.web3j.abi.datatypes.Address(_owner)), 
	                Collections.<TypeReference<?>>emptyList());
	        
	        
	       
	        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
	        		credential.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();

		     BigInteger nonce = ethGetTransactionCount.getTransactionCount();
		     
			RawTransaction rawTransaction  = RawTransaction.createTransaction(
					nonce, gasProvider.getGasPrice(TicketCoinCore.FUNC_ENROLLTICKET),  
					gasProvider.getGasLimit(TicketCoinCore.FUNC_ENROLLTICKET), 
					Configuration.getProperty(Configuration.TICKETCOIN_CORE_ADDRESS),
					BigInteger.ZERO, 
					FunctionEncoder.encode(function)
					);
			
					
					
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credential);
			String hexValue = Numeric.toHexString(signedMessage);
			return web3j.ethSendRawTransaction(hexValue).sendAsync();

			
	        
	    }
	 
	 
	 public static TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) 
				throws Exception 
		{

			int attempts = 40;
			int sleep_millis = 1000;
			
			Optional<TransactionReceipt> receipt = getReceipt(web3j, transactionHash);

			while(attempts-- > 0 && !receipt.isPresent()) {
				Thread.sleep(sleep_millis);
				receipt = getReceipt(web3j, transactionHash);
			}

			if (attempts <= 0) {
				throw new RuntimeException("No Tx receipt received");
			}

			return receipt.get();
		}
	 
	 /**
		 * Returns the TransactionRecipt for the specified tx hash as an optional.
		 */
		public static Optional<TransactionReceipt> getReceipt(Web3j web3j, String transactionHash) 
				throws Exception 
		{
			EthGetTransactionReceipt receipt = web3j
					.ethGetTransactionReceipt(transactionHash)
					.sendAsync()
					.get();

			return receipt.getTransactionReceipt();
		}
		
		
		public void startTxListener(final Web3j web3j, final String transactionHash)
		{
			CompletableFuture.runAsync(new Runnable() {
			    @Override
			    public void run() {
			        // Simulate a long-running Job
			        try {
			        	TransactionReceipt txReceipt = new TicketCoinCoreUtil(credential, web3j, gasProvider).waitForReceipt(web3j, transactionHash);
			        		
			        		System.out.println(
			    					"*****************************************\n" +
			    					"block: " + txReceipt.getBlockNumber() + "\n" +
			    					"gas Used" + txReceipt.getGasUsed() +  "\n" +
			    					"Price in wei " + txReceipt.getGasUsed().multiply(gasProvider.getGasPrice()) +  "\n" +
			    					"*****************************************" 
			    					);
			        		
			        		
			        } catch (Exception e) {
			            throw new IllegalStateException(e);
			        }
			    }
			});
			
			
			
			
			
		}
		
		
		
		
	 
	 
}
