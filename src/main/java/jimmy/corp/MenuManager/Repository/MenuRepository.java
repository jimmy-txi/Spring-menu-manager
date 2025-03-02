package jimmy.corp.MenuManager.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import jimmy.corp.MenuManager.Entity.Menu;

@RepositoryRestResource
public interface MenuRepository extends JpaRepository<Menu,Integer> {
    
    @Query("SELECT p FROM Menu p WHERE p.nom LIKE :mots")
    Page<Menu> rechercheParContenu(@Param("mots")String mc, org.springframework.data.domain.Pageable pageable);

}
