# wallet-eth-android
 wallet-eth 以太坊代币钱包 助记词 私钥 keystore 转账(bip39、bip32、bip44、web3j)

## 生成钱包地址

``` Java
// 生成钱包地址
Wallet wallet = WalletManager.generateWalletAddress();
// 根据助记词获取地址
WalletManager.generateAddress(wallet.getMnemonic());
// 通过助记词获取私钥
WalletManager.generatePrivateKey(wallet.getMnemonic());
// 生成钱包keystore
Wallet wallet1 = WalletManager.generateWalletKeystore("123456", wallet.getMnemonic());
// 通过keystore 获取私钥
WalletManager.generatePrivateKey("123456", wallet1.getKeystore());
```

助记词：glare pave fatal catch cake large mad exit any hood expose neither <br>
地址： 0x3a5c0fe05f7515a8283b1d2a1a241cbabafbf094<br>
keystore： {"address":"3a5c0fe05f7515a8283b1d2a1a241cbabafbf094","id":"13b913ac-2d19-4473-8515-d6c54fe9bad8","version":3,"crypto":{"cipher":"aes-128-ctr","ciphertext":"66f392a77aff1f0c341519561c9eb5857ef44255b35d1781f2e3fd59a65e624f","cipherparams":{"iv":"dc81d0a5b103137c0b2f728a54ce6c17"},"kdf":"scrypt","kdfparams":{"dklen":32,"n":4096,"p":6,"r":8,"salt":"7dfa3c3de3cacfab8c93bd1790dce2476696222f10153913a7f0632b4026f914"},"mac":"2419dbdd294b4fe0bde158de2fefb218237b66316c0523911c18dad0828669f8"}}<br>

## 转账
``` Java
String hash = WalletManager.sendTransactionByMnemonic("0x8c10a04b0ce0414b089efe89e311f75fbf964563",
             "volume final loyal match glare era olive size craft deposit palm label",
             "0xe94791399f3a0e6d9ac23e64102005efef1bb424", String.valueOf(0.1));


//        WalletManager.sendTransactionByMnemonicAsync("0x8c10a04b0ce0414b089efe89e311f75fbf964563",
//                "volume final loyal match glare era olive size craft deposit palm label",
//                "0xe94791399f3a0e6d9ac23e64102005efef1bb424", String.valueOf(0.1), null);

```

## 相关查询
``` Java
// 获取版本信息
WalletManager.getClientVersion();
// ethBalance 以太坊余额
WalletManager.getEthBalance(address);
// noce
WalletManager.getNonce(address);
// name
WalletManager.getTokenName(address);
// symbol
WalletManager.getTokenSymbol(address);
// decimals
WalletManager.getTokenDecimals(address);
// totalsupply
WalletManager.getTokenTotalSupply(address);
// token balance 代币余额
WalletManager.getTokenBalance(address);
```