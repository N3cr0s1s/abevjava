package hu.piller.enykp.niszws.obhservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "ObhServicePortType",
   targetNamespace = "http://nisz.hu/obhservice/1.0"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface ObhServicePortType {
   @WebMethod(
      action = "barmilehet"
   )
   @WebResult(
      name = "obhServiceResponse",
      targetNamespace = "http://nisz.hu/obhservice/1.0",
      partName = "ObhServiceResponse"
   )
   ObhServiceResponseType signDocument(@WebParam(name = "obhServiceRequest",targetNamespace = "http://nisz.hu/obhservice/1.0",partName = "ObhServiceRequest") ObhServiceRequestType var1);
}
