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
 * For further information about Alkacon Software, please see the
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

import org.opencms.i18n.CmsMessages;
import org.opencms.util.CmsStringUtil;

/**
 * Represents a text input field.<p>
 * 
 */
public class CmsTextField extends A_CmsField {

    /** HTML field type: text input. */
    private static final String TYPE = "text";

    /**
     * @see org.opencms.frontend.templateone.form.I_CmsField#getType()
     */
    public String getType() {

        return TYPE;
    }

    /**
     * Returns the type of the input field, e.g. "text" or "select".<p>
     * 
     * @return the type of the input field
     */
    public static String getStaticType() {

        return TYPE;
    }

    /**
     * @see org.opencms.frontend.templateone.form.I_CmsField#buildHtml(CmsFormHandler, org.opencms.i18n.CmsMessages, String)
     */
    public String buildHtml(CmsFormHandler formHandler, CmsMessages messages, String errorKey) {

        StringBuffer buf = new StringBuffer();
        String fieldLabel = getLabel();
        String errorMessage = "";
        String mandatory = "";

        if (CmsStringUtil.isNotEmpty(errorKey)) {

            if (CmsFormHandler.ERROR_MANDATORY.equals(errorKey)) {
                errorMessage = messages.key("form.error.mandatory");
            } else if (CmsStringUtil.isNotEmpty(getErrorMessage())) {
                errorMessage = getErrorMessage();
            } else {
                errorMessage = messages.key("form.error.validation");
            }

            errorMessage = messages.key("form.html.error.start") + errorMessage + messages.key("form.html.error.end");
            fieldLabel = messages.key("form.html.label.error.start")
                + fieldLabel
                + messages.key("form.html.label.error.end");
        }

        if (isMandatory()) {
            mandatory = messages.key("form.html.mandatory");
        }

        // line #1
        //if(!messages.key("form.html.useCounter").equalsIgnoreCase("true") || !(pos % 2==0)){
        if (showRowStart(messages.key("form.html.col.two"))) {
            buf.append(messages.key("form.html.row.start")).append("\n");
        }

        // line #2
        buf.append(messages.key("form.html.label.start")).append(fieldLabel).append(mandatory).append(
            messages.key("form.html.label.end")).append("\n");

        // line #3
        buf.append(messages.key("form.html.field.start")).append("<input type=\"text\" name=\"").append(getName()).append(
            "\" value=\"").append(CmsStringUtil.escapeHtml(getValue())).append("\"").append(
            formHandler.getFormConfiguration().getFormFieldAttributes()).append("/>").append(errorMessage).append(
            messages.key("form.html.field.end")).append("\n");

        // line #4
        //if(!messages.key("form.html.useCounter").equalsIgnoreCase("true") || (pos % 2==0)){
        if (showRowEnd(messages.key("form.html.col.two"))) {
            buf.append(messages.key("form.html.row.end")).append("\n");
        }

        return buf.toString();
    }

}
