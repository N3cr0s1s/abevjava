package hu.piller.enykp.alogic.signer;

import java.io.File;
import java.util.List;

public interface ISigner {
   void sign(List<File> var1) throws SignerException;

   void signAsic(List<File> var1) throws SignerException;
}
