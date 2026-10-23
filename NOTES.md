# Questions TP01
  
> Ouvrez le pom.xml. Combien de dépendances concernent JDBC ? Combien concernent l'API JDBC (java.sql) ?  
  
1 dépendance concerne JDBC 
3 dépendances concernent l'API JDBC

> Écrivez dans `NOTES.md` le nom de classe que le programme affichera, avant de lancer. Faites le changement, lancez, notez la classe obtenue et expliquez l'écart s'il y en a un.

Db 
=> Hikari

# Questions TP02

> Quel constructeur sert à un fournisseur **pas encore enregistré**, et pourquoi son identifiant est-il un `Long` et non un `long` ?

Le deuxième constructeur sert à un fournisseur pas encore enregistré :
```java
public Fournisseur(Long id, String nom, Adresse adresse) {  
    this(nom, adresse);  
    this.id = id;  
}
```

Son identifiant est un Long car ...

> Écrivez dans `NOTES.md` ce que vous pensez obtenir de `ps.getGeneratedKeys()` si l'on oublie `Statement.RETURN_GENERATED_KEYS`

rs.next serait faux
=> ![[Pasted image 20261022153535.png]]
Le fournisseur a bien été créé dans la bdd mais rs.next était bien faux donc la méthode n'a pas été plus loin

> **Prédiction.** Pour le bon 7 (`delai IS NULL`), qu'affichent `rs.getInt("delai")` puis `rs.getObject("delai", Integer.class)` ? Écrivez votre réponse, exécutez les deux lectures depuis `app.Brouillon`, notez les deux résultats. Gardez la seconde dans `delai`

`rs.getInt("delai")` => Optional[0]
![[Pasted image 20261022164418.png|416]]

`rs.getObject("delai", Integer.class)` => null 
![[Pasted image 20261022164646.png|463]]

> En deux phrases dans `NOTES.md` : pourquoi la première lecture est-elle un bug de livraison, et pas un détail d'arrondi ?

En renvoyant Optional[0] on pourrait croire que le contenu du champ est 0 alors que c'est Null. Mais vu que le Null d'un int est 0, il peut y avoir méprise.

> Recopiez dans `NOTES.md` l'identifiant que `creer` a renvoyé à votre **premier** lancement.

=> 5

# Questions TP03

> Quel type déclare `createEntityManagerFactory()` ? Quelle dépendance apporte le paquet `jakarta.persistence` ? Lancez `mvn dependency:tree -Dincludes=jakarta.persistence` et recopiez les lignes utiles.

- `createEntityManagerFactory()` déclare un type `EntityManager`. 
- `import org.hibernate.jpa.HibernatePersistenceConfiguration;`. 

```shell
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< org.sebsy:demo-data-access >---------------------
[INFO] Building demo-data-access 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- dependency:3.7.0:tree (default-cli) @ demo-data-access ---

[INFO] org.sebsy:demo-data-access:jar:1.0.0-SNAPSHOT
[INFO] \- org.hibernate.orm:hibernate-core:jar:7.4.11.Final:compile
[INFO]    \- jakarta.persistence:jakarta.persistence-api:jar:3.2.0:compile
```

> Recopiez dans `NOTES.md` le fragment qui commence par `Schema validation`, mot pour mot

Il n'y a pas de fragment qui commence par `Schema validation` 

> En une phrase : pourquoi la correction se fait-elle chez le propriétaire ?

Car Adresse n'est pas obligatoirement utilisable dans Fournisseur et la nomination "code_postal" est propre à la table fournisseur, pour cela, la correction doit se faire chez le propriétaire.

> Notez la requête affichée par logback, et ce que renvoie `estComplete()` pour le fournisseur 4.

```SQL
select f1_0.id,f1_0.rue,f1_0.code_postal,f1_0.ville,f1_0.nom from fournisseur f1_0 where f1_0.id=?
```

fournisseur4.getAdresse().estComplete() => false

> **Prédiction datée.** Avant toute exécution, écrivez dans `NOTES.md`, avec la date et l'heure, ce que fera `validate` face à cette entité de test, mappée sur la table `article`, qu'aucune entité de l'unité ne porte encore :
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

23.10.2026 - 11:15

Il va pas accepter parce qu'on n'a pas toutes les colonnes.
Et le type de id est pas bon.

Constat : ça fonctionne 

> Passez ensuite `id` en `String`, relancez et recopiez le fragment `Schema validation`

Pas de fragment `Schema validation` :

```zsh
Unable to build Hibernate SessionFactory  [persistence unit: compta]
```

> Qu'est-ce que ces deux résultats disent de ce que `validate` compare ?

`validate` compare les types.

> Quel risque reste-t-il avec un `Integer` sur cette colonne ? 

jsp

> Renommez `@Table(name = "fournisseurs")`. Relancez, recopiez le fragment `Schema validation`, remettez le bon nom.

```zsh
Unable to build Hibernate SessionFactory  [persistence unit: compta]
```

> L'application démarre-t-elle ? 

Oui

> Recopiez la première commande SQL qu'Hibernate tente d'exécuter et la réponse de MariaDB (la ligne qui contient `Error:`). 

```zsh
WARN  o.h.t.s.i.ExceptionHandlerLoggedImpl - GenerationTarget encountered exception accepting command : Error executing DDL "alter table if exists fournisseur modify column ville varchar(255)" via JDBC [(conn=1672) 
```

```zsh
WARN  o.m.j.m.server.ErrorPacket - Error: 1142-42000: ALTER command denied to user 'app'@'172.18.0.1' for table `compta`.`fournisseur`
```

> Qu'est-ce qui vous a protégé ? 

Je n'ai pas les droit pour modifier la table.

> Que se serait-il passé avec un compte qui a les droits ?

La colonne ville aurait été modifiée

> Comptez les requêtes par `factory().getStatistics().getPrepareStatementCount()`, après un `clear()` des statistiques.

![[Pasted image 20261023114221.png]]

>Concluez en deux phrases : qu'est-ce que le contexte de persistance, et combien de temps vit-il ici ?

Il vit jusqu'à un close().
