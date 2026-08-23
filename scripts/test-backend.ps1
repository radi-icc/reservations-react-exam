$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$backendPath = Join-Path $projectRoot "reservations-react-backend\\backend\\backend"
$mavenCachePath = Join-Path $backendPath ".maven-cache"

Push-Location $backendPath
try {
    & .\\mvnw.cmd "-Dmaven.repo.local=$mavenCachePath" test
}
finally {
    Pop-Location
}
