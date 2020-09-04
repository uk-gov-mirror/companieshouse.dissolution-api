package uk.gov.companieshouse.service.payment;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.model.dto.payment.PaymentDescriptionValues;
import uk.gov.companieshouse.model.dto.payment.PaymentGetResponse;
import uk.gov.companieshouse.model.dto.payment.PaymentItem;
import uk.gov.companieshouse.model.dto.payment.PaymentLinks;
import uk.gov.companieshouse.model.enums.ApplicationType;

import java.util.List;

import static uk.gov.companieshouse.model.Constants.*;

@Service
public class PaymentService {

    public PaymentGetResponse get(String eTag, ApplicationType applicationType, String companyNumber) {
        PaymentGetResponse response = new PaymentGetResponse();
        PaymentLinks paymentLinks = new PaymentLinks();
        paymentLinks.setSelf("/dissolution-request/" + companyNumber + "/payment");
        paymentLinks.setDissolutionRequest("/dissolution-request/" + companyNumber);

        PaymentItem item = createPaymentItem(applicationType);

        response.setETag(eTag);
        response.setKind(PAYMENT_KIND);
        response.setLinks(paymentLinks);
        response.setItems(List.of(item));

        return response;
    }

    private PaymentItem createPaymentItem(ApplicationType applicationType) {
        PaymentItem item = new PaymentItem();

        item.setDescription(PAYMENT_DESCRIPTION);
        item.setDescriptionIdentifier(PAYMENT_DESCRIPTION_IDENTIFIER);
        item.setDescriptionValues(new PaymentDescriptionValues());
        item.setProductType(getProductTypeCode(applicationType));
        item.setAmount(PAYMENT_AMOUNT);
        item.setAvailablePaymentMethods(List.of(PAYMENT_AVAILABLE_PAYMENT_METHOD));
        item.setClassOfPayment(List.of(PAYMENT_CLASS_OF_PAYMENT));
        item.setKind(PAYMENT_ITEM_KIND);
        item.setResourceKind(PAYMENT_RESOURCE_KIND);

        return item;
    }

    private String getProductTypeCode(ApplicationType applicationType) {
        return applicationType == ApplicationType.DS01 ? PAYMENT_PRODUCT_TYPE_DS01 : PAYMENT_PRODUCT_TYPE_LLDS01;
    }
}