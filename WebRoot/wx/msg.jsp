 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<script>
  alert("${msg}");

</script>
<c:if test="${url!=null}">
<script>
  parent.loadtable("${url}",1);
</script>
</c:if>