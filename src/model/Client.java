package model;

import main.Payable;

public class Client extends Person implements Payable {
	
	protected final int MEMBER_ID = 456;
	protected final double BALANCE = 50.00;
	protected int memberId = MEMBER_ID ;
	protected Amount balance = new Amount(BALANCE);
	
	public Client(String client){
		super(client);
	}
	
	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public Amount getBalance() {
		return balance;
	}

	public void setBalance(Amount balance) {
		this.balance = balance;
	}

	public int getMEMBER_ID() {
		return MEMBER_ID;
	}

	public double getBALANCE() {
		return BALANCE;
	}

	@Override
	public boolean pay(Amount saleAmount) {
		balance.setValue(balance.getValue()- saleAmount.getValue());
		if (balance.getValue()<= 0) {
			System.out.println("Balance necesary to pay : " + balance);
			return false;
		}else {
			System.err.println("Sale Complete, balance :" + balance);
			return true;
		}
	}
	
	
}
