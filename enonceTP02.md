# TP 02 — CRUD paramétré sur `fournisseur`

> Module *Java · Accès aux données* · Chapitre 1 · Durée estimée : **1h** (30h) · **55 min** (28h) · **50 min** (20h) · Niveau : débutant
> Statut : **obligatoire** dans les trois volumes

## Objectifs d'apprentissage

- Lire et écrire en `PreparedStatement`, sans **aucune** concaténation.
- Récupérer une clé générée par la base (`Statement.RETURN_GENERATED_KEYS`).
- Traduire le nombre de lignes renvoyé par `executeUpdate()` en résultat pour l'appelant.
- Lire une colonne NULLable sans transformer « inconnu » en `0`.
- Constater une injection SQL, puis la voir disparaître avec un paramètre lié.

## Contexte

La quincaillerie veut un écran d'administration de ses fournisseurs : les lister, les chercher,
en créer, en renommer, en supprimer. Vous écrivez la classe JDBC qui le servira. Le modèle objet
est fourni : le travail de ce TP porte sur JDBC.

## Prérequis

- Votre projet à la fin du TP 01 : `Db` opérationnel, conteneur MariaDB démarré.
- Le dossier `starter/` de ce TP, fourni dans son archive : `domain/Adresse.java` et
  `domain/Fournisseur.java`, à copier sous `src/main/java/org/sebsy/compta/domain/`.

## Instructions

Vous continuez le fichier `NOTES.md` du TP 01.

### 1 — Le modèle fourni

1. Lisez `Adresse` (un `record` : une valeur, trois colonnes de `fournisseur`) et `Fournisseur`
   (une classe, deux constructeurs). Quel constructeur sert à un fournisseur **pas encore
   enregistré**, et pourquoi son identifiant est-il un `Long` et non un `long` ?

### 2 — Les lectures

2. Créez `org.sebsy.compta.dao.FournisseurDaoJdbc`. Chaque méthode emprunte sa connexion
   à `Db.dataSource()` dans un `try-with-resources` et déclare `throws SQLException` (le TP 03
   retirera cette déclaration).
3. Écrivez `toutes()`, `parId(long)`, `parNom(String)` et `parVille(String)`. Les recherches
   unitaires renvoient un `Optional<Fournisseur>`, les autres une `List<Fournisseur>`. Colonnes :
   `id`, `nom`, `rue`, `code_postal`, `ville`, lues **par leur nom**.

<details>
<summary>Indice — une ligne vers un objet</summary>

Une méthode privée `versFournisseur(ResultSet rs)` lit la ligne courante et construit
`new Fournisseur(rs.getLong("id"), rs.getString("nom"), new Adresse(…))`. Les quatre lectures
l'appellent, dans un `while (rs.next())` ou un `if (rs.next())`.
</details>

### 3 — Les écritures

4. `creer(Fournisseur f)` insère le fournisseur et renvoie un **nouveau** `Fournisseur` portant
   l'identifiant généré par la base.
5. **Prédiction, puis erreur volontaire.** Écrivez dans `NOTES.md` ce que vous pensez obtenir de
   `ps.getGeneratedKeys()` si l'on oublie `Statement.RETURN_GENERATED_KEYS`. Retirez l'argument,
   puis appelez `creer` depuis un `main` jetable, `app.Brouillon` : `EssaiFournisseurs` n'arrive
   qu'à la question 10. Lancez, recopiez ce qui se passe, remettez l'argument.
6. `renommer(long id, String nom)` et `supprimer(long id)` renvoient un `boolean` : `true` si une
   ligne a été touchée, `false` sinon.

<details>
<summary>Indice — la clé générée</summary>

`cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)`, puis, après `executeUpdate()`,
`try (ResultSet cles = ps.getGeneratedKeys()) { cles.next(); cles.getLong(1); }`.
</details>

### 4 — Le piège du NULL

7. Créez `dao.jdbc.BonDaoJdbc` avec `Optional<Integer> delai(long idBon)`, qui lit `bon.delai`.
8. **Prédiction.** Pour le bon 7 (`delai IS NULL`), qu'affichent `rs.getInt("delai")` puis
   `rs.getObject("delai", Integer.class)` ? Écrivez votre réponse, exécutez les deux lectures
   depuis `app.Brouillon`, notez les deux résultats. Gardez la seconde dans `delai`.
9. En deux phrases dans `NOTES.md` : pourquoi la première lecture est-elle un bug de livraison,
   et pas un détail d'arrondi ?

### 5 — Le programme d'essai

10. Écrivez `org.sebsy.compta.app.EssaiFournisseurs`. Il affiche le résultat de : `toutes()`
    (taille), `parId(2)`, `parId(999)`, `parVille("Nantes")`, la création d'un fournisseur
    « Peinture Express » à Nantes, son renommage, `renommer(999, "X")`, sa suppression, puis
    `delai(1)` et `delai(7)`. Il laisse la base comme il l'a trouvée. Supprimez ensuite
    `app.Brouillon`.
11. Recopiez dans `NOTES.md` l'identifiant que `creer` a renvoyé à votre **premier** lancement.

### 6 — L'injection

12. Ajoutez temporairement une variante de `parNom` qui construit sa requête par concaténation
    (`Statement`, `"… WHERE nom = '" + nom + "'"`) et qui renvoie une `List<Fournisseur>`.
    Appelez-la avec `' OR '1'='1` : combien de fournisseurs revient-il ? Appelez `parNom` avec
    la même chaîne : combien ?
13. Supprimez la variante concaténée : elle ne doit pas figurer dans le livrable.

## Livrables

- Code poussé, un commit par étape.
- `NOTES.md` complété : réponses 1 et 9, prédictions et observations 5 et 8, l'identifiant de
  la question 11, les deux comptes de la question 12.
- Critère de « terminé » : `EssaiFournisseurs` affiche 4 fournisseurs, `Optional.empty` pour
  `parId(999)`, un fournisseur pour Nantes, un identifiant non nul à la création, `false` pour
  `renommer(999, …)`, `Optional[3]` pour le bon 1 et `Optional.empty` pour le bon 7.

## Pour aller plus loin (optionnel)

- Tentez `supprimer(1)`. Recopiez l'exception : quelle contrainte l'empêche, et pourquoi est-ce
  à la base, et non à Java, de la faire respecter ?
