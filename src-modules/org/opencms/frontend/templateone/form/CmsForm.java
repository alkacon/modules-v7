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

package org.opencms.frontend.templateone.form;

import org.opencms.configuration.CmsConfigurationException;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsMessages;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.util.CmsMacroResolver;
import org.opencms.util.CmsStringUtil;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;
import org.opencms.xml.types.CmsXmlHtmlValue;
import org.opencms.xml.types.I_CmsXmlContentValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.fileupload.FileItem;

/**
 * Represents an input form with all configured fields and options.<p>
 * 
 * Provides the necessary information to create an input form, email messages and confirmation outputs.<p>
 * 
 * @since 6.0.0 
 */
public class CmsForm {
    
    /** Mail type: html mail. */
    public static final String MAILTYPE_HTML = "html";

    /** Mail type: text mail. */
    public static final String MAILTYPE_TEXT = "text";
    
    /** Configuration node name for the optional captcha. */
    public static final String NODE_CAPTCHA = "FormCaptcha";

    /** Configuration node name for the confirmation mail checkbox label text. */
    public static final String NODE_CONFIRMATIONMAILCHECKBOXLABEL = "ConfirmationCheckboxLabel";
    
    /** Configuration node name for the confirmation mail enabled node. */
    public static final String NODE_CONFIRMATIONMAILENABLED = "ConfirmationMailEnabled";

    /** Configuration node name for the confirmation mail input field node. */
    public static final String NODE_CONFIRMATIONMAILFIELD = "ConfirmationField";
    
    /** Configuration node name for the confirmation mail optional flag node. */
    public static final String NODE_CONFIRMATIONMAILOPTIONAL = "ConfirmationMailOptional";
    
    /** Configuration node name for the confirmation mail subject node. */
    public static final String NODE_CONFIRMATIONMAILSUBJECT = "ConfirmationMailSubject";
    
    /** Configuration node name for the confirmation mail text node. */
    public static final String NODE_CONFIRMATIONMAILTEXT = "ConfirmationMailText";
    
    /** Configuration node name for the Email node. */
    public static final String NODE_EMAIL = "Email";

    /** Configuration node name for the field value node. */
    public static final String NODE_FIELDDEFAULTVALUE = "FieldDefault";
    
    /** Configuration node name for the field error message node. */
    public static final String NODE_FIELDERRORMESSAGE = "FieldErrorMessage";
    
    /** Configuration node name for the field item node. */
    public static final String NODE_FIELDITEM = "FieldItem";
    
    /** Configuration node name for the field description node. */
    public static final String NODE_FIELDLABEL = "FieldLabel";
    
    /** Configuration node name for the field mandatory node. */
    public static final String NODE_FIELDMANDATORY = "FieldMandatory";

    /** Configuration node name for the field type node. */
    public static final String NODE_FIELDTYPE = "FieldType";

    /** Configuration node name for the field validation node. */
    public static final String NODE_FIELDVALIDATION = "FieldValidation";
    
    /** Configuration node name for the form attributes node. */
    public static final String NODE_FORMATTRIBUTES = "FormAttributes";
    
    /** Configuration node name for the form check page text node. */
    public static final String NODE_FORMCHECKTEXT = "CheckText";

    /** Configuration node name for the form confirmation text node. */
    public static final String NODE_FORMCONFIRMATION = "FormConfirmation";
    
    /** Configuration node name for the form field attributes node. */
    public static final String NODE_FORMFIELDATTRIBUTES = "FormFieldAttributes";
    
    /** Configuration node name for the form text node. */
    public static final String NODE_FORMTEXT = "FormText";
    
    /** Configuration node name for the input field node. */
    public static final String NODE_INPUTFIELD = "InputField";
    
    /** Configuration node name for the item description node. */
    public static final String NODE_ITEMDESCRIPTION = "ItemDescription";
    
    /** Configuration node name for the item selected node. */
    public static final String NODE_ITEMSELECTED = "ItemSelected";
    
    /** Configuration node name for the item value node. */
    public static final String NODE_ITEMVALUE = "ItemValue";
    
    /** Configuration node name for the Email bcc recipient(s) node. */
    public static final String NODE_MAILBCC = "MailBCC";
    
    /** Configuration node name for the Email cc recipient(s) node. */
    public static final String NODE_MAILCC = "MailCC";
    
    /** Configuration node name for the Email sender address node. */
    public static final String NODE_MAILFROM = "MailFrom";

    /** Configuration node name for the Email subject node. */
    public static final String NODE_MAILSUBJECT = "MailSubject";
    
    /** Configuration node name for the Email text node. */
    public static final String NODE_MAILTEXT = "MailText";
    
    /** Configuration node name for the Email recipient(s) node. */
    public static final String NODE_MAILTO = "MailTo";
    
    /** Configuration node name for the Email type node. */
    public static final String NODE_MAILTYPE = "MailType";
    
    /** Configuration node name for the optional form configuration. */
    public static final String NODE_OPTIONALCONFIGURATION = "OptionalFormConfiguration";
    
    /** Configuration node name for the optional confirmation mail configuration. */
    public static final String NODE_OPTIONALCONFIRMATION = "OptionalConfirmationMail";
    
    /** Configuration node name for the Show check page node. */
    public static final String NODE_SHOWCHECK = "ShowCheck";
    
    /** Configuration node name for the optional target URI. */
    public static final String NODE_TARGET_URI = "TargetUri";
    
    /** Request parameter name for the optional send confirmation email checkbox. */
    public static final String PARAM_SENDCONFIRMATION = "sendconfirmation";
    
