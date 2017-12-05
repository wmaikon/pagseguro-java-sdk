/*
 * 2007-2016 [PagSeguro Internet Ltda.]
 * 
 * NOTICE OF LICENSE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Copyright: 2007-2016 PagSeguro Internet Ltda.
 * Licence: http://www.apache.org/licenses/LICENSE-2.0
 */

package br.com.uol.pagseguro.api.preapproval;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import br.com.uol.pagseguro.api.Endpoints;
import br.com.uol.pagseguro.api.PagSeguro;
import br.com.uol.pagseguro.api.exception.PagSeguroLibException;
import br.com.uol.pagseguro.api.http.HttpClient;
import br.com.uol.pagseguro.api.http.HttpMethod;
import br.com.uol.pagseguro.api.http.HttpRequestBody;
import br.com.uol.pagseguro.api.http.HttpResponse;
import br.com.uol.pagseguro.api.preapproval.cancel.CancelPreApprovalResponseXML;
import br.com.uol.pagseguro.api.preapproval.cancel.CancelledPreApproval;
import br.com.uol.pagseguro.api.preapproval.cancel.PreApprovalCancellation;
import br.com.uol.pagseguro.api.preapproval.cancel.PreApprovalCancellationV2MapConverter;
import br.com.uol.pagseguro.api.preapproval.search.PreApprovalSearchResource;
import br.com.uol.pagseguro.api.utils.Builder;
import br.com.uol.pagseguro.api.utils.CharSet;
import br.com.uol.pagseguro.api.utils.RequestMap;
import br.com.uol.pagseguro.api.utils.logging.Log;
import br.com.uol.pagseguro.api.utils.logging.LoggerFactory;


/**
 * Factory to pre approval
 *
 * @author PagSeguro Internet Ltda.
 */
public class PreApprovalsResource {

  private static final Log LOGGER = LoggerFactory.getLogger(PreApprovalsResource.class.getName());

  private static final PreApprovalRegistrationV2MapConverter PRE_APPROVAL_REGISTRATION_MC =
      new PreApprovalRegistrationV2MapConverter();

  private static final PreApprovalChargingV2MapConverter PRE_APPROVAL_CHARGING_MC =
      new PreApprovalChargingV2MapConverter();

  private static final PreApprovalCancellationV2MapConverter PRE_APPROVAL_CANCELLATION_MC =
      new PreApprovalCancellationV2MapConverter();

  private static final PreApprovalSubscriptionV2MapConverter PRE_APPROVAL_SUBSCRIPTION_MC =
	      new PreApprovalSubscriptionV2MapConverter();

  private final PagSeguro pagSeguro;
  private final HttpClient httpClient;

  public PreApprovalsResource(PagSeguro pagSeguro, HttpClient httpClient) {
    this.pagSeguro = pagSeguro;
    this.httpClient = httpClient;
  }

  /**
   * Pre Approval Registration
   *
   * @param preApprovalRegistrationBuilder Builder for Pre Approval Registration
   * @return Response of pre approval registration
   * @see PreApprovalRegistration
   * @see RegisteredPreApproval
   */
  public RegisteredPreApproval register(
      Builder<PreApprovalRegistration> preApprovalRegistrationBuilder) {
    return register(preApprovalRegistrationBuilder.build());
  }

  /**
   * Pre Approval Registration
   *
   * @param preApprovalRegistration Pre Approval Registration
   * @return Response of pre approval registration
   * @see PreApprovalRegistration
   * @see RegisteredPreApproval
   */
  public RegisteredPreApproval register(PreApprovalRegistration preApprovalRegistration) {
    LOGGER.info("Iniciando registro pre approval");
    LOGGER.info("Convertendo valores");
    final RequestMap map = PRE_APPROVAL_REGISTRATION_MC.convert(preApprovalRegistration);
    LOGGER.info("Valores convertidos");
    final HttpResponse response;
    try {
      LOGGER.debug(String.format("Parametros: %s", map));
      response = httpClient.execute(HttpMethod.POST, String.format(Endpoints.PRE_APPROVAL_REQUEST,
          pagSeguro.getHost()), null, map.toHttpRequestBody(CharSet.ENCODING_ISO));
      LOGGER.debug(String.format("Resposta: %s", response.toString()));
    } catch (IOException e) {
      LOGGER.error("Erro ao executar registro pre approval");
      throw new PagSeguroLibException(e);
    }
    LOGGER.info("Parseando XML de resposta");
    RegisterPreApprovalResponseXML registeredPreApproval = response.parseXMLContent(pagSeguro,
        RegisterPreApprovalResponseXML.class);
    LOGGER.info("Parseamento finalizado");
    LOGGER.info("Registro pre approval finalizado");
    return registeredPreApproval;
  }

