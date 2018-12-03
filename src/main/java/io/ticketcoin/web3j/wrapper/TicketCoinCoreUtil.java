package io.ticketcoin.web3j.wrapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;

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
		     
			RawTransaction rawTransaction  = RawTransaction.createContractTransaction(
					nonce, gasProvider.getGasPrice(TicketCoinCore.FUNC_ENROLLTICKET),  gasProvider.getGasLimit(TicketCoinCore.FUNC_ENROLLTICKET), BigInteger.ZERO, 
					FunctionEncoder.encode(function)
					);
			
					
					
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credential);
			String hexValue = Numeric.toHexString(signedMessage);
			return web3j.ethSendRawTransaction(hexValue).sendAsync();

			
	        
	    }
}
