/*
 * Copyright (c) 2002-2025, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.attachmentrequest.util;

/**
 * 
 * AttachmentRequestConstants
 *
 */
public class AttachmentRequestConstants
{
    // CONSTANTS
    public static final String METADATA_CLIENT_CODE                                 = "client_code";
    public static final String METADATA_PROVIDER_NAME                               = "provider_name";
    public static final String METADATA_FILE_NAME                                   = "file_name";
    public static final String METADATA_FILE_PATH                                   = "file_path";
    public static final String METADATA_FILES                                       = "files";

    // ATTRIBUTES
    public static final String ATTRIBUTE_DB_IDENTITY_LAST_NAME                      = "family_name";
    public static final String ATTRIBUTE_DB_IDENTITY_PREFERRED_USER_NAME            = "preferred_username";
    public static final String ATTRIBUTE_DB_IDENTITY_FIRSTNAME                      = "first_name";
    public static final String ATTRIBUTE_DB_IDENTITY_GENDER                         = "gender";
    public static final String ATTRIBUTE_DB_IDENTITY_BIRTHDATE                      = "birthdate";
    public static final String ATTRIBUTE_DB_IDENTITY_BIRTHPLACE                     = "birthplace";
    public static final String ATTRIBUTE_DB_IDENTITY_BIRTHCOUNTRY                   = "birthcountry";
    public static final String ATTRIBUTE_DB_IDENTITY_BIRTHPLACE_CODE                = "birthplace_code";
    public static final String ATTRIBUTE_DB_IDENTITY_BIRTHCOUNTRY_CODE              = "birthcountry_code";

    public static final String ATTRIBUTE_KEY_LAST_NAME                              = "lastName";
    public static final String ATTRIBUTE_KEY_PREFERRED_USER_NAME                    = "preferredUsername";
    public static final String ATTRIBUTE_KEY_FIRSTNAME                              = "firstname";
    public static final String ATTRIBUTE_KEY_GENDER                                 = "gender";
    public static final String ATTRIBUTE_KEY_BIRTHDATE                              = "birthdate";
    public static final String ATTRIBUTE_KEY_BIRTHPLACE                             = "birthplace";
    public static final String ATTRIBUTE_KEY_BIRTHCOUNTRY                           = "birthcountry";
    public static final String ATTRIBUTE_KEY_BIRTHPLACE_CODE                        = "birthplace_code";
    public static final String ATTRIBUTE_KEY_BIRTHCOUNTRY_CODE                      = "birthcountry_code";

    public static final String PREFIX_KEY_ERROR_MESSAGE                             = "attachment-request.pj_certification_form.error.message.";

    // Propeties
    public static final String PROPERTY_USER_INFO_DISPLAY_MIN_LEVEL                 = "attachment-request.user-info-block.display.min_level";
    public static final String PROPERTY_USER_INFO_DISPLAY_MAX_LEVEL                 = "attachment-request.user-info-block.display.max_level";
    public static final String PROPERTY_ACCESS_PROCESS_ATTACHMENT_REQUEST_MIN_LEVEL = "attachment-request.access.process.attachment.request.min_level";
    public static final String PROPERTY_ACCESS_PROCESS_ATTACHMENT_REQUEST_MAX_LEVEL = "attachment-request.access.process.attachment.request.max_level";
    
    public static final String MESSAGE_ERROR_ATTACHMENT                             = "attachmentrequest.pj_certification_form.error.message.attachment.empty";
}
