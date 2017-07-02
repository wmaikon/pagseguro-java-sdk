package br.com.uol.pagseguro.api.preapproval;

import br.com.uol.pagseguro.api.common.domain.CreditCard;
import br.com.uol.pagseguro.api.common.domain.enums.PaymentMethodGroup;

public interface PaymentMethodSubscription {

	PaymentMethodGroup getType();
	CreditCard getCreditCard();
	
}
