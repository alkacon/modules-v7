<%@page buffer="none" session="false" import="org.opencms.i18n.*,org.opencms.jsp.*, org.opencms.frontend.templateone.form.*, java.util.*" %><%

// Initialize JSP action element
CmsJspActionElement cms = new CmsJspActionElement(pageContext, request, response);

CmsFormHandler formHandler = (CmsFormHandler)request.getAttribute("formhandler");

CmsMessages messages = formHandler.getMessages(); 

CmsCaptchaField captchaField = formHandler.getFormConfiguration().getCaptchaField();

%><%= formHandler.getFormConfiguration().getFormCheckText() %><%

if (captchaField != null) {
%>
<script type="text/javascript" language="JavaScript">
<!--

function runConfirmValues() {
	document.forms.confirmvalues.<%= captchaField.getName() %>.value = "" + document.forms.captcha.<%= captchaField.getName() %>.value;
	return true;
}

//-->
</script>
<form name="captcha" method="post" enctype="multipart/form-data">
<%
}
%>

<table border="0" style="margin-top: 14px;">
<%
List resultList = formHandler.getFormConfiguration().getFields();

for (int i = 0, n = resultList.size(); i < n; i++) {
	I_CmsField current = (I_CmsField)resultList.get(i);
	if (!CmsHiddenField.class.isAssignableFrom(current.getClass())) {
		out.print("<tr>\n\t<td valign=\"top\">" + current.getLabel() + "</td>");
		out.print("\n\t<td valign=\"top\" style=\"font-weight: bold;\">" + formHandler.convertToHtmlValue(current.toString()) + "</td></tr>\n");
	}
}

if (captchaField != null) {

	CmsCaptchaSettings captchaSettings = captchaField.getCaptchaSettings();
	String fieldLabel = captchaField.getLabel();	
	String errorMessage = (String)formHandler.getErrors().get(captchaField.getName());	
	
	if (errorMessage != null) {
		// create the error message for the field
		if (CmsFormHandler.ERROR_MANDATORY.equals(errorMessage)) {
			errorMessage = messages.key("form.error.mandatory");
		} else {
			errorMessage = messages.key("form.error.validation");
		}
		errorMessage = messages.key("form.html.error.start") + errorMessage + messages.key("form.html.error.end");
		fieldLabel = messages.key("form.html.label.error.start") + fieldLabel + messages.key("form.html.label.error.end");
	} else {
		errorMessage = "";
	}	
	
	out.println("<tr>\n\t<td valign=\"middle\">" + fieldLabel + "</td>");
	out.println("\t<td valign=\"top\" style=\"font-weight: bold;\">");
	out.println("\t<img src=\"" + cms.link("/system/modules/org.opencms.frontend.templateone.form/pages/captcha") + "?" + captchaSettings.toRequestParams(cms.getCmsObject()) + "\" width=\"" + captchaSettings.getImageWidth() + "\" height=\"" + captchaSettings.getImageHeight() + "\" alt=\"\" border=\"1\"><br>");
	out.println("\t<input type=\"text\" name=\"" + captchaField.getName() + "\" value=\"\">" + errorMessage);
	out.println("\t</td>\n</tr>\n");
}

%></table><%

if (captchaField != null) {
%>
</form>
<%
}
%>

<table border="0" style="margin-top: 14px;">
<tr>
<form name="confirmvalues" method="post" enctype="multipart/form-data" action="<%= cms.link(cms.getRequestContext().getUri()) %>" onSubmit="return runConfirmValues();">
<input type="hidden" name="<%= CmsFormHandler.PARAM_FORMACTION %>" value="<%= CmsFormHandler.ACTION_CONFIRMED %>">
<input type="hidden" name="<%= CmsCaptchaField.C_PARAM_CAPTCHA_PHRASE %>" value="">
<%= formHandler.createHiddenFields() %>
<td><input type="submit" value="<%= messages.key("form.button.checked") %>" class="formbutton">&nbsp;&nbsp;&nbsp;&nbsp;</td>
</form>


<form name="displayvalues" method="post" enctype="multipart/form-data" action="<%= cms.link(cms.getRequestContext().getUri()) %>">
<input type="hidden" name="<%= CmsFormHandler.PARAM_FORMACTION %>" value="<%= CmsFormHandler.ACTION_CORRECT_INPUT %>">
<%= formHandler.createHiddenFields() %>
<td><input type="submit" value="<%= messages.key("form.button.correct") %>" class="formbutton"></td>
</form>
</tr>
</table>