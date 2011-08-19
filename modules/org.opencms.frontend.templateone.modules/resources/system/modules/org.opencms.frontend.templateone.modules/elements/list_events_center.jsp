<%@ page session="false" buffer="none" import="org.opencms.util.*, org.opencms.frontend.templateone.modules.*" %><%--
--%><%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %><%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%

CmsTemplateModules cms = new CmsTemplateModules(pageContext, request, response);

// get currently active locale to initialize message bundle
String locale = cms.getRequestContext().getLocale().toString();
pageContext.setAttribute("locale", locale);

boolean showNumber = Integer.parseInt(request.getParameter("elementcount")) == Integer.MAX_VALUE;
pageContext.setAttribute("shownumber", "" + showNumber);

%><cms:contentload collector="%(param.collector)" param="%(param.folder)event_%(number).html|event|%(param.elementcount)" preload="true"><%--

--%><cms:contentinfo var="contentInfoOuter" scope="request" /><%--
--%><c:if test="${! contentInfoOuter.emptyResult}"><%--

--%><fmt:setLocale value="${locale}" /><%--
--%><fmt:bundle basename="org/opencms/frontend/templateone/modules/workplace"><%--

--%><cms:contentload editable="true" pageSize="%(param.count)" pageIndex="%(param.pageIndex)" pageNavLength="10"><%--
--%><cms:contentinfo var="contentInfo" scope="request" /><%--

--%><c:if test="${(contentInfo.resultIndex % contentInfo.pageSize) == 1}"><%--

	result size: <cms:contentinfo value="${contentInfo.resultSize}" />
	page size: <cms:contentinfo value="${contentInfo.pageSize}" />
	page count: <cms:contentinfo value="${contentInfo.pageCount}" />
	page index: <cms:contentinfo value="${contentInfo.pageIndex}" />

--%><c:if test="${shownumber == 'true' && contentInfo.pageCount > 1}"><p><fmt:message key="navbar.center.resultsize" />: <cms:contentinfo value="${contentInfo.resultSize}" /></c:if><%--
--%><c:if test="${contentInfo.resultSize > contentInfo.pageSize}">
&nbsp;|&nbsp;
<fmt:message key="navbar.center.pagelinks" />:&nbsp;
<c:forEach var="i" begin="${contentInfo.pageNavStartIndex}" end="${contentInfo.pageNavEndIndex}">
<c:choose>
<c:when test="${(i == param.pageIndex) || (i == 1 && param.pageIndex == null)}">
[<c:out value="${i}" />]&nbsp;
</c:when>
<c:otherwise>
[<a href="<cms:link><%= cms.getRequestContext().getUri() %>?pageIndex=<c:out value="${i}" /></cms:link>"><c:out value="${i}" /></a>]&nbsp;
</c:otherwise>
</c:choose>
</c:forEach>
</c:if>
<c:if test="${shownumber == 'true'}"></p></c:if>
</c:if>
<p style="margin-top: 8px; padding-top: 0px;">
<a href="<cms:link><cms:contentshow element="%(opencms.filename)" />?uri=<%= cms.getRequestContext().getUri() %></cms:link>"><b><cms:contentshow element="Title" /></b></a><br>
<cms:contentcheck ifexists="ShortDescription"><cms:contentshow element="ShortDescription" /><br></cms:contentcheck>
<c:set var="dateString">
	<cms:contentshow element="EventDates/EventDate" />
</c:set>
<%
	cms.setDate("dateString");
%>
<fmt:message key="eventarticle.eventdate" />: <fmt:formatDate value="${date}" type="date" pattern="EEEE" />, <fmt:formatDate value="${date}" dateStyle="long" type="date" /><br>
<cms:contentcheck ifexists="RegistrationClose">
<c:set var="dateString">
	<cms:contentshow element="RegistrationClose" />
</c:set>
<%
	cms.setDate("dateString");
%>
<fmt:message key="eventarticle.registrationclose" />: <fmt:formatDate value="${date}" /><br>
</cms:contentcheck>
<small><a href="<cms:link><cms:contentshow element="%(opencms.filename)" />?uri=<%= cms.getRequestContext().getUri() %></cms:link>"><fmt:message key="item.readmore" /></a></small>
</p>
</cms:contentload><%--
--%></fmt:bundle></c:if></cms:contentload>