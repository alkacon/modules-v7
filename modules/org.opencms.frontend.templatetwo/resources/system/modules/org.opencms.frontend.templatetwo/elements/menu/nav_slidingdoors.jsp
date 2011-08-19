<%@page session="false" import="org.opencms.frontend.templatetwo.*, org.opencms.file.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms"%><%

	// This element defines the a top level navigation element which can be
	// selected in the style configuration file.
	//
	// Single navigation enties of the first navigation level are shown as tabs.
	//
	// It uses the CmsTemplateMenu class to provide some auxiliary methods
	// in order to prevent the use of scriplet code in this jsp.
	// For details on the CmsTemplateMenu class, see the source code which can
	// be found at the following VFS location:
	// /system/modules/org.opencms.frontend.templatetwo/java_src/CmsTemplateMenu.java

	CmsTemplateMenu cms = new CmsTemplateMenu(pageContext, request, response);
	//in order to omit folder levels, set the NavStartLevel property to the level number where 0 is the root "/" level
	int navStartLevel = Integer.parseInt(cms.property("NavStartLevel", "search", "0"));
	cms.setElements(cms.getNavigation().getNavigationForFolder(CmsResource.getPathPart(cms.getRequestContext().getFolderUri(), navStartLevel)));
	pageContext.setAttribute("cms", cms);
%>

<div id="nav_main">
	<ul>
		<!-- <li id="title">Navigation</li>  -->
		
		<c:set var="oldLevel" value="" />
		<c:forEach items="${cms.elements}" var="elem">
			<c:set var="currentLevel" value="${elem.navTreeLevel}" />
			
			<c:choose>
				<c:when test="${empty oldLevel}"></c:when>
				<c:when test="${currentLevel > oldLevel}"><ul></c:when>
				<c:when test="${currentLevel == oldLevel}"></li></c:when>
				<c:when test="${oldLevel > currentLevel}">
					<c:forEach begin="${currentLevel+1}" end="${oldLevel}"></li></ul></c:forEach>
				</c:when>
			</c:choose>
			
			<li <c:if test="${cms.isCurrent[elem]}">id="current"</c:if>>
			<a href="<cms:link>${elem.resourceName}</cms:link>">${elem.navText}</a>
			
			<c:set var="oldLevel" value="${currentLevel}" />
		</c:forEach>
		
		<c:forEach begin="${cms.topLevel+1}" end="${oldLevel}"></li></ul></c:forEach>
	</ul>
</div>

