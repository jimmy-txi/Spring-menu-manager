package jimmy.corp.MenuManager.Repository;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jimmy.corp.MenuManager.Entity.Plat;

public interface PlatRepository extends JpaRepository<Plat,Integer> {

    @Query("SELECT p FROM Plat p WHERE p.nom LIKE :mots")
    Page<Plat> rechercheParContenu(@Param("mots")String mc, org.springframework.data.domain.Pageable pageable);
}
