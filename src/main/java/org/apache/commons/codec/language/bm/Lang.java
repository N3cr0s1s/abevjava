package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class Lang {
   private static final Map<NameType, Lang> Langs = new EnumMap(NameType.class);
   private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
   private final Languages languages;
   private final List<Lang.LangRule> rules;

   public static Lang instance(NameType nameType) {
      return (Lang)Langs.get(nameType);
   }

   public static Lang loadFromResource(String languageRulesResourceName, Languages languages) {
      List<Lang.LangRule> rules = new ArrayList();
      InputStream lRulesIS = Lang.class.getClassLoader().getResourceAsStream(languageRulesResourceName);
      if (lRulesIS == null) {
         throw new IllegalStateException("Unable to resolve required resource:org/apache/commons/codec/language/bm/lang.txt");
      } else {
         Scanner scanner = new Scanner(lRulesIS, "UTF-8");
         boolean inExtendedComment = false;

         while(scanner.hasNextLine()) {
            String rawLine = scanner.nextLine();
            String line = rawLine;
            if (inExtendedComment) {
               if (rawLine.endsWith("*/")) {
                  inExtendedComment = false;
               }
            } else if (rawLine.startsWith("/*")) {
               inExtendedComment = true;
            } else {
               int cmtI = rawLine.indexOf("//");
               if (cmtI >= 0) {
                  line = rawLine.substring(0, cmtI);
               }

               line = line.trim();
               if (line.length() != 0) {
                  String[] parts = line.split("\\s+");
                  if (parts.length != 3) {
                     throw new IllegalArgumentException("Malformed line '" + rawLine + "' in language resource '" + languageRulesResourceName + "'");
                  }

                  Pattern pattern = Pattern.compile(parts[0]);
                  String[] langs = parts[1].split("\\+");
                  boolean accept = parts[2].equals("true");
                  rules.add(new Lang.LangRule(pattern, new HashSet(Arrays.asList(langs)), accept));
               }
            }
         }

         return new Lang(rules, languages);
      }
   }

   private Lang(List<Lang.LangRule> rules, Languages languages) {
      this.rules = Collections.unmodifiableList(rules);
      this.languages = languages;
   }

   public String guessLanguage(String text) {
      Languages.LanguageSet ls = this.guessLanguages(text);
      return ls.isSingleton() ? ls.getAny() : "any";
   }

   public Languages.LanguageSet guessLanguages(String input) {
      String text = input.toLowerCase(Locale.ENGLISH);
      Set<String> langs = new HashSet(this.languages.getLanguages());
      Iterator i$ = this.rules.iterator();

      while(i$.hasNext()) {
         Lang.LangRule rule = (Lang.LangRule)i$.next();
         if (rule.matches(text)) {
            if (rule.acceptOnMatch) {
               langs.retainAll(rule.languages);
            } else {
               langs.removeAll(rule.languages);
            }
         }
      }

      Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
      return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
   }

   static {
      NameType[] arr$ = NameType.values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         NameType s = arr$[i$];
         Langs.put(s, loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(s)));
      }

   }

   private static final class LangRule {
      private final boolean acceptOnMatch;
      private final Set<String> languages;
      private final Pattern pattern;

      private LangRule(Pattern pattern, Set<String> languages, boolean acceptOnMatch) {
         this.pattern = pattern;
         this.languages = languages;
         this.acceptOnMatch = acceptOnMatch;
      }

      public boolean matches(String txt) {
         return this.pattern.matcher(txt).find();
      }

      // $FF: synthetic method
      LangRule(Pattern x0, Set x1, boolean x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
