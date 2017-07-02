package br.com.uol.pagseguro.api.preapproval;

import br.com.uol.pagseguro.api.common.domain.converter.SenderV2MapConverter;
import br.com.uol.pagseguro.api.utils.AbstractMapConverter;
import br.com.uol.pagseguro.api.utils.RequestMap;

public class PreApprovalSubscriptionV2MapConverter extends
AbstractMapConverter<PreApprovalSubscription> {
	

  private final static SenderV2MapConverter SENDER_MC = new SenderV2MapConverter();
  private final static PaymentMethodSubscriptionV2MapConverter PAYMENT_METHOD_MC = new PaymentMethodSubscriptionV2MapConverter();

	@Override
	protected void convert(RequestMap requestMap, PreApprovalSubscription subscription) {
		requestMap.putString("plan", subscription.getPlan());
		requestMap.putString("reference", subscription.getReference());
		requestMap.putMap(SENDER_MC.convert(subscription.getSender()));
		requestMap.putMap(PAYMENT_METHOD_MC.convert(subscription.getPaymentMethod()));
	}

}
