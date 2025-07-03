package hu.piller.enykp.alogic.templateutils.blacklist;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
   public Blacklist createBlacklist() {
      return new Blacklist();
   }

   public Blacklist.Template createBlacklistTemplate() {
      return new Blacklist.Template();
   }
}
