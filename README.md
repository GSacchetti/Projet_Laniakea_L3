# Projet_Laniakea_L3
Viusalisation 3D, des trajectoires d'amas de Galaxie au sein de l'univers :

I- Récupération des données :
  -Les données cosmologiques se trouvent dans des tables dont chaque groupement de bits represente une information particuliere correspondant à l'amas en question.
  -Pour récupérer ces données, l'objet Amas est donc créé, un tableau d'Amas est ensuite créé pour permettre la lecture des informations contenus dans les tables.

II- Analyse des données : 
- Par application de formules physique (Attraction gravitationnelle, Loi d'expansion de l'univers de Hubble,..), les positions, vitesses et accélérations de chaque Amas sont calculés pour chaque frames de la simulation et stockées dans un tableau Amas[][].

III- Visualisation
