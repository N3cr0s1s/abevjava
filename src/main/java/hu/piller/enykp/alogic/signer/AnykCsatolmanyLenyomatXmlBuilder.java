package hu.piller.enykp.alogic.signer;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class AnykCsatolmanyLenyomatXmlBuilder {
   public String createXmlText(AnykCsatolmanyLenyomat var1) throws Exception {
      Document var2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element var3 = var2.createElementNS("http://nisz.hu/anykAttachment/1.0", "document");
      var2.appendChild(var3);
      var3.appendChild(this.createElement(var2, "fileName", var1.getFileName()));
      var3.appendChild(this.createElement(var2, "hashAlgorithm", "SHA-256"));
      var3.appendChild(this.createElement(var2, "hash", var1.getHash()));
      return this.getXmlTextFromDocument(var2);
   }

   private Element createElement(Document var1, String var2, String var3) {
      Element var4 = var1.createElement(var2);
      var4.appendChild(var1.createTextNode(var3));
      return var4;
   }

   private String getXmlTextFromDocument(Document var1) throws Exception {
      StringWriter var2 = new StringWriter();
      Transformer var3 = TransformerFactory.newInstance().newTransformer();
      var3.setOutputProperty("encoding", "UTF-8");
      var3.setOutputProperty("omit-xml-declaration", "yes");
      var3.transform(new DOMSource(var1), new StreamResult(var2));
      return var2.toString();
   }
}
