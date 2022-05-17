# Release Note

Pour toutes informations sur l'usage du package, rendez-vous [sur le site de référence](http://webresources-dev.forem.be/webcompo/index.html#exemples/popup). 

| Version | Date | Auteur |  
|:-----------|:-----------|:-----------|:-----------|
| [1.1.2](#Version-1.1.2) | 22/12/2021 | Thomas Kingunia |
| [1.1.1](#Version-1.1.1) | 09/12/2021 | Feron Melvyn |
| [1.1.0](#Version-1.1.0) | 30/08/2021 | Feron Melvyn |
| [1.0.2](#Version-1.0.2) | 18/06/2021 | Feron Melvyn |
| [1.0.1](#Version-1.0.1) | 15/04/2021 | Feron Melvyn |
| [1.0.0](#Version-1.0.0) | 25/02/2021 | Thomas Kingunia |
| [0.3.0](#Version-0.3.0) | 21/10/2020 | Thomas Kingunia |
| [0.2.2](#Version-0.2.2) | 01/10/2020 | Feron Melyvn |
| [0.2.1](#Version-0.2.1) | 23/09/2020 | Feron Melyvn |
| [0.2.0](#Version-0.2.0) | 11/09/2020 | D'Hasseler Jérémy |
| [0.1.8](#Version-0.1.8) | 03/09/2020 | Barzin Felix |
| [0.1.6](#Version-0.1.6) | 23/06/2020 | Feron Melvyn |
| [0.1.5](#Version-0.1.5) | 12/06/2020 | Messina Dorian |
| [0.1.4](#Version-0.1.4) | 19/05/2020 | Lambinet Yoann |

## Version 1.1.2
1. Changes

2. Breaking changes

3. Correction de bug
    - Empeche le bouton de fermeture de prendre le style primary et garde le style link en tout temps.

## Version 1.1.1
1. Changes

2. Breaking changes

3. Correction de bug
    - Gère le disconnectedCallback de la popup, enlève les listener et enlève la class noscroll sur le body

## Version 1.1.0
1. Changes
    - Ajout de la possibiliter d'annuler la fermeture d'une popup sur confirmation.

2. Breaking changes

3. Correction de bug

## Version 1.0.2
1. Changes

2. Breaking changes

3. Correction de bug
    - Empêche la fermeture d'une popup fermée et l'ouverture d'une popup déjà ouverte

## Version 1.0.1
1. Changes

2. Breaking changes

3. Correction de bug
    - Ajout d'une description à l'icone de fermeture

## Version 1.0.0
1. Changes
    - Optimisation d'accessibilité 
        - Le focus reste dans la popup
        - L'ajout automatique de l'attribut aria-described permet le lancement de la lecture du contenu par les lecteurs d'écrans
        - La croix de fermeture n'est pas accessible via le clavier (uniquement souris) étant donné qu'un bouton de fermeture doit toujours être présent
    - Revue de la gestion de la création d'unique ID
    - Gestion de la class no-scroll sur le body à l'ouverture et fermeture de la popup
    - L'affichage du bouton cancel change si c'est le seul bouton affiché

2. Breaking changes
    - L'attribut title est remplacé par l'attribut label

3. Correction de bug

## Version 0.3.0
1. Changes
    - Ajouter un scroll sur le body
    - Ajout de la possibilité d'ajouter des boutons custom au footer du popup

2. Breaking changes

3. Correction de bug

## Version 0.2.2
1. Changes

2. Breaking changes

3. Correction de bug
    - Suppression du message d'erreur à l'ouverture de la popup depuis un élément hidden.

## Version 0.2.1
1. Changes

2. Breaking changes

3. Correction de bug
    - Correction de la vérification de l'attribut required et displaybuttons

## Version 0.2.0
1. Changes
    - La validation permettant d'activer ou désactiver le bouton de confirmation 
      passe maintenant par l'interrogation de la propriété isValid du composant enfant. 

2. Breaking changes

3. Correction de bug

## Version 0.1.8
1. Changes

2. Breaking changes

3. Correction de bug
    -   Ajout de l'attribut 'type' sur l'element HTML <button> pour empêcher le comportement submit.

## Version 0.1.6
1. Changes

2. Breaking changes

3. Correction de bug
    -   Suppression de la possibilité d'accéder à la popup via la tabulation lorsque celle-ci est masquée.
    -   Correction d'un problème qui permettait, via la tabulation, d'accéder au premier élément externe suivant la popup avant d'être ramené à l'intérieur

## Version 0.1.5
1. Changes
    -  Ajout de la gestion des checkbox dans la vérification des champs requis.
2. Breaking changes

3. Correction de bug

## Version 0.1.4
1. Changes
    -  Ajout de l'attribut title sur les boutons
2. Breaking changes

3. Correction de bug
    -  Correction des libellé des attributs dans le switch de l'attributChangeCallback.

