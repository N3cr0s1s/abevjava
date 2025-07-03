package hu.piller.kripto.keys;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Hashtable;

public class KeyWrapperFilter {
   public static final String PROP_TYPE = "type";
   public static final String PROP_ALIAS = "alias";
   public static final String PROP_STORE_LOCATION = "storelocation";
   public static final String PROP_STORE_TYPE = "storetype";
   public static final String PROP_CREATED_BEFORE = "createdbefore";
   public static final String PROP_CREATED_AFTER = "createdafter";
   public static final String PROP_CREATION_DATE = "creationdate";
   public static final String PROP_SELECTED = "selected";
   private int type = -1;
   private String alias;
   private String storeLocation;
   private int storeType = -1;
   private Date createdBefore;
   private Date createdAfter;
   private Date creationDate;
   private int selected = -1;

   public KeyWrapperFilter() {
   }

   public KeyWrapperFilter(Hashtable props) {
      Object prop = props.get("type");
      if (prop != null && prop instanceof Integer) {
         this.setType((Integer)prop);
      }

      prop = props.get("alias");
      if (prop != null && prop instanceof String) {
         this.setAlias((String)prop);
      }

      prop = props.get("storelocation");
      if (prop != null && prop instanceof String) {
         this.setStoreLocation((String)prop);
      }

      prop = props.get("storetype");
      if (prop != null && prop instanceof Integer) {
         this.setStoreType((Integer)prop);
      }

      prop = props.get("createdafter");
      if (prop != null && prop instanceof Date) {
         this.setCreatedAfter((Date)prop);
      }

      prop = props.get("createdbefore");
      if (prop != null && prop instanceof Date) {
         this.setCreatedBefore((Date)prop);
      }

      prop = props.get("creationdate");
      if (prop != null && prop instanceof Date) {
         this.setCreationDate((Date)prop);
      }

      prop = props.get("selected");
      if (prop != null && prop instanceof Integer) {
         this.setSelected((Integer)prop);
      }

   }

   public boolean accept(KeyWrapper kw) {
      return (this.type == -1 || this.type == kw.getType()) && (this.alias == null || this.alias.length() == 0 || this.alias.trim().equalsIgnoreCase(kw.getAlias().trim()) && (this.storeLocation == null || this.storeLocation.length() == 0 || this.storeLocation.trim().equalsIgnoreCase(kw.getStoreLocation().trim()))) && (this.storeType == -1 || this.storeType == kw.getStoreType()) && (this.creationDate == null || kw.getCreationDate() == null || this.creationDate.compareTo(kw.getCreationDate()) == 0) && (this.createdBefore == null || kw.getCreationDate() == null || this.createdBefore.after(kw.getCreationDate())) && (this.createdAfter == null || kw.getCreationDate() == null || this.createdAfter.before(kw.getCreationDate())) && (this.selected == -1 || this.selected == 0 && !kw.isSelected());
   }

   public void setType(int type) {
      if (type != 1 && type != 0 && type != 2 && type != 3 && type != 4) {
         throw new InvalidParameterException();
      } else {
         this.type = type;
      }
   }

   public void setAlias(String alias) {
      if (alias != null && alias.trim().length() > 0) {
         this.alias = alias;
      } else {
         throw new InvalidParameterException();
      }
   }

   public void setStoreLocation(String storeLocation) {
      if (storeLocation != null && storeLocation.length() > 0) {
         this.storeLocation = storeLocation;
      } else {
         throw new InvalidParameterException();
      }
   }

   public void setStoreType(int storeType) {
      if (StoreManager.TYPES.containsKey(new Integer(storeType))) {
         this.storeType = storeType;
      } else {
         throw new InvalidParameterException();
      }
   }

   public void setCreatedBefore(Date createdBefore) {
      if (createdBefore == null || this.createdAfter != null && !this.createdAfter.before(createdBefore)) {
         throw new InvalidParameterException();
      } else {
         this.createdBefore = createdBefore;
      }
   }

   public void setCreatedAfter(Date createdAfter) {
      if (createdAfter == null || this.createdBefore != null && !createdAfter.before(this.createdBefore)) {
         throw new InvalidParameterException();
      } else {
         this.createdAfter = createdAfter;
      }
   }

   public void setCreationDate(Date creationDate) {
      if (creationDate != null) {
         this.creationDate = creationDate;
      } else {
         throw new InvalidParameterException();
      }
   }

   public void setSelected(int selected) {
      if (-1 <= selected && selected <= 1) {
         this.selected = selected;
      } else {
         throw new InvalidParameterException();
      }
   }
}