    /** Resource type ID of XML content forms. */
    private static final String TYPE_NAME = "emailform";

    private CmsCaptchaField m_captchaField;

    private List m_configurationErrors;
    private String m_confirmationMailCheckboxLabel;
    private boolean m_confirmationMailEnabled;
    private int m_confirmationMailField;
    private boolean m_confirmationMailOptional;
    private String m_confirmationMailSubject;
    private String m_confirmationMailText;

    private String m_confirmationMailTextPlain;
    
    /** Stores the form input fields. */
    private List m_fields;

    /** Allows to access form fields internally by name. */
    private Map m_fieldsByName;
    private String m_formAction;
    private String m_formAttributes;
    private String m_formCheckText;
    private String m_formConfirmationText;
    private String m_formFieldAttributes;

    private String m_formText;

    private boolean m_hasMandatoryFields;
    private String m_mailBCC;
    private String m_mailCC;
    private String m_mailFrom;
    private String m_mailSubject;
    private String m_mailSubjectPrefix;
    private String m_mailText;
    private String m_mailTextPlain;
    private String m_mailTo;

    private String m_mailType;
    
    private boolean m_showCheck;
    private  String m_targetUri;

    /** Configuration node name for the optional captcha. */
    public static final String NODE_CAPTCHA_PRESET = "Preset";

    /** Configuration node name for the optional captcha. */
    public static final String NODE_CAPTCHA_CHARACTERS = "Characters";

    /** The map of request parameters. */
    private Map m_parameterMap;
    
    /**
     * Default constructor which parses the configuration file.<p>
     * 
     * @param jsp the initialized CmsJspActionElement to access the OpenCms API
     * @param messages the localized messages
     * @param initial if true, field values are filled with values specified in the configuration file, otherwise from the request
     * @throws Exception if parsing the configuration fails
     */
    public CmsForm(CmsFormHandler jsp, CmsMessages messages, boolean initial)
    throws Exception {

        init(jsp, messages, initial, null, null);
    }

    /**
     * Constructor which parses the configuration file using a given configuration file URI.<p>
     * 
     * @param jsp the initialized CmsJspActionElement to access the OpenCms API
     * @param messages the localized messages
     * @param initial if true, field values are filled with values specified in the configuration file, otherwise from the request
     * @param formConfigUri URI of the form configuration file, if not provided, current URI is used for configuration
     * @param formAction the desired action submiktted by the form
     * 
     * @throws Exception if parsing the configuration fails
     */
    public CmsForm(CmsFormHandler jsp, CmsMessages messages, boolean initial, String formConfigUri, String formAction)
    throws Exception {

        init(jsp, messages, initial, formConfigUri, formAction);
    }
    
    /**
     * Returns the resource type name of XML content forms.<p>
     * 
     * @return the resource type name of XML content forms
     */
    public static String getStaticType() {
        return TYPE_NAME;
    }

    /**
     * Tests, if the captcha field (if configured at all) should be displayed on the check page.<p>
     * 
     * @return true, if the captcha field should be displayed on the check page
     */
    public boolean captchaFieldIsOnCheckPage() {
        return getShowCheck();
    }

    /**
     * Tests, if the captcha field (if configured at all) should be displayed on the input page.<p>
     * 
     * @return true, if the captcha field should be displayed on the input page
     */
    public boolean captchaFieldIsOnInputPage() {
        return !getShowCheck();
    }

    /**
     * Returns the (opt.) captcha field of this form.<p>
     * 
     * @return the (opt.) captcha field of this form
     */
    public CmsCaptchaField getCaptchaField() {
        
        return m_captchaField;
    }

    /**
     * Returns the form configuration errors.<p>
     *
     * @return the form configuration errors
     */
    public List getConfigurationErrors() {

        return m_configurationErrors;
    }

    /**
     * Returns the label for the optional confirmation mail checkbox on the input form.<p>
     *
     * @return the label for the optional confirmation mail checkbox on the input form
     */
    public String getConfirmationMailCheckboxLabel() {

        return m_confirmationMailCheckboxLabel;
    }

    /**
     * Returns the index number of the input field containing the email address for the optional confirmation mail.<p>
     *
     * @return the index number of the input field containing the email address for the optional confirmation mail
     */
    public int getConfirmationMailField() {

        return m_confirmationMailField;
    }

    /**
     * Returns the subject of the optional confirmation mail.<p>
     *
     * @return the subject of the optional confirmation mail
     */
    public String getConfirmationMailSubject() {

        return m_confirmationMailSubject;
    }

    /**
     * Returns the text of the optional confirmation mail.<p>
     *
     * @return the text of the optional confirmation mail
     */
    public String getConfirmationMailText() {

        return m_confirmationMailText;
    }

    /**
     * Returns the plain text of the optional confirmation mail.<p>
     *
     * @return the plain text of the optional confirmation mail
     */
    public String getConfirmationMailTextPlain() {

        return m_confirmationMailTextPlain;
    }

    /**
     * Returns a list of field objects for the online form.<p>
     * 
     * @return a list of field objects for the online form
     */
    public List getFields() {

        return m_fields;
    }

    /**
     * Returns the value for a field specified by it's name (Xpath).<p>
     * 
     * @param fieldName the field's name (Xpath)
     * @return the field value, or null
     */
    public String getFieldStringValueByName(String fieldName) {
        
        I_CmsField field = (I_CmsField)m_fieldsByName.get(fieldName);
        if (field != null) {
            
            String fieldValue = field.getValue();
            return (fieldValue != null) ? fieldValue.trim() : "";
        }
        
        return "";
    }

