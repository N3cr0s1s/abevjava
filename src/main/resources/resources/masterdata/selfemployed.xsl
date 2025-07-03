<?xml version="1.0" encoding="windows-1250"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
	<xsl:include href="resources/masterdata/base.xsl"/>
	
	<xsl:template match="/">
	<Repository>
      <Entity>
		<type>Egy�ni v�llalkoz�</type>
		<id>1</id>
		<Block>
			<name>T�rzsadatok</name>
			<seq>1</seq>
			<MasterData>
				<key>N�v titulus</key>
				<val/>
			</MasterData>
			<MasterData>
				<key>Vezet�kneve</key>
				<val><xsl:value-of select="Entity/Block[name = 'T�rzsadatok']/MasterData[key='NAV_Vezet�kneve']/val"/></val>
			</MasterData>
			<MasterData>
				<key>Keresztneve</key>
				<val><xsl:value-of select="Entity/Block[name = 'T�rzsadatok']/MasterData[key='Keresztneve']/val"/></val>
			</MasterData>
			<MasterData>
				<key>Ad�z� ad�sz�ma</key>
				<val><xsl:value-of select="Entity/Block[name = 'T�rzsadatok']/MasterData[key='Ad�z� ad�sz�ma']/val"/></val>
			</MasterData>
			<MasterData>
				<key>Ad�z� neme</key>
				<val/>
			</MasterData>
			<MasterData>
				<key>Ad�z� ad�azonos�t� jele</key>
				<val><xsl:value-of select="Entity/Block[name = 'T�rzsadatok']/MasterData[key='Ad�z� ad�azonos�t� jele']/val"/></val>
			</MasterData>			
			<MasterData>
				<key>K�z�ss�gi ad�sz�m orsz�g k�d</key>
				<xsl:call-template name="buildValueList">
					<xsl:with-param name="navTechnicalBlock" select="'NAV_K�z�ss�gi_ad�sz�m'"/>
					<xsl:with-param name="panids" select="'K�z�ss�gi ad�sz�m orsz�g k�d'"/>
				</xsl:call-template>
			</MasterData>
			<MasterData>
				<key>K�z�ss�gi ad�sz�m</key>
				<xsl:call-template name="buildValueList">
					<xsl:with-param name="navTechnicalBlock" select="'NAV_K�z�ss�gi_ad�sz�m'"/>
					<xsl:with-param name="panids" select="'K�z�ss�gi ad�sz�m'"/>
				</xsl:call-template>
			</MasterData>
			<MasterData>
				<key>E-mail c�me</key>
				<val/>
			</MasterData>
			<MasterData>
				<key>Ad�z� telefonsz�ma</key>
				<val/>
			</MasterData>
		</Block>
		
		<xsl:call-template name="szuletesi_adatok"/>
		
		<xsl:call-template name="buildAddressBlock">
			<xsl:with-param name="addressType" select="'�lland� c�m'"/>
			<xsl:with-param name="prefix" select="''"/>
		</xsl:call-template>
		
		<xsl:call-template name="buildAddressBlock">
			<xsl:with-param name="addressType" select="'Levelez�si c�m'"/>
			<xsl:with-param name="prefix" select="'L '"/>
		</xsl:call-template>		
		
		<xsl:call-template name="egyeb_adatok"/>
		
		<xsl:call-template name="vpop_torzsadatok"/>		
		
		<xsl:call-template name="buildAddressBlock">
			<xsl:with-param name="addressType" select="'Telephelyek'"/>
			<xsl:with-param name="prefix" select="'T '"/>
		</xsl:call-template>		
      </Entity>
	</Repository>  
    </xsl:template>

</xsl:stylesheet>