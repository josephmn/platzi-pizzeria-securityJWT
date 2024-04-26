package com.platzi.pizza.service;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.persistence.repository.PizzaPagSortRepository;
import com.platzi.pizza.persistence.repository.PizzaRepository;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import com.platzi.pizza.service.exception.EmailApiException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService {

    /*
    private final JdbcTemplate jdbcTemplate;
     */
    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }
    /*
    public PizzaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PizzaEntity> getAll() {
        return this.jdbcTemplate.query(
                "SELECT * FROM pizza",
                new BeanPropertyRowMapper<>(PizzaEntity.class)
        );
    }

    public List<PizzaEntity> getAllInactive() {
        return this.jdbcTemplate.query(
                "SELECT * FROM pizza WHERE available = 0",
                new BeanPropertyRowMapper<>(PizzaEntity.class)
        );
    }
     */

    public List<PizzaEntity> getAll() {
        return this.pizzaRepository.findAll();
    }

    public Page<PizzaEntity> getPall(int page, int elements) {
        Pageable pageRequest = PageRequest.of(page, elements);
        return this.pizzaPagSortRepository.findAll(pageRequest);
    }

    public List<PizzaEntity> getAvailable() {
        System.out.println(this.pizzaRepository.countByVeganTrue());
        return this.pizzaRepository.findAllByAvailableTrueOrderByPrice();
    }

    public Page<PizzaEntity> getAvailablePage(int page, int elements, String sortBy, String sortDirection) {
        /*
         * First example: with int page, int elements, String sortBy
         */
        /*
        Pageable pageRequest = PageRequest.of(page, elements, Sort.by(sortBy));
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest);
         */

        /*
         * Second example: with int page, int elements, String sortBy, String sortDirection
         */
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, elements, sort);
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest);
    }

    public List<PizzaEntity> getByName(String name) {
        return this.pizzaRepository.findAllByAvailableTrueAndNameIgnoreCase(name);
    }

    public PizzaEntity getByNameFirst(String name) {
        /*
         * Use optional in program functional
         */
        //return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElse(null);
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("Pizza not exists"));
    }

    public List<PizzaEntity> getWith(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getWithout(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getCheapest(double price) {
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    public PizzaEntity get(int idPizza) {
        return this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public PizzaEntity save(PizzaEntity pizza) {
        return this.pizzaRepository.save(pizza);
    }

    public void delete(int idPizza) {
        this.pizzaRepository.deleteById(idPizza);
    }

    @Transactional(noRollbackFor = EmailApiException.class)
    public void updatePrice(UpdatePizzaPriceDto dto) {
        this.pizzaRepository.updatePrice(dto);
        this.sendEmail();
    }

    /*
     * example using @Transactional for sending email
     */
    public void sendEmail() {
        throw new EmailApiException();
    }

    public boolean exists(int idPizza) {
        return this.pizzaRepository.existsById(idPizza);
    }
}
