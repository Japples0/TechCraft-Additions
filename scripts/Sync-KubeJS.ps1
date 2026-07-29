param(
    [string] $DevInstancePath = $env:TECHCRAFT_DEV_INSTANCE
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $PSScriptRoot
$sourceRoot = Join-Path $repoRoot "pack\kubejs"

if ([string]::IsNullOrWhiteSpace($DevInstancePath)) {
    throw "Set -DevInstancePath or TECHCRAFT_DEV_INSTANCE to the isolated development instance path."
}

if (-not (Test-Path -LiteralPath $sourceRoot -PathType Container)) {
    throw "KubeJS source folder is missing: $sourceRoot"
}

if (-not (Test-Path -LiteralPath $DevInstancePath -PathType Container)) {
    throw "Development instance path does not exist: $DevInstancePath"
}

$resolvedSource = (Resolve-Path -LiteralPath $sourceRoot).Path
$resolvedInstance = (Resolve-Path -LiteralPath $DevInstancePath).Path
$instanceName = Split-Path -Leaf $resolvedInstance

if ($instanceName -ne "Dev-TechCraft") {
    throw "Refusing to sync: destination instance must be named Dev-TechCraft. Got: $instanceName"
}

if ($resolvedInstance -match "TechCraft \(1\)") {
    throw "Refusing to sync into the live TechCraft profile: $resolvedInstance"
}

if ($resolvedInstance -match "\\saves(\\|$)") {
    throw "Refusing to sync into or below a saves folder: $resolvedInstance"
}

$destinationRoot = Join-Path $resolvedInstance "kubejs"
if (-not (Test-Path -LiteralPath $destinationRoot -PathType Container)) {
    throw "Destination KubeJS folder is missing: $destinationRoot"
}

$resolvedDestination = (Resolve-Path -LiteralPath $destinationRoot).Path

Write-Host "TechCraft: Additions KubeJS sync"
Write-Host "Source:      $resolvedSource"
Write-Host "Destination: $resolvedDestination"

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$backupRoot = Join-Path $resolvedDestination "_codex_backups\techcraft_additions_sync_$timestamp"
$sourceFiles = Get-ChildItem -LiteralPath $resolvedSource -Recurse -File

foreach ($sourceFile in $sourceFiles) {
    $relativePath = $sourceFile.FullName.Substring($resolvedSource.Length + 1)
    $destinationFile = Join-Path $resolvedDestination $relativePath
    $destinationParent = Split-Path -Parent $destinationFile

    if (-not $destinationParent.StartsWith($resolvedDestination, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Refusing unsafe destination path: $destinationFile"
    }

    if (Test-Path -LiteralPath $destinationFile -PathType Leaf) {
        $backupFile = Join-Path $backupRoot $relativePath
        $backupParent = Split-Path -Parent $backupFile
        New-Item -ItemType Directory -Force -Path $backupParent | Out-Null
        Copy-Item -LiteralPath $destinationFile -Destination $backupFile -Force
        Write-Host "Backed up:   $relativePath"
    }

    New-Item -ItemType Directory -Force -Path $destinationParent | Out-Null
    Copy-Item -LiteralPath $sourceFile.FullName -Destination $destinationFile -Force
    Write-Host "Synced:      $relativePath"
}

Write-Host "Sync complete."
