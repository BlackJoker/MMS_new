<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" version='1.0' encoding='UTF-8' indent='yes'/>

   <xsl:template match="/">
      <html>
         <head>
            <title>Module</title>
         </head>
         <body>
            <xsl:apply-templates />
         </body>
      </html>
   </xsl:template>

   <xsl:template match="Modul">
      <table border="2">
      <xsl:apply-templates />
	  </table>
   </xsl:template>

   <xsl:template match="modulname">
		 <tr bgcolor="#D8D8D8">
         <td><b>Modulname:</b></td>
         <xsl:value-of select="." />
		 </tr>
   </xsl:template>
   
   <xsl:template match="version">
		 
   </xsl:template>
   
   <xsl:template match="jahrgang">
  		 <tr bgcolor="#D8D8D8">
         <td><b>Jahrgang:</b></td>
         <xsl:value-of select="." />
		 </tr>
   </xsl:template>
   
      <xsl:template match="user">
		
   </xsl:template>
   
      <xsl:template match="inbearbeitung">
		 
   </xsl:template>
   
   <xsl:template match="akzeptiert">
		 
   </xsl:template>
   
   <xsl:template match="datum">
		
   </xsl:template>
   
   <xsl:template match="zuordnungen/zuordnung">
		 <tr bgcolor="#D8D8D8">
         <td rowspan="4" valign="top"><b>Zurdnung:</b></td>
		 </tr>
         <xsl:apply-templates />
   </xsl:template>
   
      <xsl:template match="id">
        
   </xsl:template>
        
   <xsl:template match="name">
         <tr><td><b>Name:</b></td><td>
         <xsl:value-of select="." /></td></tr>
   </xsl:template>
   
         <xsl:template match="sid">
         
   </xsl:template>
   
         <xsl:template match="studiengang">
         <tr><td><b>Studiengang:</b></td><td>
         <xsl:value-of select="." /></td></tr>
   </xsl:template>
   
         <xsl:template match="abschluss">
         <tr><td><b>Abschluss:</b></td><td>
         <xsl:value-of select="." /></td></tr>
   </xsl:template>
   
   <xsl:template match="felder/feld">
		 <tr bgcolor="#D8D8D8">
         <xsl:apply-templates />
		 </tr>
   </xsl:template>
	
   <xsl:template match="label">
		 <td><b>
         <xsl:value-of select="." />: 
		 </b></td>
   </xsl:template>
   
    <xsl:template match="value">
		<td>
         <xsl:value-of select="." />
		 </td>
   </xsl:template>
   
   <xsl:template match="dezernat">
              
   </xsl:template>


</xsl:stylesheet>