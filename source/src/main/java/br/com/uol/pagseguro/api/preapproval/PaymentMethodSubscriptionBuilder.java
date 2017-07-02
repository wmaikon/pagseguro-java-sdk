package br.com.uol.pagseguro.api.preapproval;

import br.com.uol.pagseguro.api.common.domain.CreditCard;
import br.com.uol.pagseguro.api.common.domain.enums.PaymentMethodGroup;
import br.com.uol.pagseguro.api.utils.Builder;

public class PaymentMethodSubscriptionBuilder implements Builder<PaymentMethodSubscription> {

	public PaymentMethodGroup type;
	public CreditCard creditCard;

	public PaymentMethodGroup getType() {
		return type;
	}

	public PaymentMethodSubscriptionBuilder withType(PaymentMethodGroup type) {
		this.type = type;
		return this;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public PaymentMethodSubscriptionBuilder withCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
		return this;
	}
	
	public PaymentMethodSubscriptionBuilder withCreditCard(Builder<CreditCard> creditCard) {
		return withCreditCard(creditCard.build());
	}

	@Override
	public PaymentMethodSubscription build() {
		return new SimplePaymentMethodSubscription(this);
	}
	
	private class SimplePaymentMethodSubscription implements PaymentMethodSubscription {

		private PaymentMethodSubscriptionBuilder paymentMethodSubscriptionBuilder;

		public SimplePaymentMethodSubscription(PaymentMethodSubscriptionBuilder paymentMethodSubscriptionBuilder) {
			this.paymentMethodSubscriptionBuilder = paymentMethodSubscriptionBuilder;
		}

		@Override
		public PaymentMethodGroup getType() {
			return paymentMethodSubscriptionBuilder.type;
		}

		@Override
		public CreditCard getCreditCard() {
			return paymentMethodSubscriptionBuilder.creditCard;
		}
		
	}

}
