 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<script>
  alert("${msg}");
  <% System.out.print("22222222222222222222222222222"); %>
  
  location.href="${pageContext.request.contextPath}/wxphone/followtask";
</script>