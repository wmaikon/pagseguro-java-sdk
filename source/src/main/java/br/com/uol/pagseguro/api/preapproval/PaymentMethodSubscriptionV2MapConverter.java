package br.com.uol.pagseguro.api.preapproval;

import br.com.uol.pagseguro.api.common.domain.converter.CreditCardV3MapConverter;
import br.com.uol.pagseguro.api.utils.AbstractMapConverter;
import br.com.uol.pagseguro.api.utils.RequestMap;

public class PaymentMethodSubscriptionV2MapConverter extends
AbstractMapConverter<PaymentMethodSubscription> {

	private final static CreditCardV3MapConverter CREDIT_CARD_MC = new CreditCardV3MapConverter();
	
	@Override
	protected void convert(RequestMap requestMap, PaymentMethodSubscription paymentMethod) {
		requestMap.putString("type", paymentMethod.getType().getValue());
		requestMap.putMap(CREDIT_CARD_MC.convert(paymentMethod.getCreditCard()));
	}

}
