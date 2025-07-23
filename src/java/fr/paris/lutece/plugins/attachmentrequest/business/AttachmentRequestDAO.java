/*
 * Copyright (c) 2002-2025, Mairie de Paris
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

package fr.paris.lutece.plugins.attachmentrequest.business;

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * This class provides Data Access methods for AttachmentRequest objects
 */
public final class AttachmentRequestDAO implements IAttachmentRequestDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT    = "SELECT id, id_file, customer_id, client_code, provider_name, date_creation FROM attachment_request WHERE id = ?";
    private static final String SQL_QUERY_INSERT    = "INSERT INTO attachment_request ( id, id_file, customer_id, client_code, provider_name, date_creation ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE    = "DELETE FROM attachment_request WHERE id = ? ";
    private static final String SQL_QUERY_UPDATE    = "UPDATE attachment_request SET id = ?, id_file = ?, customer_id = ?, client_code = ?, provider_name = ?, date_creation = ? WHERE id = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id, id_file, customer_id, client_code, provider_name, date_creation FROM attachment_request";
    private static final String SQL_QUERY_SELECT_BY_CUID = "SELECT id, id_file, customer_id, client_code, provider_name, date_creation FROM attachment_request WHERE customer_id = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( AttachmentRequest attachmentRequest )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {

            int nIndex = 0;
            daoUtil.setInt( ++nIndex, attachmentRequest.getId( ) );
            daoUtil.setInt( ++nIndex, attachmentRequest.getIdFile( ) );
            daoUtil.setString( ++nIndex, attachmentRequest.getCustomerId( ) );
            daoUtil.setString( ++nIndex, attachmentRequest.getClientCode( ) );
            daoUtil.setString( ++nIndex, attachmentRequest.getProviderName( ) );
            daoUtil.setTimestamp( ++nIndex, Timestamp.from( Instant.now( ) ) );
    
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                attachmentRequest.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AttachmentRequest load( int nId )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );
    
            AttachmentRequest attachmentRequest = null;
    
            if ( daoUtil.next( ) )
            {
                attachmentRequest = new AttachmentRequest( );
    
                attachmentRequest.setId( daoUtil.getInt( "id" ) );
                attachmentRequest.setIdFile( daoUtil.getInt( "id_file" ) );
                attachmentRequest.setCustomerId( daoUtil.getString( "customer_id" ) );
                attachmentRequest.setClientCode( daoUtil.getString( "client_code" ) );
                attachmentRequest.setProviderName( daoUtil.getString( "provider_name" ) );
                attachmentRequest.setDateCreation( daoUtil.getTimestamp( "date_creation" ) );
            }
    
            return attachmentRequest;
        }
    }
    
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<AttachmentRequest> loadByCuid( String strCuid )
    {
        List<AttachmentRequest> listAttachmentRequest = new ArrayList<>();

        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CUID ) )
        {
            daoUtil.setString( 1, strCuid );
            daoUtil.executeQuery( );
            
            while ( daoUtil.next( ) )
            {
                AttachmentRequest attachmentRequest = new AttachmentRequest( );
    
                attachmentRequest.setId( daoUtil.getInt( "id" ) );
                attachmentRequest.setIdFile( daoUtil.getInt( "id_file" ) );
                attachmentRequest.setCustomerId( daoUtil.getString( "customer_id" ) );
                attachmentRequest.setClientCode( daoUtil.getString( "client_code" ) );
                attachmentRequest.setProviderName( daoUtil.getString( "provider_name" ) );
                attachmentRequest.setDateCreation( daoUtil.getTimestamp( "date_creation" ) );
                
                listAttachmentRequest.add( attachmentRequest );
            }   
        }
        return listAttachmentRequest;

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nAttachmentRequestId )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nAttachmentRequestId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( AttachmentRequest attachmentRequest )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, attachmentRequest.getId( ) );
            daoUtil.setInt( ++nIndex, attachmentRequest.getIdFile( ) );
            daoUtil.setString( ++nIndex, attachmentRequest.getCustomerId( ) );
            daoUtil.setString( ++nIndex, attachmentRequest.getClientCode( ) );
            daoUtil.setString( ++nIndex, attachmentRequest.getProviderName( ) );
            daoUtil.setTimestamp( ++nIndex, attachmentRequest.getDateCreation( ) );
            daoUtil.setInt( ++nIndex, attachmentRequest.getId( ) );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<AttachmentRequest> selectAttachmentRequestsList( )
    {
        List<AttachmentRequest> listAttachmentRequests = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
        {
            daoUtil.executeQuery( );
    
            while ( daoUtil.next( ) )
            {
                AttachmentRequest attachmentRequest = new AttachmentRequest( );
                attachmentRequest.setId( daoUtil.getInt( "id" ) );
                attachmentRequest.setIdFile( daoUtil.getInt( "id_file" ) );
                attachmentRequest.setCustomerId( daoUtil.getString( "customer_id" ) );
                attachmentRequest.setClientCode( daoUtil.getString( "client_code" ) );
                attachmentRequest.setProviderName( daoUtil.getString( "provider_name" ) );
                attachmentRequest.setDateCreation( daoUtil.getTimestamp( "date_creation" ) );
                listAttachmentRequests.add( attachmentRequest );
            }
    
            return listAttachmentRequests;
        }
    }

}
