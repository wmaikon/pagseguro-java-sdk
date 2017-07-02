package br.com.uol.pagseguro.api.preapproval;

import br.com.uol.pagseguro.api.common.domain.Sender;
import br.com.uol.pagseguro.api.utils.Builder;

/**
 * @author andreireitz
 *
 */
public class PreApprovalSubscriptionBuilder implements Builder<PreApprovalSubscription> {

	public String plan;
	public String reference;
	public Sender sender;
	public PaymentMethodSubscription paymentMethod;

	public String getPlan() {
		return plan;
	}

	public PreApprovalSubscriptionBuilder withPlan(String plan) {
		this.plan = plan;
		return this;
	}

	public String getReference() {
		return reference;
	}

	public PreApprovalSubscriptionBuilder withReference(String reference) {
		this.reference = reference;
		return this;
	}

	public Sender getSender() {
		return sender;
	}
	
	public PreApprovalSubscriptionBuilder withSender(Sender sender) {
		this.sender = sender;
		return this;
	}

	public PreApprovalSubscriptionBuilder withSender(Builder<Sender> sender) {
		return withSender(sender.build());
	}

	public PaymentMethodSubscription getPaymentMethod() {
		return paymentMethod;
	}

	public PreApprovalSubscriptionBuilder withPaymentMethod(Builder<PaymentMethodSubscription> paymentMethod) {
		return withPaymentMethod(paymentMethod.build());
	}

	public PreApprovalSubscriptionBuilder withPaymentMethod(PaymentMethodSubscription paymentMethod) {
		this.paymentMethod = paymentMethod;
		return this;
	}

	@Override
	public PreApprovalSubscription build() {
		return new SimplePreApprovalSubscription(this);
	}
	
	private class SimplePreApprovalSubscription implements PreApprovalSubscription {

		private PreApprovalSubscriptionBuilder preApprovalSubscriptionBuilder;

		public SimplePreApprovalSubscription(PreApprovalSubscriptionBuilder preApprovalSubscriptionBuilder) {
			this.preApprovalSubscriptionBuilder = preApprovalSubscriptionBuilder;
		}

		@Override
		public String getPlan() {
			return preApprovalSubscriptionBuilder.plan;
		}

		@Override
		public String getReference() {
			return preApprovalSubscriptionBuilder.reference;
		}

		@Override
		public Sender getSender() {
			return preApprovalSubscriptionBuilder.sender;
		}

		@Override
		public PaymentMethodSubscription getPaymentMethod() {
			return preApprovalSubscriptionBuilder.paymentMethod;
		}
		
	}

}
