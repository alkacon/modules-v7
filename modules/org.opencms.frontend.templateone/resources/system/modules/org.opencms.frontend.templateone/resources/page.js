/* parse the entered query String */
function parseSearchQuery(theForm, message) {
	var queryValue = theForm.elements["query2"].value;
	var testValue = queryValue.replace(/ /g, "");
	if (testValue.length < 3) {
		alert(message);
		return (false);
	}
	theForm.elements["query"].value = queryValue;
	return (true);
}

/* open the print version popup window */
function openPrintVersion() {
	var params = "print=true";
	if (document.location.search != "") {
		params = document.location.search + "&" + params;		
	} else {
		params = "?" + params;
	}
	window.open(document.location.pathname + params, "print", "width=670,height=750,dependent=yes,status=no,toolbar=no,location=no,scrollbars=yes");
}

/* open the imprint popup window */
function openImprint(imprintUri, pageUri, theLocale, site) {
	pageUri = encodeURIComponent(pageUri); 
	pageUri = "?__locale=" + theLocale + "&uri=" + pageUri + "&site=" + site;
	window.open(imprintUri + pageUri, "imprint", "width=670,height=550,dependent=yes,status=no,toolbar=no,location=no,scrollbars=yes,resizable=yes");
}

/* open the recommend page form popup window */
function openRecommendForm(recommendUri, pageUri, theLocale, site) {	
	if (window.location.search != "") {
		pageUri += window.location.search;		
	}
	pageUri = encodeURIComponent(pageUri); 
	pageUri = "?__locale=" + theLocale + "&uri=" + pageUri + "&site=" + site;
	window.open(recommendUri + pageUri, "recommend", "width=670,height=700,dependent=yes,status=no,toolbar=no,location=no,scrollbars=yes,resizable=yes");
}

/* open the recommend page form popup window */
function openLetterForm(letterUri, pageUri, theLocale, site) {	
	if (window.location.search != "") {
		pageUri += window.location.search;		
	}
	pageUri = encodeURIComponent(pageUri); 
	pageUri = "?__locale=" + theLocale + "&uri=" + pageUri + "&site=" + site;
	window.open(letterUri + pageUri, "contact", "width=670,height=700,dependent=yes,status=no,toolbar=no,location=no,scrollbars=yes,resizable=yes");
}