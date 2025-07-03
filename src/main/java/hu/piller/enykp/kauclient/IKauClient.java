package hu.piller.enykp.kauclient;

import java.util.Map;

public interface IKauClient {
   KauResult authenticate(Map<String, String> var1, String var2, String var3) throws KauClientException;
}
