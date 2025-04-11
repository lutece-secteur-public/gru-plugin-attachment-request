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

/**
 * 
 * IdentityDTO
 *
 */
public class IdentityFormDTO
{
    private String strGender;
    private String strFirstname;
    private String strLastName;
    private String strPreferredUsername;
    private String strBirthdate;
    private String strBirthcountryCode;
    private String strBirthplaceCode;
    private String strBirthcountry;
    private String strBirthplace;
   
    /**
     * @return the strGender
     */
    public String getGender( )
    {
        return strGender;
    }
    
    /**
     * @param strGender the strGender to set
     */
    public void setGender( String strGender )
    {
        this.strGender = strGender;
    }
    
    /**
     * @return the strFirstname
     */
    public String getFirstname( )
    {
        return strFirstname;
    }
    
    /**
     * @param strFirstname the strFirstname to set
     */
    public void setFirstname( String strFirstname )
    {
        this.strFirstname = strFirstname;
    }
    
    /**
     * @return the strLastName
     */
    public String getLastName( )
    {
        return strLastName;
    }
    
    /**
     * @param strLastName the strLastName to set
     */
    public void setLastName( String strLastName )
    {
        this.strLastName = strLastName;
    }
    
    /**
     * @return the strPreferredUsername
     */
    public String getPreferredUsername( )
    {
        return strPreferredUsername;
    }
    
    /**
     * @param strPreferredUsername the strPreferredUsername to set
     */
    public void setPreferredUsername( String strPreferredUsername )
    {
        this.strPreferredUsername = strPreferredUsername;
    }
    
    /**
     * @return the strBirthdate
     */
    public String getBirthdate( )
    {
        return strBirthdate;
    }
    
    /**
     * @param strBirthdate the strBirthdate to set
     */
    public void setBirthdate( String strBirthdate )
    {
        this.strBirthdate = strBirthdate;
    }
    
    /**
     * @return the strBirthcountryCode
     */
    public String getBirthcountryCode( )
    {
        return strBirthcountryCode;
    }
    
    /**
     * @param strBirthcountryCode the strBirthcountryCode to set
     */
    public void setBirthcountryCode( String strBirthcountryCode )
    {
        this.strBirthcountryCode = strBirthcountryCode;
    }
    /**
     * @return the strBirthplaceCode
     */
    public String getBirthplaceCode( )
    {
        return strBirthplaceCode;
    }
    
    /**
     * @param strBirthplaceCode the strBirthplaceCode to set
     */
    public void setBirthplaceCode( String strBirthplaceCode )
    {
        this.strBirthplaceCode = strBirthplaceCode;
    }

    /**
     * @return the strBirthcountry
     */
    public String getBirthcountry( )
    {
        return strBirthcountry;
    }

    /**
     * @param strBirthcountry the strBirthcountry to set
     */
    public void setBirthcountry( String strBirthcountry )
    {
        this.strBirthcountry = strBirthcountry;
    }

    /**
     * @return the strBirthplace
     */
    public String getBirthplace( )
    {
        return strBirthplace;
    }

    /**
     * @param strBirthplace the strBirthplace to set
     */
    public void setBirthplace( String strBirthplace )
    {
        this.strBirthplace = strBirthplace;
    }
    
    

}
