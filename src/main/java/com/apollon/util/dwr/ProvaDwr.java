package com.apollon.util.dwr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
 
import javax.servlet.ServletContext;
 
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

import com.apollon.webapp.util.bean.MyBeanDwr;
 
/**
 * 
 * @author matte
 * inserire nella jsp le seguenti lib
 
<script src="<c:url value="/dwr/engine.js"/>"></script>
<script src="<c:url value="/dwr/util.js"/>"></script>
<script src="<c:url value="/dwr/interface/ReverseClass.js"/>"></script>

<script src="https://www.apollotransfert.com/dwr/engine.js"></script>
<script src="https://www.apollotransfert.com/dwr/util.js"></script>
<script src="https://www.apollotransfert.com/dwr/interface/ReverseClass.js"></script>

 */
public class ProvaDwr {
   private int count = 0;
   WebContext webContext = WebContextFactory.get();
   ServletContext servletContext = webContext.getServletContext();
   ServerContext serverContext = ServerContextFactory.get(servletContext);
 
   /**
   * This method continually calls the update method utill the 
   * for loop completes
   */
   public void callReverseDWR() {
      System.out.println(" Ur in callReverseDWR ");
      try { 
      for (int i = 0; i < 10; i++) {
         update();
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      } catch (Exception e) {
         System.out.println("Error in callReverseDWR");
         e.printStackTrace(); 
      }
   }
 
   /**
   * This method updates ReversePage.jsp &lt;ul id=&quot;updates&quot;&gt; 
   * using dwr reverse ajax
   */
   public void update() {
      try { 
         List<MyBeanDwr> messages = new ArrayList<MyBeanDwr>();
         messages.add(new MyBeanDwr("testing" + count++)); 
         Collection sessions = serverContext.getScriptSessionsByPage("/ReverseAjaxwithDWR/ReversePage.jsp"); 
         Util utilAll = new Util(sessions);
         utilAll.addOptions("updates", messages, "value"); 
      } catch (Exception e) {
         System.out.println("Error in Update"); 
         e.printStackTrace(); 
      }
   }
}