    /** 
     * Returns the global form attributes.<p>
     * 
     * @return the global form attributes
     */
    public String getFormAttributes() {

        return m_formAttributes;
    }

    /**
     * Returns the form check text.<p>
     * 
     * @return the form check text
     */
    public String getFormCheckText() {

        return m_formCheckText;
    }

    /**
     * Returns the form confirmation text.<p>
     * 
     * @return the form confirmation text
     */
    public String getFormConfirmationText() {

        return m_formConfirmationText;
    }

    /**
     * Returns the optional form input field attributes.<p>
     * 
     * @return the optional form input field attributes
     */
    public String getFormFieldAttributes() {

        return m_formFieldAttributes;
    }

    /**
     * Returns the form text.<p>
     * 
     * @return the form text
     */
    public String getFormText() {

        return m_formText;
    }

    /**
     * Returns the mail bcc recipient(s).<p>
     * 
     * @return the mail bcc recipient(s)
     */
    public String getMailBCC() {

        return m_mailBCC;
    }

    /**
     * Returns the mail cc recipient(s).<p>
     * 
     * @return the mail cc recipient(s)
     */
    public String getMailCC() {

        return m_mailCC;
    }

    /**
     * Returns the mail sender address.<p>
     * 
     * @return the mail sender address
     */
    public String getMailFrom() {

        return m_mailFrom;
    }

    /**
     * Returns the mail subject.<p>
     * 
     * @return the mail subject
     */
    public String getMailSubject() {

        return m_mailSubject;
    }

    /**
     * Returns the mail subject prefix.<p>
     * 
     * @return the mail subject prefix
     */
    public String getMailSubjectPrefix() {

        return m_mailSubjectPrefix;
    }

    /**
     * Returns the mail text.<p>
     * 
     * @return the mail text
     */
    public String getMailText() {

        return m_mailText;
    }

    /**
     * Returns the mail text as plain text.<p>
     * 
     * @return the mail text as plain text
     */
    public String getMailTextPlain() {

        return m_mailTextPlain;
    }

    /**
     * Returns the mail recipient(s).<p>
     * 
     * @return the mail recipient(s)
     */
    public String getMailTo() {

        return m_mailTo;
    }

    /**
     * Returns the mail type ("text" or "html").<p>
     * 
     * @return the mail type
     */
    public String getMailType() {

        return m_mailType;
    }

    /**
     * Returns if the check page should be shown.<p>
     *
     * @return true if the check page should be shown, otherwise false
     */
    public boolean getShowCheck() {

        return m_showCheck;
    }

    /**
     * Returns the target URI of this form.<p>
     * 
     * This optional target URI can be used to redirect the user to an OpenCms page instead of displaying a confirmation
     * text from the form's XML content.<p>
     * 
     * @return the target URI
     */
    public String getTargetUri() {
        return m_targetUri;
    }

    /**
     * Tests if a captcha field is configured for this form.<p>
     * 
     * @return true, if a captcha field is configured for this form
     */
    public boolean hasCaptchaField() {
        return m_captchaField != null;
    }

    /**
     * Returns if the form has configuration errors.<p>
     *
     * @return true if the form has configuration errors, otherwise false
     */
    public boolean hasConfigurationErrors() {

        return m_configurationErrors.size() > 0;
    }

    /**
     * Returns true if at least one of the configured fields is mandatory.<p>
     *
     * @return true if at least one of the configured fields is mandatory, otherwise false
     */
    public boolean hasMandatoryFields() {

        return m_hasMandatoryFields;
    }

    /**
     * Tests if this form has a target URI specified.<p>
     * 
     * This optional target URI can be used to redirect the user to an OpenCms page instead of displaying a confirmation
     * text from the form's XML content.<p>
     * 
     * @return the target URI
     */
    public boolean hasTargetUri() {
        return CmsStringUtil.isNotEmpty(m_targetUri);
    }

    /**
     * Initializes the form configuration and creates the necessary form field objects.<p>
     * 
     * @param jsp the initialized CmsJspActionElement to access the OpenCms API
     * @param messages the localized messages
     * @param initial if true, field values are filled with values specified in the XML configuration
     * @param formConfigUri URI of the form configuration file, if not provided, current URI is used for configuration
     * @param formAction the desired action submitted by the form
     * 
     * @throws Exception if parsing the configuration fails
     */
    public void init(CmsFormHandler jsp, CmsMessages messages, boolean initial, String formConfigUri, String formAction)
    throws Exception {
 
        m_parameterMap = jsp.getParameterMap();
        // read the form configuration file from VFS
        if (CmsStringUtil.isEmpty(formConfigUri)) {
            formConfigUri = jsp.getRequestContext().getUri();
        }
        CmsFile file = jsp.getCmsObject().readFile(formConfigUri);
        CmsXmlContent content = CmsXmlContentFactory.unmarshal(jsp.getCmsObject(), file);

        // get current Locale
        Locale locale = jsp.getRequestContext().getLocale();

        // init member variables
        initMembers();
        
        m_formAction = formAction;
        m_fields = new ArrayList();
        m_fieldsByName = new HashMap();

        // initialize general form configuration
        initFormGlobalConfiguration(content, jsp.getCmsObject(), locale, messages);

        // initialize the form input fields
        initInputFields(content, jsp, locale, messages, initial);
        
        // init. the optional captcha field
        initCaptchaField(jsp, content, locale, initial);
        
        // add the captcha field to the list of all fields, if the form has no check page
        if (captchaFieldIsOnInputPage() && m_captchaField != null) {
            addField(m_captchaField);
        }
    }
    
