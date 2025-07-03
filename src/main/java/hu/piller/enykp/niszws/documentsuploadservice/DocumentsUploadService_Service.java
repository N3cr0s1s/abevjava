package hu.piller.enykp.niszws.documentsuploadservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(
   name = "DocumentsUploadService",
   targetNamespace = "http://tarhely.gov.hu/anykgw2",
   wsdlLocation = "file:/D:/SANDBOX/qfkau/lib/classes/resources/niszws/DocumentsUploadService.wsdl"
)
public class DocumentsUploadService_Service extends Service {
   private static final URL DOCUMENTSUPLOADSERVICE_WSDL_LOCATION;
   private static final WebServiceException DOCUMENTSUPLOADSERVICE_EXCEPTION;
   private static final QName DOCUMENTSUPLOADSERVICE_QNAME = new QName("http://tarhely.gov.hu/anykgw2", "DocumentsUploadService");

   public DocumentsUploadService_Service() {
      super(__getWsdlLocation(), DOCUMENTSUPLOADSERVICE_QNAME);
   }

   public DocumentsUploadService_Service(WebServiceFeature... var1) {
      super(__getWsdlLocation(), DOCUMENTSUPLOADSERVICE_QNAME, var1);
   }

   public DocumentsUploadService_Service(URL var1) {
      super(var1, DOCUMENTSUPLOADSERVICE_QNAME);
   }

   public DocumentsUploadService_Service(URL var1, WebServiceFeature... var2) {
      super(var1, DOCUMENTSUPLOADSERVICE_QNAME, var2);
   }

   public DocumentsUploadService_Service(URL var1, QName var2) {
      super(var1, var2);
   }

   public DocumentsUploadService_Service(URL var1, QName var2, WebServiceFeature... var3) {
      super(var1, var2, var3);
   }

   @WebEndpoint(
      name = "DocumentsUploadServicePort"
   )
   public DocumentsUploadService getDocumentsUploadServicePort() {
      return (DocumentsUploadService)super.getPort(new QName("http://tarhely.gov.hu/anykgw2", "DocumentsUploadServicePort"), DocumentsUploadService.class);
   }

   @WebEndpoint(
      name = "DocumentsUploadServicePort"
   )
   public DocumentsUploadService getDocumentsUploadServicePort(WebServiceFeature... var1) {
      return (DocumentsUploadService)super.getPort(new QName("http://tarhely.gov.hu/anykgw2", "DocumentsUploadServicePort"), DocumentsUploadService.class, var1);
   }

   private static URL __getWsdlLocation() {
      if (DOCUMENTSUPLOADSERVICE_EXCEPTION != null) {
         throw DOCUMENTSUPLOADSERVICE_EXCEPTION;
      } else {
         return DOCUMENTSUPLOADSERVICE_WSDL_LOCATION;
      }
   }

   static {
      URL var0 = null;
      WebServiceException var1 = null;

      try {
         var0 = new URL("file:/D:/SANDBOX/qfkau/lib/classes/resources/niszws/DocumentsUploadService.wsdl");
      } catch (MalformedURLException var3) {
         var1 = new WebServiceException(var3);
      }

      DOCUMENTSUPLOADSERVICE_WSDL_LOCATION = var0;
      DOCUMENTSUPLOADSERVICE_EXCEPTION = var1;
   }
}
