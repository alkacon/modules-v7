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

import java.util.List;

/**
 * Defines the methods required for form fields.<p>
 * 
 */
public interface I_CmsField {

    /**
     * Returns the list of items for select boxes, radio buttons and checkboxes.<p>
     * 
     * The list contains CmsFieldItem objects with the following information:
     * <ol>
     * <li>the value of the item</li>
     * <li>the description of the item</li>
     * <li>the selection of the item (true or false)</li>
     * </ol>
     * 
     * @return the list of items for select boxes, radio buttons and checkboxes
     */
    List getItems();

    /**
     * Returns the description text of the input field.<p>
     * 
     * @return the description text of the input field
     */
    String getLabel();

    /**
     * Returns the name of the input field.<p>
     * 
     * @return the name of the input field
     */
    String getName();

    /**
     * Returns the type of the input field, e.g. "text" or "select".<p>
     * 
     * @return the type of the input field
     */
    String getType();

    /**
     * Returns the regular expression that is used for validation of the field.<p>
     * 
     * @return the regular expression that is used for validation of the field
     */
    String getValidationExpression();

    /**
     * Returns the initial value of the field.<p>
     * 
     * @return the initial value of the field
     */
    String getValue();
    
    /**
     * Returns a optional, custom error message to be displayed instead of the standard validation error message.<p>
     * 
     * @return a custom error message for validation errors, or null
     */
    String getErrorMessage();

    /**
     * Returns if this input field is mandatory.<p>
     * 
     * @return true if this input field is mandatory, otherwise false
     */
    boolean isMandatory();

    /**
     * Checks if an item list is needed for this field.<p>
     * 
     * @return true if an item list is needed for this field, otherwise false
     */
    boolean needsItems();

    /**
     * Validates this field by validating it's constraints and input value.<p>
     * 
     * @param formHandler the handler of the current form
     * @return null in case of no error, {@link CmsFormHandler#ERROR_VALIDATION} if validation of the input value failed, {@link CmsFormHandler#ERROR_VALIDATION} if validation of the input value failed
     */
    String validate(CmsFormHandler formHandler);
    
    /**
     * Builds the HTML input element for this element to be used in a frontend JSP.<p>
     * 
     * @param formHandler the handler of the current form
     * @param messages a resource bundle containing HTML snippets to build the HTML element
     * @param errorKey the key of the current error message
     * 
     * @return the HTML input element for this element to be used in a frontend JSP
     */
    String buildHtml(CmsFormHandler formHandler, CmsMessages messages, String errorKey);
    
    
    /**
     * Returns the placeholder.<p>
     *
     * @return the placeholder
     */
    int getPlaceholder();

    /**
     * Sets the placeholder.<p>
     *
     * @param placeholder the placeholder to set
     */
    void setPlaceholder(int placeholder);

    /**
     * Returns the position.<p>
     *
     * @return the position
     */
    int getPosition();
    
    /**
     * Sets the position.<p>
     *
     * @param position the position to set
     */
    void setPosition(int position);
    
}