    /**
     * Tests if the check page was submitted.<p>
     * 
     * @return true, if the check page was submitted
     */
    public boolean isCheckPageSubmitted() {
        return CmsFormHandler.ACTION_CONFIRMED.equals(m_formAction);
    }

    /**
     * Returns if the optional confirmation mail is enabled.<p>
     *
     * @return true if the optional confirmation mail is enabled, otherwise false
     */
    public boolean isConfirmationMailEnabled() {

        return m_confirmationMailEnabled;
    }

    /**
     * Returns if the confirmation mail if optional, i.e. selectable by the form submitter.<p>
     *
     * @return true if the confirmation mail if optional, i.e. selectable by the form submitter, otherwise false
     */
    public boolean isConfirmationMailOptional() {

        return m_confirmationMailOptional;
    }

    /**
     * Tests if the input page was submitted.<p>
     * 
     * @return true, if the input page was submitted
     */
    public boolean isInputFormSubmitted() {
        return CmsFormHandler.ACTION_SUBMIT.equals(m_formAction);
    }

    /**
     * Removes the captcha field from the list of all fields, if present.<p>
     * 
     * @return the removed captcha field, or null
     */
    public I_CmsField removeCaptchaField() {
        
        for (int i = 0, n = m_fields.size(); i < n; i++) {
            
            I_CmsField field = (I_CmsField)m_fields.get(i);
            if (field != null && CmsCaptchaField.class.isAssignableFrom(getClass())) {
                
                removeField(field);
                return field;
            }
        }

        return null;
    }

    /**
     * Adds a field to the form.<p>
     * 
     * @param field the field to be added to the form
     */
    protected void addField(I_CmsField field) {
        
        m_fields.add(field);
        
        // the fields are also internally backed in a map keyed by their field name
        m_fieldsByName.put(field.getName(), field);
    }

    /**
     * Marks the individual items of checkboxes, selectboxes and radiobuttons as selected depending on the given request parameters.<p>
     * 
     * @param field the current field
     * @param value the value of the input field
     * 
     * @return <code>"true"</code> if the current item is selected or checked, otherwise false
     */
    protected String readSelectedFromRequest(I_CmsField field, String value) {

        String result = "";
        if (field.needsItems()) {
            // select box or radio button or checkbox
            try {
                String[] values = (String[])m_parameterMap.get(field.getName());
                for (int i = 0; i < values.length; i++) {
                    if (CmsStringUtil.isNotEmpty(values[i]) && values[i].equals(value)) {
                        // mark this as selected
                        result = Boolean.toString(true);
                    } 
                }
            } catch (Exception e) {
                // keep value null;
            }

        } else {
            // always display fields value arrays
            result = Boolean.toString(true);
        }
        return result;
    }

    /**
     * Removes a field from the form fields.<p>
     * 
     * @param field the field to be removed
     */
    protected void removeField(I_CmsField field) {
        
        m_fields.remove(field);
        
        // the fields are also internally backed in a map keyed by their field name
        m_fieldsByName.remove(field);
    }

    /**
     * Sets the form configuration errors.<p>
     *
     * @param configurationErrors the form configuration errors
     */
    protected void setConfigurationErrors(List configurationErrors) {

        m_configurationErrors = configurationErrors;
    }

    /**
     * Sets the label for the optional confirmation mail checkbox on the input form.<p>
     *
     * @param confirmationMailCheckboxLabel the label for the optional confirmation mail checkbox on the input form
     */
    protected void setConfirmationMailCheckboxLabel(String confirmationMailCheckboxLabel) {

        m_confirmationMailCheckboxLabel = confirmationMailCheckboxLabel;
    }

    /**
     * Sets if the optional confirmation mail is enabled.<p>
     *
     * @param confirmationMailEnabled true if the optional confirmation mail is enabled, otherwise false
     */
    protected void setConfirmationMailEnabled(boolean confirmationMailEnabled) {

        m_confirmationMailEnabled = confirmationMailEnabled;
    }

    /**
     * Sets the index number of the input field containing the email address for the optional confirmation mail.<p>
     *
     * @param confirmationMailFieldName the name of the input field containing the email address for the optional confirmation mail
     */
    protected void setConfirmationMailField(int confirmationMailFieldName) {

        m_confirmationMailField = confirmationMailFieldName;
    }

    /**
     * Sets if the confirmation mail if optional, i.e. selectable by the form submitter.<p>
     *
     * @param confirmationMailOptional true if the confirmation mail if optional, i.e. selectable by the form submitter, otherwise false
     */
    protected void setConfirmationMailOptional(boolean confirmationMailOptional) {

        m_confirmationMailOptional = confirmationMailOptional;
    }

    /**
     * Sets the subject of the optional confirmation mail.<p>
     *
     * @param confirmationMailSubject the subject of the optional confirmation mail
     */
    protected void setConfirmationMailSubject(String confirmationMailSubject) {

        m_confirmationMailSubject = confirmationMailSubject;
    }

    /**
     * Sets the text of the optional confirmation mail.<p>
     *
     * @param confirmationMailText the text of the optional confirmation mail
     */
    protected void setConfirmationMailText(String confirmationMailText) {

        m_confirmationMailText = confirmationMailText;
    }

    /**
     * Sets the plain text of the optional confirmation mail.<p>
     *
     * @param confirmationMailTextPlain the plain text of the optional confirmation mail
     */
    protected void setConfirmationMailTextPlain(String confirmationMailTextPlain) {

        m_confirmationMailTextPlain = confirmationMailTextPlain;
    }

