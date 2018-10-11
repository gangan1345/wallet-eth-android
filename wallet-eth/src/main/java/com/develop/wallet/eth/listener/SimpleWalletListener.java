package com.develop.wallet.eth.listener;

/**
 * @author Angus
 */
public abstract class SimpleWalletListener implements WalletListener {
    @Override
    public void onSendTransaction(String hash) {

    }

    @Override
    public void onQueryTokenBalance(String balance) {

    }
}
