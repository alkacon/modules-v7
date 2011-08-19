<%@ page 
	buffer="none" 
	import="org.opencms.main.*,org.opencms.workplace.demos.widget.*" %>
<%	
	// initialize the workplace class
	CmsAdminWidgetDemo4 wp = new CmsAdminWidgetDemo4(pageContext, request, response);
		
//////////////////// start of switch statement 
switch (wp.getAction()) {
    case CmsAdminWidgetDemo4.ACTION_CANCEL:
//////////////////// ACTION: cancel button pressed
	wp.actionCloseDialog();
	break;

    case CmsAdminWidgetDemo4.ACTION_SAVE:
//////////////////// ACTION: save edited values
	wp.setParamAction(wp.DIALOG_OK);

%><%= wp.htmlStart("administration/index.html") %>

<%= wp.bodyStart(null) %>

<%= wp.dialogStart() %>

<form name="EDITOR" id="EDITOR" method="post" action="<%= wp.getDialogUri() %>" class="nomargin" onsubmit="return submitAction('<%= wp.DIALOG_OK %>', null, 'EDITOR');">

<%= wp.dialogContentStart("Test for widgets in a dialog: Result page") %>
<%= wp.dialogWhiteBoxStart() %>

<h2>Result page:</h2>

<h3>String widget: <%= wp.getParamValue("stringwidget") %></h3>
<h3>Textarea widget: <%= wp.getParamValue("textwidget") %></h3>
<h3>Boolean widget: <%= wp.getParamValue("boolwidget") %></h3>
<h3>VFS file widget: <%= wp.getParamValue("vfsfilewidget") %></h3>
<h3>VFS image gallery: <%= wp.getParamValue("imagegalwidget") %></h3>

<%= wp.dialogWhiteBoxEnd() %>
<%= wp.dialogContentEnd() %>

<%= wp.paramsAsHidden() %>
<%= wp.widgetParamsAsHidden() %>
<% if (wp.getParamFramename() == null) { %>
<input type="hidden" name="<%= wp.PARAM_FRAMENAME %>" value="">
<%  } %>

<%= wp.dialogButtons(new int[] {wp.BUTTON_BACK, wp.BUTTON_OK}, new String[2]) %>

</form>

<%= wp.dialogEnd() %>
<%= wp.bodyEnd() %>
<%= wp.htmlEnd() %>


<%
    break;
	
    case CmsAdminWidgetDemo4.ACTION_DEFAULT:
    default:
//////////////////// ACTION: show dialog (default)
	wp.setParamAction(wp.DIALOG_SAVE);


%><%= wp.htmlStart("administration/index.html") %>

<script type="text/javascript" src="<%= wp.getResourceUri() %>editors/xmlcontent/edit.js"></script>

<%= wp.getWidgetIncludes() %>

<script type="text/javascript">
<!--

// flag indicating if form initialization is finished
var initialized = false;

// the OpenCms context path
var contextPath = "<%= OpenCms.getSystemInfo().getOpenCmsContext() %>";

// action parameters of the form
var actionAddElement = "<%= wp.EDITOR_ACTION_ELEMENT_ADD %>";
var actionRemoveElement = "<%= wp.EDITOR_ACTION_ELEMENT_REMOVE %>";

function init() {

<%= wp.getWidgetInitCalls() %>

	setTimeout("scrollForm();", 200);
	initialized = true;
}



function exitEditor() {
	try {
		// close file selector popup if present
		closeTreeWin();
	} catch (e) {}
}

<%= wp.getWidgetInitMethods() %>

//-->
</script>

<%= wp.bodyStart(null, "onload='init();' onunload='exitEditor();'") %>

<%= wp.dialogStart() %>

<form name="EDITOR" id="EDITOR" method="post" action="<%= wp.getDialogUri() %>" class="nomargin" onsubmit="return submitAction('<%= wp.DIALOG_OK %>', null, 'EDITOR');">

<%= wp.dialogContentStart("Test for widgets in a dialog") %>
 
<%= wp.buildDemo4Form() %>

<%= wp.dialogContentEnd() %>
<%= wp.dialogButtonsOkCancel() %>

<%= wp.paramsAsHidden() %>
<%= wp.widgetParamsAsHidden("default") %>
<% if (wp.getParamFramename() == null) { %>
<input type="hidden" name="<%= wp.PARAM_FRAMENAME %>" value="">
<%  } %>

</form>

<%= wp.getWidgetHtmlEnd() %>

<%= wp.dialogEnd() %>
<%= wp.bodyEnd() %>
<%= wp.htmlEnd() %>

<%
} 
//////////////////// end of switch statement 
%>