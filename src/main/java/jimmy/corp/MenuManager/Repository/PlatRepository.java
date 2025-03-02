package jimmy.corp.MenuManager.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import jimmy.corp.MenuManager.Entity.Plat;

@RepositoryRestResource(collectionResourceRel = "plats", path = "plats")
public interface PlatRepository extends JpaRepository<Plat,Integer> {

    @Query("SELECT p FROM Plat p WHERE p.nom LIKE :mots")
    Page<Plat> rechercheParContenu(@Param("mots")String mc, org.springframework.data.domain.Pageable pageable);

}
