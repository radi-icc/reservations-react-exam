$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$backendPath = Join-Path $projectRoot "reservations-react-backend\\backend\\backend"
$mavenCachePath = Join-Path $backendPath ".maven-cache"

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    throw "Java 17 ou une version plus recente est requis pour lancer l'API."
}

Push-Location $backendPath
try {
    & .\\mvnw.cmd "-Dmaven.repo.local=$mavenCachePath" spring-boot:run
}
finally {
    Pop-Location
}
