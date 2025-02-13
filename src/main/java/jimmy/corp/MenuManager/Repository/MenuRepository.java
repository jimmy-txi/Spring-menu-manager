package jimmy.corp.MenuManager.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jimmy.corp.MenuManager.Entity.Menu;

public interface MenuRepository extends JpaRepository<Menu,Integer> {
    
}
