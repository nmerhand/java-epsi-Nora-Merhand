# Questions TP01
  
> Ouvrez le pom.xml. Combien de dépendances concernent JDBC ? Combien concernent l'API JDBC (java.sql) ?  
  
1 dépendance concerne JDBC 
3 dépendances concernent l'API JDBC

> Écrivez dans `NOTES.md` le nom de classe que le programme affichera, avant de lancer. Faites le changement, lancez, notez la classe obtenue et expliquez l'écart s'il y en a un.

Db 
=> Hikari

# Question TP02

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

