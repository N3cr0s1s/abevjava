package hu.piller.enykp.niszws.documentsuploadservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "DocumentsUploadService",
   targetNamespace = "http://tarhely.gov.hu/anykgw2"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface DocumentsUploadService {
   @WebMethod(
      operationName = "DocumentsUpload"
   )
   @WebResult(
      name = "DocumentsUploadResponse",
      targetNamespace = "http://tarhely.gov.hu/anykgw2",
      partName = "documentsUploadResponse"
   )
   DocumentsUploadResponse documentsUpload(@WebParam(name = "DocumentsUploadRequest",targetNamespace = "http://tarhely.gov.hu/anykgw2",partName = "documentsUploadRequest") DocumentsUploadRequest var1);
}
