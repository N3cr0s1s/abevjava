package hu.piller.enykp.niszws.authtokenservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "AuthTokenService",
   targetNamespace = "http://tarhely.gov.hu/anykgw2"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface AuthTokenService {
   @WebMethod(
      operationName = "GetAuthToken"
   )
   @WebResult(
      name = "AuthTokenResponse",
      targetNamespace = "http://tarhely.gov.hu/anykgw2",
      partName = "authTokenResponse"
   )
   AuthTokenResponse getAuthToken(@WebParam(name = "AuthTokenRequest",targetNamespace = "http://tarhely.gov.hu/anykgw2",partName = "authTokenRequest") AuthTokenRequest var1);
}
