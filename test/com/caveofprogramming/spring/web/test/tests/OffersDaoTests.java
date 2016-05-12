package com.caveofprogramming.spring.web.test.tests;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caveofprogramming.spring.web.dao.Offer;
import com.caveofprogramming.spring.web.dao.OffersDao;
import com.caveofprogramming.spring.web.dao.User;
import com.caveofprogramming.spring.web.dao.UsersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = {
		"classpath:com/caveofprogramming/spring/web/config/dao-context.xml",
		"classpath:com/caveofprogramming/spring/web/config/security-context.xml",
		"classpath:com/caveofprogramming/spring/web/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class OffersDaoTests {

	@Autowired
	private OffersDao offersDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private DataSource dataSource;

	private User user1 = new User("akbarhirani", "akbar hirani", "123456",
			"akbar@hirani.com", true, "ROLE_USER");
	private User user2 = new User("deanwinchester", "dean winchester",
			"123456", "akbar@hirani.com", true, "ROLE_ADMIN");
	private User user3 = new User("barryallen", "barry allen", "123456",
			"akbar@hirani.com", true, "ROLE_USER");
	private User user4 = new User("oliverqueen", "oliver queen", "123456",
			"akbar@hirani.com", false, "user");

	private Offer offer1 = new Offer(user1, "This is a test offer");
	private Offer offer2 = new Offer(user1, "This is a test offer2");
	private Offer offer3 = new Offer(user2, "This is a test offer3");
	private Offer offer4 = new Offer(user3, "This is a test offer4");
	private Offer offer5 = new Offer(user3, "This is a test offer5");
	private Offer offer6 = new Offer(user3, "This is a test offer6");
	private Offer offer7 = new Offer(user4, "This is a test offer7");

	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);

		jdbc.execute("delete from offer");
		jdbc.execute("delete from users");
	}

	@Test
	public void testCreate() {
		userDao.create(user1);
		userDao.create(user2);
		userDao.create(user3);
		userDao.create(user4);

		offersDao.saveOrUpdate(offer1);

		List<Offer> offers1 = offersDao.getOffers();

		assertEquals("Number of offers should be 1", 1, offers1.size());

		assertEquals("Retrieved offer should equal inserted offer", offer1,
				offers1.get(0));

		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		List<Offer> offers = offersDao.getOffers();

		assertEquals("Number of offers should be 6", 6, offers.size());

	}

	@Test
	public void testGetUsername() {
		userDao.create(user1);
		userDao.create(user2);
		userDao.create(user3);
		userDao.create(user4);

		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		List<Offer> offers = offersDao.getOffers(user3.getUsername());

		assertEquals("Should be 3 offers for this user.", 3, offers.size());

		List<Offer> offers2 = offersDao.getOffers("12345");

		assertEquals("Should be 3 offers for this user.", 0, offers2.size());

		List<Offer> offers3 = offersDao.getOffers(user2.getUsername());

		assertEquals("Should be 1 offers for this user.", 1, offers3.size());
	}

	@Test
	public void testUpdate() {
		userDao.create(user1);
		userDao.create(user2);
		userDao.create(user3);
		userDao.create(user4);

		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		offer3.setText("This is all new text.");
		offersDao.saveOrUpdate(offer3);

		Offer retreived = offersDao.getOffer(offer3.getId());

		assertEquals("Retreived offer should be updated.", offer3, retreived);
	}

	@Test
	public void testDelete() {
		userDao.create(user1);
		userDao.create(user2);
		userDao.create(user3);
		userDao.create(user4);

		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		Offer retreived1 = offersDao.getOffer(offer2.getId());
		assertNotNull("Offer with id: " + retreived1.getId()
				+ "should not be null.", retreived1);

		offersDao.delete(offer2.getId());

		Offer retreived = offersDao.getOffer(offer2.getId());
		assertNull("Offer with id: " + retreived1.getId() + "should be null.",
				retreived);
	}

	@Test
	public void testGetByID() {
		userDao.create(user1);
		userDao.create(user2);
		userDao.create(user3);
		userDao.create(user4);

		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		Offer retrieved1 = offersDao.getOffer(offer1.getId());
		assertEquals("offers should match", offer1, retrieved1);

		Offer retrieved2 = offersDao.getOffer(offer7.getId());
		assertNull("Should not retrieve offer for disabled user.", retrieved2);
	}
}
