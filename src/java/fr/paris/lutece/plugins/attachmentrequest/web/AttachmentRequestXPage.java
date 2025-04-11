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
package fr.paris.lutece.plugins.attachmentrequest.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.asynchronousupload.service.IAsyncUploadHandler;
import fr.paris.lutece.plugins.attachmentrequest.business.AttachmentRequest;
import fr.paris.lutece.plugins.attachmentrequest.business.IdentityFormDTO;
import fr.paris.lutece.plugins.attachmentrequest.service.AttachmentRequestService;
import fr.paris.lutece.plugins.attachmentrequest.service.listener.IAttachmentRequestListener;
import fr.paris.lutece.plugins.attachmentrequest.util.AttachmentRequestUtils;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskDto;
import fr.paris.lutece.plugins.verifybackurl.service.AuthorizedUrlService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.FileServiceException;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * 
 * AttachmentRequestXPage
 *
 */
@Controller( xpageName = "attachmentrequest", pageTitleI18nKey = "attachment-request.xpage.attachmentrequest.pageTitle", pagePathI18nKey = "attachment-request.xpage.attachmentrequest.pagePathLabel" )
public class AttachmentRequestXPage extends MVCApplication
{

    /**
     * 
     */
    private static final long          serialVersionUID                              = 7316503936900721541L;

    // VIEW
    private static final String        VIEW_ATTACHMENT_REQUEST                       = "formAttachmentRequest";
    private static final String        VIEW_SUCCESS_ATTACHMENT_REQUEST               = "successAttachmentRequest";
    private static final String        VIEW_IN_PROGRESS_ATTACHMENT_REQUEST           = "inProgressAttachmentRequest";

    // ACTION
    private static final String        ACTION_ATTACHMENT_REQUEST                     = "doAttachmentRequest";

    // TEMPLATE
    private static final String        TEMPLATE_ATTACHMENT_REQUEST_FORM              = "skin/plugins/attachment-request/attachment_request_form.html";
    private static final String        TEMPLATE_ATTACHMENT_REQUEST_SUCCESS           = "skin/plugins/attachment-request/attachment_request_success.html";
    private static final String        TEMPLATE_ATTACHMENT_REQUEST_IN_PROGRESS       = "skin/plugins/attachment-request/attachment_request_in_progress.html";
    private static final String        TEMPLATE_ATTACHMENT_REQUEST_UNAUTHORIZED      = "skin/plugins/attachment-request/attachment_request_unauthorized.html";

    // MARK
    private static final String        MARK_GENDER_LIST                              = "genderlist";
    private static final String        MARK_IDENTITY                                 = "identity";
    private static final String        MARK_SERVICE_URL                              = "serviceUrl";
    private static final String        MARK_DATE_ATTACHEMENT_REQUEST                 = "dateAttachmentRequest";
    private static final String        MARK_HANDLER                                  = "handler";
    private static final String        MARK_LIST_FILES                               = "listFiles";
    private static final String        MARK_CAN_SHOW_USERDATA_FORM                   = "canShowUserDataForm";
    private static final String        MARK_CUSTOMER_ID                              = "customerId";

    // PARAMETER
    private static final String        PARAMETER_CODE_APP                            = "appCode";
    private static final String        PARAMETER_ATTACHMENTS                         = "attachments";

    // PROPERTY
    private static final String        PROPERTY_KEY_GENDER_LIST                      = AppPropertiesService.getProperty( "attachment-request.application.listref.gender" );
    private static final String        PROPERTY_BUCKET_PREFIX                        = AppPropertiesService.getProperty( "attachment-request.bucket.prefix" );
    private static final String        MESSAGE_ERROR_TOKEN                           = "Invalid Security Token";

    private String                     _strAppCode                                   = null;
    private List<IAttachmentRequestListener> _listPjCertifierListener                = SpringContextService.getBeansOfType( IAttachmentRequestListener.class );
    private IAsyncUploadHandler        _handler                                      = SpringContextService.getBean( "attachment-request.attachmentRequestAsynchronousUploadHandler" );

    /**
     * View of the attached certification form
     * @param request
     * @return XPage
     * @throws UserNotSignedException
     */
    @View( VIEW_ATTACHMENT_REQUEST )
    public XPage getCertificationAttachment( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );
        IdentityFormDTO identityFormDTO = new IdentityFormDTO( );
        String strAppCode = request.getParameter( PARAMETER_CODE_APP );
        
        if( _strAppCode == null || ( StringUtils.isEmpty( strAppCode ) && !strAppCode.equals( _strAppCode ) ) )
        {
            _strAppCode = strAppCode;
        }

        LuteceUser luteceUser = getAuthenticatedUser( request );

