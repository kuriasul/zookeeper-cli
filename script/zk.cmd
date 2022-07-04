@rem ----------------------------------------------------------------------------
@rem Zookeeper client command line tool
@rem ----------------------------------------------------------------------------

@echo off

@setlocal

set ERROR_CODE=0

@setlocal

if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%/bin/java.exe" goto init

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:init

set ZK_CMD_LINE_ARGS=%*

cd /d %~dp0
cd ..
set ZK_HOME=%CD%

set ZK_JAVA_EXE="%JAVA_HOME%/bin/java.exe"

set ZK_AGENT_JAR=%ZK_HOME%/agent/zookeeper-cli-agent.jar

set ZK_APPLICATION_JAR=%ZK_HOME%/boot/zookeeper-cli-application.jar

%ZK_JAVA_EXE% -javaagent:%ZK_AGENT_JAR% -jar %ZK_APPLICATION_JAR% %ZK_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

exit /B %ERROR_CODE%
