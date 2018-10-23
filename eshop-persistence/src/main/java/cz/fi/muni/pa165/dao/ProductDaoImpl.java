package cz.fi.muni.pa165.dao;

import java.util.List;


import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import cz.fi.muni.pa165.entity.Product;

@Repository
public class ProductDaoImpl implements ProductDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    public void create(Product product) {
        em.persist(product);
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    @Override
    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    @Override
    public void remove(Product p) throws IllegalArgumentException{
        em.remove(findById(p.getId()));
    }


    @Override
    public List <Product> findByName(String name){
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Cannot search for null name");

        try {
            return em
                    .createQuery("select p from Product p where name=:name",
                            Product.class).setParameter("name", name)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

}





