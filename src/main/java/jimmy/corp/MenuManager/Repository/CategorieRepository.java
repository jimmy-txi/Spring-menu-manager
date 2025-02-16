package jimmy.corp.MenuManager.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import jimmy.corp.MenuManager.Entity.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie,Integer> {
    
}
