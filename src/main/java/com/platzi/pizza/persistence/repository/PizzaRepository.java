package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends ListCrudRepository<PizzaEntity, Integer> {
    /*
     * Query Method -> Pizza Available True and Order By Price
     */
    List<PizzaEntity> findAllByAvailableTrueOrderByPrice();

    /*
     * Query Method -> Pizza by Name
     */
    List<PizzaEntity> findAllByAvailableTrueAndNameIgnoreCase(String name);

    /*
     * Query Method -> Search Ingredient in Pizza
     */
    List<PizzaEntity> findAllByAvailableTrueAndDescriptionContainingIgnoreCase(String description);

    /*
     * Query Method -> Search Ingredient not in Pizza
     */
    List<PizzaEntity> findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(String description);

    /*
     * Query Method -> Count Pizza Vegan
     */
    int countByVeganTrue();

    /*
     * Query Method -> Pizza by Name with FirstBy using Optional
     */
    Optional<PizzaEntity> findFirstByAvailableTrueAndNameIgnoreCase(String name);

    /*
     * Query Method -> Pizza mas baratas de la pizzeria, solo top 3
     */
    List<PizzaEntity> findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(double price);

    /*
     * Update price Pizza with SQL Native, this work
     */
    /*@Query(value =
            "UPDATE pizza " +
                    "SET price = :newPrice " +
                    "WHERE id_pizza = :idPizza", nativeQuery = true)
     void updatePrice(@Param("idPizza") int idPizza, @Param("newPrice") double newPrice);
     *
     */

    /*
     * Update price Pizza with SQL Native, Using Spring Special Language
     */
    @Query(value =
            "UPDATE pizza " +
                    "SET price = :#{#newPizzaPrice.newPrice} " +
                    "WHERE id_pizza = :#{#newPizzaPrice.pizzaId}", nativeQuery = true)
    @Modifying
    void updatePrice(@Param("newPizzaPrice") UpdatePizzaPriceDto newPizzaPrice);

}
