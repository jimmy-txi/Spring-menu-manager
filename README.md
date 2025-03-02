Menu Manager
============

Menu Manager est une application web de gestion de plats et de menus destinée aux professionnels de la restauration. 
Elle propose à la fois une interface web intuitive et une API REST pour faciliter l'intégration avec d'autres systèmes.

Table des Matières
------------------
- Fonctionnalités
- Technologies Utilisées
- Installation et Configuration
- Utilisation
- API REST
- Fonctionnalités à Venir
- Auteurs

Fonctionnalités
---------------
- Gestion des Plats et des Menus  
  Créer, modifier, supprimer et lister des plats et des menus.

- Interface Web Intuitive  
  Utilisation de Thymeleaf pour une interface dynamique et responsive.

- Filtrage, Tri et Pagination  
  Système de filtrage (par mot-clé, par catégorie, par plage de calories), tri par colonne et pagination pour faciliter la navigation dans les listes.

- Calcul des Informations Nutritionnelles  
  Affichage en temps réel des totaux nutritionnels (calories, lipides, protéines, glucides) pour chaque menu.

- API REST  
  Exposition automatique des entités via Spring Data REST pour permettre des intégrations externes.


Technologies Utilisées
----------------------
- Java 21 – Langage de programmation principal.
- Spring Boot 3.4.2 – Framework pour la création d’applications web et d’API REST.
- Spring Data JPA – Gestion de la persistance via JPA.
- Spring Data REST – Exposition automatique des repositories sous forme d’API REST.
- Thymeleaf – Moteur de templates pour la partie web.
- Spring Boot DevTools – Outils de développement pour le rechargement automatique.
- MariaDB JDBC Driver – Connecteur pour la base de données MariaDB.
- Lombok – Réduction du code boilerplate (getters, setters, etc.).
- HTML/CSS/JavaScript – Technologies front-end pour la création de l’interface utilisateur.


Installation et Configuration
-------------------------------
1. Cloner le Projet

   git clone https://github.com/jimmy-txi/Spring-menu-manager.git
   cd MenuManager

2. Configurer la Base de Données

   Modifiez le fichier src/main/resources/application.properties (ou application.yml) pour configurer votre connexion à MariaDB :

   spring.datasource.url=jdbc:mariadb://localhost:3306/votre_base_de_donnees
   spring.datasource.username=votre_utilisateur
   spring.datasource.password=votre_mot_de_passe
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

3. Compilation et Exécution

   Utilisez Maven pour compiler et démarrer l’application :

   mvn clean install
   mvn spring-boot:run

   L’application sera accessible par défaut sur http://localhost:8086


Utilisation
-----------
Interface Web
-------------
- Page d’Accueil  
  Accédez à http://localhost:8086 pour visualiser la page d’accueil.

- Gestion des Plats  
  • Liste des plats : http://localhost:8086/plats  
  • Page d’édition d’un plat : http://localhost:8086/platEdit?id=1&mc=&p=0&s=5

- Gestion des Menus  
  • Liste des menus : http://localhost:8086/menus  
  • Page d’édition d’un menu : http://localhost:8086/menuEdit?id=1&mc=&p=0&s=5

Filtrage, Tri et Pagination
----------------------------
La page des plats permet de filtrer par :
- Mot‑clé (mc)
- Catégorie (via un menu déroulant)
- Plage de calories (minCalories et maxCalories)

Les liens dans l’en-tête du tableau permettent également de trier par différentes colonnes (ex. tri par nom, calories, etc.) et la pagination facilite la navigation.


API REST
--------
L’application expose une API REST grâce à Spring Data REST. Pour activer l’API, vous devez annoter vos repositories avec @RepositoryRestResource.

Exemple pour l'entité Plat :

    @RepositoryRestResource(collectionResourceRel = "plats", path = "plats")
    public interface PlatRepository extends JpaRepository<Plat, Integer> { }

Exemple pour l'entité Menu :

    @RepositoryRestResource(collectionResourceRel = "menus", path = "menus")
    public interface MenuRepository extends JpaRepository<Menu, Integer> { }

Note :  
Pour préfixer tous les endpoints par /api, ajoutez la propriété suivante dans application.properties :

    spring.data.rest.base-path=/api

Ainsi, l’API REST sera accessible via :
- http://localhost:8086/api/plats
- http://localhost:8086/api/menus

Exemples d'URL à tester pour l'API REST :
- Liste des plats : GET http://localhost:8086/api/plats
- Détails d'un plat (ID 50) : GET http://localhost:8086/api/plats/50
- Création d'un plat : POST http://localhost:8086/api/plats  
  Exemple de payload JSON :
    {
      "nom": "Nouvelle Pizza",
      "nbCalories": 700,
      "nbLipides": 18,
      "nbProteines": 22,
      "nbGlucides": 90,
      "categorie": { "id": 1 }
    }
- Liste des menus : GET http://localhost:8086/api/menus
- Détails d'un menu (ID 50) : GET http://localhost:8086/api/menus/50


Fonctionnalités à Venir
-----------------------
- Authentification et Sécurité  
  Mise en place d’un système d’authentification et d’autorisation pour sécuriser l’accès aux fonctionnalités.

- Interface Utilisateur Améliorée  
  Amélioration de l’UI/UX avec des animations, un design responsive et une meilleure ergonomie.

- Exportation des Données  
  Possibilité d’exporter les listes de plats et menus en formats CSV ou Excel.

- Intégration avec des API Nutritionnelles  
  Enrichissement automatique des informations nutritionnelles en connectant l’application à des services tiers.

- Tests Automatisés  
  Mise en place de tests unitaires et d’intégration pour assurer la robustesse de l’application.

Auteurs
-------
Développé par Jimmy Txi

Pour toute question ou suggestion, n'hésitez pas à me contacter.
