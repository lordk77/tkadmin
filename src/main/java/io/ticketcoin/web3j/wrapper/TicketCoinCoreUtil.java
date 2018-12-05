package io.ticketcoin.web3j.wrapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

import org.hibernate.Session;
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
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.utils.Convert.Unit;

import io.ticketcoin.config.Configuration;
import io.ticketcoin.dashboard.persistence.dao.GenericDAO;
import io.ticketcoin.dashboard.persistence.dao.TicketDAO;
import io.ticketcoin.dashboard.persistence.model.ETHTransaction;
import io.ticketcoin.dashboard.persistence.model.Ticket;
import io.ticketcoin.dashboard.persistence.service.TicketService;
import io.ticketcoin.dashboard.utils.HibernateUtils;
import io.ticketcoin.utility.UUIDConverter;

public class TicketCoinCoreUtil {
	

	private static Web3j web3j = Web3j.build(new HttpService(Configuration.getProperty(Configuration.ETH_NODE)));
	private static Credentials credentials = Credentials.create(Configuration.getProperty(Configuration.CTO_PRIVATE_KEY));;
	private static ContractGasProvider gasProvider = new StaticGasProvider(Convert.toWei("8", Unit.GWEI).toBigInteger(),new BigInteger("3000000"));;
	
	private static TicketCoinCore tcc = TicketCoinCore.load(Configuration.getProperty(Configuration.TICKETCOIN_CORE_ADDRESS), web3j, credentials,  gasProvider);
	
	
    
	 public CompletableFuture<EthSendTransaction> enrollTicket(Ticket ticket) throws IOException, InterruptedException, ExecutionException {
	        
		 
		 //Prepare the function
		 final Function function = new Function(
	                TicketCoinCore.FUNC_ENROLLTICKET, 
	                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(UUIDConverter.convertToBigInteger(UUID.fromString(ticket.getTicketUUID()))), 
	                new org.web3j.abi.datatypes.generated.Uint256(UUIDConverter.convertToBigInteger(UUID.fromString(ticket.getOrder().getEventUUID()))), 
	                new org.web3j.abi.datatypes.generated.Uint256(0), 
	                new org.web3j.abi.datatypes.generated.Uint256(ticket.getValidFrom()!=null? ticket.getValidFrom().getTime():null), 
	                new org.web3j.abi.datatypes.generated.Uint256(ticket.getValidTo()!=null? ticket.getValidTo().getTime():0), 
	                new org.web3j.abi.datatypes.generated.Int256(ticket.getAllowedTransfers()!=null?ticket.getAllowedTransfers():0), 
	                new org.web3j.abi.datatypes.generated.Uint256(ticket.getTransferRule()!=null?ticket.getTransferRule():0), 
	                new org.web3j.abi.datatypes.generated.Uint256(Ticket.STATE_VALID), 
	                new org.web3j.abi.datatypes.Address(ticket.getOwnedBy().getWallet().getAddress())), 
	                Collections.<TypeReference<?>>emptyList());
	        
	        
	       //Prepare raw transaction
	        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
		    BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			RawTransaction rawTransaction  = RawTransaction.createTransaction(
					nonce, gasProvider.getGasPrice(TicketCoinCore.FUNC_ENROLLTICKET),  
					gasProvider.getGasLimit(TicketCoinCore.FUNC_ENROLLTICKET), 
					Configuration.getProperty(Configuration.TICKETCOIN_CORE_ADDRESS),
					BigInteger.ZERO, 
					FunctionEncoder.encode(function)
					);
			
					
			//Sign message		
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);
			
			//Send message
			return web3j.ethSendRawTransaction(hexValue).sendAsync();
 
	    }
	 
	 
	 public static TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) 
				throws Exception 
		{

			int attempts = 40;
			int sleep_millis = 5000;
			
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
		
		
		public void startTxListener(final String transactionHash)
		{
			CompletableFuture.runAsync(new Runnable() {
			    @Override
			    public void run() {
			        // Simulate a long-running Job
			        try {
			        	TransactionReceipt txReceipt = new TicketCoinCoreUtil().waitForReceipt(web3j, transactionHash);
			        		
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
		
		
		public void enrollTicket(Long ticketId)
		{
			CompletableFuture.runAsync(new Runnable() {

				TicketCoinCoreUtil tcu = new TicketCoinCoreUtil();
				
				@Override
			    public void run() {

			    	Session session = null;
			    	ETHTransaction transaction = new ETHTransaction();
			    	TicketDAO td = new TicketDAO();
			    	GenericDAO<ETHTransaction> txDao = new GenericDAO<ETHTransaction>(ETHTransaction.class);
			    	Ticket ticket = null;
					try
					{
						session = HibernateUtils.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						
						ticket = td.findById(ticketId);
						
						CompletableFuture<EthSendTransaction> cp = tcu.enrollTicket(ticket);
						String transactionHash =  cp.get().getResult();
						transaction.setTransactionHash(transactionHash);	
						transaction.setTransactionDetailURL(Configuration.getProperty(Configuration.TX_DETAIL_URL)+"/"+transactionHash);
						ticket.setTransaction(transaction);
						txDao.save(transaction);
						td.save(ticket);
						session.getTransaction().commit();
					}
					catch(Exception e)
					{
						e.printStackTrace();
						session.getTransaction().rollback();
					}
					
			    	

			        try 
			        {
			        	
			        	TransactionReceipt txReceipt = new TicketCoinCoreUtil().waitForReceipt(web3j, transaction.getTransactionHash());
			        	
			        	transaction.setBlockNumber(txReceipt.getBlockNumber().toString());
			        	transaction.setGasUsed(txReceipt.getGasUsed());
			        	transaction.setGasPriceInWEI(gasProvider.getGasPrice());
			        	
			        	
			        	try
			        	{
			        		new TicketService().updateTokenID(ticketId, tcc.getTransferEvents(txReceipt).get(0).tokenId.longValue());
			        	}
			        	catch(Exception e)
						{
							e.printStackTrace();
						}
			        	
			        	
			        	try
						{
							session = HibernateUtils.getSessionFactory().getCurrentSession();
							session.beginTransaction();
							txDao.save(transaction);
							session.getTransaction().commit();
						}
						catch(Exception e)
						{
							e.printStackTrace();
							session.getTransaction().rollback();
							throw e;
						}
			        	
			        		
			        		
			        } catch (Exception e) {
			        	e.printStackTrace();
			            throw new IllegalStateException(e);
			        }
			    }
			});
		}
		
		
		
		
	 
	 
}
