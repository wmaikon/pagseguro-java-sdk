package br.com.uol.pagseguro.api.preapproval;

import br.com.uol.pagseguro.api.common.domain.Sender;

public interface PreApprovalSubscription {

	String getPlan();

	String getReference();

	Sender getSender();

	PaymentMethodSubscription getPaymentMethod();

}
