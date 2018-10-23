package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

 
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

	@PersistenceUnit
	private EntityManagerFactory emf;

	private Category electro;
	private Category kitchen;
	private Product flashlight;
	private Product robot;
	private Product plate;

	@BeforeClass
	private void init(){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		electro = new Category();
		electro.setName("Electro");

		kitchen = new Category();
		kitchen.setName("Kitchen");

		flashlight = new Product();
		flashlight.setName("Flashlight");
		plate.addCategory(electro);

		robot = new Product();
		robot.setName("Robot");
		plate.addCategory(electro);
		plate.addCategory(kitchen);

		plate = new Product();
		plate.setName("Plate");
		plate.addCategory(kitchen);

		em.persist(electro);
		em.persist(kitchen);
		em.persist(flashlight);
		em.persist(robot);
		em.persist(plate);

		em.getTransaction().commit();
		em.close();
	}

	@Test
	private void electroTest(){
		EntityManager em = emf.createEntityManager();
		Category c = em.find(Category.class, electro.getId());
		assertContainsProductWithName(c.getProducts(),"Robot");
		assertContainsProductWithName(c.getProducts(),"Flashlight");
		em.close();
	}

	@Test
	private void kitchenTest(){
		EntityManager em = emf.createEntityManager();
		Category c = em.find(Category.class, kitchen.getId());
		assertContainsProductWithName(c.getProducts(),"Robot");
		assertContainsProductWithName(c.getProducts(),"Plate");
		em.close();
	}

	@Test
	private void flashlightTest(){
		EntityManager em = emf.createEntityManager();
		Product p = em.find(Product.class, flashlight.getId());
		assertContainsCategoryWithName(p.getCategories(),"Electro");
		em.close();
	}

	@Test
	private void robotTest(){
		EntityManager em = emf.createEntityManager();
		Product p = em.find(Product.class, robot.getId());
		assertContainsCategoryWithName(p.getCategories(),"Electro");
		assertContainsCategoryWithName(p.getCategories(),"Kitchen");
		em.close();
	}


	@Test
	private void plateTest(){
		EntityManager em = emf.createEntityManager();
		Product p = em.find(Product.class, plate.getId());
		assertContainsCategoryWithName(p.getCategories(),"Kitchen");
		em.close();
	}


	private void assertContainsCategoryWithName(Set<Category> categories,
			String expectedCategoryName) {
		for(Category cat: categories){
			if (cat.getName().equals(expectedCategoryName))
				return;
		}
			
		Assert.fail("Couldn't find category "+ expectedCategoryName+ " in collection "+categories);
	}
	private void assertContainsProductWithName(Set<Product> products,
			String expectedProductName) {
		
		for(Product prod: products){
			if (prod.getName().equals(expectedProductName))
				return;
		}
			
		Assert.fail("Couldn't find product "+ expectedProductName+ " in collection "+products);
	}

	
}