  /**
   * Pre Approval Cancellation
   *
   * @param preApprovalCancellationBuilder Builder for Pre Approval Cancellation
   * @return Response of Pre Approval Cancellation
   * @see PreApprovalCancellation
   * @see CancelledPreApproval
   */
  public CancelledPreApproval cancel(
      Builder<PreApprovalCancellation> preApprovalCancellationBuilder) {
    return cancel(preApprovalCancellationBuilder.build());
  }

  /**
   * Pre Approval Cancellation
   *
   * @param preApprovalCancellation Pre Approval Cancellation
   * @return Response of Pre Approval Cancellation
   * @see PreApprovalCancellation
   * @see CancelledPreApproval
   */
  public CancelledPreApproval cancel(PreApprovalCancellation preApprovalCancellation) {
    LOGGER.info("Iniciando cancelamento pre approval");
    LOGGER.info("Convertendo valores");
    final RequestMap map = PRE_APPROVAL_CANCELLATION_MC.convert(preApprovalCancellation);
    LOGGER.info("Valores convertidos");
    final HttpResponse response;
    try {
      LOGGER.debug(String.format("Parametros: preApprovalCode:%s, %s",
          preApprovalCancellation.getCode(), map));
      response = httpClient.execute(HttpMethod.GET, String.format(Endpoints.PRE_APPROVAL_CANCEL,
          pagSeguro.getHost(), preApprovalCancellation.getCode(),
          map.toUrlEncode(CharSet.ENCODING_UTF)), null, null);
      LOGGER.info(String.format("Resposta: %s", response.toString()));
    } catch (IOException e) {
      LOGGER.error("Erro ao executar cancelamento pre approval");
      throw new PagSeguroLibException(e);
    }
    LOGGER.info("Parseando XML de resposta");
    CancelPreApprovalResponseXML cancelledPreApproval = response.parseXMLContent(pagSeguro,
        CancelPreApprovalResponseXML.class);
    LOGGER.info("Parseamento finalizado");
    LOGGER.info("Cancelamento pre approval finalizado");
    return cancelledPreApproval;
  }

  /**
   * Pre Approval Search
   *
   * @return Factory to pre approval search
   * @see PreApprovalSearchResource
   */
  public PreApprovalSearchResource search() {
    return new PreApprovalSearchResource(pagSeguro, httpClient);
  }

  /**
   * Pre Approval Charging
   *
   * @param preApprovalChargingBuilder Builder for Pre Approval Charging
   * @return Response of Pre Approval Charging
   * @see PreApprovalCharging
   * @see ChargedPreApproval
   */
  public ChargedPreApproval charge(Builder<PreApprovalCharging> preApprovalChargingBuilder) {
    return charge(preApprovalChargingBuilder.build());
  }

  /**
   * Pre Approval Charging
   *
   * @param preApprovalCharging Pre Approval Charging
   * @return Response of Pre Approval Charging
   * @see PreApprovalCharging
   * @see ChargedPreApproval
   */
  public ChargedPreApproval charge(PreApprovalCharging preApprovalCharging) {
    LOGGER.info("Iniciando cobranca");
    LOGGER.info("Convertendo valores");
    final RequestMap map = PRE_APPROVAL_CHARGING_MC.convert(preApprovalCharging);
    LOGGER.info("Valores convertidos");
    final HttpResponse response;
    try {
      LOGGER.debug(String.format("Parametros: %s", map));
      response = httpClient.execute(HttpMethod.POST, String.format(Endpoints.PRE_APPROVAL_CHARGE,
          pagSeguro.getHost()), null, map.toHttpRequestBody(CharSet.ENCODING_ISO));
      LOGGER.debug(String.format("Resposta: %s", response.toString()));
    } catch (IOException e) {
      LOGGER.error("Erro ao executar cobranca");
      throw new PagSeguroLibException(e);
    }
    LOGGER.info("Parseando XML de resposta");
    ChargePreApprovalResponseXML chargedPreApproval = response.parseXMLContent(pagSeguro,
        ChargePreApprovalResponseXML.class);
    LOGGER.info("Parseamento finalizado");
    LOGGER.info("Cobranca finalizada");
    return chargedPreApproval;
  }

