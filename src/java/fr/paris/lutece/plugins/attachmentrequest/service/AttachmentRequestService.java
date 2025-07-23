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
package fr.paris.lutece.plugins.attachmentrequest.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fr.paris.lutece.plugins.attachmentrequest.business.AttachmentRequest;
import fr.paris.lutece.plugins.attachmentrequest.business.AttachmentRequestHome;
import fr.paris.lutece.plugins.attachmentrequest.util.AttachmentRequestConstants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AuthorType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ResponseStatusType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityResourceType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskCreateRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskCreateResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskStatusType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskUpdateStatusRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskUpdateStatusResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityServiceExtended;
import fr.paris.lutece.plugins.identitystore.v3.web.service.ServiceContractService;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * AttachmentRequestService
 *
 */
public class AttachmentRequestService
{
    // BEANS
    public static final String             BEAN_IDENTITY_STORE_NAME       = "attachment-request.identitystore.service";
    public static final String             BEAN_SERVICE_CONTRACT_NAME     = "attachment-request.serviceContract.service";
   
    //PROPERTIES
    public static final String             PROPERTY_IDENTITY_CLIENT_CODE  = AppPropertiesService.getProperty( "attachment-request.identitystore.client.code", "MyDashboard" );

    private static IdentityServiceExtended _identityService               = SpringContextService.getBean( BEAN_IDENTITY_STORE_NAME );
    private static ServiceContractService _serviceContractService         = SpringContextService.getBean( BEAN_SERVICE_CONTRACT_NAME );

    /**
     * Constructor
     */
    private AttachmentRequestService( )
    {

    }

    /**
     * Get identity by guid
     * 
     * @param strGuid
     * @return the identity find
     */
    public static IdentityDto getIdentityByGuid( String strGuid )
    {
        IdentityDto identity = null;
        try
        {
            IdentitySearchResponse response = _identityService.getIdentityByConnectionId( strGuid, PROPERTY_IDENTITY_CLIENT_CODE, getRequestAuthor( ) );

            if ( response != null && !ResponseStatusType.NOT_FOUND.equals( response.getStatus( ).getType( ) ) && response.getIdentities( ) != null && !response.getIdentities( ).isEmpty( ) )
            {
                identity = response.getIdentities( ).get( 0 );
            }
        } catch ( IdentityStoreException | AppException e )
        {
            AppLogService.info( "Error getting Identity with guid {} ", strGuid, e );
        }
        return identity;
    }

    /**
     * Update identity
     * 
     * @param strCustomerId
     * @param identityDto
     * @return
     */
    public static boolean updateIdentity( String strCustomerId, IdentityDto identityDto )
    {
        IdentityChangeRequest identityChange = new IdentityChangeRequest( );
        identityChange.setIdentity( identityDto );

        try
        {
            IdentityChangeResponse response = _identityService.updateIdentity( strCustomerId, identityChange, PROPERTY_IDENTITY_CLIENT_CODE, getRequestAuthor( ) );
            if ( response != null && !ResponseStatusType.NOT_FOUND.equals( response.getStatus( ).getType( ) ) )
            {
                return true;
            }
        } catch ( IdentityStoreException | AppException e )
        {
            AppLogService.info( "Error updating Identity with customer id {} ", strCustomerId, e );
        }
        return false;
    }

    /**
     * Get identity task search
     * 
     * @param request
     * @return
     */
    public static IdentityTaskSearchResponse getIdentityTaskSearch( IdentityTaskSearchRequest request )
    {
        try
        {
            IdentityTaskSearchResponse identityTaskSearchResponse = _identityService.searchIdentityTasks( request, PROPERTY_IDENTITY_CLIENT_CODE, getRequestAuthor( ) );

            if ( identityTaskSearchResponse.getStatus( ).getHttpCode( ) == 200 && !identityTaskSearchResponse.getTasks( ).isEmpty( ) )
            {
                return identityTaskSearchResponse;
            }

        } catch ( IdentityStoreException e )
        {
            AppLogService.error( "An error occurred while searching Identity Tasks", e );
        }

        return null;
    }