        try
        {
            IdentityDto identityDto = AttachmentRequestService.getIdentityByGuid( luteceUser.getName( ) );
            
            //Block if unable to complete attachment request
            if( !AttachmentRequestUtils.canProcessAttachmentRequest( identityDto, _strAppCode ) )
            {
                return getXPage( TEMPLATE_ATTACHMENT_REQUEST_UNAUTHORIZED, getLocale( request ) );
            }
            
            //If a task already exists then redirect the user
            if ( identityDto != null && AttachmentRequestService.getIdentityTaskInProgress ( identityDto.getCustomerId( ) ) != null )
            {
                return redirectView( request, VIEW_IN_PROGRESS_ATTACHMENT_REQUEST );
            }
            
            
            identityFormDTO = AttachmentRequestUtils.convertToIdentityFormDTO( identityDto );
            model.put( MARK_CAN_SHOW_USERDATA_FORM, AttachmentRequestUtils.canShowUserDataForm( identityDto ) );

        } catch ( AppException e )
        {
            AppLogService.error( "An error appear during retreaving Identity information for app_code {} and user guid {} ", _strAppCode, luteceUser.getName( ), e );
        }
        
        getServiceUrl( request, model );
        model.put( MARK_HANDLER, _handler );
        model.put( MARK_LIST_FILES, _handler.getListUploadedFiles( PARAMETER_ATTACHMENTS, request.getSession( ) ) );
        model.put( MARK_GENDER_LIST, getGenderList( ) );
        model.put( MARK_IDENTITY, identityFormDTO );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_ATTACHMENT_REQUEST ) );

        return getXPage( TEMPLATE_ATTACHMENT_REQUEST_FORM, getLocale( request ), model );

    }

    /**
     * Action which allows the filing of the certification request attached
     * @param request
     * @return XPage
     * @throws AccessDeniedException
     * @throws UserNotSignedException
     */
    @Action( ACTION_ATTACHMENT_REQUEST )
    public XPage doCertificationAttachment( HttpServletRequest request ) throws AccessDeniedException, UserNotSignedException
    {
        // CSRF Token control
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_ATTACHMENT_REQUEST ) )
        {
            throw new AccessDeniedException( MESSAGE_ERROR_TOKEN );
        }

        LuteceUser luteceUser = getAuthenticatedUser( request );
        
        //Retrieving form data
        IdentityFormDTO identityFormDTO = new IdentityFormDTO( );

        try
        {
            //Identity data
            IdentityDto identityDto = AttachmentRequestService.getIdentityByGuid( luteceUser.getName( ) );
            boolean canShowUserDataForm = AttachmentRequestUtils.canShowUserDataForm( identityDto );
            
            if( canShowUserDataForm )
            {
                populate( identityFormDTO, request );
        
                //Checking that the data entered in the form does not contain any errors
                Map<String, String> hashErros = AttachmentRequestUtils.checkIdentityForm( identityFormDTO );
                if ( !hashErros.isEmpty( ) )
                {
                    hashErros.forEach( ( x, y ) -> addError( y, x ) );
                    return redirectView( request, VIEW_ATTACHMENT_REQUEST );
                }
            }
            
            //Block if unable to complete attachment request
            if( !AttachmentRequestUtils.canProcessAttachmentRequest( identityDto, _strAppCode ) )
            {
                return getXPage( TEMPLATE_ATTACHMENT_REQUEST_UNAUTHORIZED, getLocale( request ) );
            }
            
            //Update identity with form information
            AttachmentRequestUtils.updateIdentityDto( identityDto, identityFormDTO );
            
            //If a task already exists then redirect the user
            if ( AttachmentRequestService.getIdentityTaskInProgress ( identityDto.getCustomerId( ) ) != null )
            {
                return redirectView( request, VIEW_IN_PROGRESS_ATTACHMENT_REQUEST );
            }
            
            //Update identity
            if( canShowUserDataForm )
            {
                //Check suspicious identity
                String strUrlSuspiciousIdentity = checkSuspiciousIdentity( request, identityDto );
                if( StringUtils.isNotEmpty( strUrlSuspiciousIdentity ) )
                {
                    return redirect( request, strUrlSuspiciousIdentity );
                }
                
                AttachmentRequestUtils.keepOnlyModifiedAttributes( identityDto );
                AttachmentRequestService.updateIdentity( identityDto.getCustomerId( ), identityDto );
            }
            
            //Saving files to S3
            storeFiles( request, identityDto.getCustomerId( ) );
                        
            //Notify user
            notifyUser( luteceUser.getName( ) );

        } catch ( Exception appEx )
        {

            AppLogService.error( "An error occurred while creating the attachment request for  user guid {} ", luteceUser.getName( ), appEx );
            return redirectView( request, VIEW_ATTACHMENT_REQUEST );
        }

        return redirectView( request, VIEW_SUCCESS_ATTACHMENT_REQUEST );

    }


    private String checkSuspiciousIdentity( HttpServletRequest request, IdentityDto identityDto )
    {
        if( _listPjCertifierListener != null )
        {
            for( IAttachmentRequestListener pjCertifierListener : _listPjCertifierListener )
            {
                if ( pjCertifierListener.containsSuspiciousIdentities( identityDto ) )
                {
                    return pjCertifierListener.getUrlSuspiciousIdentity( identityDto, request, _strAppCode );                      
                }
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Store files
     * @param request
     * @throws FileServiceException
     */
    private void storeFiles( HttpServletRequest request, String strCustomerId ) throws FileServiceException
    {
        //Retrieving the list of files from asynchronous upload
        List<FileItem> listFileItems = _handler.getListUploadedFiles( PARAMETER_ATTACHMENTS, request.getSession( ));
        
        //Contract Service
        ServiceContractSearchResponse serviceContractSearchResponse = AttachmentRequestService.getActiveServiceContract( _strAppCode );        
        String strClientCode = serviceContractSearchResponse.getServiceContract( ).getClientCode( );
        String strBucket = ( PROPERTY_BUCKET_PREFIX + strClientCode.substring( 0, 3 ) ).toLowerCase( );
        
        //Saving files to the database and creating an attachmentRequest occurrence
        for ( FileItem fileItem : listFileItems )
        {
            String idFile = FileService.getInstance( ).getFileStoreServiceProvider( ).storeFileItem( fileItem );            
            
            AttachmentRequest attachmentRequest = new AttachmentRequest( );
            attachmentRequest.setCustomerId( strCustomerId );
            attachmentRequest.setIdFile( Integer.parseInt( idFile ) );
            attachmentRequest.setClientCode( strClientCode );
            attachmentRequest.setProviderName( strBucket );
            
            AttachmentRequestService.createAttachmentRequest( attachmentRequest );           
        }
        
        //Deleting files in session
        _handler.removeSessionFiles( request.getSession( ) );
    }
    
    /**
     * Notify user by listener
     * @param demand
     */
    private void notifyUser( String strGuid )
    {
        //NotifyUser by listener
        if( _listPjCertifierListener != null )
        {
            _listPjCertifierListener.forEach( l -> l.notifyUser( strGuid ) );
        }
    }
    

    /**
     * View that informs the user that the attachment certification request has been successfully submitted
     * @param request
     * @return XPage
     * @throws UserNotSignedException
     */
    @View( VIEW_SUCCESS_ATTACHMENT_REQUEST )
    public XPage getSuccessCertificationAttachment( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        getAuthenticatedUser( request );

        getServiceUrl( request, model );

        return getXPage( TEMPLATE_ATTACHMENT_REQUEST_SUCCESS, getLocale( request ), model );
    }

    /**
     * View that informs the user that an attached certification request already exists
     * @param request
     * @return XPage
     * @throws UserNotSignedException
     */
    @View( VIEW_IN_PROGRESS_ATTACHMENT_REQUEST )
    public XPage getInProgressCertificationAttachment( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        LuteceUser luteceUser = getAuthenticatedUser( request );
        
        IdentityDto identityDto = AttachmentRequestService.getIdentityByGuid( luteceUser.getName( ) );
        
        if( identityDto != null )
        {
            IdentityTaskDto identityTaskDto = AttachmentRequestService.getIdentityTaskInProgress ( identityDto.getCustomerId( ) );
            
            if( identityTaskDto != null )
            {
                model.put( MARK_DATE_ATTACHEMENT_REQUEST, identityTaskDto.getCreationDate( ) );
            }
            model.put( MARK_CUSTOMER_ID, identityDto.getCustomerId( ) );
            getServiceUrl( request, model );
        }

        return getXPage( TEMPLATE_ATTACHMENT_REQUEST_IN_PROGRESS, getLocale( request ), model );
    }

    /**
     * Get service url
     * @param request
     * @param model
     */
    private void getServiceUrl( HttpServletRequest request, Map<String, Object> model )
    {
        String strBackUrl = AuthorizedUrlService.getInstance( ).getServiceBackUrl( request );
        if ( !StringUtils.isEmpty( strBackUrl ) )
        {
            model.put( MARK_SERVICE_URL, strBackUrl );
        }
    }

    /**
     * Get gender list
     * 
     * @return list of gender
     */
    private ReferenceList getGenderList( )
    {
        ReferenceList lstGenderList = new ReferenceList( );

        int i = 0;

        for ( String sItem : PROPERTY_KEY_GENDER_LIST.split( ";" ) )
        {
            ReferenceItem refItm = new ReferenceItem( );
            refItm.setName( sItem );
            refItm.setCode( String.valueOf( i ) );
            lstGenderList.add( refItm );
            i++;
        }

        return lstGenderList;
    }
    
    /**
     * Get authenticated user
     * @param request
     * @return authenticated user
     * @throws UserNotSignedException
     */
    private LuteceUser getAuthenticatedUser( HttpServletRequest request ) throws UserNotSignedException
    {
        //Verification that the user is properly authenticated
        LuteceUser luteceUser = getRegistredUser( request );

        if ( luteceUser == null )
        {
            throw new UserNotSignedException( );
        }
        return luteceUser;
    }

}
