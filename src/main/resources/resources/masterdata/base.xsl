<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
				
	<xsl:template name="buildValueList">
		<xsl:param name="navTechnicalBlock"/>
		<xsl:param name="panids"/>
		<xsl:if test="count(/Entity/Block/name[text() = $navTechnicalBlock]) = 0">
			<val/>
		</xsl:if>
		<xsl:if test="count(/Entity/Block/name[text() = $navTechnicalBlock]) > 0">	
			<xsl:for-each select="Entity/Block[name = $navTechnicalBlock]/MasterData[key=$panids]">
				<val><xsl:value-of select="val/text()"/></val>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="buildAddressBlock">
		<xsl:param name="addressType"/>
		<xsl:param name="prefix"/>
		<xsl:if test="Entity/Block[name=$addressType]">
			<xsl:if test="Entity/Block[name=$addressType]/MasterData[key='NAV_CIM_OSZT_KOD']/val/text() = 'HEL'">
				<xsl:call-template name="HEL_addressBlock">
					<xsl:with-param name="addressType" select="$addressType"/>
					<xsl:with-param name="prefix" select="$prefix"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="Entity/Block[name=$addressType]/MasterData[key='NAV_CIM_OSZT_KOD']/val/text() = 'TAN'">
				<xsl:call-template name="TAN_addressBlock">
					<xsl:with-param name="addressType" select="$addressType"/>
					<xsl:with-param name="prefix" select="$prefix"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="Entity/Block[name=$addressType]/MasterData[key='NAV_CIM_OSZT_KOD']/val/text() = 'POF'">
				<xsl:call-template name="POF_addressBlock">
					<xsl:with-param name="addressType" select="$addressType"/>
					<xsl:with-param name="prefix" select="$prefix"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		<xsl:if test="$addressType = 'Telephelyek'">
			<xsl:call-template name="EMPTY_addressBlock">
				<xsl:with-param name="addressType" select="$addressType"/>
				<xsl:with-param name="prefix" select="$prefix"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="HEL_addressBlock">
		<xsl:param name="addressType"/>
		<xsl:param name="prefix"/>
		<Block>			
          <name><xsl:value-of select="$addressType"/></name>
		  <seq>1</seq>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Település')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Település')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület neve')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Közterület neve')]/val"/></val>
          </MasterData>
		  <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület jellege')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Közterület jellege')]/val"/></val>
          </MasterData>		            
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Házszám')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Házszám')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Épület')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Épület')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Lépcsőház')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Lépcsőház')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Emelet')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Emelet')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Ajtó')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Ajtó')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Irányítószám')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Irányítószám')]/val"/></val>
          </MasterData>
		</Block>
	</xsl:template>
	
	<xsl:template name="TAN_addressBlock">
		<xsl:param name="addressType"/>
		<xsl:param name="prefix"/>
		<Block>			
          <name><xsl:value-of select="$addressType"/></name>
		  <seq>1</seq>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Település')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Település')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület neve')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key='NAV_CIM_UTCA']/val"/></val>
          </MasterData>
		  <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület jellege')"/></key>
               <val/>
          </MasterData>		            
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Házszám')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Épület')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Lépcsőház')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Emelet')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Ajtó')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Irányítószám')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Irányítószám')]/val"/></val>
          </MasterData>
		</Block>
	</xsl:template>
	
	<xsl:template name="POF_addressBlock">
		<xsl:param name="addressType"/>
		<xsl:param name="prefix"/>
		<Block>			
          <name><xsl:value-of select="$addressType"/></name>
		  <seq>1</seq>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Település')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Település')]/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület neve')"/></key>
               <val/>
          </MasterData>
		  <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület jellege')"/></key>
               <val>Pf.</val>
          </MasterData>		            
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Házszám')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key='NAV_CIM_PF']/val"/></val>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Épület')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Lépcsőház')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Emelet')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Ajtó')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Irányítószám')"/></key>
               <val><xsl:value-of select="Entity/Block[name = $addressType]/MasterData[key=concat($prefix,'Irányítószám')]/val"/></val>
          </MasterData>
		</Block>
	</xsl:template>
	
	<xsl:template name="EMPTY_addressBlock">
		<xsl:param name="addressType"/>
		<xsl:param name="prefix"/>
		<Block>			
          <name><xsl:value-of select="$addressType"/></name>
		  <seq>1</seq>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Település')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület neve')"/></key>
               <val/>
          </MasterData>
		  <MasterData>
               <key><xsl:value-of select="concat($prefix,'Közterület jellege')"/></key>
               <val/>
          </MasterData>		            
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Házszám')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Épület')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Lépcsőház')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Emelet')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Ajtó')"/></key>
               <val/>
          </MasterData>
          <MasterData>
               <key><xsl:value-of select="concat($prefix,'Irányítószám')"/></key>
               <val/>
          </MasterData>
		</Block>
	</xsl:template>
	
	<xsl:template name="szuletesi_adatok">
		<Block>
			<name>Születési adatok</name>
			<seq>1</seq>
			<MasterData>
				<key>Születési családnév</key>
				<val><xsl:value-of select="Entity/Block[name = 'Születési adatok']/MasterData[key='Születési családnév']/val"/></val>
			</MasterData>
			<MasterData>
				<key>Születési utónév</key>
				<val><xsl:value-of select="Entity/Block[name = 'Születési adatok']/MasterData[key='Születési utónév']/val"/></val>
          </MasterData>
          <MasterData>
				<key>Születési időpont</key>
				<val><xsl:value-of select="Entity/Block[name = 'Születési adatok']/MasterData[key='Születési időpont']/val"/></val>
          </MasterData>
          <MasterData>
				<key>Születési hely</key>
				<val><xsl:value-of select="Entity/Block[name = 'Születési adatok']/MasterData[key='Születési hely']/val"/></val>
          </MasterData>
          <MasterData>
				<key>Anyja születési családneve</key>
				<val><xsl:value-of select="Entity/Block[name = 'Születési adatok']/MasterData[key='Anyja születési családneve']/val"/></val>
          </MasterData>
          <MasterData>
				<key>Anyja születési utóneve</key>
				<val><xsl:value-of select="Entity/Block[name = 'Születési adatok']/MasterData[key='Anyja születési utóneve']/val"/></val>
          </MasterData>
		</Block>
	</xsl:template>
	
	<xsl:template name="egyeb_adatok">
		<Block>
			<name>Egyéb adatok</name>
			<seq>1</seq>
			<MasterData>
               <key>Ügyintéző neve</key>
               <val></val>
			</MasterData>
			<MasterData>
               <key>Ügyintéző telefonszáma</key>
               <val></val>
			</MasterData>
			<MasterData>
               <key>Ügyintéző e-mail címe</key>
               <val></val>
			</MasterData>
			<MasterData>
				<key>Pénzintézet neve</key>
				<xsl:call-template name="buildValueList">
					<xsl:with-param name="navTechnicalBlock" select="'NAV_Bankszámlaszám'"/>
					<xsl:with-param name="panids" select="'Pénzintézet neve'" />
				</xsl:call-template>				
			</MasterData>
			<MasterData>			
				<key>Pénzintézet-azonosító</key>
				<xsl:call-template name="buildValueList">
					<xsl:with-param name="navTechnicalBlock" select="'NAV_Bankszámlaszám'"/>
					<xsl:with-param name="panids" select="'Pénzintézet-azonosító'" />
				</xsl:call-template>				
			</MasterData>
			<MasterData>
				<key>Számla-azonosító</key>
				<xsl:call-template name="buildValueList">
					<xsl:with-param name="navTechnicalBlock" select="'NAV_Bankszámlaszám'"/>
					<xsl:with-param name="panids" select="'Számla-azonosító'" />
				</xsl:call-template>
			</MasterData>
		</Block>       
	</xsl:template>
	
	<xsl:template name="vpop_torzsadatok">
		<Block>
          <name>VPOP törzsadatok</name>
		  <seq>1</seq>
          <MasterData>
               <key>VPID</key>
               <val><xsl:value-of select="Entity/Block[name = 'VPOP törzsadatok']/MasterData[key='VPID']/val"/></val>
          </MasterData>
          <MasterData>
               <key>Regisztrációs szám</key>
               <val><xsl:value-of select="Entity/Block[name = 'VPOP törzsadatok']/MasterData[key='Regisztrációs szám']/val"/></val>
          </MasterData>
          <MasterData>
			<key>Engedélyszám</key>
			<xsl:call-template name="buildValueList">
				<xsl:with-param name="navTechnicalBlock" select="'NAV_VPOP_engedélyszám'"/>
				<xsl:with-param name="panids" select="'Engedélyszám'" />
			</xsl:call-template>			
          </MasterData>
          <MasterData>
               <key>Bejelentő adóazonosító jele</key>
               <val></val>
          </MasterData>	
		</Block>
	</xsl:template>
				
</xsl:stylesheet>				