    /**
     * Sets the global form attributes.<p>
     * 
     * @param formAttributes the global form attributes
     */
    protected void setFormAttributes(String formAttributes) {

        m_formAttributes = formAttributes;
    }

    /**
     * Sets the form check text.<p>
     * 
     * @param formCheckText the form confirmation text
     */
    protected void setFormCheckText(String formCheckText) {

        m_formCheckText = formCheckText;
    }

    /**
     * Sets the form confirmation text.<p>
     * 
     * @param formConfirmationText the form confirmation text
     */
    protected void setFormConfirmationText(String formConfirmationText) {

        m_formConfirmationText = formConfirmationText;
    }

    /**
     * Sets the optional form input field attributes.<p>
     * 
     * @param formFieldAttributes the optional form input field attributes
     */
    protected void setFormFieldAttributes(String formFieldAttributes) {

        m_formFieldAttributes = formFieldAttributes;
    }

    /**
     * Sets the form text.<p>
     * 
     * @param formText the form text
     */
    protected void setFormText(String formText) {

        m_formText = formText;
    }

    /**
     * Sets if at least one of the configured fields is mandatory.<p>
     *
     * @param hasMandatoryFields true if at least one of the configured fields is mandatory, otherwise false
     */
    protected void setHasMandatoryFields(boolean hasMandatoryFields) {

        m_hasMandatoryFields = hasMandatoryFields;
    }

    /**
     * Sets the mail bcc recipient(s).<p>
     * 
     * @param mailBCC the mail bcc recipient(s)
     */
    protected void setMailBCC(String mailBCC) {

        m_mailBCC = mailBCC;
    }

    /**
     * Sets the mail cc recipient(s).<p>
     * 
     * @param mailCC the mail cc recipient(s)
     */
    protected void setMailCC(String mailCC) {

        m_mailCC = mailCC;
    }

    /**
     * Sets the mail sender address.<p>
     * 
     * @param mailFrom the mail sender address
     */
    protected void setMailFrom(String mailFrom) {

        m_mailFrom = mailFrom;
    }
    
    /**
     * Sets the mail subject.<p>
     * 
     * @param mailSubject the mail subject
     */
    protected void setMailSubject(String mailSubject) {

        m_mailSubject = mailSubject;
    }

    /**
     * Sets the mail subject prefix.<p>
     * 
     * @param mailSubjectPrefix the mail subject prefix
     */
    protected void setMailSubjectPrefix(String mailSubjectPrefix) {

        m_mailSubjectPrefix = mailSubjectPrefix;
    }

    /**
     * Sets the mail text.<p>
     * 
     * @param mailText the mail text
     */
    protected void setMailText(String mailText) {

        m_mailText = mailText;
    }
    
    /**
     * Sets the mail text as plain text.<p>
     * 
     * @param mailTextPlain the mail text as plain text
     */
    protected void setMailTextPlain(String mailTextPlain) {

        m_mailTextPlain = mailTextPlain;
    }
    
    /**
     * Sets the mail recipient(s).<p>
     * 
     * @param mailTo the mail recipient(s)
     */
    protected void setMailTo(String mailTo) {

        m_mailTo = mailTo;
    }
    
    /**
     * Sets the mail type ("text" or "html").<p>
     * 
     * @param mailType the mail type
     */
    protected void setMailType(String mailType) {

        m_mailType = mailType;
    }
    
    /**
     * Sets if the check page should be shown.<p>
     *
     * @param showCheck true if the check page should be shown, otherwise false
     */
    protected void setShowCheck(boolean showCheck) {

        m_showCheck = showCheck;
    }
    
    /**
     * Sets the target URI of this form.<p>
     * 
     * This optional target URI can be used to redirect the user to an OpenCms page instead of displaying a confirmation
     * text from the form's XML content.<p>
     * 
     * @param targetUri the target URI
     */
    protected void setTargetUri(String targetUri) {
        m_targetUri = targetUri;
    }
    
    /**
     * Creates the checkbox field to activate the confirmation mail in the input form.<p>
     * 
     * @param messages the localized messages
     * @param initial if true, field values are filled with values specified in the XML configuration, otherwise values are read from the request
     * @return the checkbox field to activate the confirmation mail in the input form
     */
    private I_CmsField createConfirmationMailCheckbox(CmsMessages messages, boolean initial) {

        A_CmsField field = new CmsCheckboxField();
        field.setName(PARAM_SENDCONFIRMATION);
        field.setLabel(messages.key("form.confirmation.label"));
        // check the field status
        boolean isChecked = false;
        if (!initial && Boolean.valueOf(getParameter(PARAM_SENDCONFIRMATION)).booleanValue()) {
            // checkbox is checked by user
            isChecked = true;
        }
        // create item for field
        CmsFieldItem item = new CmsFieldItem(Boolean.toString(true), getConfirmationMailCheckboxLabel(), isChecked);
        List items = new ArrayList(1);
        items.add(item);
        field.setItems(items);
        return field;
    }
    
    /**
     * Checks if the given value is empty and returns in that case the default value.<p>
     * 
     * @param value the configuration value to check
     * @param defaultValue the default value to return in case the value is empty
     * @return the checked value
     */
    private String getConfigurationValue(String value, String defaultValue) {

        if (CmsStringUtil.isNotEmpty(value)) {
            return value;
        }
        return defaultValue;
    }
    
