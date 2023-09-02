@echo off
setlocal enabledelayedexpansion

set "bug_lines="
set "balance_lines="
set "localization_lines="
set "uiqol_lines="
set "commit_hash=%1"

if "%commit_hash%"=="" (
    echo No commit hash provided.
    exit /b 1
)

for /f "delims=" %%a in (gitlog.txt) do (
    if "%%a"=="commit %commit_hash%" (
        goto writeProperties
    )
    
    set "line=%%a"
     if "!line:~0,4!"=="Bug:" (
        set "bug_lines=!bug_lines!!line!\n"
    ) else if "!line:~0,5!"=="Text:" (
        set "bug_lines=!bug_lines!!line!\n"
    ) else if "!line:~0,5!"=="Nerf:" (
        set "balance_lines=!balance_lines!!line!\n"
    ) else if "!line:~0,5!"=="Buff:" (
        set "balance_lines=!balance_lines!!line!\n"
    ) else if "!line:~0,4!"=="Loc:" (
        set "localization_lines=!localization_lines!!line!\n"
    ) else if "!line:~0,3!"=="UI:" (
        set "uiqol_lines=!uiqol_lines!!line!\n"
    ) else if "!line:~0,4!"=="QoL:" (
        set "uiqol_lines=!uiqol_lines!!line!\n"
    ) else if "!line:~0,4!"=="QOL:" (
        set "uiqol_lines=!uiqol_lines!!line!\n"
    )
)

:writeProperties
echo bug_lines=!bug_lines! > bug_lines.properties
echo balance_lines=!balance_lines! >> bug_lines.properties
echo localization_lines=!localization_lines! >> bug_lines.properties
echo uiqol_lines=!uiqol_lines! >> bug_lines.properties