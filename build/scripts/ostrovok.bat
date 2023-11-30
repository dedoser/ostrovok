@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  ostrovok startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and OSTROVOK_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\ostrovok-1.0-SNAPSHOT.jar;%APP_HOME%\lib\koin-ktor-3.5.0.jar;%APP_HOME%\lib\koin-logger-slf4j-3.5.0.jar;%APP_HOME%\lib\koin-core-jvm-3.5.0.jar;%APP_HOME%\lib\ktor-server-resources-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-server-netty-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-server-content-negotiation-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-json-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-server-host-common-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-server-core-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-resources-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-serialization-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-events-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-http-cio-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-websockets-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-http-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-network-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-utils-jvm-2.3.5.jar;%APP_HOME%\lib\ktor-io-jvm-2.3.5.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.9.0.jar;%APP_HOME%\lib\mongodb-driver-sync-4.11.0.jar;%APP_HOME%\lib\logback-classic-1.4.5.jar;%APP_HOME%\lib\spring-data-mongodb-4.1.5.jar;%APP_HOME%\lib\hazelcast-5.3.2.jar;%APP_HOME%\lib\elasticsearch-java-8.11.1.jar;%APP_HOME%\lib\jackson-core-2.15.2.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.9.0.jar;%APP_HOME%\lib\kotlinx-datetime-jvm-0.4.1.jar;%APP_HOME%\lib\kotlin-reflect-1.8.22.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.5.1.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.5.1.jar;%APP_HOME%\lib\atomicfu-jvm-0.22.0.jar;%APP_HOME%\lib\kotlin-stdlib-1.9.0.jar;%APP_HOME%\lib\spring-data-commons-3.1.5.jar;%APP_HOME%\lib\slf4j-api-2.0.4.jar;%APP_HOME%\lib\mongodb-driver-core-4.11.0.jar;%APP_HOME%\lib\bson-record-codec-4.11.0.jar;%APP_HOME%\lib\bson-4.11.0.jar;%APP_HOME%\lib\logback-core-1.4.5.jar;%APP_HOME%\lib\spring-tx-6.0.13.jar;%APP_HOME%\lib\spring-context-6.0.13.jar;%APP_HOME%\lib\spring-aop-6.0.13.jar;%APP_HOME%\lib\spring-beans-6.0.13.jar;%APP_HOME%\lib\spring-expression-6.0.13.jar;%APP_HOME%\lib\spring-core-6.0.13.jar;%APP_HOME%\lib\elasticsearch-rest-client-8.11.1.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\parsson-1.0.0.jar;%APP_HOME%\lib\jakarta.json-api-2.0.1.jar;%APP_HOME%\lib\opentelemetry-api-1.29.0.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.7.3.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.7.3.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.9.10.jar;%APP_HOME%\lib\annotations-23.0.0.jar;%APP_HOME%\lib\netty-codec-http2-4.1.97.Final.jar;%APP_HOME%\lib\alpn-api-1.1.3.v20160715.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.97.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.97.Final.jar;%APP_HOME%\lib\spring-jcl-6.0.13.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\httpasyncclient-4.1.5.jar;%APP_HOME%\lib\httpcore-nio-4.4.13.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\opentelemetry-context-1.29.0.jar;%APP_HOME%\lib\config-1.4.2.jar;%APP_HOME%\lib\jansi-2.4.0.jar;%APP_HOME%\lib\netty-codec-http-4.1.97.Final.jar;%APP_HOME%\lib\netty-handler-4.1.97.Final.jar;%APP_HOME%\lib\netty-codec-4.1.97.Final.jar;%APP_HOME%\lib\netty-transport-classes-kqueue-4.1.97.Final.jar;%APP_HOME%\lib\netty-transport-classes-epoll-4.1.97.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.97.Final.jar;%APP_HOME%\lib\netty-transport-4.1.97.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.97.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.97.Final.jar;%APP_HOME%\lib\netty-common-4.1.97.Final.jar


@rem Execute ostrovok
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %OSTROVOK_OPTS%  -classpath "%CLASSPATH%"  %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable OSTROVOK_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%OSTROVOK_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