    /**
     * Initializes the optional captcha field.<p>
     * 
     * @param jsp the initialized CmsJspActionElement to access the OpenCms API
     * @param xmlContent the XML configuration content
     * @param locale the currently active Locale
     * @param initial if true, field values are filled with values specified in the XML configuration, otherwise values are read from the request
     */
    private void initCaptchaField(CmsJspActionElement jsp, CmsXmlContent xmlContent, Locale locale, boolean initial) {
        
        boolean captchaFieldIsOnInputPage = captchaFieldIsOnInputPage();
        boolean displayCheckPage = captchaFieldIsOnCheckPage() && isInputFormSubmitted();
        boolean submittedCheckPage = captchaFieldIsOnCheckPage() && isCheckPageSubmitted();

        // Todo: read the captcha settings here, don't provide xmlcontent with form!!!
        if (captchaFieldIsOnInputPage || displayCheckPage || submittedCheckPage) {

            CmsObject cms = jsp.getCmsObject();
            
            I_CmsXmlContentValue xmlValueCaptcha = xmlContent.getValue(NODE_CAPTCHA, locale);
            if (xmlValueCaptcha != null) {

                // get the field label
                String xPathCaptcha = xmlValueCaptcha.getPath() + "/";
                String stringValue = xmlContent.getStringValue(cms, xPathCaptcha + NODE_FIELDLABEL, locale);
                String fieldLabel = getConfigurationValue(stringValue, "");

                // get the field value
                String fieldValue = "";
                if (!initial) {
                    fieldValue = getParameter(CmsCaptchaField.C_PARAM_CAPTCHA_PHRASE);
                    if (fieldValue == null) {
                        fieldValue = "";
                    }
                }

                // get the image settings from the XML content
                CmsCaptchaSettings captchaSettings = CmsCaptchaSettings.getInstance(jsp);
                captchaSettings.init(cms, xmlContent, locale);
                m_captchaField = new CmsCaptchaField(captchaSettings, fieldLabel, fieldValue);
            }
        }
    }
    
    /**
     * Initializes the general online form settings.<p>
     * 
     * @param content the XML configuration content
     * @param cms the CmsObject to access the content values
     * @param locale the currently active Locale
     * @param messages the localized messages
     * @throws Exception if initializing the form settings fails
     */
    private void initFormGlobalConfiguration(CmsXmlContent content, CmsObject cms, Locale locale, CmsMessages messages)
    throws Exception {

        // get the form text
        String stringValue = content.getStringValue(cms, NODE_FORMTEXT, locale);
        setFormText(getConfigurationValue(stringValue, ""));
        // get the form confirmation text
        stringValue = content.getStringValue(cms, NODE_FORMCONFIRMATION, locale);
        setFormConfirmationText(getConfigurationValue(stringValue, ""));
        // get the optional target URI
        stringValue = content.getStringValue(cms, NODE_TARGET_URI, locale);
        setTargetUri(getConfigurationValue(stringValue, ""));
        // get the mail from address
        stringValue = content.getStringValue(cms, NODE_MAILFROM, locale);
        setMailFrom(getConfigurationValue(stringValue, ""));
        // get the mail to address(es)
        stringValue = content.getStringValue(cms, NODE_MAILTO, locale);
        setMailTo(getConfigurationValue(stringValue, ""));
        // get the mail subject
        stringValue = content.getStringValue(cms, NODE_MAILSUBJECT, locale);
        setMailSubject(getConfigurationValue(stringValue, ""));
        // get the optional mail subject prefix from localized messages
        stringValue = messages.key("form.mailsubject.prefix");
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(stringValue)) {
            // prefix present, set it
            setMailSubjectPrefix(stringValue + " ");
        } else {
            // no prefix present
            setMailSubjectPrefix("");
        }
        
        CmsXmlHtmlValue mailTextValue = (CmsXmlHtmlValue)content.getValue(NODE_MAILTEXT, locale);
        if (mailTextValue != null) {
            // get the mail text as plain text
            stringValue = mailTextValue.getPlainText(cms);
            setMailTextPlain(getConfigurationValue(stringValue, ""));
            // get the mail text
            stringValue = mailTextValue.getStringValue(cms);
            setMailText(getConfigurationValue(stringValue, ""));
        } else {
            setMailTextPlain("");
            setMailText("");
        }

        // optional configuration options
        String pathPrefix = NODE_OPTIONALCONFIGURATION + "/";

        // get the mail type
        stringValue = content.getStringValue(cms, pathPrefix + NODE_MAILTYPE, locale);
        setMailType(getConfigurationValue(stringValue, MAILTYPE_HTML));
        // get the mail CC recipient(s)
        stringValue = content.getStringValue(cms, pathPrefix + NODE_MAILCC, locale);
        setMailCC(getConfigurationValue(stringValue, ""));
        // get the mail BCC recipient(s)
        stringValue = content.getStringValue(cms, pathPrefix + NODE_MAILBCC, locale);
        setMailBCC(getConfigurationValue(stringValue, ""));
        // get the form check page flag
        stringValue = content.getStringValue(cms, pathPrefix + NODE_SHOWCHECK, locale);
        setShowCheck(Boolean.valueOf(stringValue).booleanValue());
        // get the check page text
        stringValue = content.getStringValue(cms, pathPrefix + NODE_FORMCHECKTEXT, locale);
        setFormCheckText(getConfigurationValue(stringValue, ""));
        // get the form attributes
        stringValue = content.getStringValue(cms, pathPrefix + NODE_FORMATTRIBUTES, locale);
        if (CmsStringUtil.isNotEmpty(stringValue)) {
            setFormAttributes(" " + stringValue);
        }
        // get the field attributes
        stringValue = content.getStringValue(cms, pathPrefix + NODE_FORMFIELDATTRIBUTES, locale);
        if (CmsStringUtil.isNotEmpty(stringValue)) {
            setFormFieldAttributes(" " + stringValue);
        } else {
            // no field attributes specified, check default field attributes
            String defaultAttributes = messages.key("form.field.default.attributes");
            if (CmsStringUtil.isNotEmpty(defaultAttributes)) {
                setFormFieldAttributes(" " + defaultAttributes);
            }
        }

