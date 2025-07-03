package hu.piller.enykp.niszws.obhservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(
   name = "ObhService",
   targetNamespace = "http://nisz.hu/obhservice/1.0",
   wsdlLocation = "file:/D:/SANDBOX/qfkau/lib/classes/resources/niszws/obhService.wsdl"
)
public class ObhService extends Service {
   private static final URL OBHSERVICE_WSDL_LOCATION;
   private static final WebServiceException OBHSERVICE_EXCEPTION;
   private static final QName OBHSERVICE_QNAME = new QName("http://nisz.hu/obhservice/1.0", "ObhService");

   public ObhService() {
      super(__getWsdlLocation(), OBHSERVICE_QNAME);
   }

   public ObhService(WebServiceFeature... var1) {
      super(__getWsdlLocation(), OBHSERVICE_QNAME, var1);
   }

   public ObhService(URL var1) {
      super(var1, OBHSERVICE_QNAME);
   }

   public ObhService(URL var1, WebServiceFeature... var2) {
      super(var1, OBHSERVICE_QNAME, var2);
   }

   public ObhService(URL var1, QName var2) {
      super(var1, var2);
   }

   public ObhService(URL var1, QName var2, WebServiceFeature... var3) {
      super(var1, var2, var3);
   }

   @WebEndpoint(
      name = "ObhServicePort"
   )
   public ObhServicePortType getObhServicePort() {
      return (ObhServicePortType)super.getPort(new QName("http://nisz.hu/obhservice/1.0", "ObhServicePort"), ObhServicePortType.class);
   }

   @WebEndpoint(
      name = "ObhServicePort"
   )
   public ObhServicePortType getObhServicePort(WebServiceFeature... var1) {
      return (ObhServicePortType)super.getPort(new QName("http://nisz.hu/obhservice/1.0", "ObhServicePort"), ObhServicePortType.class, var1);
   }

   private static URL __getWsdlLocation() {
      if (OBHSERVICE_EXCEPTION != null) {
         throw OBHSERVICE_EXCEPTION;
      } else {
         return OBHSERVICE_WSDL_LOCATION;
      }
   }

   static {
      URL var0 = null;
      WebServiceException var1 = null;

      try {
         var0 = new URL("file:/D:/SANDBOX/qfkau/lib/classes/resources/niszws/obhService.wsdl");
      } catch (MalformedURLException var3) {
         var1 = new WebServiceException(var3);
      }

      OBHSERVICE_WSDL_LOCATION = var0;
      OBHSERVICE_EXCEPTION = var1;
   }
}
