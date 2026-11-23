# TP 06 — Mapping fin d'`article` et de `bon`

> Module *Java · Accès aux données* · Chapitre 2 · Durée estimée : **2h** (30h) · **1h45** (28h) · **1h15** (20h) · Niveau : intermédiaire
> Statut : **obligatoire** dans les trois volumes

## Objectifs d'apprentissage

- Choisir le type Java de chaque colonne : `BigDecimal`, `LocalDateTime`, `Integer` nullable,
  `enum`, `boolean`.
- Mapper une énumération par son nom (`STRING`), et montrer ce que coûterait sa position.
- Distinguer `@Transient` (JPA) du mot-clé `transient` (sérialisation).
- Lire les refus de `validate` quand le type Java ne correspond pas à la colonne.
- Écrire un `equals`/`hashCode` d'entité qui survit à un `persist` et à un proxy, et le prouver
  par des tests.

## Contexte

`article` et `bon` portent l'argent, les dates et le statut des commandes. Un mapping qui compile
peut quand même mentir : un prix en `double`, un délai inconnu lu comme 0, un statut stocké par sa
position. Ce TP fait constater chacun de ces écarts, puis pose l'identité des entités, dont
dépendront les collections du chapitre 3.

## Prérequis

- Votre projet à la fin du TP 05. S'il n'est pas terminé ou ne compile pas, partez de `../enonce_tp-06-mapping-article (1)/tp-06-mapping-article/projet-starter` (voir
  `POINT-DE-DEPART.md`).
- JUnit 6 et AssertJ sont dans le `pom.xml` (portée `test`). Les tests de ce TP lisent la base
  `compta` : depuis l'IDE, déclarez les variables dans la configuration d'exécution des tests.
- Maven ne voit pas les variables déclarées dans la configuration d'exécution de l'IDE. Depuis un
  terminal (bash, WSL, Git Bash), passez-les en préfixe de la commande :
  ```bash
  DB_URL='jdbc:mariadb://localhost:3306/compta?useServerPrepStmts=true&connectionTimeZone=SERVER' DB_USER=app DB_PASSWORD=app mvn test
  ```

## Instructions

Vous continuez `NOTES.md`. Un commit par étape.

### 1 — `Article`

1. Annotez `domain.Article` : `@Entity`, `@Table(name = "article")`, `@Id` et
   `@GeneratedValue(strategy = GenerationType.IDENTITY)`. Mappez :
   - `ref` avec `@Column(nullable = false, length = 13, unique = true, updatable = false)` ;
   - `prix` en `BigDecimal` avec `@Column(nullable = false, precision = 7, scale = 2)` ;
   - `designation` et `actif` (`boolean`).

   Ajoutez `managedClass(Article.class)` dans `Jpa`.
2. Ajoutez un champ `prixTtc` (prix × 1,20, arrondi à deux décimales), calculé et jamais
   persisté. Quelle annotation porte-t-il ? Pourquoi le mot-clé `transient` ne conviendrait-il pas ?
3. Passez temporairement le **champ** `prix` en `double`, sans toucher à son annotation. Les
   signatures publiques peuvent rester en `BigDecimal` : convertissez dans le getter
   (`BigDecimal.valueOf(prix)`), dans le setter (`.doubleValue()`) et dans `getPrixTtc`, qui doit
   passer par `getPrix()`. Lancez `EssaiMapping` et recopiez la cause de l'erreur. Retirez ensuite
   `precision` et `scale` de l'annotation, relancez, recopiez la ligne `Schema validation`.
   Revenez à `BigDecimal` et à l'annotation de la question 1.
4. Écrivez `app.EssaiMapping` : il lit l'article 1 et affiche son prix TTC, puis tente, dans une
   transaction, de `persist` un nouvel article. Recopiez le message que renvoie MariaDB. Pourquoi
   `validate` a-t-il accepté une entité qu'on ne peut pas insérer ?

### 2 — `Bon` et son statut

