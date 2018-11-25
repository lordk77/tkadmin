package io.ticketcoin.dashboard.persistence.service;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;

import io.ticketcoin.dashboard.persistence.dao.TicketDAO;
import io.ticketcoin.dashboard.persistence.dao.WalletDAO;
import io.ticketcoin.dashboard.persistence.model.Card;
import io.ticketcoin.dashboard.persistence.model.Wallet;
import io.ticketcoin.dashboard.persistence.model.Wallet.WalletType;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class WalletService extends GenericService<Wallet>{

	public WalletService() {
		super(Wallet.class);
	}
	
	public Wallet createEthereumWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException
	{
		Wallet wallet = new Wallet();

		ECKeyPair ecKeyPair = Keys.createEcKeyPair();
		BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
		WalletFile aWallet = org.web3j.crypto.Wallet.createLight(UUID.randomUUID().toString(), ecKeyPair);
		String sAddress = "0x"+aWallet.getAddress();
		String sPrivatekeyInHex = privateKeyInDec.toString(16);
		 
		wallet.setAddress(sAddress);
		wallet.setPrivate_key(sPrivatekeyInHex);
		wallet.setType(WalletType.ETH_WALLET);
		
		return wallet;
	}
	
	
	public List<Wallet> getUserWallets(String userName)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<Wallet> retval = new WalletDAO().getUserWallets(userName);
			session.getTransaction().commit();
			return retval;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}
	}

	
	public Card getWalletByAddress(String address)
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Card retval = new WalletDAO().getWalletByAddress(address);
			session.getTransaction().commit();
			return retval;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		}
	}
	
}
