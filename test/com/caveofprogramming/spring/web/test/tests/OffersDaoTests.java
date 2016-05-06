package com.caveofprogramming.spring.web.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);

		jdbc.execute("delete from offer");
		jdbc.execute("delete from users");
	}

	@Test
	public void testOffer() {
		User user = new User("akbar", "Akbar Hirani", "123456",
				"akbar@123.com", true, "user");

		assertTrue("User creation should return true", userDao.create(user));

		Offer offer = new Offer(user, "user");

		assertTrue("Offer creation should return true", offersDao.create(offer));

		List<Offer> offers = offersDao.getOffers();

		assertEquals("Number of users should be 1", 1, offers.size());

		assertEquals("Created offer should be identical to retreived offer",
				offer, offers.get(0));

		offer = offers.get(0);
		offer.setText("New text.");
		
		assertTrue("Offer update should be true.", offersDao.update(offer));

		Offer updated = offersDao.getOffer(offer.getId());

		assertEquals("Updated offer should match retreived updated offer.",
				offer, updated);

		//Test get by ID
		Offer offer2 = new Offer(user, "This is a test offer.");

		assertTrue("Offer 2 creation should return true.", offersDao.create(offer2));
		
		List<Offer> useroffers = offersDao.getOffers(user.getUsername());
		assertEquals("Number of offer should be 2.", 2, useroffers.size());
		
		List<Offer> list2 = offersDao.getOffers();

		for(Offer current : list2){
			Offer retreived = offersDao.getOffer(current.getId());
			
			assertEquals("Offer by ID should match offer from list", current, retreived);
		}
		
		//Test deletion
		offersDao.delete(offer.getId());
		
		List<Offer> empty = offersDao.getOffers();

		assertEquals("Number of offers should be 1.", 1, empty.size());
	}

}
