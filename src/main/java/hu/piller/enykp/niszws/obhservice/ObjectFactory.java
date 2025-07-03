package hu.piller.enykp.niszws.obhservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _ObhServiceResponse_QNAME = new QName("http://nisz.hu/obhservice/1.0", "obhServiceResponse");
   private static final QName _ObhServiceRequest_QNAME = new QName("http://nisz.hu/obhservice/1.0", "obhServiceRequest");

   public ObhServiceResponseType createObhServiceResponseType() {
      return new ObhServiceResponseType();
   }

   public ObhServiceRequestType createObhServiceRequestType() {
      return new ObhServiceRequestType();
   }

   public Document createDocument() {
      return new Document();
   }

   public Error createError() {
      return new Error();
   }

   public SignedDocument createSignedDocument() {
      return new SignedDocument();
   }

   public ObhServiceResponseType.Documents createObhServiceResponseTypeDocuments() {
      return new ObhServiceResponseType.Documents();
   }

   public ObhServiceRequestType.Documents createObhServiceRequestTypeDocuments() {
      return new ObhServiceRequestType.Documents();
   }

   @XmlElementDecl(
      namespace = "http://nisz.hu/obhservice/1.0",
      name = "obhServiceResponse"
   )
   public JAXBElement<ObhServiceResponseType> createObhServiceResponse(ObhServiceResponseType var1) {
      return new JAXBElement(_ObhServiceResponse_QNAME, ObhServiceResponseType.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://nisz.hu/obhservice/1.0",
      name = "obhServiceRequest"
   )
   public JAXBElement<ObhServiceRequestType> createObhServiceRequest(ObhServiceRequestType var1) {
      return new JAXBElement(_ObhServiceRequest_QNAME, ObhServiceRequestType.class, (Class)null, var1);
   }
}