    /**
     * Creating an ATTACHMENT_CERTIFICATION_REQUEST type task
     * 
     * @param request
     * @param metadata
     * @return true if the task has been created
     */
    public static boolean createIdentityTask( String strCustomerId, Map<String,String> metadata  )
    {
        try
        {

            IdentityTaskCreateResponse identityTaskCreateResponse = _identityService.createIdentityTask( initIdentityTaskCreateRequest( strCustomerId, metadata ), PROPERTY_IDENTITY_CLIENT_CODE,
                    getRequestAuthor( ) );

            if ( identityTaskCreateResponse.getStatus( ).getHttpCode( ) == 200 )
            {
                return true;
            }

        } catch ( IdentityStoreException e )
        {
            AppLogService.error( "An error occurred while creating identity task {} for cuid {}", IdentityTaskType.ATTACHMENT_CERTIFICATION_REQUEST, strCustomerId, e );
        }

        return false;
    }
    
    
    /**
     * Update identity task status
     * 
     * @param strTaskCode
     * @param statusType
     * @return true if the status has been updated 
     */
    public static boolean updateIdentityTaskStatus( String strTaskCode, IdentityTaskStatusType statusType )
    {
        try
        {
            IdentityTaskUpdateStatusRequest request = new IdentityTaskUpdateStatusRequest( );
            request.setStatus( statusType );
            
            IdentityTaskUpdateStatusResponse identityTaskUpdateStatusResponse = _identityService.updateIdentityTaskStatus( strTaskCode, request, PROPERTY_IDENTITY_CLIENT_CODE, getRequestAuthor( ) );

            if ( identityTaskUpdateStatusResponse.getStatus( ).getHttpCode( ) == 200 )
            {
                return true;
            }

        } catch ( IdentityStoreException e )
        {
            AppLogService.error( "An error occurred while updating task {}", strTaskCode, e);
        }

        return false;
    }

    /**
     * Get identity task by task code
     * 
     * @param strTaskCode
     * @return the identity tasj by task code
     */
    public IdentityTaskDto getIdentityTask( String strTaskCode )
    {
        try
        {
            IdentityTaskGetResponse identityTaskGetResponse = _identityService.getIdentityTask( strTaskCode, PROPERTY_IDENTITY_CLIENT_CODE, getRequestAuthor( ) );

            if ( identityTaskGetResponse.getStatus( ).getHttpCode( ) == 200 )
            {
                return identityTaskGetResponse.getTask( );
            }
        } catch ( IdentityStoreException e )
        {
            AppLogService.error( "An error occurred while retrieving task {}", strTaskCode, e );
        }

        return null;
    }
    
    /**
     * Get active service contract
     * @param strApplicationCode
     * @return the active service contract
     */
    public static ServiceContractSearchResponse getActiveServiceContract( String strApplicationCode )
    {
        ServiceContractSearchResponse serviceContractSearchResponse = null;
        try
        {
            serviceContractSearchResponse = _serviceContractService.getActiveServiceContract( strApplicationCode, PROPERTY_IDENTITY_CLIENT_CODE, getRequestAuthor( ) );
        } 
        catch ( IdentityStoreException e )
        {
            AppLogService.error( "Error ServiceContract for application {}", e.getMessage( ), strApplicationCode );
        }
        
        return serviceContractSearchResponse;
    }

