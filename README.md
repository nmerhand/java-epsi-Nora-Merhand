# Projet de départ — demo-data-access

Le projet Maven que vous ferez grandir du TP 01 au TP 14. Copiez ce dossier sous le nom
`demo-data-access` et travaillez dedans.

Il contient :

- `pom.xml` : toutes les dépendances du module, versions figées, rangées par chapitre ;
- `src/main/java/org/sebsy/compta/infra/Env.java` : la lecture des variables d'environnement,
  fournie complète ;
- `src/main/resources/logback.xml` : la journalisation (pool à `INFO`, SQL Hibernate à `DEBUG`
  à partir du chapitre 2) ;
- `.gitignore`.

La base MariaDB du module est dans le dossier `env/` de l'archive du TP 01 (`docker compose up -d`,
voir `env/README.md`).

## Mise en route, avant la première question

1. `mvn -q compile` passe dans le dossier copié.
2. `git init`, puis un premier commit (« Initialiser le projet »).
3. Créez un dépôt **privé** sur GitHub, poussez-y la branche `main`.
4. Ajoutez votre formateur comme collaborateur du dépôt : `ssy-<votre-école>` (`ssy-esaip`,
   `ssy-sdv`, `ssy-epsi`).
5. Vérifiez que `.gitignore` est bien commité : `target/`, `.idea/` et tout fichier `*.env`
   restent hors du dépôt.

## Conventions valables pour tous les TP

- Aucun identifiant dans le code ni dans le dépôt : variables d'environnement. Sur un TP noté,
  un mot de passe dans un fichier versionné rend le rendu non évalué.
- Aucune concaténation pour construire du SQL ou du JPQL : paramètres liés.
- Aucune ressource fermée à la main : `try-with-resources`, ou une transaction gérée.
- Aucun type `java.sql` ni `SQLException` hors de la couche DAO (à partir du TP 03), sauf dans
  les programmes d'essai de `app` qui sondent la base pour vérifier un résultat.
- `BigDecimal` dès qu'il s'agit d'argent, jamais `double`.
- Un commit par étape logique, message en français, à l'impératif.
