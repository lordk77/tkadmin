package io.ticketcoin.dashboard.persistence.service;

import io.ticketcoin.dashboard.persistence.model.Wallet;

public class WalletService extends GenericService<Wallet>{

	public WalletService() {
		super(Wallet.class);
	}
	
	public Wallet createEthereumWallet()
	{
		Wallet wallet = new Wallet();
		wallet.setAddress("xxxxx");
		wallet.setPrivate_key("yyyy");
		wallet.setType("ticketcoin_ethereum");
		
		return wallet;
	}

	
}
