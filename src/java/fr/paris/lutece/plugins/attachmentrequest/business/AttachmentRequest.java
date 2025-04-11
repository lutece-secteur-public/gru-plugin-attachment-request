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

import java.sql.Timestamp;

/**
 * This is the business class for the object AttachmentRequest
 */
public class AttachmentRequest
{
    // Variables declarations
    private int       _nId;
    private int       _nIdFile;
    private String    _strCustomerId;
    private String    _strProviderName;
    private String    _strClientCode;
    private Timestamp _dateDateCreation;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the IdFile
     * 
     * @return The IdFile
     */
    public int getIdFile( )
    {
        return _nIdFile;
    }

    /**
     * Sets the IdFile
     * 
     * @param nIdFile
     *            The IdFile
     */
    public void setIdFile( int nIdFile )
    {
        _nIdFile = nIdFile;
    }

    /**
     * Returns the CustomerId
     * 
     * @return The CustomerId
     */
    public String getCustomerId( )
    {
        return _strCustomerId;
    }

    /**
     * Sets the CustomerId
     * 
     * @param strCustomerId
     *            The CustomerId
     */
    public void setCustomerId( String strCustomerId )
    {
        _strCustomerId = strCustomerId;
    }

    /**
     * Returns the ProviderName
     * 
     * @return The ProviderName
     */
    public String getProviderName( )
    {
        return _strProviderName;
    }

    /**
     * Sets the ProviderName
     * 
     * @param strProviderName
     *            The ProviderName
     */
    public void setProviderName( String strProviderName )
    {
        _strProviderName = strProviderName;
    }
    
    

    /**
     * @return the _strClientCode
     */
    public String getClientCode( )
    {
        return _strClientCode;
    }

    /**
     * @param strClientCode the _strClientCode to set
     */
    public void setClientCode( String strClientCode )
    {
        this._strClientCode = strClientCode;
    }

    /**
     * Returns the DateCreation
     * 
     * @return The DateCreation
     */
    public Timestamp getDateCreation( )
    {
        return _dateDateCreation;
    }

    /**
     * Sets the DateCreation
     * 
     * @param dateDateCreation
     *            The DateCreation
     */
    public void setDateCreation( Timestamp dateDateCreation )
    {
        _dateDateCreation = dateDateCreation;
    }
}