    /**
     * Returns IdentityTaskDto if the user has a task in progress
     * 
     * @param strCustomerId
     * @return identityTaskDto if the user has a task in progress
     */
    public static IdentityTaskDto getIdentityTaskInProgress( String strCustomerId )
    {
        List<IdentityTaskStatusType> listTaskStatus = new ArrayList<>( );
        IdentityTaskSearchRequest request = new IdentityTaskSearchRequest( );

        listTaskStatus.add( IdentityTaskStatusType.TODO );
        listTaskStatus.add( IdentityTaskStatusType.IN_PROGRESS );

        request.setTaskStatus( listTaskStatus );
        request.setTaskType( IdentityTaskType.ATTACHMENT_CERTIFICATION_REQUEST );

        IdentityTaskSearchResponse response = getIdentityTaskSearch( request );

        if ( response != null && response.getTasks( ) != null )
        {
            for ( IdentityTaskDto taskDto : response.getTasks( ) )
            {
                if ( taskDto.getResourceId( ).equals( strCustomerId ) )
                {
                    return taskDto;
                }
            }
        }

        return null;
    }

    
    /**
     * Init IdentityTaskCreateRequest for ACCOUNT_CERTIFICATION_PJ task type
     * 
     * @param strCustomerId
     * @param metadata
     * @return IdentityTaskCreateRequest
     */
    private static IdentityTaskCreateRequest initIdentityTaskCreateRequest( String strCustomerId, Map<String,String> metadata )
    {
        IdentityTaskCreateRequest identityTaskCreateRequest = new IdentityTaskCreateRequest( );
        IdentityTaskDto identityTaskDto = new IdentityTaskDto( );

        identityTaskDto.setTaskType( IdentityTaskType.ATTACHMENT_CERTIFICATION_REQUEST.name( ) );
        identityTaskDto.setLastUpdateClientCode( PROPERTY_IDENTITY_CLIENT_CODE );
        identityTaskDto.setResourceType( IdentityResourceType.CUID.name( ) );
        identityTaskDto.setTaskStatus( IdentityTaskStatusType.TODO );
        identityTaskDto.setCreationDate( Timestamp.from( Instant.now( ) ) );
        identityTaskDto.setResourceId( strCustomerId );

        identityTaskDto.setMetadata( metadata );

        identityTaskCreateRequest.setTask( identityTaskDto );

        return identityTaskCreateRequest;
    }

    /**
     * Get request author
     * 
     * @return RequestAuthor
     */
    private static RequestAuthor getRequestAuthor( )
    {
        RequestAuthor requestAuthor = new RequestAuthor( );
        requestAuthor.setName( PROPERTY_IDENTITY_CLIENT_CODE );
        requestAuthor.setType( AuthorType.owner );
        return requestAuthor;
    }
    
    
    /**
     * This method removes user attachments by retrieving information from the taskstack metadata 
     * that contains the provider and file path in S3
     * @param identityTaskDto
     */
    public static void deleteFiles( IdentityTaskDto identityTaskDto )
    {
        if ( identityTaskDto.getMetadata( ) != null )
        {
            String strProvider = identityTaskDto.getMetadata( ).get( AttachmentRequestConstants.METADATA_PROVIDER_NAME );
            JSONParser parser = new JSONParser( );
            
            //FileStore Provider
            IFileStoreServiceProvider provider = FileStoreService.getInstance( ).getFileStoreServiceProvider( strProvider );

            try
            {
                JSONArray jsonFiles = ( JSONArray ) parser.parse( identityTaskDto.getMetadata( ).get( AttachmentRequestConstants.METADATA_FILES ) );

                for ( Object jsonFile : jsonFiles )
                {
                    JSONObject jsonObj = ( JSONObject ) jsonFile;
                    provider.delete( jsonObj.get( AttachmentRequestConstants.METADATA_FILE_PATH ).toString( ) );
                }

            } catch ( Exception e )
            {
                AppLogService.error( "An error occurred while deleting file by provider {} ", strProvider, e.getMessage( ) );
            }
        }
    }
    
    /**
     * Gets list of attachment request
     * @return list of attachment request
     */
    public static List<AttachmentRequest> getListAttachmentRequest ( )
    {
       return AttachmentRequestHome.getAttachmentRequestsList( ); 
    }
    
    /**
     * Gets list of attachment request by cuid
     * @return list of attachment request
     */
    public static List<AttachmentRequest> getListAttachmentRequestByCuid ( String strCuid )
    {
       return AttachmentRequestHome.findByCuid( strCuid ); 
    }
    
    /**
     * Create attachment request
     * @param attachmentRequest
     * @return attachment request created
     */
    public static AttachmentRequest createAttachmentRequest ( AttachmentRequest attachmentRequest )
    {
        return AttachmentRequestHome.create( attachmentRequest );
    }
    
    /**
     * Remove attachment request by id
     * @param nIdAttachmentRequest
     */
    public static void removeAttachmentRequest ( int nIdAttachmentRequest )
    {
        AttachmentRequestHome.remove( nIdAttachmentRequest );
    }
}
