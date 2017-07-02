package br.com.uol.pagseguro.api.preapproval;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.uol.pagseguro.api.PagSeguro;
import br.com.uol.pagseguro.api.utils.XMLUnmarshallListener;

@XmlRootElement(name = "directPreApproval")
public class SubscribedPreApprovalResponseXML implements SubscribedPreApproval, XMLUnmarshallListener {

	private String code;

	public String getCode() {
		return code;
	}

	@XmlElement
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public void onUnmarshal(PagSeguro pagseguro, String rawData) {
	}

}
