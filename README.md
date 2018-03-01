# One-dimensional bin packing

Ce solveur du problème de bin packing à une dimension est proposé en réponse au problème [XspeedIt|https://github.com/voyages-sncf-technologies/xspeedit].

Il implémente les heuristiques suvantes :
* Naive : Prend les items les uns après les autres dans l'ordre où ils se trouvent. Place un item donné dans le 
container courant si possible. Sinon, utilise un nouveau container et ne revient plus jamais aux containers déjà 
utilisés.
* First Fit : Prend les items les uns après les autres dans l'ordre où ils se trouvent. Place un item donné dans
le premier container qui peut l'accueillir parmi les containers déjà utilisés, s'il y en a un. Sinon, le place 
dans un nouveau container.
* First Fit Decreasing : Trie les items par taille décroissante, puis procède comme l'heuristique First Fit.

## Usage
```
sbt "run items capacity"
```

où :
* items est une chaîne de caractères représentant la liste des items à traiter. Chaque caractère de la chaîne
représente un item par une taille, comprise entre 1 et 9.
* capacity est la capacité d'un container

```
sbt test
```
lance les tests unitaires

## Exemple
sbt "run 163841689525773 10"

Articles : 163841689525773\
Capacité : 10\
Borne inférieure du nombre de cartons : 7\
Robot glouton naïf : 163/8/41/6/8/9/52/5/7/73 => 10 cartons utilisés\
Robot glouton (First Fit) : 163/81/46/82/9/55/73/7 => 8 cartons utilisés\
Robot glouton (First Fit Decreasing) : 91/82/81/73/73/64/6/55 => 8 cartons utilisés
