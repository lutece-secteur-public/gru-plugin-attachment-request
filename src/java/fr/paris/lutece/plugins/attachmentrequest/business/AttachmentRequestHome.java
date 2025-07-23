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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for AttachmentRequest objects
 */

public final class AttachmentRequestHome
{

    // Static variable pointed at the DAO instance

    private static IAttachmentRequestDAO _dao = SpringContextService.getBean( "attachment-request.attachmentRequestDao" );


    /**
     * Private constructor - this class need not be instantiated
     */

    private AttachmentRequestHome(  )
    {
    }

    /**
     * Create an instance of the attachmentRequest class
     * @param attachmentRequest The instance of the AttachmentRequest which contains the informations to store
     * @return The  instance of attachmentRequest which has been created with its primary key.
     */

    public static AttachmentRequest create( AttachmentRequest attachmentRequest )
    {
        _dao.insert( attachmentRequest );

        return attachmentRequest;
    }


    /**
     * Update of the attachmentRequest which is specified in parameter
     * @param attachmentRequest The instance of the AttachmentRequest which contains the data to store
     * @return The instance of the  attachmentRequest which has been updated
     */

    public static AttachmentRequest update( AttachmentRequest attachmentRequest )
    {
        _dao.store( attachmentRequest );

        return attachmentRequest;
    }


    /**
     * Remove the attachmentRequest whose identifier is specified in parameter
     * @param nAttachmentRequestId The attachmentRequest Id
     */


    public static void remove( int nAttachmentRequestId )
    {
        _dao.delete( nAttachmentRequestId );
    }


    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a attachmentRequest whose identifier is specified in parameter
     * @param nKey The attachmentRequest primary key
     * @return an instance of AttachmentRequest
     */

    public static AttachmentRequest findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Returns an instance of a attachmentRequest whose identifier is specified in parameter
     * @param strCuid The attachmentRequest customer id
     * @return an instance of AttachmentRequest
     */

    public static List<AttachmentRequest> findByCuid( String strCuid )
    {
        return _dao.loadByCuid( strCuid );
    }

    /**
     * Load the data of all the attachmentRequest objects and returns them in form of a list
     * @return the list which contains the data of all the attachmentRequest objects
     */

    public static List<AttachmentRequest> getAttachmentRequestsList( )
    {
        return _dao.selectAttachmentRequestsList( );
    }

}
