/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.frontend.templateone;

import org.opencms.file.CmsPropertyDefinition;
import org.opencms.util.CmsStringUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.httpclient.util.URIUtil;

/**
 * Provides methods to build interactive JSP forms.<p>
 * 
 * @since 6.0.0 
 */
public abstract class CmsTemplateForm extends CmsTemplateBean {

    /** Name of the resource bundle containing the localized form messages.<p> */
    public static final String MESSAGE_BUNDLE_FORM = "templateone_form";
    
    /** Request parameter name for the action parameter to determine if the form has been submitted.<p> */
    public static final String PARAM_ACTION = "action";

    /** Holds the error messages for form validation.<p> */
    private Map m_errors;
    
    /** Stores the URI of the JSP form.<p> */
    private String m_formUri;
    
    /** Stores the URI of the calling page including eventual request parameter appendings.<p> */
    private String m_pageUri;
    
    /** Stores the complete URL of the calling page including eventual request parameter appendings.<p> */
    private String m_pageUrl;
    
    /** Stores the URI of the page containing the texts for the form.<p> */
    private String m_textsUri;

    /**
     * Empty constructor, required for every JavaBean.<p>
     */
    public CmsTemplateForm() {

        super();
    }

    /**
     * Constructor, with parameters.<p>
     * 
     * Use this constructor for the template.<p>
     * 
     * @param context the JSP page context object
     * @param req the JSP request 
     * @param res the JSP response 
     */
    public CmsTemplateForm(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        super();
        init(context, req, res);
    }

    /**
     * Returns the input String with line breaks added at the specified separator character.<p>
     * 
     * @param inputString the input String to modify
     * @param sepChar the separator character to look up in the input String
     * @param lineLength the line length after which a break should occur
     * @param separator the separator String to append as line breaks, e.g. <code>&lt;br&gt;</code>
     * @return the modified input String with line breaks added
     */
    public static String getFormattedString(String inputString, char sepChar, int lineLength, String separator) {

        if (inputString.length() > lineLength) {
            // length of input String is larger than allowed line length
            int inputLength = inputString.length();
            StringBuffer result = new StringBuffer(inputLength + 4);
            while (inputLength > lineLength) {
                int count = lineLength;
                while (count < inputLength && inputString.charAt(count) != sepChar) {
                    // look for separator position
                    count++;
                }
                if (count < inputLength) {
                    // separator found, add line break
                    result.append(inputString.substring(0, count + 1));
                    result.append(separator);
                    inputString = inputString.substring(count + 1);
                    inputLength = inputString.length();
                } else {
                    // no separator found, leave look
                    break;
                }
            }
            // append end of input String to output
            result.append(inputString);
            inputString = result.toString();
        }
        return inputString;
    }

    /**
     * Oversimplistic method to validate email addresses.<p>
     * 
     * @param theAddress the email address to validate
     * @return true if theAddress is at least a String of the form xx@yy.zz
     */
    public static boolean isValidEmailAddress(String theAddress) {

        if (theAddress == null) {
            return false;
        }
        return (theAddress.lastIndexOf(".") < (theAddress.length() - 2) && theAddress.lastIndexOf(".") > theAddress.indexOf("@"));
    }

    /**
     * Returns the template configuration path in the OpenCms VFS.<p>
     * 
     * Overwrites the method in the super class because the uri is set to the
     * form uri in the module.<p>
     * 
     * @return the template configuration path
     */
    public String getConfigPath() {

        // store form uri
        String uri = getRequestContext().getUri();
        // set uri to page uri to obtain configuration path
        getRequestContext().setUri(getPageUri(true));
        String path = property(PROPERTY_CONFIGPATH, "search", "");
        // reset uri to form uri
        getRequestContext().setUri(uri);
        return path;
    }

    /**
     * Returns the error message for the specified key or an empty String if no error is present for the key.<p> 
     * 
     * @param key error key to look up
     * @return the error message for the specified key or an empty String
     */
    public String getError(String key) {

        if (m_errors != null) {
            String message = (String)m_errors.get(key);
            if (message == null) {
                return "";
            } else {
                return message;
            }
        } else {
            return "";
        }
    }

    /**
     * Returns the error Map holding the validation errors.<p>
     * 
     * @return the error Map holding the validation errors
     */
    public Map getErrors() {

        return m_errors;
    }

    /**
     * Returns the content of the specified page element from the defined text page.<p>
     * 
     * @param element the name of the element to display
     * @param stripHtml indicates if HTML tags should be filtered from the element contents
     * @return the content of the specified page element from the defined text page
     */
    public String getFormText(String element, boolean stripHtml) {

        String content = getContent(getTextsUri(), element, getRequestContext().getLocale());
        if (stripHtml) {
            // remove the tags from the content
            content = removeHtmlTags(content);
        }
        return content;
    }

    /**
     * Returns the subsituted link to the JSP form.<p>
     * 
     * This can be used for the "action" attribute value of the &lt;form&gt; tag.<p>
     * 
     * @return the subsituted link to the JSP form
     */
    public String getFormUri() {

        return link(m_formUri);
    }

    /**
     * Returns the title of the recommended page.<p>
     * 
     * If the title property is not found, the page URI
     * is returned instead.<p>
     * 
     * @return the title of the recommended page
     */
    public String getPageTitle() {

        String title = property(CmsPropertyDefinition.PROPERTY_TITLE, getPageUri(true), "");
        if ("".equals(title)) {
            title = getPageUri(true);
        }
        return title;
    }

