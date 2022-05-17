@echo off
setlocal enabledelayedexpansion

rem Check number of arguments
set argC=0
for %%x in (%*) do set /A argC+=1
if "%argC%" neq "1" (
    if "%argC%" neq "2" (
        echo Usage: %0 config-folder [mode: {check,extract}]
        echo.
        echo Examples:
        echo # Use config folder ^"src^" (assuming default ^"check^" mode^) for immutable files check
        echo %0 src
        echo or explicitly giving the ^"check^" mode
        echo %0 src check
        echo # Use config folder ^"src^" as a destination to extract immutable files into it
        echo %0 src extract
        exit /B 2
    )
)

rem Check mode
set mode=%2

if [!mode!] equ [] (
	echo empty mode param, assuming mode = 'check'
	set mode=check
) else (
	if [!mode!] neq [check] (
	    if [!mode!] neq [extract] (
	        echo mode '!mode!' is neither 'check' nor 'extract'
	        exit /B 2
	    )
	)
)

echo running in '!mode!' mode

rem Validate config folder
set folder=%~dpfn1
if not exist %folder%\* (
    echo ** error: Config folder not found: %folder%
    exit /B 2
)

rem Verify docker file is available
set repo=adobe
set image=aem-ethos/dispatcher-publish
set version=2.0.90
set "imageurl=%repo%/%image%:%version%"

for /F "tokens=* USEBACKQ" %%f in (`docker images -q %imageurl%`) do (
    set location=%%f
)

rem Load docker file
if [%location%] equ [] (
    echo Required image not found, trying to load from archive...
    for %%F in (%0) do set dirname=%%~dpF
    set file=!dirname!dispatcher-publish.tar.gz
    if not exist !file! (
        echo ** error: unable to find archive at expected location: !file!
        exit /B 2
    )
    docker load -i !file!
    if !ERRORLEVEL! neq 0 (
        exit /B 2
    )
)

rem Run docker
set scriptDir=%~dp0

set configVolumeMountMode=rw
if [!mode!] equ [check] (
    set configVolumeMountMode=ro
)

docker run --rm ^
    -v %folder%:/etc/httpd-actual:!configVolumeMountMode! ^
    -v %scriptDir%immutability_check.sh:/usr/sbin/immutability_check.sh:ro ^
    --entrypoint /bin/sh %imageurl% /usr/sbin/immutability_check.sh /etc/httpd/immutable.files.txt /etc/httpd-actual !mode!
