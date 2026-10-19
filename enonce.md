# TP 01 — Connexion & DataSource

> Module *Java · Accès aux données* · Chapitre 1 · Durée estimée : **45 min** (30h) · **45 min** (28h) · **40 min** (20h) · Niveau : débutant
> Statut : **obligatoire** dans les trois volumes (30h / 28h / 20h)

## Objectifs d'apprentissage

À l'issue de ce TP, vous êtes capable de :

- exécuter une requête depuis Java avec `DriverManager`, et lire son résultat ;
- dire pourquoi le pilote est une **dépendance** alors que l'API JDBC n'en est pas une ;
- construire un `DataSource` HikariCP unique, configuré **par variables d'environnement** ;
- mesurer ce que coûte l'ouverture d'une connexion, comparé à un emprunt au pool ;
- lire en entier une exception de connexion et en expliquer la cause.

## Contexte

La quincaillerie dispose d'une base `compta`, celle du module de SQL. Elle tourne dans un
conteneur MariaDB 12.3. Aujourd'hui, on établit le contact avec elle depuis Java, d'abord de la
façon la plus simple, puis de la façon qu'on garde pour la suite du module.

## Prérequis

- JDK **25** et Maven **3.9+** (`java -version`, `mvn -version`), Docker en fonctionnement, Git.
- L'environnement démarré : le dossier `env/` est fourni dans l'archive de ce TP.
  `cd env && docker compose up -d`, puis `docker compose ps` (voir `env/README.md`).
- Le projet de départ `projet-starter/`, fourni dans la même archive. Son `README.md` porte la
  liste de mise en route (dépôt GitHub privé compris) : faites-la d'abord.

## Instructions

Vous tenez un fichier `NOTES.md` à la racine du projet. Plusieurs questions vous demandent d'y
écrire une **prédiction avant d'exécuter**, puis ce que vous avez observé. Une prédiction fausse
n'est pas pénalisée ; une prédiction absente, si.

### 1 — Le projet de départ

1. Copiez `projet-starter/` sous le nom `demo-data-access` et ouvrez-le dans votre IDE.
   `mvn -q compile` doit passer.
2. Ouvrez le `pom.xml`. Combien de dépendances concernent JDBC ? Combien concernent l'**API**
   JDBC (`java.sql`) ? Répondez dans `NOTES.md`.

La classe `org.sebsy.compta.infra.Env` est fournie : `Env.required("DB_URL")` renvoie la variable
d'environnement, ou lève une exception qui dit laquelle manque.

### 2 — Les identifiants

3. Déclarez trois variables d'environnement dans la **configuration d'exécution de votre IDE**
   (IntelliJ : *Run > Edit Configurations > Environment variables*) :

   ```
   DB_URL=jdbc:mariadb://localhost:3306/compta?useServerPrepStmts=true&connectionTimeZone=SERVER
   DB_USER=app
   DB_PASSWORD=app
   ```

> Une variable exportée dans un terminal n'est **pas** vue par un programme lancé depuis l'IDE.
> C'est l'écueil le plus fréquent de ce TP.

Aucun identifiant n'est écrit dans le code ni dans un fichier versionné. Sur les TP notés (03, 07,
09, 12), un mot de passe dans un fichier versionné rend le rendu non évalué.

### 3 — Le premier programme

4. Écrivez `org.sebsy.compta.app.PremierContact`. Dans un `try-with-resources`, il ouvre une
   connexion avec `DriverManager.getConnection(url, user, password)` (valeurs lues par
   `Env.required`), exécute `SELECT COUNT(*) FROM fournisseur`, puis affiche :
   - `cnx.getClass().getName()` ;
   - le nombre de fournisseurs.
5. Complétez le programme : il affiche ensuite la liste des fournisseurs (`id`, `nom`, `ville`)
   avec une boucle `while (rs.next())`, en lisant les colonnes **par leur nom**.

Attendu : 4 fournisseurs, de *Française d'Imports* (Nantes) à *Quincaillerie du Maine* (Le Mans).

### 4 — Le pool

6. Écrivez `org.sebsy.compta.infra.Db`. Elle expose **un seul** `DataSource` HikariCP pour toute
   l'application (`Db.dataSource()`) et une méthode `Db.close()`. Réglez :
   - `poolName` à `compta-pool` ;
   - `maximumPoolSize` : une valeur choisie, pas le défaut ;
   - `connectionTimeout` : échouer en quelques secondes plutôt qu'attendre ;
   - `leakDetectionThreshold` à 20 s (`20_000`) : il servira au TP 03.

<details>
<summary>Indice — quelle classe instancier, et quand ?</summary>

`HikariConfig` porte la configuration, `HikariDataSource` est le pool. Le second se construit à
partir du premier, une seule fois : un champ `static final` initialisé par une méthode privée. Le
projet n'a pas de conteneur d'injection, une abstraction de plus n'apporterait rien.
</details>

7. **Prédiction.** Dans `PremierContact`, vous allez remplacer `DriverManager.getConnection(…)`
   par `Db.dataSource().getConnection()`. Écrivez dans `NOTES.md` le nom de classe que le
   programme affichera, **avant** de lancer. Faites le changement, lancez, notez la classe
   obtenue et expliquez l'écart s'il y en a un.
8. Écrivez `app.MesurePool` : il chronomètre 100 connexions ouvertes et refermées avec
   `DriverManager`, puis 100 emprunts et restitutions au pool (`System.nanoTime()`). Démarrez le
   pool **avant** le chronomètre. Lancez-le trois fois et reportez les six mesures dans
   `NOTES.md`, en microsecondes par connexion.

### 5 — La preuve par l'erreur

9. Arrêtez le conteneur (`docker compose stop db`). **Prédiction** : combien de temps avant
   l'échec de `PremierContact` ? Lancez, mesurez, et recopiez dans `NOTES.md` la ligne de
   l'exception et ses deux premières lignes `Caused by`. Pourquoi ce délai ?
10. Redémarrez le conteneur (`docker compose start db`). Retirez `DB_PASSWORD` de la
    configuration d'exécution et relancez. Recopiez l'exception. Le message vous dit-il quoi
    corriger ?

## Livrables

- Le projet poussé sur la branche `main` de votre dépôt privé, un commit par étape.
- `NOTES.md` : réponse à la question 2, prédiction et observation de la question 7, les six
  mesures de la question 8, les messages recopiés et vos explications des questions 9 et 10.
- Critère de « terminé » : `PremierContact` passe par `Db`, affiche le nom de classe de la
  connexion puis les 4 fournisseurs, et `MesurePool` affiche ses deux mesures.

## Pour aller plus loin (optionnel)

- Remplacez `mariadb` par `postgresql` dans `DB_URL` sans rien changer d'autre. Lisez l'erreur :
  qu'est-ce qui manque au projet ?
