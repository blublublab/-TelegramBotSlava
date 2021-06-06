@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
for %%i in ("%~dp0..") do set "BASEDIR=%%~fi"

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\io\reactivex\rxjava\1.3.8\rxjava-1.3.8.jar;"%REPO%"\com\heroku\sdk\heroku-maven-plugin\3.0.3\heroku-maven-plugin-3.0.3.jar;"%REPO%"\com\heroku\sdk\heroku-deploy\3.0.3\heroku-deploy-3.0.3.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.10.2\jackson-core-2.10.2.jar;"%REPO%"\org\apache\commons\commons-compress\1.19\commons-compress-1.19.jar;"%REPO%"\org\apache\commons\commons-text\1.8\commons-text-1.8.jar;"%REPO%"\org\eclipse\jgit\org.eclipse.jgit\5.6.0.201912101111-r\org.eclipse.jgit-5.6.0.201912101111-r.jar;"%REPO%"\com\jcraft\jsch\0.1.55\jsch-0.1.55.jar;"%REPO%"\com\jcraft\jzlib\1.1.1\jzlib-1.1.1.jar;"%REPO%"\com\googlecode\javaewah\JavaEWAH\1.1.6\JavaEWAH-1.1.6.jar;"%REPO%"\org\bouncycastle\bcpg-jdk15on\1.64\bcpg-jdk15on-1.64.jar;"%REPO%"\org\bouncycastle\bcprov-jdk15on\1.64\bcprov-jdk15on-1.64.jar;"%REPO%"\org\bouncycastle\bcpkix-jdk15on\1.64\bcpkix-jdk15on-1.64.jar;"%REPO%"\com\heroku\api\heroku-api\0.43\heroku-api-0.43.jar;"%REPO%"\com\heroku\api\heroku-json-jackson\0.43\heroku-json-jackson-0.43.jar;"%REPO%"\com\heroku\api\heroku-http-apache\0.43\heroku-http-apache-0.43.jar;"%REPO%"\org\apache\maven\maven-plugin-api\3.5.0\maven-plugin-api-3.5.0.jar;"%REPO%"\org\apache\maven\maven-model\3.5.0\maven-model-3.5.0.jar;"%REPO%"\org\eclipse\sisu\org.eclipse.sisu.plexus\0.3.3\org.eclipse.sisu.plexus-0.3.3.jar;"%REPO%"\javax\enterprise\cdi-api\1.0\cdi-api-1.0.jar;"%REPO%"\javax\annotation\jsr250-api\1.0\jsr250-api-1.0.jar;"%REPO%"\org\eclipse\sisu\org.eclipse.sisu.inject\0.3.3\org.eclipse.sisu.inject-0.3.3.jar;"%REPO%"\org\apache\maven\maven-core\3.5.0\maven-core-3.5.0.jar;"%REPO%"\org\apache\maven\maven-settings\3.5.0\maven-settings-3.5.0.jar;"%REPO%"\org\apache\maven\maven-settings-builder\3.5.0\maven-settings-builder-3.5.0.jar;"%REPO%"\org\apache\maven\maven-builder-support\3.5.0\maven-builder-support-3.5.0.jar;"%REPO%"\org\apache\maven\maven-repository-metadata\3.5.0\maven-repository-metadata-3.5.0.jar;"%REPO%"\org\apache\maven\maven-model-builder\3.5.0\maven-model-builder-3.5.0.jar;"%REPO%"\com\google\guava\guava\20.0\guava-20.0.jar;"%REPO%"\org\apache\maven\maven-resolver-provider\3.5.0\maven-resolver-provider-3.5.0.jar;"%REPO%"\org\apache\maven\resolver\maven-resolver-impl\1.0.3\maven-resolver-impl-1.0.3.jar;"%REPO%"\org\apache\maven\resolver\maven-resolver-api\1.0.3\maven-resolver-api-1.0.3.jar;"%REPO%"\org\apache\maven\resolver\maven-resolver-spi\1.0.3\maven-resolver-spi-1.0.3.jar;"%REPO%"\org\apache\maven\resolver\maven-resolver-util\1.0.3\maven-resolver-util-1.0.3.jar;"%REPO%"\org\apache\maven\shared\maven-shared-utils\3.1.0\maven-shared-utils-3.1.0.jar;"%REPO%"\com\google\inject\guice\4.0\guice-4.0-no_aop.jar;"%REPO%"\javax\inject\javax.inject\1\javax.inject-1.jar;"%REPO%"\aopalliance\aopalliance\1.0\aopalliance-1.0.jar;"%REPO%"\org\codehaus\plexus\plexus-interpolation\1.24\plexus-interpolation-1.24.jar;"%REPO%"\org\codehaus\plexus\plexus-utils\3.0.24\plexus-utils-3.0.24.jar;"%REPO%"\org\codehaus\plexus\plexus-classworlds\2.5.2\plexus-classworlds-2.5.2.jar;"%REPO%"\org\codehaus\plexus\plexus-component-annotations\1.7.1\plexus-component-annotations-1.7.1.jar;"%REPO%"\org\sonatype\plexus\plexus-sec-dispatcher\1.4\plexus-sec-dispatcher-1.4.jar;"%REPO%"\org\sonatype\plexus\plexus-cipher\1.4\plexus-cipher-1.4.jar;"%REPO%"\org\apache\commons\commons-lang3\3.5\commons-lang3-3.5.jar;"%REPO%"\org\apache\maven\maven-artifact\3.5.0\maven-artifact-3.5.0.jar;"%REPO%"\org\apache\maven\plugins\maven-dependency-plugin\3.1.1\maven-dependency-plugin-3.1.1.jar;"%REPO%"\org\apache\maven\reporting\maven-reporting-api\3.0\maven-reporting-api-3.0.jar;"%REPO%"\org\apache\maven\reporting\maven-reporting-impl\2.3\maven-reporting-impl-2.3.jar;"%REPO%"\org\apache\maven\doxia\doxia-core\1.2\doxia-core-1.2.jar;"%REPO%"\xerces\xercesImpl\2.9.1\xercesImpl-2.9.1.jar;"%REPO%"\xml-apis\xml-apis\1.3.04\xml-apis-1.3.04.jar;"%REPO%"\commons-validator\commons-validator\1.3.1\commons-validator-1.3.1.jar;"%REPO%"\commons-beanutils\commons-beanutils\1.7.0\commons-beanutils-1.7.0.jar;"%REPO%"\commons-digester\commons-digester\1.6\commons-digester-1.6.jar;"%REPO%"\org\apache\maven\doxia\doxia-sink-api\1.4\doxia-sink-api-1.4.jar;"%REPO%"\org\apache\maven\doxia\doxia-logging-api\1.4\doxia-logging-api-1.4.jar;"%REPO%"\org\apache\maven\doxia\doxia-site-renderer\1.4\doxia-site-renderer-1.4.jar;"%REPO%"\org\apache\maven\doxia\doxia-decoration-model\1.4\doxia-decoration-model-1.4.jar;"%REPO%"\org\apache\maven\doxia\doxia-module-xhtml\1.4\doxia-module-xhtml-1.4.jar;"%REPO%"\org\apache\maven\doxia\doxia-module-fml\1.4\doxia-module-fml-1.4.jar;"%REPO%"\org\codehaus\plexus\plexus-i18n\1.0-beta-7\plexus-i18n-1.0-beta-7.jar;"%REPO%"\org\codehaus\plexus\plexus-container-default\1.0-alpha-30\plexus-container-default-1.0-alpha-30.jar;"%REPO%"\junit\junit\3.8.1\junit-3.8.1.jar;"%REPO%"\org\codehaus\plexus\plexus-velocity\1.1.7\plexus-velocity-1.1.7.jar;"%REPO%"\org\apache\velocity\velocity\1.5\velocity-1.5.jar;"%REPO%"\oro\oro\2.0.8\oro-2.0.8.jar;"%REPO%"\org\apache\velocity\velocity-tools\2.0\velocity-tools-2.0.jar;"%REPO%"\commons-chain\commons-chain\1.1\commons-chain-1.1.jar;"%REPO%"\dom4j\dom4j\1.1\dom4j-1.1.jar;"%REPO%"\sslext\sslext\1.2-0\sslext-1.2-0.jar;"%REPO%"\org\apache\struts\struts-core\1.3.8\struts-core-1.3.8.jar;"%REPO%"\antlr\antlr\2.7.2\antlr-2.7.2.jar;"%REPO%"\org\apache\struts\struts-taglib\1.3.8\struts-taglib-1.3.8.jar;"%REPO%"\org\apache\struts\struts-tiles\1.3.8\struts-tiles-1.3.8.jar;"%REPO%"\org\codehaus\plexus\plexus-archiver\3.6.0\plexus-archiver-3.6.0.jar;"%REPO%"\org\iq80\snappy\snappy\0.4\snappy-0.4.jar;"%REPO%"\org\tukaani\xz\1.8\xz-1.8.jar;"%REPO%"\org\apache\maven\shared\file-management\3.0.0\file-management-3.0.0.jar;"%REPO%"\org\apache\maven\shared\maven-shared-io\3.0.0\maven-shared-io-3.0.0.jar;"%REPO%"\org\apache\maven\maven-compat\3.0\maven-compat-3.0.jar;"%REPO%"\org\apache\maven\wagon\wagon-provider-api\2.10\wagon-provider-api-2.10.jar;"%REPO%"\org\codehaus\plexus\plexus-io\3.0.0\plexus-io-3.0.0.jar;"%REPO%"\org\apache\maven\shared\maven-dependency-analyzer\1.10\maven-dependency-analyzer-1.10.jar;"%REPO%"\org\ow2\asm\asm\6.1.1\asm-6.1.1.jar;"%REPO%"\org\apache\maven\shared\maven-dependency-tree\3.0.1\maven-dependency-tree-3.0.1.jar;"%REPO%"\org\eclipse\aether\aether-util\0.9.0.M2\aether-util-0.9.0.M2.jar;"%REPO%"\org\apache\maven\shared\maven-common-artifact-filters\3.0.1\maven-common-artifact-filters-3.0.1.jar;"%REPO%"\org\sonatype\sisu\sisu-inject-plexus\1.4.2\sisu-inject-plexus-1.4.2.jar;"%REPO%"\org\sonatype\sisu\sisu-inject-bean\1.4.2\sisu-inject-bean-1.4.2.jar;"%REPO%"\org\sonatype\sisu\sisu-guice\2.1.7\sisu-guice-2.1.7-noaop.jar;"%REPO%"\org\apache\maven\shared\maven-artifact-transfer\0.9.1\maven-artifact-transfer-0.9.1.jar;"%REPO%"\commons-lang\commons-lang\2.6\commons-lang-2.6.jar;"%REPO%"\commons-collections\commons-collections\3.2.2\commons-collections-3.2.2.jar;"%REPO%"\classworlds\classworlds\1.1\classworlds-1.1.jar;"%REPO%"\org\twdata\maven\mojo-executor\2.3.1\mojo-executor-2.3.1.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar;"%REPO%"\org\apache\httpcomponents\httpcore\4.4\httpcore-4.4.jar;"%REPO%"\org\apache\httpcomponents\httpasyncclient\4.0.2\httpasyncclient-4.0.2.jar;"%REPO%"\org\apache\httpcomponents\httpcore-nio\4.3.2\httpcore-nio-4.3.2.jar;"%REPO%"\commons-logging\commons-logging\1.1.3\commons-logging-1.1.3.jar;"%REPO%"\org\json\json\20140107\json-20140107.jar;"%REPO%"\mysql\mysql-connector-java\8.0.25\mysql-connector-java-8.0.25.jar;"%REPO%"\com\google\protobuf\protobuf-java\3.11.4\protobuf-java-3.11.4.jar;"%REPO%"\org\telegram\telegrambots\3.5\telegrambots-3.5.jar;"%REPO%"\org\telegram\telegrambots-meta\3.5\telegrambots-meta-3.5.jar;"%REPO%"\com\google\inject\guice\4.1.0\guice-4.1.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.8.0\jackson-annotations-2.8.0.jar;"%REPO%"\com\fasterxml\jackson\jaxrs\jackson-jaxrs-json-provider\2.8.7\jackson-jaxrs-json-provider-2.8.7.jar;"%REPO%"\com\fasterxml\jackson\jaxrs\jackson-jaxrs-base\2.8.7\jackson-jaxrs-base-2.8.7.jar;"%REPO%"\com\fasterxml\jackson\module\jackson-module-jaxb-annotations\2.8.7\jackson-module-jaxb-annotations-2.8.7.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.8.7\jackson-databind-2.8.7.jar;"%REPO%"\org\glassfish\jersey\media\jersey-media-json-jackson\2.25.1\jersey-media-json-jackson-2.25.1.jar;"%REPO%"\org\glassfish\jersey\core\jersey-common\2.25.1\jersey-common-2.25.1.jar;"%REPO%"\org\glassfish\jersey\bundles\repackaged\jersey-guava\2.25.1\jersey-guava-2.25.1.jar;"%REPO%"\org\glassfish\hk2\osgi-resource-locator\1.0.1\osgi-resource-locator-1.0.1.jar;"%REPO%"\org\glassfish\jersey\ext\jersey-entity-filtering\2.25.1\jersey-entity-filtering-2.25.1.jar;"%REPO%"\org\glassfish\jersey\containers\jersey-container-grizzly2-http\2.25.1\jersey-container-grizzly2-http-2.25.1.jar;"%REPO%"\org\glassfish\hk2\external\javax.inject\2.5.0-b32\javax.inject-2.5.0-b32.jar;"%REPO%"\org\glassfish\grizzly\grizzly-http-server\2.3.28\grizzly-http-server-2.3.28.jar;"%REPO%"\org\glassfish\grizzly\grizzly-http\2.3.28\grizzly-http-2.3.28.jar;"%REPO%"\org\glassfish\grizzly\grizzly-framework\2.3.28\grizzly-framework-2.3.28.jar;"%REPO%"\javax\ws\rs\javax.ws.rs-api\2.0.1\javax.ws.rs-api-2.0.1.jar;"%REPO%"\org\glassfish\jersey\core\jersey-server\2.25.1\jersey-server-2.25.1.jar;"%REPO%"\org\glassfish\jersey\core\jersey-client\2.25.1\jersey-client-2.25.1.jar;"%REPO%"\org\glassfish\jersey\media\jersey-media-jaxb\2.25.1\jersey-media-jaxb-2.25.1.jar;"%REPO%"\javax\annotation\javax.annotation-api\1.2\javax.annotation-api-1.2.jar;"%REPO%"\org\glassfish\hk2\hk2-api\2.5.0-b32\hk2-api-2.5.0-b32.jar;"%REPO%"\org\glassfish\hk2\hk2-utils\2.5.0-b32\hk2-utils-2.5.0-b32.jar;"%REPO%"\org\glassfish\hk2\external\aopalliance-repackaged\2.5.0-b32\aopalliance-repackaged-2.5.0-b32.jar;"%REPO%"\org\glassfish\hk2\hk2-locator\2.5.0-b32\hk2-locator-2.5.0-b32.jar;"%REPO%"\org\javassist\javassist\3.20.0-GA\javassist-3.20.0-GA.jar;"%REPO%"\javax\validation\validation-api\1.1.0.Final\validation-api-1.1.0.Final.jar;"%REPO%"\org\apache\httpcomponents\httpmime\4.5.3\httpmime-4.5.3.jar;"%REPO%"\commons-io\commons-io\2.5\commons-io-2.5.jar;"%REPO%"\com\squareup\okhttp\okhttp\2.7.5\okhttp-2.7.5.jar;"%REPO%"\com\squareup\okio\okio\1.6.0\okio-1.6.0.jar;"%REPO%"\com\mashape\unirest\unirest-java\1.4.9\unirest-java-1.4.9.jar;"%REPO%"\org\apache\httpcomponents\httpclient\4.5.13\httpclient-4.5.13.jar;"%REPO%"\commons-codec\commons-codec\1.11\commons-codec-1.11.jar;"%REPO%"\com\vaadin\external\google\android-json\0.0.20131108.vaadin1\android-json-0.0.20131108.vaadin1.jar;"%REPO%"\org\bots\-TelegramBotSlava\1.0-SNAPSHOT\-TelegramBotSlava-1.0-SNAPSHOT.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="tgbot" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" Main %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
