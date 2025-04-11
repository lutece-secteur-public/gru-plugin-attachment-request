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

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.paris.lutece.plugins.attachmentrequest.business.AttachmentRequest;
import fr.paris.lutece.plugins.attachmentrequest.util.AttachmentRequestConstants;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * 
 * Transfer the files stored in the database to S3 and create a task stack so that it can be processed by the SNs
 *
 */
public class AttachmentRequestDaemon extends Daemon
{

    @Override
    public void run( )
    {
        setLastRunLogs( traitement( ) );
    }
    
    /**
     * 
     * @return
     */
    private String traitement ( )
    {
        List<AttachmentRequest> listAttachmentRequest = AttachmentRequestService.getListAttachmentRequest( );
        
        if( CollectionUtils.isNotEmpty( listAttachmentRequest ) )
        {
            Instant start = Instant.now( );
            StringBuilder sbLogs = new StringBuilder( );
            
            sbLogs.append( "\n" );
            sbLogs.append( "******************************" );
            sbLogs.append( "\n" );
            sbLogs.append( "Start daemon AttachmentRequestDaemon" );
            sbLogs.append( "\n" );
            sbLogs.append( "Number of files to process: " + listAttachmentRequest.size( ) );

            //Traitement
            if( CollectionUtils.isNotEmpty( listAttachmentRequest ) )
            {
                Map<String, List<AttachmentRequest>> maps = groupByCustomerIdAndProvider( listAttachmentRequest ) ;
                
                maps.forEach( (key, value) -> transfertFilesToS3( value, sbLogs ) );
            }
            
            Instant end = Instant.now( );
            Duration duration = Duration.between( start, end );
          
            sbLogs.append( "\n" );
            sbLogs.append( "Processing time in seconds: " + duration.getSeconds( ) );
            sbLogs.append( "\n" );
            sbLogs.append( "End daemon AttachmentRequestDaemon" );
            sbLogs.append( "\n" );
            sbLogs.append( "******************************" );
            sbLogs.append( "\n" );
    
            String strResult = sbLogs.toString(  );
            AppLogService.info( strResult );
            
            return strResult;
        }
        
        return StringUtils.EMPTY;
    }
    
    /**
     * Allows you to group occurrences by customer id and provider
     * @param attachmentRequests
     * @return map
     */
    private static Map<String, List<AttachmentRequest>> groupByCustomerIdAndProvider(List<AttachmentRequest> attachmentRequests) {
        return attachmentRequests.stream()
                .collect(Collectors.groupingBy(
                        obj -> obj.getCustomerId() + "_" + obj.getProviderName( )
                ));
    }
    
    /**
     * Transfer the files stored in the database to S3 and create a task stack so that it can be processed by the SNs
     * @param listAttachmentRequest
     * @param sbLogs
     */
    @SuppressWarnings( "unchecked" )
    private void transfertFilesToS3 ( List<AttachmentRequest> listAttachmentRequest, StringBuilder sbLogs  )
    {
      String strClientCode = listAttachmentRequest.get( 0 ).getClientCode( );
      String strProviderName = listAttachmentRequest.get( 0 ).getProviderName( );
      String strCustomerId = listAttachmentRequest.get( 0 ).getCustomerId( );
        
      //FileStoreServiceProvider
      IFileStoreServiceProvider fileStoreService = FileStoreService
              .getInstance( )
              .getFileStoreServiceProvider( strProviderName );
      
      //Metadata task
      Map<String,String> metadata = new HashMap<>( );
      metadata.put( AttachmentRequestConstants.METADATA_CLIENT_CODE, strClientCode.toUpperCase( ) );
      metadata.put( AttachmentRequestConstants.METADATA_PROVIDER_NAME, strProviderName );
      
      JSONArray jsonFiles = new JSONArray();
      
      //Files processed Counter
      int nNbFilesProcessed = 0; 
      
      //Transfet files to S3 and create task
      for ( AttachmentRequest attachmentRequest : listAttachmentRequest )
      {
          try
          { 
              File file = FileService.getInstance( ).getFileStoreServiceProvider( ).getFile( String.valueOf( attachmentRequest.getIdFile( ) ) );
              if( file != null)
              {
                  //Transfert to S3
                  String strFilePath = fileStoreService.storeFile( file );
                  
                  //Metadata for task
                  JSONObject jsonFile = new JSONObject( );
                  jsonFile.put( AttachmentRequestConstants.METADATA_FILE_NAME, file.getTitle( ) );
                  jsonFile.put( AttachmentRequestConstants.METADATA_FILE_PATH, strFilePath );            
                  jsonFiles.add( jsonFile );
                  
                  //Deleting file information and database file
                  FileService.getInstance( ).getFileStoreServiceProvider( ).delete( String.valueOf(  attachmentRequest.getIdFile( ) ) );
                  AttachmentRequestService.removeAttachmentRequest( attachmentRequest.getId( ) );
                  
                  //Counter
                  nNbFilesProcessed++;
              }          
          }
          catch (Exception e)
          {
              AppLogService.error( "An error occurred while transferring the id file {} to S3.", attachmentRequest.getIdFile( ), e.getMessage( )  );
          }
      }
      
      if( !jsonFiles.isEmpty( ) )
      {   
          metadata.put( AttachmentRequestConstants.METADATA_FILES, jsonFiles.toString( ) );
        
          //Creating an identity task for the user
          AttachmentRequestService.createIdentityTask( strCustomerId, metadata );
      }
 
      sbLogs.append( "\n" );
      sbLogs.append( "Number of files processed: " + nNbFilesProcessed );
    }
     
}
