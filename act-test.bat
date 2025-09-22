@echo off
setlocal EnableDelayedExpansion

echo ===================================
echo Testing Maven Publish Workflow
echo ===================================
echo.

REM Check if act is installed
where act >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: act is not installed or not in PATH
    echo Please install act using one of these methods:
    echo   - choco install act-cli
    echo   - Download from: https://github.com/nektos/act/releases
    echo   - Or use winget install nektos.act
    exit /b 1
)

REM Check if .secrets.act exists
if not exist .secrets.act (
    echo WARNING: .secrets.act file not found!
    echo This is needed for testing authentication with GitHub Packages.
    echo.
    echo To create .secrets.act file, add this line:
    echo GITHUB_TOKEN=your_personal_access_token_here
    echo.
    echo You can create a Personal Access Token at:
    echo https://github.com/settings/tokens
    echo Required scopes: write:packages, read:packages
    echo.
    set /p continue="Continue without secrets file? (y/N): "
    if /i "!continue!" neq "y" (
        exit /b 1
    )
    set SECRET_FILE=
) else (
    set SECRET_FILE=--secret-file .secrets.act
    echo ✅ Found .secrets.act file
)

echo.
echo 🚀 Running workflow with act (dry run mode)...
echo.

REM Run act with workflow_dispatch event
act workflow_dispatch ^
    -W .github/workflows/maven-publish.yml ^
    %SECRET_FILE% ^
    --input version=4.3.0-TEST ^
    --input skip_tests=true ^
    --dryrun ^
    --verbose

echo.
if %errorlevel% equ 0 (
    echo ✅ Workflow test completed successfully!
    echo.
    echo Next steps:
    echo 1. Review the output above for any issues
    echo 2. Test without --dryrun flag if needed
    echo 3. Commit and push your workflow changes
) else (
    echo ❌ Workflow test failed!
    echo Check the output above for error details.
)

echo.
echo For real testing (not dry run), remove the --dryrun flag from this script.
echo.
pause