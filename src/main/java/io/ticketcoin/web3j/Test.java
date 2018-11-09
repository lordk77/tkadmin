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
			Web3ClientVersion web3ClientVersion;
			web3ClientVersion = web3j.web3ClientVersion().send();
			System.out.println(web3ClientVersion.getWeb3ClientVersion());
			//Mainnet
//			testEthGetTransactionByHash("0xa8b48b65e0fefaae0cd7368c26a705cef5734ff04d31c4ab7f2732ed463cfd1e");
//			testEthGetBalance("0x00d94c9F8ff6c2F40fb4905C206D642e4E6Ab130");
			
			//rinkeby
//			testEthGetTransactionByHash("0x4114bd00a0d3b568771a874742f551084d95a4bb67994c170fb9b5ce9cc79215");
//			testEthGetBalance("0xF60Be701e3c97c371D27b48Af185c901cf4a3C8b");
			
			
			String seed = UUID.randomUUID().toString();
			ECKeyPair exKey = Keys.createEcKeyPair();

			BigInteger privateKey = exKey.getPrivateKey();
			BigInteger publicKey = exKey.getPublicKey();
			System.out.println(publicKey.toString(16));
			System.out.println(privateKey.toString(16));

			
			WalletFile wallet = Wallet.createLight(seed,exKey);
			System.out.println( wallet.getAddress());
			/*	
			Credentials credentials = Credentials.create("MY PRIVATE KEY");			
			
			String fileName = WalletUtils.generateNewWalletFile("burubu", new File("C:\\wallet"));
			System.out.println(WalletUtils.getTestnetKeyDirectory());
			Credentials c = WalletUtils.loadCredentials("burubu", "C:\\wallet\\" + fileName);
			System.out.println(c.getAddress());
			System.out.println(c.getEcKeyPair().getPublicKey().toString(16));
			System.out.println(c.getEcKeyPair().getPrivateKey().toString(16));
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	  public static void testEthGetTransactionByHash(String txHash) throws Exception {
	        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txHash).send();
	        Transaction transaction = ethTransaction.getTransaction().get();
	        System.out.println(transaction.getFrom());
	    }
	  
	  
	  public static void testEthGetBalance(String address) throws Exception {
	        EthGetBalance ethGetBalance = web3j.ethGetBalance(
	                address, DefaultBlockParameter.valueOf("latest")).send();
	        
	        System.out.println(ethGetBalance.getBalance());
	    }
	  
	  
	  
}
