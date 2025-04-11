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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.AttributeDefinitionDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.attachmentrequest.business.IdentityFormDTO;
import fr.paris.lutece.plugins.attachmentrequest.service.AttachmentRequestService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * AttachmentRequestUtils
 *
 */
public class AttachmentRequestUtils
{
    /**
     * Private constructor
     */
    private AttachmentRequestUtils ()
    {
        //Do nothing
    }
    
    
    /**
     * Convert to identity form dto
     * @param identityDTO
     * @return
     */
    public static IdentityFormDTO convertToIdentityFormDTO ( IdentityDto identityDTO )
    {
        IdentityFormDTO identityFormDTO = new IdentityFormDTO( );
        
        if( identityDTO != null )
        {
            for( AttributeDto attribute : identityDTO.getAttributes( ) )
            {
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_LAST_NAME ) )
                {
                    identityFormDTO.setLastName( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_PREFERRED_USER_NAME ) )
                {
                    identityFormDTO.setPreferredUsername( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_FIRSTNAME ) )
                {
                    identityFormDTO.setFirstname( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_GENDER ) )
                {
                    identityFormDTO.setGender( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHDATE ) )
                {
                    identityFormDTO.setBirthdate( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHPLACE ) )
                {
                    identityFormDTO.setBirthplace( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHCOUNTRY ) )
                {
                    identityFormDTO.setBirthcountry( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHPLACE_CODE ) )
                {
                    identityFormDTO.setBirthplaceCode( attribute.getValue( ) );
                }
                if( attribute.getKey( ).equals( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHCOUNTRY_CODE ) )
                {
                    identityFormDTO.setBirthcountryCode( attribute.getValue( ) );
                }
            }
        }
        
        return identityFormDTO;        
    }
    
    /**
     * Update identityDto data
     * @param identityDto
     * @param identityFormDTO
     */
    public static void updateIdentityDto ( IdentityDto identityDto, IdentityFormDTO identityFormDTO )
    {
        if( identityDto != null )
        {
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_LAST_NAME, identityFormDTO.getLastName( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_PREFERRED_USER_NAME, identityFormDTO.getPreferredUsername( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_FIRSTNAME, identityFormDTO.getFirstname( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_GENDER, identityFormDTO.getGender( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHDATE, identityFormDTO.getBirthdate( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHPLACE, identityFormDTO.getBirthplace( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHCOUNTRY, identityFormDTO.getBirthcountry( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHPLACE_CODE, identityFormDTO.getBirthplaceCode( )  );
            setAttributeDto( identityDto, AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHCOUNTRY_CODE, identityFormDTO.getBirthcountryCode( )  );

        }
    }
    
    /**
     * Set attribute dto with form value
     * @param identityDto
     * @param strAttributeKey
     * @param value
     */
    private static void setAttributeDto ( IdentityDto identityDto, String strAttributeKey, String value )
    {
        boolean bIdentityContainsAttribute = false;
        for( AttributeDto attribute : identityDto.getAttributes( ) )
        {
            if( attribute.getKey( ).equals( strAttributeKey ) )
            {
                attribute.setValue( value );
                bIdentityContainsAttribute = true;
            }
        }
        
        //Create attribute if it does not exist in identity
        if( !bIdentityContainsAttribute )
        {
            AttributeDto newAttribute = new AttributeDto( );
            newAttribute.setKey( strAttributeKey );
            newAttribute.setValue( value );
            newAttribute.setCertificationLevel( 100 );
            newAttribute.setCertifier( "DEC" );
            newAttribute.setCertificationDate( new Date() );
            newAttribute.setLastUpdateDate( Timestamp.from( Instant.now( ) ) );
            
            identityDto.getAttributes( ).add( newAttribute );
        }   
    }
    
    
    public static Map<String, String> checkIdentityForm ( IdentityFormDTO identityFormDto )
    {
        Map<String, String> errors = new HashMap< >( );
        
        if( StringUtils.isEmpty( identityFormDto.getGender( ) ) )
        {
            errors.put( AttachmentRequestConstants.ATTRIBUTE_KEY_GENDER, getMessageError( AttachmentRequestConstants.PREFIX_KEY_ERROR_MESSAGE + AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_GENDER ) );
        }
        if( StringUtils.isEmpty( identityFormDto.getLastName( ) ) )
        {
            errors.put( AttachmentRequestConstants.ATTRIBUTE_KEY_LAST_NAME, getMessageError( AttachmentRequestConstants.PREFIX_KEY_ERROR_MESSAGE + AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_LAST_NAME ) );
        }
        if( StringUtils.isEmpty( identityFormDto.getFirstname( ) ) )
        {
            errors.put( AttachmentRequestConstants.ATTRIBUTE_KEY_FIRSTNAME, getMessageError( AttachmentRequestConstants.PREFIX_KEY_ERROR_MESSAGE + AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_FIRSTNAME ) );
        }
        if( StringUtils.isEmpty( identityFormDto.getBirthdate( ) ) )
        {
            errors.put( AttachmentRequestConstants.ATTRIBUTE_KEY_BIRTHDATE, getMessageError( AttachmentRequestConstants.PREFIX_KEY_ERROR_MESSAGE + AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHDATE ) );
        }
        if( StringUtils.isEmpty( identityFormDto.getBirthcountry( ) ) )
        {
            errors.put( AttachmentRequestConstants.ATTRIBUTE_KEY_BIRTHCOUNTRY, getMessageError( AttachmentRequestConstants.PREFIX_KEY_ERROR_MESSAGE + AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHCOUNTRY ) );
        }
        if( StringUtils.isEmpty( identityFormDto.getBirthplace( ) ) )
        {
            errors.put( AttachmentRequestConstants.ATTRIBUTE_KEY_BIRTHPLACE, getMessageError( AttachmentRequestConstants.PREFIX_KEY_ERROR_MESSAGE + AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHPLACE ) );
        }    
        
        return errors;
    }
    
    private static String getMessageError ( String key )
    {
        return  I18nService.getLocalizedString( key, Locale.getDefault( ) );
    }
    
    /**
     * Allows the display of the user information block in the form to be conditional.
     * @param identityDto
     * @return Returns true if the user information block should be displayed
     */
    public static boolean canShowUserDataForm( IdentityDto identityDto )
    {
        int nLevelMin  = AppPropertiesService.getPropertyInt( AttachmentRequestConstants.PROPERTY_USER_INFO_DISPLAY_MIN_LEVEL, 0 );
        int nLevelMax  = AppPropertiesService.getPropertyInt( AttachmentRequestConstants.PROPERTY_USER_INFO_DISPLAY_MAX_LEVEL, 0 );
        
        //If the values are 0, the control is disabled.
        if( nLevelMin == 0 && nLevelMax == 0 )
        {
            return true;
        }
        if( identityDto != null && identityDto.getAttributes( ) != null && !identityDto.getAttributes( ).isEmpty( ) )
        {
            for ( String attributeKey : getListDbAttributes( ) )
            {
                Integer nCertificationLevel = getCertificationLevel( identityDto.getAttributes( ), attributeKey );
    
                if ( nCertificationLevel != null && ( nCertificationLevel >= nLevelMin && nCertificationLevel >= nLevelMax ) )
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * This method determines whether a user is authorized to initiate an attachment request. 
     * It verifies that the user's certification level is strictly lower than the level required by the service contract, 
     * and that the minimum certification level required by the SN system falls within the LEVEL_MIN and LEVEL_MAX parameters.
     * @param identityDto
     * @param strApplicationCode
     * @return true if can proccess
     */
    public static boolean canProcessAttachmentRequest ( IdentityDto identityDto , String strApplicationCode )
    {
        int nLevelMin  = AppPropertiesService.getPropertyInt( AttachmentRequestConstants.PROPERTY_ACCESS_PROCESS_ATTACHMENT_REQUEST_MIN_LEVEL, 0 );
        int nLevelMax  = AppPropertiesService.getPropertyInt( AttachmentRequestConstants.PROPERTY_ACCESS_PROCESS_ATTACHMENT_REQUEST_MAX_LEVEL, 0 );
        
        //If the values are 0, the control is disabled.
        if( nLevelMin == 0 && nLevelMax == 0 )
        {
            return true;
        }
        
        ServiceContractSearchResponse contractSearchResponse = AttachmentRequestService.getActiveServiceContract( strApplicationCode );
        
        if( contractSearchResponse != null && contractSearchResponse.getServiceContract( ) != null 
                && !contractSearchResponse.getServiceContract( ).isAuthorizedAttachementCertification( )  )
        {
            return false;
        }
        
        for ( String attributeKey : getListDbAttributes( ) )
        {
            Integer nCertificationLevel = getCertificationLevel( identityDto.getAttributes( ), attributeKey );
            
            if ( nCertificationLevel != null && contractSearchResponse != null && contractSearchResponse.getServiceContract( ) != null
                    && contractSearchResponse.getServiceContract( ).getAttributeDefinitions( ) != null )
            {
                Optional<AttributeDefinitionDto> optionalContract = contractSearchResponse.getServiceContract( ).getAttributeDefinitions( ).stream( )
                        .filter( e -> e.getKeyName( ).equals( attributeKey ) ).findFirst( );

                if ( optionalContract.isPresent( ) && optionalContract.get( ).getAttributeRequirement( ) != null
                        && nCertificationLevel >= Integer.valueOf( optionalContract.get( ).getAttributeRequirement( ).getLevel( ) )
                        && Integer.valueOf( optionalContract.get( ).getAttributeRequirement( ).getLevel( ) ) >= nLevelMin
                        && Integer.valueOf( optionalContract.get( ).getAttributeRequirement( ).getLevel( ) ) <= nLevelMax )
                {
                    return false;
                }
            }
        }
        return true;
        
    }
    
    
    /**
     * This method returns the certification level based on a user's attribute.
     * @param listUserAttributes
     * @param strTargetAttribute
     * @return
     */
    private static Integer getCertificationLevel ( List<AttributeDto> listUserAttributes, String strTargetAttribute )
    {
        if( listUserAttributes != null && !listUserAttributes.isEmpty( ) )
        {
            for ( AttributeDto attribute : listUserAttributes )
            {
                if( attribute.getKey( ).equals( strTargetAttribute ) )
                {
                    return attribute.getCertificationLevel( );
                }
            }
        }
        return null;
    }
    
    /**
     * Get list db attributes
     * @return
     */
    private static List<String> getListDbAttributes ( )
    {
        List<String> listAttribute = new ArrayList<>();
        listAttribute.add( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_LAST_NAME );
        listAttribute.add( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_PREFERRED_USER_NAME );
        listAttribute.add( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_FIRSTNAME );
        listAttribute.add( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_GENDER );
        listAttribute.add( AttachmentRequestConstants.ATTRIBUTE_DB_IDENTITY_BIRTHDATE );
        
        return listAttribute;
        
    }
    
    /**
     * Filters the attributes of the given IdentityDto to keep only those that have been modified.
     * @param newIdentity
     */
    public static void keepOnlyModifiedAttributes ( IdentityDto newIdentity  )
    {        
        IdentityDto oldIdentity = AttachmentRequestService.getIdentityByGuid( newIdentity.getConnectionId( ) );
        
        List<AttributeDto> listAttrbutes = new ArrayList<>();
        
        if( oldIdentity != null && oldIdentity.getAttributes( ) != null )
        {
            for( AttributeDto newAttribute : newIdentity.getAttributes( ) )
            {
                Optional<AttributeDto> oldAttribute = oldIdentity.getAttributes( )
                        .stream( )
                        .filter( attOld -> attOld.getKey( ).equals( newAttribute.getKey( ) ) )
                        .findAny( ) ;
                
                if( ( !oldAttribute.isPresent( ) || hasChanged ( newAttribute, oldAttribute.get( ) ) ) 
                        && StringUtils.isNotEmpty( newAttribute.getValue( ) ))
                {
                    listAttrbutes.add( newAttribute );
                }
            }
        }
        newIdentity.setAttributes( listAttrbutes );
        
    }

    
    /**
     * 
     * @param newAttribute the new dashboard attribute submitted
     * @param oldAttribute the old dashboard attribute saved
     * @return true if the two attributes are different
     */
    private static boolean hasChanged( AttributeDto newAttribute, AttributeDto oldAttribute )
    {
        if( oldAttribute == null)
        {
            return true;
        }
        
        return ( newAttribute.getValue( ) != null && !newAttribute.getValue( ).equals( oldAttribute.getValue( ) ) );
    }
}
