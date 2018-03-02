# One-dimensional bin packing

Ce solveur du problème de bin packing à une dimension est proposé en réponse au problème [XspeedIt|https://github.com/voyages-sncf-technologies/xspeedit].

Il implémente les heuristiques suivantes :
* Naive : Prend les items les uns après les autres dans l'ordre où ils se trouvent. Place un item donné dans le 
container courant si possible. Sinon, utilise un nouveau container et ne revient plus jamais aux containers déjà 
utilisés.
* First Fit : Prend les items les uns après les autres dans l'ordre où ils se trouvent. Place un item donné dans
le premier container qui peut l'accueillir parmi les containers déjà utilisés, s'il y en a un. Sinon, le place 
dans un nouveau container.
* First Fit Decreasing : Trie les items par taille décroissante, puis procède comme l'heuristique First Fit.
* Best Fit : Prend les items les uns après les autres dans l'ordre où ils se trouvent. Place un item donné dans 
le container qui sera le plus rempli aprsè que l'item y sera placé.
* Best Fit Decreasing : Trie les items par taille décroissante, puis procède comme l'heuristique Best Fit.
* Worst Fit : Prend les items les uns après les autres dans l'ordre où ils se trouvent. Place un item donné dans 
le container qui sera le moins rempli aprsè que l'item y sera placé.
* Wost Fit Decreasing : Trie les items par taille décroissante, puis procède comme l'heuristique Best Fit.

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

## Exemples
```
sbt "run 163841689525773 10"
```

Articles : 163841689525773\
Capacité : 10\
Borne inférieure du nombre de cartons : 8\
Robot glouton naïf : 163/8/41/6/8/9/52/5/7/73 => 10 cartons utilisés\
Robot glouton (First Fit) : 163/81/46/82/9/55/73/7 => 8 cartons utilisés\
Robot glouton (First Fit Decreasing) : 91/82/81/73/73/64/6/55 => 8 cartons utilisés\
Robot glouton (Best Fit) : 163/81/46/82/9/55/73/7 => 8 cartons utilisés\
Robot glouton (Best Fit Decreasing) : 91/82/81/73/73/64/6/55 => 8 cartons utilisés\
Robot glouton (Worst Fit) : 163/8/415/62/8/9/53/7/7 => 9 cartons utilisés\
Robot glouton (Worst Fit Decreasing) : 9/81/81/73/72/64/63/55 => 8 cartons utilisés


```
sbt "run 1638416895257732673497234442 10"
```
Articles : 1638416895257732673497234442\
Capacité : 10\
Borne inférieure du nombre de cartons : 14\
Robot glouton naïf : 163/8/41/6/8/9/52/5/7/73/26/73/4/9/72/34/442 => 17 cartons utilisés\
Robot glouton (First Fit) : 163/81/46/82/9/55/73/72/63/72/432/9/7/44/4 => 15 cartons utilisés\
Robot glouton (First Fit Decreasing) : 91/91/82/82/73/73/73/73/64/64/64/55/442/2 => 14 cartons utilisés\
Robot glouton (Best Fit) : 163/81/46/82/9/55/73/72/64/73/9/72/34/442 => 14 cartons utilisés\
Robot glouton (Best Fit Decreasing) : 91/91/82/82/73/73/73/73/64/64/64/55/442/2 => 14 cartons utilisés\
Robot glouton (Worst Fit) : 163/8/415/62/8/9/53/72/7/63/7/423/9/7/44/42 => 16 cartons utilisés\
Robot glouton (Worst Fit Decreasing) : 9/9/82/82/73/73/73/73/64/64/64/55/442/211 => 14 cartons utilisés

