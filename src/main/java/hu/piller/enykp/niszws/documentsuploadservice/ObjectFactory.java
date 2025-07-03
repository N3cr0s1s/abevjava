package hu.piller.enykp.niszws.documentsuploadservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _AuthTokenRequest_QNAME = new QName("http://tarhely.gov.hu/anykgw2", "AuthTokenRequest");
   private static final QName _DocumentsUploadRequest_QNAME = new QName("http://tarhely.gov.hu/anykgw2", "DocumentsUploadRequest");
   private static final QName _AuthTokenResponse_QNAME = new QName("http://tarhely.gov.hu/anykgw2", "AuthTokenResponse");
   private static final QName _DocumentsUploadResponse_QNAME = new QName("http://tarhely.gov.hu/anykgw2", "DocumentsUploadResponse");

   public AuthTokenResponse createAuthTokenResponse() {
      return new AuthTokenResponse();
   }

   public DocumentsUploadResponse createDocumentsUploadResponse() {
      return new DocumentsUploadResponse();
   }

   public AuthTokenRequest createAuthTokenRequest() {
      return new AuthTokenRequest();
   }

   public DocumentsUploadRequest createDocumentsUploadRequest() {
      return new DocumentsUploadRequest();
   }

   public Document createDocument() {
      return new Document();
   }

   public OfficeAuthentication createOfficeAuthentication() {
      return new OfficeAuthentication();
   }

   public DocumentResult createDocumentResult() {
      return new DocumentResult();
   }

   public Base64Binary createBase64Binary() {
      return new Base64Binary();
   }

   public HexBinary createHexBinary() {
      return new HexBinary();
   }

   @XmlElementDecl(
      namespace = "http://tarhely.gov.hu/anykgw2",
      name = "AuthTokenRequest"
   )
   public JAXBElement<AuthTokenRequest> createAuthTokenRequest(AuthTokenRequest var1) {
      return new JAXBElement(_AuthTokenRequest_QNAME, AuthTokenRequest.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://tarhely.gov.hu/anykgw2",
      name = "DocumentsUploadRequest"
   )
   public JAXBElement<DocumentsUploadRequest> createDocumentsUploadRequest(DocumentsUploadRequest var1) {
      return new JAXBElement(_DocumentsUploadRequest_QNAME, DocumentsUploadRequest.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://tarhely.gov.hu/anykgw2",
      name = "AuthTokenResponse"
   )
   public JAXBElement<AuthTokenResponse> createAuthTokenResponse(AuthTokenResponse var1) {
      return new JAXBElement(_AuthTokenResponse_QNAME, AuthTokenResponse.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://tarhely.gov.hu/anykgw2",
      name = "DocumentsUploadResponse"
   )
   public JAXBElement<DocumentsUploadResponse> createDocumentsUploadResponse(DocumentsUploadResponse var1) {
      return new JAXBElement(_DocumentsUploadResponse_QNAME, DocumentsUploadResponse.class, (Class)null, var1);
   }
}
