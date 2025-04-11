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

import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.sthree.service.file.implementation.S3StorageFileService;
import fr.paris.lutece.portal.service.file.IFileDownloadUrlService;
import fr.paris.lutece.portal.service.file.IFileRBACService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * FileStoreService
 *
 */
public class FileStoreService
{

    //FileStoreService
    private static FileStoreService _fileStoreService;
    
    //Map that will contain the list of File Service Provider
    private Map<String, IFileStoreServiceProvider> _mapFileStoreProvider;
    
    //Initialisation parameters
    private IFileDownloadUrlService _fileDownloadUrlService = SpringContextService.getBean( "s3FileDownloadUrlService" );
    private IFileRBACService _fileRBACService = SpringContextService.getBean( "defaultFileRBACService" );
    private String s3Url = AppPropertiesService.getProperty( "s3Url", "" );
    private String s3Key = AppPropertiesService.getProperty( "s3Key", "" );
    private String s3Password = AppPropertiesService.getProperty( "s3Password", "" );
    private String s3DefaultFilePath = AppPropertiesService.getProperty( "s3DefaultFilePath", "" );
    private String s3ForcePathStyle = AppPropertiesService.getProperty( "s3ForcePathStyle", "" );
    private String s3Region = AppPropertiesService.getProperty( "s3Region", "" );
    private String s3ChecksumAlgorithm = AppPropertiesService.getProperty( "s3ChecksumAlgorithm", "" );
    private String s3ProxyHost = AppPropertiesService.getProperty( "s3ProxyHost", "" );
    private String s3ProxyUsername = AppPropertiesService.getProperty( "s3ProxyUsername", "" );
    private String s3ProxyPassword = AppPropertiesService.getProperty( "s3ProxyPassword", "" );
    private String s3RequestTimeout = AppPropertiesService.getProperty( "s3RequestTimeout", "" );
    private String s3ConnectionTimeout = AppPropertiesService.getProperty( "s3ConnectionTimeout", "" );
    
    /**
     * 
     * Get instance
     */
    public static FileStoreService getInstance( )
    {
        if( _fileStoreService == null )
        {
            _fileStoreService = new FileStoreService( );
        }
        
        return _fileStoreService;
    }    
    
    /**
     * get the FileStoreService provider
     * 
     * @param strFileStoreServiceProviderName
     * @return the current FileStoreService provider
     */
    public IFileStoreServiceProvider getFileStoreServiceProvider( String strFileStoreServiceProviderName )
    {
        if( _mapFileStoreProvider == null )
        {
            _mapFileStoreProvider = new HashMap< >( );
        }
        
        //To respect the bucket format
        strFileStoreServiceProviderName = strFileStoreServiceProviderName.toLowerCase( );
        
        if( _mapFileStoreProvider.get( strFileStoreServiceProviderName ) != null )
        {
            return _mapFileStoreProvider.get( strFileStoreServiceProviderName );
        }
        else
        {
            //New instantiation of S3StorageFileService
            S3StorageFileService s3StorageFileService = new S3StorageFileService( _fileDownloadUrlService,
                    _fileRBACService,
                    s3Url,
                    strFileStoreServiceProviderName,
                    s3Key,
                    s3Password,
                    s3DefaultFilePath,
                    s3ForcePathStyle,
                    s3Region,
                    s3ChecksumAlgorithm,
                    s3ProxyHost,
                    s3ProxyUsername,
                    s3ProxyPassword,
                    s3RequestTimeout,
                    s3ConnectionTimeout );
            
            s3StorageFileService.setName( strFileStoreServiceProviderName );           
           _mapFileStoreProvider.put( strFileStoreServiceProviderName, s3StorageFileService )  ;
           
           return s3StorageFileService;
        }
    }
}
