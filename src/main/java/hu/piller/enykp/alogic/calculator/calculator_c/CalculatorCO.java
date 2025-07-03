package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IResult;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

public class CalculatorCO implements ICommandObject {
   private Hashtable parameters;
   private Calculator calculator;
   private String command;
   private IResult result;
   private String param;
   private static final String[] cmds = new String[]{"calculator.evaluateExpression", "calculator.listUsedFunctions"};
   private static final Vector cmd_list;
   private static final Vector cmd_list_hlp;

   public CalculatorCO(Calculator var1) {
      this.calculator = var1;
   }

   public void execute() {
      if (cmds[0].equalsIgnoreCase(this.command)) {
         if (this.calculator != null) {
            Object var1 = this.calculator.calculateExpression(this.param);
            this.result.setResult(var1);
         }
      } else if (cmds[1].equalsIgnoreCase(this.command)) {
         Functions var5 = (Functions)FnFactory.createFunctionList();
         if (var5 != null) {
            Iterator var2 = var5.getIterator();
            if (var2 != null) {
               Vector var4 = new Vector(512, 4096);

               while(var2.hasNext()) {
                  Entry var3 = (Entry)var2.next();
                  var4.add(var3.getKey());
               }

               this.result.setResult(var4);
            }
         }
      }

   }

   public void setParameters(Hashtable var1) {
      this.parameters = var1;
      this.command = null;
      this.result = null;
      this.param = null;
      if (var1 != null) {
         Object var2 = var1.get("command");
         this.command = var2 == null ? null : var2.toString();
         var2 = var1.get("result");
         this.result = var2 instanceof IResult ? (IResult)var2 : null;
         var2 = var1.get(new Integer(0));
         this.param = var2 == null ? null : var2.toString();
      }

   }

   public ICommandObject copy() {
      return new CalculatorCO(this.calculator);
   }

   public boolean isCommandIdentical(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.startsWith(cmds[0])) {
            if (var1.split(" ").length > 1) {
               return true;
            }
         } else if (var1.equalsIgnoreCase(cmds[1])) {
            return true;
         }
      }

      return false;
   }

   public Vector getCommandList() {
      return cmd_list_hlp;
   }

   public Hashtable getCommandHelps() {
      return null;
   }

   public Object getState(Object var1) {
      return null;
   }

   static {
      cmd_list = new Vector(Arrays.asList(cmds));
      cmd_list_hlp = new Vector(Arrays.asList(cmds[0] + " <exp>", cmds[1]));
   }
}
