package uk.gov.companieshouse.model;

public final class Constants {

   private Constants() {
   }

   /* Dissolution */
   public static final String DISSOLUTION_KIND = "dissolution-request#dissolution-request";

   /* Payment */
   public static final String PAYMENT_KIND = "dissolution-request#payment";
   public static final String PAYMENT_ITEM_KIND = "dissolution-request#payment-details";
   public static final String PAYMENT_RESOURCE_KIND = "dissolution-request#dissolution-request";
   public static final String PAYMENT_DESCRIPTION = "Dissolution application";
   public static final String PAYMENT_DESCRIPTION_IDENTIFIER = "Dissolution application";
   public static final String PAYMENT_PRODUCT_TYPE = "Dissolution application";
   public static final String PAYMENT_AMOUNT = "8";
   public static final String PAYMENT_AVAILABLE_PAYMENT_METHOD = "credit-card";
   public static final String PAYMENT_CLASS_OF_PAYMENT = "data-maintenance";

   /* Email */
   public static final String EMAIL_APP_ID = "dissolution-api";
   public static final String EMAIL_TOPIC = "email-send";

   public static final String SUCCESSFUL_PAYMENT_EMAIL_SUBJECT = "Your application to strike off and dissolve a company has been submitted";
   public static final String SUCCESSFUL_PAYMENT_MESSAGE_TYPE = "dissolution_payment_confirmation";

   public static final String APPLICATION_ACCEPTED_EMAIL_SUBJECT = "Your application to strike off and dissolve a company has been accepted";
   public static final String APPLICATION_ACCEPTED_MESSAGE_TYPE = "dissolution_submission_accepted";

   public static final String APPLICATION_REJECTED_EMAIL_SUBJECT = "Your application to strike off and dissolve a company has been rejected";
   public static final String APPLICATION_REJECTED_MESSAGE_TYPE = "dissolution_submission_rejected";

   public static final String SIGNATORY_TO_SIGN_EMAIL_SUBJECT = "You need to sign the application to strike off and dissolve a company";
   public static final String SIGNATORY_TO_SIGN_MESSAGE_TYPE = "dissolution_signatory_to_sign";

   /* Headers */
   public static final String HEADER_AUTHORIZATION = "Authorization";
   public static final String HEADER_ACCEPT = "Accept";
   public static final String HEADER_CONTENT_TYPE = "Content-Type";

   public static final String CONTENT_TYPE_HTML = "text/html";
   public static final String CONTENT_TYPE_JSON = "application/json";
   public static final String CONTENT_TYPE_PDF = "application/pdf";
}
