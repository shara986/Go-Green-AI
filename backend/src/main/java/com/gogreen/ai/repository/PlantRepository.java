package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.enums.PlantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlantRepository extends JpaRepository<Plant, UUID> {

    List<Plant> findByNurseryId(UUID nurseryId);

    List<Plant> findByCategoryId(UUID categoryId);

    List<Plant> findByPlantType(PlantType plantType);

    List<Plant> findByActiveTrue();

    long countByActiveTrue();

    Optional<Plant> findByNurseryIdAndSku(UUID nurseryId, String sku);

    boolean existsByNurseryIdAndSku(UUID nurseryId, String sku);

    @Query("select p from Plant p where " +
            "( :search is null or lower(p.name) like lower(concat('%', :search, '%')) " +
            "   or lower(p.scientificName) like lower(concat('%', :search, '%')) " +
            "   or lower(p.sku) like lower(concat('%', :search, '%')) ) " +
            "and ( :active is null or p.active = :active )")
    Page<Plant> searchPlants(@Param("search") String search,
                             @Param("active") Boolean active,
                             Pageable pageable);

    @Query("select c.name as categoryName, count(p) from Plant p join p.category c group by c.name order by count(p) desc")
    List<Object[]> countPlantsByCategory();
}
