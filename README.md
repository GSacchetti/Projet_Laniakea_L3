# Projet_Laniakea_L3
Participants : Amaury Bodart, Guillaume Sacchetti, Guillaume Fourniols, Tahiriniaina Andrian Rakotoarisoa, Thomas Tsangshumyuen, Kevin Siharath

Viusalisation 3D, des trajectoires d'amas de Galaxie au sein de l'univers :

I- Récupération des données :
  -Les données cosmologiques se trouvent dans des tables dont chaque groupement de bits represente une information particuliere correspondant à l'amas en question.
  -Pour récupérer ces données, l'objet Amas est donc créé, un tableau d'Amas est ensuite créé pour permettre la lecture des informations contenus dans les tables.

II- Analyse des données : 
- Par application de formules physique (Attraction gravitationnelle, Loi d'expansion de l'univers de Hubble,..), les positions, vitesses et accélérations de chaque Amas sont calculés pour chaque frames de la simulation et stockées dans un tableau Amas[][].

III- Visualisation :

-Le rendu est fait en utilisant Java3D. La bibliothèque n'est pas réellement ni performante ni pratique, mais a le mérite d'être en accès libre. La visualisation des objets se fait par l'ajout de modeles 3D a un graphe. Les transformations sont effectuees sur les objets en ajoutant des transformateurs (interpolateurs) plus en surface du graphe, tout les noeuds descendants sont donc concernes.

-Utilise le rendu contenu dans le tableau d'Amas[][] pour representer les objets ainsi que leur chemin au cours du temps.

Le compte rendu : Rapport post-mortem _ Galaxies.pdf

Pour avoir accès à la gestion du projet, le fichier est Planning.ods.

La javadoc est disponible avec le fichier doc.rar.

L'UML : UML.jpg et UML2.png

Pour voir notre vidéo de présentation il faut télécharger la vidéo avec ce lien : https://mega.nz/#!PL5hhaRJ!PgFN88RUQCjs08Oo0uLEYYveR2qIEiUiVUG1WD4YLAM

Pour lancer la simulation, utiliser Projet_Galaxies.jar.