  /**
   * Pre Approval Subscription
   *
   * @param subscription Pre Approval Subscription
   * @return Response of Pre Approval Subscribed
   * @see PreApprovalSubscription
   * @see SubscribedPreApproval
   */
  public SubscribedPreApproval subscribe(PreApprovalSubscription subscription){
	  LOGGER.info("Iniciando adesao");
	    LOGGER.info("Convertendo valores");
	    final RequestMap map = PRE_APPROVAL_SUBSCRIPTION_MC.convert(subscription);
	    LOGGER.info("Valores convertidos");
	    final HttpResponse response;
	    try {
	      LOGGER.debug(String.format("Parametros: %s", map));
	      response = httpClient.execute(HttpMethod.POST, String.format(Endpoints.PRE_APPROVAL_SUBSCRIBE,
	          pagSeguro.getHost()), null, map.toHttpRequestBody(CharSet.ENCODING_ISO));
	      LOGGER.debug(String.format("Resposta: %s", response.toString()));
	    } catch (IOException e) {
	      LOGGER.error("Erro ao executar adesao");
	      throw new PagSeguroLibException(e);
	    }
	    LOGGER.info("Parseando XML de resposta");
	    SubscribedPreApprovalResponseXML subscribedPreApproval = response.parseXMLContent(pagSeguro,
	    		SubscribedPreApprovalResponseXML.class);
	    LOGGER.info("Parseamento finalizado");
	    LOGGER.info("Cobranca finalizada");
	    return subscribedPreApproval;
  }

  public SubscribedPreApproval subscribe(Builder<PreApprovalSubscription> subscription){
	  return subscribe(subscription.build());
  }

  /**
   * Pre Approval Subscription with body in JSON Object.
   * As the above method PreApprovalsResource{@link #subscribe(PreApprovalSubscription)} is NOT
   * working (actually, status 405), souding as not implemented in V2. The URL for
   * PRE_APPROVAL_SUBSCRIBE was altered, removing the prefix "v2" and V1 clearly doesn' support
   * content/body in form format. This method was written as an alternative until that get done.
   *
   * @param subscription Pre Approval Subscription
   * @return Response of Pre Approval Subscribed
   * @see PreApprovalSubscription
   * @see SubscribedPreApproval
   */
  public SubscribedPreApproval subscribeJson(PreApprovalSubscription subscription){
    LOGGER.info("Iniciando adesao com body em json");
    final HttpResponse response;
    try {
      response = httpClient.execute(HttpMethod.POST, String.format(Endpoints.PRE_APPROVAL_SUBSCRIBE,
              pagSeguro.getHost()), getSubscribeJsonHeaders(), getSubscribeJsonBody(subscription));
      LOGGER.debug(String.format("Resposta: %s", response.toString()));
    } catch (IOException e) {
      LOGGER.error("Erro ao executar adesao");
      throw new PagSeguroLibException(e);
    }
    LOGGER.info("Parseando XML de resposta");
    SubscribedPreApprovalResponseXML subscribedPreApproval = response.parseXMLContent(pagSeguro,
            SubscribedPreApprovalResponseXML.class);
    LOGGER.info("Parseamento finalizado");
    LOGGER.info("Cobranca finalizada");
    return subscribedPreApproval;
  }

  public SubscribedPreApproval subscribeJson(Builder<PreApprovalSubscription> subscription){
    return subscribeJson(subscription.build());
  }

  private static Map<String, String> getSubscribeJsonHeaders(){
    Map<String, String> headers = new HashMap();
    headers.put("Content-type", "application/json");
    headers.put("Accept", "application/vnd.pagseguro.com.br.v3+xml;charset=ISO-8859-1");
    return headers;
  }

  private static HttpRequestBody getSubscribeJsonBody(PreApprovalSubscription subscription){
    HttpRequestBody body = new HttpRequestBody(
            String.format("application/json; charset=%s", CharSet.ENCODING_ISO),
            parse2JsonString(subscription),
            CharSet.ENCODING_ISO);
    return body;
  }

  private static String parse2JsonString(PreApprovalSubscription subscription) {
    String s = null;
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));

    /* WORKAROUND
     Creditcard has billingAddress object but actually api, wants it as a reference inside holder.
     Probably a difference between v1 and v2? Lets see it later...
      */
    JsonNode node = mapper.valueToTree(subscription);
    JsonNode billingAddress = ((ObjectNode) node.get("paymentMethod").get("creditCard")).remove("billingAddress");
    ((ObjectNode) node.get("paymentMethod")).put("type", "CREDITCARD");
    ((ObjectNode) node.get("paymentMethod").get("creditCard").get("holder")).set("billingAddress", billingAddress);

    try {
      s = mapper.writeValueAsString(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return s;
  }
}
