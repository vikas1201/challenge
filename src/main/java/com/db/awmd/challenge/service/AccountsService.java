package com.db.awmd.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferMoney;
import com.db.awmd.challenge.exception.NoSufficientBalanceException;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;
  
  @Getter
  private final NotificationService notification;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository,NotificationService notification) {
    this.accountsRepository = accountsRepository;
	this.notification = notification;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  
  public synchronized void transferMoney(TransferMoney transMoney) throws NoSufficientBalanceException{
	  Account fromAccount = getAccount(transMoney.getAccountFrom());
	  Account toAccount = getAccount(transMoney.getAccountTo());
	  if(fromAccount.getBalance().compareTo(transMoney.getAmount()) == 1 ) {
		  fromAccount.setBalance(fromAccount.getBalance().subtract(transMoney.getAmount()));
		  this.accountsRepository.addOrUpdateAccount(fromAccount.getAccountId(), fromAccount);
		  toAccount.setBalance(toAccount.getBalance().add(transMoney.getAmount()));
		  this.accountsRepository.addOrUpdateAccount(toAccount.getAccountId(), toAccount);
	  } else {
		  log.error("No Sufficient Balance.");
		  throw new NoSufficientBalanceException("No Sufficient Balance");
	  }
	  notification.notifyAboutTransfer(fromAccount, "Amount debited from account.");
	  notification.notifyAboutTransfer(toAccount, "Amount Credited to account.");
  }
}
