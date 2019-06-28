# Projet_Laniakea_L3
Viusalisation 3D, des trajectoires d'amas de Galaxie au sein de l'univers :

I- Récupération des données :
  -Les données cosmologiques se trouvent dans des tables dont chaque groupement de bits represente une information particuliere correspondant à l'amas en question.
  -Pour récupérer ces données, l'objet Amas est donc créé, un tableau d'Amas est ensuite créé pour permettre la lecture des informations contenus dans les tables.

II- Analyse des données : 
- Par application de formules physique (Attraction gravitationnelle, Loi d'expansion de l'univers de Hubble,..), les positions, vitesses et accélérations de chaque Amas sont calculés pour chaque frames de la simulation et stockées dans un tableau Amas[][].

III- Visualisation :

-Le rendu est fait en utilisant Java3D. La bibliothèque n'est pas réellement ni performante ni pratique, mais a le mérite d'être en accès libre. La visualisation des objets se fait par l'ajout de modeles 3D a un graphe. Les transformations sont effectuees sur les objets en ajoutant des transformateurs (interpolateurs) plus en surface du graphe, tout les noeuds descendants sont donc concernes.

-Utilise le rendu contenu dans le tableau d'Amas[][] pour representer les objets ainsi que leur chemin au cours du temps.

Pour avoir accès à la gestion du projet, le fichier est Planning.ods
Pour lancer la simulation, utiliser Projet_Galaxies.jar.
