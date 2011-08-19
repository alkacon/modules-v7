<%@ page 
	buffer="none" 
	import="org.opencms.main.*,org.opencms.workplace.demos.widget.*" %>
<%	
	// initialize the workplace class
	CmsAdminWidgetDemo1 wp = new CmsAdminWidgetDemo1(pageContext, request, response);
		
//////////////////// start of switch statement 
switch (wp.getAction()) {
    case CmsAdminWidgetDemo1.ACTION_CANCEL:
//////////////////// ACTION: cancel button pressed
	wp.actionCloseDialog();
	break;

    case CmsAdminWidgetDemo1.ACTION_SAVE:
//////////////////// ACTION: save edited values
	wp.setParamAction(wp.DIALOG_OK);
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=<%= wp.getEncoding() %>">
<title>Result page</title>

<link rel=stylesheet type="text/css" href="<%= wp.getStyleUri(wp.getJsp(),"workplace.css") %>">

<script type="text/javascript">
<!--

function submitAction(actionValue, theForm, formName) {
	if (theForm == null) {
		theForm = document.forms[formName];
	}
	theForm.framename.value = window.name;
	if (actionValue == "<%= wp.DIALOG_OK %>") {
		return true;
	} else if (actionValue == "<%= wp.DIALOG_BACK %>") {
		theForm.action.value = "<%= wp.DIALOG_BACK %>";
		theForm.submit();
		return false;
	} else {
		theForm.action.value = "<%= wp.DIALOG_CANCEL %>";
		theForm.submit();
		return false;
	}
}

//-->
</script>

<body class="buttons-head" unselectable="on">

<form name="EDITOR" id="EDITOR" method="post" action="<%= wp.getDialogUri() %>" class="nomargin" onsubmit="return submitAction('<%= wp.DIALOG_OK %>', null, 'EDITOR');">

<h2>Result page:</h2>

<h3>String widget: <%= wp.getParamValue("stringwidget") %></h3>
<h3>Textarea widget: <%= wp.getParamValue("textwidget") %></h3>
<h3>Boolean widget: <%= wp.getParamValue("boolwidget") %></h3>
<h3>VFS file widget: <%= wp.getParamValue("vfsfilewidget") %></h3>
<h3>VFS image gallery: <%= wp.getParamValue("imagegalwidget") %></h3>

<%= wp.paramsAsHidden() %>
<%= wp.widgetParamsAsHidden() %>
<% if (wp.getParamFramename() == null) { %>
<input type="hidden" name="<%= wp.PARAM_FRAMENAME %>" value="">
<%  } %>

<%= wp.dialogButtons(new int[] {wp.BUTTON_BACK, wp.BUTTON_OK}, new String[2]) %>

</form>

</body>
</html>

<%
    break;
	
    case CmsAdminWidgetDemo1.ACTION_DEFAULT:
    default:
//////////////////// ACTION: show dialog (default)
	wp.setParamAction(wp.DIALOG_SAVE);
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=<%= wp.getEncoding() %>">
<title>Input form</title>

<link rel=stylesheet type="text/css" href="<%= wp.getStyleUri(wp.getJsp(),"workplace.css") %>">

<script type="text/javascript" src="<%= wp.getResourceUri() %>editors/xmlcontent/edit.js"></script>
<%= wp.getWidgetIncludes() %>

<script type="text/javascript">
<!--

// flag indicating if form initialization is finished
var initialized = false;

// the OpenCms context path
var contextPath = "<%= OpenCms.getSystemInfo().getOpenCmsContext() %>";

// needed when strings are filled in delayed
var stringsPresent = false;
var stringsInserted = false;

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

function submitAction(actionValue, theForm, formName) {
	if (theForm == null) {
		theForm = document.forms[formName];
	}
	theForm.framename.value = window.name;
	exitEditor();
	if (actionValue == "<%= wp.DIALOG_SAVE %>") {
		return true;
	} else {
		theForm.action.value = "<%= wp.DIALOG_CANCEL %>";
		theForm.submit();
		return false;
	}
}

<%= wp.getWidgetInitMethods() %>

//-->
</script>

</head>
<body class="buttons-head" unselectable="on" onload="init();" onunload="exitEditor();">

<form name="EDITOR" id="EDITOR" method="post" action="<%= wp.getDialogUri() %>" class="nomargin" onsubmit="return submitAction('<%= wp.DIALOG_SAVE %>', null, 'EDITOR');">
 
<%= wp.buildDemo1Form() %>

<%= wp.paramsAsHidden() %>
<input type="hidden" name="<%= wp.PARAM_FRAMENAME %>" value="">

<%= wp.dialogButtonsOkCancel() %>

</form>

<%= wp.getWidgetHtmlEnd() %>

</body>
</html>

<%
} 
//////////////////// end of switch statement 
%>