5. Créez l'énumération `domain.Statut` : `BROUILLON`, `VALIDE`, `LIVRE`, `ANNULE`.
6. Créez et mappez `domain.Bon` : `id` en `IDENTITY` ; `numero` (`int`, `unique`,
   `updatable = false`) ; `dateCommande` en `LocalDateTime` sur la colonne `date_cmde`
   (`updatable = false`) ; `delai` en `Integer` ; `statut` avec `@Enumerated(EnumType.STRING)` et
   `@Column(nullable = false, length = 20)`. Le fournisseur et les lignes du bon attendent le
   chapitre 3. Ajoutez `managedClass(Bon.class)`, et faites lister les bons par `EssaiMapping`, triés
   par numéro, en JPQL : `from Bon b order by b.numero`.
7. Passez `delai` en `int`, relancez, recopiez le message. Quel bon le provoque, et pourquoi ?
   Revenez à `Integer`.
8. Passez `statut` en `EnumType.ORDINAL`, relancez, recopiez la ligne `Schema validation`, revenez
   à `STRING`. Puis, sans toucher à la base :
   - affichez `Statut.LIVRE.ordinal()` ;
   - déplacez `ANNULE` juste avant `LIVRE` et réaffichez ;
   - relancez `EssaiMapping` : les statuts lus ont-ils changé ?
   - si la colonne stockait la position, que liraient les cinq bons livrés ?

   Remettez l'ordre d'origine.

### 3 — L'objet-valeur

9. En deux phrases : pourquoi `Adresse` est-elle un objet-valeur et pas une entité ? Citez une
   autre donnée du domaine `compta` qui mériterait un `@Embeddable`, et dites pourquoi.

### 4 — Identité

10. Écrivez `equals`/`hashCode` sur `Article.ref` et sur `Bon.numero`, avec `instanceof`, en lisant
    l'autre objet par son getter (`autre.getRef()`, pas `autre.ref`).
11. Écrivez `src/test/java/org/sebsy/compta/domain/IdentiteTest.java` :
    - une unité de persistance propre au test (`new PersistenceConfiguration("identite-test")`,
      mêmes variables d'environnement, `validate`) qui gère `Fournisseur`, `Article` et une entité
      de test, `FournisseurParId`, mappée sur la table `fournisseur` (`id`, `nom`, `ville`) avec un
      `equals`/`hashCode` sur `id`, comme un IDE les génère ;
    - chaque test travaille dans une transaction **toujours annulée** : la table reste intacte ;
    - test (a) : un `FournisseurParId` ajouté à un `HashSet` **avant** `persist`, cherché **après** :
      `size()` vaut 1 et `contains` vaut `false` ;
    - test (b) : la même chose avec un `Fournisseur`, dont l'identité porte sur `nom` : `contains`
      vaut `true`.
12. Test (c) : lisez l'article 1 par `find` dans un premier gestionnaire, puis obtenez
    `getReference(Article.class, 1L)` dans un second. `getReference` renvoie un proxy, une
    sous-classe générée d'`Article` (vérifiez-le sur `getClass()`). Affirmez que l'article chargé
    est égal à la référence. Remplacez ensuite `autre.getRef()` par `autre.ref` dans `equals`,
    relancez le test, recopiez le message d'échec dans `NOTES.md`, et rétablissez le getter.

<details>
<summary>Indice — pourquoi l'objet du test (a) devient introuvable</summary>

`HashSet` range un élément dans le seau que désigne son `hashCode` au moment de l'ajout. Après
`persist`, l'identifiant passe de `null` à une valeur, le `hashCode` change, et `contains` cherche
dans un autre seau. L'élément est toujours là, `size()` le prouve, mais il est inatteignable.
</details>

## Livrables

- Code poussé, un commit par étape.
- `NOTES.md` : réponses 2, 3, 4, 7, 8, 9 et le message d'échec de la question 12.
- Terminé quand : l'application démarre en `validate` avec `Fournisseur`, `Article` et `Bon` ;
  `EssaiMapping` affiche l'article 1 et les sept bons ; `mvn test` passe les trois tests de
  `IdentiteTest`.

## Pour aller plus loin (optionnel)

- Ajoutez temporairement un `setRef` à `Article`, puis écrivez un test qui ajoute un article à un
  `HashSet`, change sa référence et le cherche. Qu'est-ce que ce test dit du contrat d'une clé
  métier, et du rôle de `updatable = false` ?
