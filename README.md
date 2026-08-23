# Projet Reservations

Application de catalogue et de reservation de spectacles, composee d'un frontend React et d'une API Spring Boot.

## Prerequis

- Node.js 20.19 ou plus recent (Node 24 est utilise sur la machine de developpement actuelle)
- Java 17 a 24
- Docker Desktop avec Docker Compose pour la base de donnees MySQL

## Demarrage local

Dans PowerShell, depuis la racine du depot :

```powershell
docker compose up -d mysql
./scripts/start-backend.ps1
./scripts/start-frontend.ps1
```

L'application est disponible sur `http://localhost:5173`, l'API sur `http://localhost:8085/api` et Swagger sur `http://localhost:8085/swagger-ui/index.html`.

Au premier demarrage, MySQL initialise automatiquement le schema et des donnees de demonstration : lieux, spectacles, representations, tarifs, artistes et formules d'affiliation. Les comptes membres doivent etre crees depuis l'ecran d'inscription.

## Configuration

Le frontend utilise `reservations-react-frontend/.env`. Copiez le modele uniquement si vous devez changer l'URL de l'API :

```powershell
Copy-Item reservations-react-frontend/.env.example reservations-react-frontend/.env
```

L'API accepte les variables d'environnement suivantes : `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS` et `EXTERNAL_SHOWS_API_URL`. Les valeurs par defaut correspondent au service MySQL de ce depot et sont reservees au developpement local.

## Commandes de verification

```powershell
./scripts/test-backend.ps1
Set-Location reservations-react-frontend
npm run build
```

`npm ci` est utilise par le script frontend afin d'installer exactement les versions figees dans `package-lock.json`.

Pour verifier le schema MySQL reel (apres son initialisation), activez le smoke-test dedie :

```powershell
$env:RUN_MYSQL_TESTS = "true"
$env:MYSQL_TEST_URL = "jdbc:mysql://localhost:3306/reservations_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Paris"
Set-Location reservations-react-backend/backend/backend
.\mvnw.cmd test
```

## Reinitialiser les donnees locales

Cette commande supprime uniquement le volume Docker local du projet puis recree la base avec les donnees de demonstration :

```powershell
docker compose down -v
docker compose up -d mysql
```
