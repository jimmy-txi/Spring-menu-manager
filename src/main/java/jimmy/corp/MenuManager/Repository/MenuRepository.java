package jimmy.corp.MenuManager.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jimmy.corp.MenuManager.Entity.Menu;

public interface MenuRepository extends JpaRepository<Menu,Integer> {
    
    @Query("SELECT p FROM Menu p WHERE p.nom LIKE :mots")
    Page<Menu> rechercheParContenu(@Param("mots")String mc, org.springframework.data.domain.Pageable pageable);

}