        // optional confirmation mail nodes
        pathPrefix = NODE_OPTIONALCONFIRMATION + "/";

        // get the confirmation mail enabled flag
        stringValue = content.getStringValue(cms, pathPrefix + NODE_CONFIRMATIONMAILENABLED, locale);
        setConfirmationMailEnabled(Boolean.valueOf(stringValue).booleanValue());
        // get other confirmation mail nodes only if confirmation mail is enabled
        if (isConfirmationMailEnabled()) {
            // get the confirmation mail subject
            stringValue = content.getStringValue(cms, pathPrefix + NODE_CONFIRMATIONMAILSUBJECT, locale);
            setConfirmationMailSubject(getConfigurationValue(stringValue, ""));
           
            mailTextValue = (CmsXmlHtmlValue)content.getValue(pathPrefix + NODE_CONFIRMATIONMAILTEXT, locale);
            if (mailTextValue != null) {
                // get the confirmation mail text
                stringValue = mailTextValue.getPlainText(cms);
                setConfirmationMailTextPlain(getConfigurationValue(stringValue, ""));
                stringValue = mailTextValue.getStringValue(cms);
                setConfirmationMailText(getConfigurationValue(stringValue, ""));
            } else {
                setConfirmationMailTextPlain("");
                setConfirmationMailText("");
            }
            
            // get the confirmation mail field index number
            stringValue = content.getStringValue(cms, pathPrefix + NODE_CONFIRMATIONMAILFIELD, locale);
            int fieldIndex = 1;
            try {
                fieldIndex = Integer.parseInt(getConfigurationValue(stringValue, "1")) - 1;
            } catch (Exception e) {
                // ignore this exception, use first field
            }
            setConfirmationMailField(fieldIndex);
            // get the confirmation mail optional flag
            stringValue = content.getStringValue(cms, pathPrefix + NODE_CONFIRMATIONMAILOPTIONAL, locale);
            setConfirmationMailOptional(Boolean.valueOf(stringValue).booleanValue());
            // get the confirmation mail checkbox label text
            stringValue = content.getStringValue(cms, pathPrefix + NODE_CONFIRMATIONMAILCHECKBOXLABEL, locale);
            setConfirmationMailCheckboxLabel(getConfigurationValue(
                stringValue,
                messages.key("form.confirmation.checkbox")));
        }
    }
    
    /**
     * Initializes the field objects of the form.<p>
     * 
     * @param content the XML configuration content
     * @param jsp the initialized CmsJspActionElement to access the OpenCms API
     * @param locale the currently active Locale
     * @param messages the localized messages
     * @param initial if true, field values are filled with values specified in the XML configuration, otherwise values are read from the request
     * @throws CmsConfigurationException if parsing the configuration fails 
     */
    private void initInputFields(
        CmsXmlContent content,
        CmsJspActionElement jsp,
        Locale locale,
        CmsMessages messages,
        boolean initial) throws CmsConfigurationException {

        CmsObject cms = jsp.getCmsObject();
        List fieldValues = content.getValues(NODE_INPUTFIELD, locale);
        int fieldValueSize = fieldValues.size();
        CmsFieldFactory fieldFactory = CmsFieldFactory.getSharedInstance();
        Map fileUploads = (Map)jsp.getRequest().getSession().getAttribute(CmsFormHandler.ATTRIBUTE_FILEITEMS);
        
        for (int i = 0; i < fieldValueSize; i++) {
            I_CmsXmlContentValue inputField = (I_CmsXmlContentValue)fieldValues.get(i);
            String inputFieldPath = inputField.getPath() + "/";
            A_CmsField field = null;

            // get the field from the factory for the specified type
            String stringValue = content.getStringValue(cms, inputFieldPath + NODE_FIELDTYPE, locale);
            field = fieldFactory.getField(stringValue);
            
            // create the field name
            field.setName(inputFieldPath.substring(0, inputFieldPath.length() - 1));
            // get the field label
            stringValue = content.getStringValue(cms, inputFieldPath + NODE_FIELDLABEL, locale);
            field.setLabel(getConfigurationValue(stringValue, ""));
            // validation error message
            stringValue = content.getStringValue(cms, inputFieldPath + NODE_FIELDERRORMESSAGE, locale);
            field.setErrorMessage(stringValue);
            // get the field value
            if (initial && CmsStringUtil.isEmpty(getParameter(field.getName()))) {
                // only fill in values from configuration file if called initially
                String fieldValue = content.getStringValue(cms, inputFieldPath + NODE_FIELDDEFAULTVALUE, locale);
                if (CmsStringUtil.isNotEmpty(fieldValue)) {
                    CmsMacroResolver resolver = CmsMacroResolver.newInstance().setCmsObject(cms).setJspPageContext(
                        jsp.getJspContext());
                    field.setValue(resolver.resolveMacros(fieldValue));
                }
            } else {
                // get field value from request for standard fields
                String[] parameterValues = (String[])m_parameterMap.get(field.getName()); 
                StringBuffer value = new StringBuffer();
                if (parameterValues != null) {
                    for (int j = 0; j < parameterValues.length; j++) {
                        if (j != 0) {
                            value.append(", ");
                        }
                        value.append(parameterValues[j]);
                    }
                }
                field.setValue(value.toString());
            }

            // fill object members in case this is no hidden field
            if (!CmsHiddenField.class.isAssignableFrom(field.getClass())) {
                // get the field validation regular expression
                stringValue = content.getStringValue(cms, inputFieldPath + NODE_FIELDVALIDATION, locale);
                if (CmsEmailField.class.isAssignableFrom(field.getClass()) && CmsStringUtil.isEmpty(stringValue)) {
                    // set default email validation expression for confirmation email address input field
                    field.setValidationExpression(CmsEmailField.VALIDATION_REGEX);
                } else {
                    field.setValidationExpression(getConfigurationValue(stringValue, ""));
                }
                if (CmsFileUploadField.class.isAssignableFrom(field.getClass())) {
                    if (fileUploads != null) {
                        FileItem attachment = (FileItem)fileUploads.get(field.getName());
                        if (attachment != null) {
                            ((CmsFileUploadField)field).setFileSize(attachment.get().length);
                        }
                    }
                }
                // get the field mandatory flag
                stringValue = content.getStringValue(cms, inputFieldPath + NODE_FIELDMANDATORY, locale);
                boolean isMandatory = Boolean.valueOf(stringValue).booleanValue();
                field.setMandatory(isMandatory);
                if (isMandatory) {
                    // set flag that determines if mandatory fields are present
                    setHasMandatoryFields(true);
                }

                if (field.needsItems()) {
                    // create items for checkboxes, radio buttons and selectboxes
                    String fieldValue = content.getStringValue(cms, inputFieldPath + NODE_FIELDDEFAULTVALUE, locale);
                    if (CmsStringUtil.isNotEmpty(fieldValue)) {
                        // get items from String                       
                        StringTokenizer T = new StringTokenizer(fieldValue, "|");
                        List items = new ArrayList(T.countTokens());
                        while (T.hasMoreTokens()) {
                            String part = T.nextToken();
                            // check preselection of current item
                            boolean isPreselected = part.indexOf('*') != -1;
                            String value = "";
                            String label = "";
                            String selected = "";
                            int delimPos = part.indexOf(':');
                            if (delimPos != -1) {
                                // a special label text is given
                                value = part.substring(0, delimPos);
                                label = part.substring(delimPos + 1);
                            } else {
                                // no special label text present, use complete String
                                value = part;
                                label = value;
                            }

                            if (isPreselected) {
                                // remove preselected flag marker from Strings
                                value = CmsStringUtil.substitute(value, "*", "");
                                label = CmsStringUtil.substitute(label, "*", "");
                            }

                            if (initial) {
                                // only fill in values from configuration file if called initially
                                if (isPreselected) {
                                    selected = Boolean.toString(true);
                                }
                            } else {
                                // get selected flag from request for current item
                                selected = readSelectedFromRequest(field, value);
                            }
                            // add new item object
                            items.add(new CmsFieldItem(value, label, Boolean.valueOf(selected).booleanValue()));
                        }
                        field.setItems(items);
                    } else {
                        // no items specified for checkbox, radio button or selectbox
                        throw new CmsConfigurationException(Messages.get().container(
                            Messages.ERR_INIT_INPUT_FIELD_MISSING_ITEM_2,
                            field.getName(),
                            field.getType()));
                    }
                }
            }

            addField(field);
        }

        // validate the form configuration
        validateFormConfiguration(messages);

        if (isConfirmationMailEnabled() && isConfirmationMailOptional()) {
            
            // add the checkbox to activate confirmation mail for customer
            I_CmsField confirmationMailCheckbox = createConfirmationMailCheckbox(messages, initial);
            addField(confirmationMailCheckbox);
        }
    }
    
    /**
     * Returns the request parameter with the specified name.<p>
     * 
     * @param parameter the parameter to return
     * 
     * @return the parameter value
     */
    private String getParameter(String parameter) {
        
        try {
            return ((String[])m_parameterMap.get(parameter))[0];
        } catch (NullPointerException e) {
            return "";
        }
    }
    
    /**
     * Initializes the member variables.<p>
     */
    private void initMembers() {

        setConfigurationErrors(new ArrayList());
        setFormAttributes("");
        setFormCheckText("");
        setFormConfirmationText("");
        setFormFieldAttributes("");
        setFormText("");
        setMailBCC("");
        setMailCC("");
        setMailFrom("");
        setMailSubject("");
        setMailText("");
        setMailTextPlain("");
        setMailTo("");
        setMailType(MAILTYPE_HTML);
        setConfirmationMailSubject("");
        setConfirmationMailText("");
        setConfirmationMailTextPlain("");
    }
    
    /**
     * Validates the loaded online form configuration and creates a list of error messages, if necessary.<p>
     * 
     * @param messages the localized messages
     */
    private void validateFormConfiguration(CmsMessages messages) {

        if (isConfirmationMailEnabled()) {
            // confirmation mail is enabled, make simple field check to avoid errors
            I_CmsField confirmField;
            try {
                // try to get the confirmation email field
                confirmField = (I_CmsField)getFields().get(getConfirmationMailField());
            } catch (IndexOutOfBoundsException e) {
                // specified confirmation email field does not exist
                getConfigurationErrors().add(messages.key("form.configuration.error.emailfield.notfound"));
                setConfirmationMailEnabled(false);
                return;
            }
            if (!CmsEmailField.class.isAssignableFrom(confirmField.getClass())) {
                // specified confirmation mail input field has wrong field type
                getConfigurationErrors().add(messages.key("form.configuration.error.emailfield.type"));
            }
        }
    }

}
