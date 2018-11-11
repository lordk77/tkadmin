package io.ticketcoin.web3j;

import java.io.File;
import java.math.BigInteger;
import java.util.UUID;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

public class Test {
	
//	private static Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/d66bc0d1ff7f4598b3bb2db162a7427d"));
	private static Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/d66bc0d1ff7f4598b3bb2db162a7427d"));

	
	public static void main(String[] args) 
	{
		try {
			//Genera uno wallet
//			File outputDir = new File(WalletUtils.getRinkebyKeyDirectory());
//			String fileName = WalletUtils.generateNewWalletFile("burubu",outputDir);
			
			
			String fileName = WalletUtils.getRinkebyKeyDirectory() +"/UTC--2018-11-10T14-33-51.48000000Z--10a94cb649026b89ed1c28e3600bb49de17f2825.json";
			
			//loads credential
			Credentials credentials = WalletUtils.loadCredentials("burubu",fileName);
			System.out.println(credentials.getAddress());
			//Checks balance
			System.out.println("Balance: " + getETHBalance(credentials.getAddress()).getBalance());			


			
			
			
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
