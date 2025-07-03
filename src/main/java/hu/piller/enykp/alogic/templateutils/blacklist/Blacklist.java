package hu.piller.enykp.alogic.templateutils.blacklist;

import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "",
   propOrder = {"template"}
)
@XmlRootElement(
   name = "blacklist"
)
public class Blacklist {
   protected List<Blacklist.Template> template;
   @XmlAttribute(
      name = "validfrom"
   )
   @XmlSchemaType(
      name = "date"
   )
   protected XMLGregorianCalendar validfrom;
   @XmlAttribute(
      name = "validto"
   )
   @XmlSchemaType(
      name = "date"
   )
   protected XMLGregorianCalendar validto;
   @XmlAttribute(
      name = "application"
   )
   protected String application;

   public List<Blacklist.Template> getTemplate() {
      if (this.template == null) {
         this.template = new ArrayList();
      }

      return this.template;
   }

   public XMLGregorianCalendar getValidfrom() {
      return this.validfrom;
   }

   public void setValidfrom(XMLGregorianCalendar var1) {
      this.validfrom = var1;
   }

   public XMLGregorianCalendar getValidto() {
      return this.validto;
   }

   public void setValidto(XMLGregorianCalendar var1) {
      this.validto = var1;
   }

   public String getApplication() {
      return this.application;
   }

   public void setApplication(String var1) {
      this.application = var1;
   }

   public static Blacklist create(String var0) throws Exception {
      JAXBContext var1 = JAXBContext.newInstance(Blacklist.class);
      Unmarshaller var2 = var1.createUnmarshaller();
      SchemaFactory var3 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      Schema var4 = var3.newSchema(new File(Directories.getSchemasPath() + "/blacklist.xsd"));
      var2.setSchema(var4);
      return (Blacklist)var2.unmarshal(new ByteArrayInputStream(var0.getBytes("utf-8")));
   }

   public static void save(Blacklist var0) throws JAXBException {
      JAXBContext var1 = JAXBContext.newInstance(Blacklist.class);
      Marshaller var2 = var1.createMarshaller();
      var2.setProperty("jaxb.formatted.output", true);
      var2.marshal(var0, new File(Directories.getOrgresourcesPath() + "/blacklist.xml"));
   }

   public HashMap<String, Blacklist.Template> getCurrentBlacklistIds() {
      HashMap var1 = new HashMap();
      XMLGregorianCalendar var2 = this.getValidfrom();
      XMLGregorianCalendar var3 = this.getValidto();
      if (this.template == null) {
         return var1;
      } else {
         Iterator var4 = this.template.iterator();

         while(true) {
            Blacklist.Template var5;
            do {
               if (!var4.hasNext()) {
                  return var1;
               }

               var5 = (Blacklist.Template)var4.next();
            } while(var5.getFrom() != null && !var5.getFrom().isValid());

            var1.put(var5.getOrg() + "_" + var5.getTid(), var5);
         }
      }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(
      name = "",
      propOrder = {"message", "targetUrl"}
   )
   public static class Template {
      @XmlElement(
         required = true
      )
      protected String message;
      @XmlElement(
         name = "target_url",
         required = true
      )
      protected String targetUrl;
      @XmlAttribute(
         name = "tid",
         required = true
      )
      protected String tid;
      @XmlAttribute(
         name = "org",
         required = true
      )
      protected String org;
      @XmlAttribute(
         name = "from"
      )
      @XmlSchemaType(
         name = "date"
      )
      protected XMLGregorianCalendar from;

      public String getMessage() {
         return this.message == null ? "" : this.message;
      }

      public void setMessage(String var1) {
         this.message = var1;
      }

      public String getTargetUrl() {
         return this.targetUrl == null ? "" : this.targetUrl;
      }

      public void setTargetUrl(String var1) {
         this.targetUrl = var1;
      }

      public String getTid() {
         return this.tid;
      }

      public void setTid(String var1) {
         this.tid = var1;
      }

      public String getOrg() {
         return this.org;
      }

      public void setOrg(String var1) {
         this.org = var1;
      }

      public XMLGregorianCalendar getFrom() {
         return this.from;
      }

      public void setFrom(XMLGregorianCalendar var1) {
         this.from = var1;
      }
   }
}
