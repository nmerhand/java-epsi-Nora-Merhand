# TP 05 — Bootstrap JPA & première entité

> Module *Java · Accès aux données* · Chapitre 2 · Durée estimée : **1h30** (30h) · **1h25** (28h) · **1h** (20h) · Niveau : intermédiaire
> Statut : **obligatoire** dans les trois volumes

## Objectifs d'apprentissage

- Démarrer Hibernate à côté de la couche JDBC, sans rien retirer à celle-ci.
- Créer une unité de persistance avec `HibernatePersistenceConfiguration` (JPA 3.2), sans
  `persistence.xml`.
- Mapper une table existante, objet-valeur compris, et faire passer `validate` au démarrage.
- Lire un refus de `validate`, et nommer un écart qu'il ne détecte pas.
- Mesurer la portée du contexte de persistance.

## Contexte

La couche JDBC des TP 01 à 04 fonctionne et reste en place. Vous ajoutez à côté une unité de
persistance JPA, sur la même base et avec le même pool. Au TP 07, une DAO JPA prendra la place
de la DAO JDBC derrière la même interface : ce TP pose ce dont elle aura besoin, la fabrique et
une première entité.

## Prérequis

- Votre projet à la fin du TP 04, ou `projet-starter/` de ce TP : voir `POINT-DE-DEPART.md`.
- `hibernate-core` et `hibernate-hikaricp` sont déclarées dans le `pom.xml` depuis le TP 01, et
  `logback.xml` affiche déjà le SQL généré (logger `org.hibernate.SQL`).
- `DB_URL`, `DB_USER` et `DB_PASSWORD` dans la configuration d'exécution, comme depuis le TP 01.
  Aucun identifiant dans le code ni dans un fichier versionné.

## Instructions

Vous continuez `NOTES.md`. Un commit par étape.

### 1 — Le démarrage

1. Créez `org.sebsy.compta.infra.Jpa`, qui expose une `SessionFactory` unique (`factory()`) et
   sait la fermer (`close()`). Construisez-la avec `new HibernatePersistenceConfiguration("compta")` :
   - `managedClasses(Fournisseur.class)` : en Java SE, rien n'est découvert tout seul ;
   - l'URL et les identifiants lus par `Env.required(…)` ;
   - `jdbcPoolSize(10)` ;
   - `schemaToolingAction(Action.VALIDATE)` ;
   - la propriété `hibernate.generate_statistics` à `true`.

   N'appelez pas `showSql(…)` : logback affiche déjà chaque requête, et `showSql` l'afficherait
   une seconde fois.
2. Dans `NOTES.md` : quel type déclare `createEntityManagerFactory()` ? Quelle dépendance apporte
   le paquet `jakarta.persistence` ? Lancez `mvn dependency:tree -Dincludes=jakarta.persistence`
   et recopiez les lignes utiles.

### 2 — La première entité

3. Annotez `domain.Fournisseur` : `@Entity`, `@Table(name = "fournisseur")`, `@Id` et
   `@GeneratedValue(strategy = GenerationType.IDENTITY)` sur `id`,
   `@Column(nullable = false, length = 60, unique = true)` sur `nom`, `@Embedded` sur `adresse`.
   Annotez `domain.Adresse` avec `@Embeddable`.
4. Écrivez `app.DemarrageJpa` : il obtient la fabrique, affiche le nom de sa classe, lit le
   fournisseur 4 dans une transaction (`callInTransaction`), affiche ce fournisseur et
   `estComplete()` de son adresse, puis ferme la fabrique. Lancez :
   `mvn -q compile exec:java -Dexec.mainClass=org.sebsy.compta.app.DemarrageJpa`.
   Le démarrage échoue. Recopiez dans `NOTES.md` le fragment qui commence par `Schema validation`,
   mot pour mot.

<details>
<summary>Indice — le message parle de <code>NoClassDefFoundError</code></summary>

La fabrique est construite au premier accès à `Jpa`. Si vous appelez `Jpa.close()` dans un
`finally`, ce second accès échoue à son tour et son message remplace celui d'Hibernate. Obtenez
la fabrique **avant** le `try`, et fermez-la par la variable.
</details>

5. Corrigez avec `@AttributeOverride` sur le champ `adresse` de `Fournisseur`, pas dans le record.
   En une phrase : pourquoi la correction se fait-elle chez le propriétaire ?
6. Relancez. Notez la requête affichée par logback, et ce que renvoie `estComplete()` pour le
   fournisseur 4.

### 3 — Ce que `validate` refuse, et ce qu'il laisse passer

7. **Prédiction datée.** Avant toute exécution, écrivez dans `NOTES.md`, avec la date et l'heure,
   ce que fera `validate` face à cette entité de test, mappée sur la table `article`, qu'aucune
   entité de l'unité ne porte encore :

   ```java
   package org.sebsy.compta.essai;

   @Entity(name = "ArticleCourt")
   @Table(name = "article")
   public class ArticleCourt {
       @Id
       Integer id;     // la colonne est un BIGINT
       String ref;
   }
   ```

   Ajoutez-la à `managedClasses`, lancez `DemarrageJpa`, et notez le constat à côté de la
   prédiction, sans corriger la prédiction. Passez ensuite `id` en `String`, relancez et recopiez
   le fragment `Schema validation`. Qu'est-ce que ces deux résultats disent de ce que `validate`
   compare ? Quel risque reste-t-il avec un `Integer` sur cette colonne ? Retirez ensuite la
   classe de `managedClasses` et du projet.
8. Renommez `@Table(name = "fournisseurs")`. Relancez, recopiez le fragment `Schema validation`,
   remettez le bon nom.
9. Passez temporairement l'action à `Action.UPDATE` et relancez. L'application démarre-t-elle ?
   Recopiez la première commande SQL qu'Hibernate tente d'exécuter et la réponse de MariaDB (la
   ligne qui contient `Error:`). Qu'est-ce qui vous a protégé ? Que se serait-il passé avec un
   compte qui a les droits ? Revenez à `VALIDATE`.

### 4 — Les portées

10. Écrivez `app.EssaiPortees`. Dans **une** transaction, appelez deux fois
    `em.find(Fournisseur.class, 1L)` et comparez les deux références avec `==`. Comptez les
    requêtes par `factory().getStatistics().getPrepareStatementCount()`, après un `clear()` des
    statistiques.
11. Faites la même lecture dans **deux** transactions successives. Comparez avec `==`, puis avec
    `equals`. Concluez en deux phrases : qu'est-ce que le contexte de persistance, et combien de
    temps vit-il ici ?

## Livrables

- Code poussé, un commit par étape.
- `NOTES.md` : réponses 2, 4 à 9, 10 et 11, avec la prédiction de la question 7 telle qu'écrite
  avant l'exécution.
- Terminé quand : `DemarrageJpa` démarre en `VALIDATE` sans erreur et affiche le fournisseur 4 ;
  `EssaiPortees` compte 1 requête pour deux `find` dans une transaction, 2 dans deux
  transactions ; `ArticleCourt` n'est plus dans le projet.

## Pour aller plus loin (optionnel)

- Retirez `Fournisseur.class` de `managedClasses` et relancez `DemarrageJpa`. À quel moment
  l'erreur survient-elle, et que dit-elle ? Pourquoi `validate` ne l'a-t-il pas signalée ?
