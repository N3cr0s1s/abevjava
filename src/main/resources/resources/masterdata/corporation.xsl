<?xml version="1.0" encoding="windows-1250"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
	<xsl:include href="resources/masterdata/base.xsl"/>
	
	<xsl:template match="/">
	<Repository>
      <Entity>
		<type>Társaság</type>
		<id>1</id>
		<Block>
			<name>Törzsadatok</name>
			<seq>1</seq>
			<MasterData>
				<key>Adózó neve</key>
				<val><xsl:value-of select="Entity/Block[name = 'Törzsadatok']/MasterData[key='Adózó neve']/val"/></val>
			</MasterData>
			<MasterData>
				<key>Adózó adószáma</key>
				<val><xsl:value-of select="Entity/Block[name = 'Törzsadatok']/MasterData[key='Adózó adószáma']/val"/></val>
			</MasterData>
			<MasterData>
				<key>Közösségi adószám ország kód</key>
				<xsl:call-template name="buildValueList">
					<xsl:with-param name="navTechnicalBlock" select="'NAV_Közösségi_adószám'"/>
					<xsl:with-param name="panids" select="'Közösségi adószám ország kód'"/>
				</xsl:call-template>
			</MasterData>
			<MasterData>
				<key>Közösségi adószám</key>
				<xsl:call-template name="buildValueList">
					<xsl:with-param name="navTechnicalBlock" select="'NAV_Közösségi_adószám'"/>
					<xsl:with-param name="panids" select="'Közösségi adószám'"/>
				</xsl:call-template>
			</MasterData>
			<MasterData>
				<key>E-mail címe</key>
				<val/>
			</MasterData>
			<MasterData>
				<key>Adózó telefonszáma</key>
				<val/>
			</MasterData>
            <MasterData>
                <key>Nyilvántartó törvényszék</key>
                <val/>
            </MasterData>
            <MasterData>
                <key>Szervezet típus</key>
                <val/>
            </MasterData>
            <MasterData>
                <key>Nyilvántartási sorszám</key>
                <val/>
            </MasterData>
            <MasterData>
                <key>Adózó fax száma</key>
                <val/>
            </MasterData>
		</Block>
		
		<xsl:call-template name="buildAddressBlock">
			<xsl:with-param name="addressType" select="'Állandó cím'"/>
			<xsl:with-param name="prefix" select="''"/>
		</xsl:call-template>
		
		<xsl:call-template name="buildAddressBlock">
			<xsl:with-param name="addressType" select="'Levelezési cím'"/>
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