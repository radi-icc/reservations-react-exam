$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$frontendPath = Join-Path $projectRoot "reservations-react-frontend"

Push-Location $frontendPath
try {
    npm ci --cache .npm-cache
    npm run dev
}
finally {
    Pop-Location
}