    /**
     * Returns the (not substituted) URI to the page that called the form.<p>
     * 
     * Includes all request parameters of the page, too.<p>
     * 
     * @return the (not substituted) URI to the page that called the form
     */
    public String getPageUri() {

        return getPageUri(false);
    }

    /**
     * Returns the (not substituted) URI to the page that called the form.<p>
     * 
     * Removes all request parameters from the page URI, if specified.<p>
     * 
     * @param removeParams if true, all request parameters will be removed from the URI
     * @return the (not substituted) URI to the page that called the form
     */
    public String getPageUri(boolean removeParams) {

        if (removeParams) {
            // remove eventual request parameters
            String uri = m_pageUri;
            if (CmsStringUtil.isNotEmpty(uri)) {
                uri = URIUtil.getPath(uri);
            }
            return uri;
        } else {
            return m_pageUri;
        }
    }

    /**
     * Returns the URL of the page to be displayed on the form.<p>
     * 
     * @return the URL of the page
     */
    public String getPageUrl() {

        if (CmsStringUtil.isEmpty(m_pageUrl)) {
            StringBuffer result = new StringBuffer(64);
            HttpServletRequest request = getRequest();
            result.append(request.getScheme());
            result.append("://");
            result.append(request.getServerName());
            int port = request.getServerPort();
            String portString = "";
            if (port != 80 && port != 443) {
                // only add port different from standard ports
                portString = ":" + port;
            }
            result.append(portString);
            result.append(link(m_pageUri));
            m_pageUrl = result.toString();
        }
        return m_pageUrl;
    }

    /**
     * Returns the URI of the page containing the texts for the form.<p>
     * 
     * @return the URI of the page containing the texts for the form
     */
    public String getTextsUri() {

        if (m_textsUri == null) {
            m_textsUri = checkTextsUri();
        }
        return m_textsUri;
    }

    /**
     * Checks if the input form has validation errors.<p>
     * 
     * @return true if at least one validation error was found, otherwise false
     */
    public boolean hasValidationErrors() {

        if (isSubmitted()) {
            if (getErrors() != null && getErrors().size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initialize this bean with the current page context, request and response.<p>
     * 
     * It is required to call one of the init() methods before you can use the 
     * instance of this bean.
     * 
     * @param context the JSP page context object
     * @param req the JSP request 
     * @param res the JSP response 
     */
    public void init(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        
        // get the page uri from request parameter
        m_pageUri = req.getParameter(CmsTemplateBean.PARAM_URI);
        // call initialization of super class
        super.init(context, req, res);
        // set site root
        String siteRoot = req.getParameter(CmsTemplateBean.PARAM_SITE);
        if (CmsStringUtil.isNotEmpty(siteRoot)) {
            getRequestContext().setSiteRoot(siteRoot);
        }
        // store the form uri
        m_formUri = getRequestContext().getUri();
        if (m_pageUri == null) {
            m_pageUri = m_formUri;
        }
        messages(MESSAGE_BUNDLE_FORM);
    }

    /**
     * Checks if the given String is true and returns the "checked" attribute for checkboxes and radiobuttons.<p>
     * 
     * @param fieldValue the value of the field to test
     * @return the "checked" attribute or an empty String
     */
    public String isChecked(String fieldValue) {

        if (Boolean.valueOf(fieldValue).booleanValue()) {
            return " checked=\"checked\"";
        } else {
            return "";
        }
    }

    /**
     * Returns true if the two parameters are equal.<p>
     * 
     * Use this method to determine which entry of a select box or radio buttons is selected.<p>
     * 
     * @param fieldValue the current option or radio button value to check
     * @param requestValue the current request value
     * @return true if the two parameters are equal
     */
    public boolean isSelected(String fieldValue, String requestValue) {

        if (fieldValue.equals(requestValue)) {
            return true;
        }
        return false;
    }

    /**
     * Returns if the form has been submitted or not.<p>
     * 
     * Checks the presence of the "action" request parameter.<p>
     * 
     * @return true if the form has been submitted, otherwise false
     */
    public boolean isSubmitted() {

        return getRequest().getParameter(PARAM_ACTION) != null;
    }

    /**
     * Sets the error Map holding the validation errors.<p>
     * 
     * @param errors error Map
     */
    public void setErrors(Map errors) {

        m_errors = errors;
    }

    /**
     * Validates the values of the input fields and creates error messages, if necessary.<p>
     * 
     * @return true if all checked input values are valid, otherwise false
     */
    public abstract boolean validate();

    /**
     * Returns the text URI from the configuration file and checks the presence.<p>
     * 
     * Returns the default texts URI if the file specified in the configuration can not be found.<p>
     * 
     * @return the text URI from the configuration file
     */
    protected abstract String checkTextsUri();

    /**
     * Removes all HTML tags from the given String by using a regular expression.<p>
     * 
     * @param content the String to scan for HTML tags
     * @return the content without HTML tags
     */
    protected String removeHtmlTags(String content) {

        if (content != null && content.indexOf("<") != -1) {
            Matcher matcher = Pattern.compile("<(.|\\n)+?>").matcher(content);
            content = matcher.replaceAll("");
        }
        return content;
    }

}