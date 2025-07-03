package hu.piller.enykp.alogic.masterdata.envelope.model;

import hu.piller.enykp.alogic.masterdata.envelope.provider.addresses.AddressesProvider;
import hu.piller.enykp.alogic.masterdata.envelope.provider.masterdata.EnvelopeMasterData;
import hu.piller.enykp.alogic.masterdata.envelope.provider.masterdata.MasterDataProvider;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.gui.model.BookModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class EnvelopeModel {
   private AddressesProvider ap = new AddressesProvider();
   private boolean orgSelectable;
   int shireCode = -1;
   private AddressOpt feladoAllandoCim;
   private AddressOpt feladoLevelezesiCim;
   private ArrayList<OrgModel> szervezetek = new ArrayList();
   private String currOrgId;
   private ArrayList<Address> szervezetCimei = new ArrayList();

   public EnvelopeModel(HashMap<EnvelopeMasterData, String> var1) {
      this.orgSelectable = true;
      this.buildModel(var1, (String)null);
   }

   public EnvelopeModel(BookModel var1) {
      this.orgSelectable = false;
      String var2 = (String)((Hashtable)var1.get_templateheaddata().get("docinfo")).get("org");
      HashMap var3 = (new MasterDataProvider()).getMasterDataFromCurrentForm(var1);
      this.buildModel(var3, var2);
   }

   public boolean isOrgSelectable() {
      return this.orgSelectable;
   }

   public String getCurrOrgId() {
      return this.currOrgId;
   }

   public AddressOpt getFeladoAllandoCim() {
      return this.feladoAllandoCim;
   }

   public AddressOpt getFeladoLevelezesiCim() {
      return this.feladoLevelezesiCim;
   }

   public ArrayList<Address> getSzervezetCimei() {
      return this.szervezetCimei;
   }

   public ArrayList<AddressOpt> getCimzettForFeladoAllandoCim() {
      return this.getCimzett(this.feladoAllandoCim);
   }

   public ArrayList<AddressOpt> getCimzettForFeladoLevelezesiCim() {
      return this.getCimzett(this.feladoLevelezesiCim);
   }

   public ArrayList<AddressOpt> getCimzett(AddressOpt var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3;
      Address var4;
      if (this.hasShireCode() && "NAV".equals(this.currOrgId)) {
         var3 = this.getSzervezetCimei().iterator();

         while(var3.hasNext()) {
            var4 = (Address)var3.next();
            if (var4.getShire() == this.shireCode) {
               var2.addAll(var4.getAddressOpts());
               break;
            }
         }
      }

      if (var2.isEmpty()) {
         var3 = this.szervezetCimei.iterator();

         while(var3.hasNext()) {
            var4 = (Address)var3.next();
            if (var4.isForZip(var1.getZip())) {
               var2.addAll(var4.getAddressOpts());
            }
         }
      }

      return var2;
   }

   public ArrayList<OrgModel> getSzervezetek() {
      return this.szervezetek;
   }

   public void updateCimzettListaForOrg(String var1) {
      ArrayList var2 = (new AddressesProvider()).getAddressesByOrgId(var1);
      if (!var2.isEmpty()) {
         this.currOrgId = var1;
         this.szervezetCimei.clear();
         this.szervezetCimei.addAll(var2);
      }

   }

   public boolean hasShireCode() {
      return this.shireCode > -1;
   }

   private void buildModel(HashMap<EnvelopeMasterData, String> var1, String var2) {
      this.buildShireCodeFromAdoszam((String)var1.get(EnvelopeMasterData.TAXNUMBER));
      ArrayList var3 = this.createFeladoCimek(var1);
      this.feladoAllandoCim = (AddressOpt)var3.get(0);
      this.feladoLevelezesiCim = (AddressOpt)var3.get(1);
      Object var4 = OrgInfo.getInstance().getOrgNames();
      if (var4 != null) {
         Vector var5 = (Vector)((Object[])((Object[])var4))[0];
         Vector var6 = (Vector)((Object[])((Object[])var4))[2];

         for(int var7 = 0; var7 < var5.size(); ++var7) {
            if (var2 != null) {
               if (var2.equals(var5.get(var7))) {
                  this.szervezetek.add(new OrgModel((String)var5.get(var7), (String)var6.get(var7)));
                  this.szervezetCimei.addAll(this.ap.getAddressesByOrgId(var2));
                  this.currOrgId = var2;
               }
            } else {
               ArrayList var8 = this.ap.getAddressesByOrgId((String)var5.get(var7));
               if (var8.size() > 0) {
                  if (this.currOrgId == null) {
                     this.currOrgId = (String)var5.get(var7);
                     this.szervezetCimei.addAll(var8);
                  }

                  this.szervezetek.add(new OrgModel((String)var5.get(var7), (String)var6.get(var7)));
               }
            }
         }
      }

   }

   private void buildShireCodeFromAdoszam(String var1) {
      if (var1 != null && !var1.trim().equals("")) {
         String var2 = var1.substring(var1.length() - 2);
         this.shireCode = Integer.parseInt(var2);
         if (this.shireCode > 2 && this.shireCode < 20) {
            this.shireCode += 20;
         }
      }

   }

   private ArrayList<AddressOpt> createFeladoCimek(HashMap<EnvelopeMasterData, String> var1) {
      AddressOpt var2 = new AddressOpt();
      var2.setTitle(this.getNev(var1));
      var2.setTitleHint("Állandó cím");
      var2.setSettlement((String)var1.get(EnvelopeMasterData.S_SETTLEMENT));
      var2.setKozteruletCim(this.getAllandoUtca(var1));

      try {
         var2.setZip(Integer.parseInt((String)var1.get(EnvelopeMasterData.S_ZIP)));
      } catch (NumberFormatException var6) {
         var2.setZip(-1);
      }

      AddressOpt var3 = new AddressOpt();
      var3.setTitle(this.getNev(var1));
      var3.setTitleHint("Levelezési cím");
      var3.setSettlement((String)var1.get(EnvelopeMasterData.M_SETTLEMENT));
      var3.setKozteruletCim(this.getLevelezesiUtca(var1));

      try {
         var3.setZip(Integer.parseInt((String)var1.get(EnvelopeMasterData.M_ZIP)));
      } catch (NumberFormatException var5) {
         var3.setZip(-1);
      }

      ArrayList var4 = new ArrayList();
      var4.add(var2);
      var4.add(var3);
      return var4;
   }

   private String getNev(HashMap<EnvelopeMasterData, String> var1) {
      String var2 = "".equals(var1.get(EnvelopeMasterData.NAME)) ? (String)var1.get(EnvelopeMasterData.FNAME_TITLE) + " " + (String)var1.get(EnvelopeMasterData.FNAME) + " " + (String)var1.get(EnvelopeMasterData.LNAME) : (String)var1.get(EnvelopeMasterData.NAME);
      return var2;
   }

   private String getAllandoUtca(HashMap<EnvelopeMasterData, String> var1) {
      StringBuilder var2 = (new StringBuilder((String)var1.get(EnvelopeMasterData.S_PUBLICPLACE))).append(" ");
      if (!"".equals(var1.get(EnvelopeMasterData.S_PUBLICPLACETYPE))) {
         var2.append((String)var1.get(EnvelopeMasterData.S_PUBLICPLACETYPE)).append(" ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.S_HOUSENUMBER))) {
         var2.append((String)var1.get(EnvelopeMasterData.S_HOUSENUMBER)).append(" ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.S_BUILDING))) {
         var2.append((String)var1.get(EnvelopeMasterData.S_BUILDING)).append("ép. ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.S_STAIRCASE))) {
         var2.append((String)var1.get(EnvelopeMasterData.S_STAIRCASE)).append("lh. ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.S_LEVEL))) {
         var2.append((String)var1.get(EnvelopeMasterData.S_LEVEL)).append("/");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.S_DOOR))) {
         var2.append((String)var1.get(EnvelopeMasterData.S_DOOR));
      }

      return var2.toString();
   }

   private String getLevelezesiUtca(HashMap<EnvelopeMasterData, String> var1) {
      StringBuilder var2 = (new StringBuilder((String)var1.get(EnvelopeMasterData.M_PUBLICPLACE))).append(" ");
      if (!"".equals(var1.get(EnvelopeMasterData.M_PUBLICPLACETYPE))) {
         var2.append((String)var1.get(EnvelopeMasterData.M_PUBLICPLACETYPE)).append(" ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.M_HOUSENUMBER))) {
         var2.append((String)var1.get(EnvelopeMasterData.M_HOUSENUMBER)).append(" ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.M_BUILDING))) {
         var2.append((String)var1.get(EnvelopeMasterData.M_BUILDING)).append("ép. ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.M_STAIRCASE))) {
         var2.append((String)var1.get(EnvelopeMasterData.M_STAIRCASE)).append("lh. ");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.M_LEVEL))) {
         var2.append((String)var1.get(EnvelopeMasterData.M_LEVEL)).append("/");
      }

      if (!"".equals(var1.get(EnvelopeMasterData.M_DOOR))) {
         var2.append((String)var1.get(EnvelopeMasterData.M_DOOR));
      }

      return var2.toString();
   }
}
