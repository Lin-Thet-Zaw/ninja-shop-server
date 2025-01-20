package com.ninjashop.ninjashop.repository;

import com.ninjashop.ninjashop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategpryRepository extends JpaRepository<Category, Long> {
    public Category findByName(String name);

    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.parent.name=:parent")
    public Category findByNameAndParent(String name, String parent);

}
