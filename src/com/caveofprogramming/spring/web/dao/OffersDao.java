package com.caveofprogramming.spring.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component("offersDao")
public class OffersDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers() {
		Criteria crit = session().createCriteria(Offer.class);

		crit.createAlias("user", "u").add(Restrictions.eq("u.enabled", true));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers(String username) {

		Criteria crit = session().createCriteria(Offer.class);

		crit.createAlias("user", "u");
		crit.add(Restrictions.eq("u.enabled", true));
		crit.add(Restrictions.eq("u.username", username));

		return crit.list();
	}

	// Refactored method
	public void saveOrUpdate(Offer offer) {

		session().saveOrUpdate(offer);
	}

	public boolean delete(int id) {
		Query query = session().createQuery("delete from Offer where id=:id");
		query.setLong("id", id);
		return query.executeUpdate() == 1;
	}

	public Offer getOffer(int id) {

		Criteria crit = session().createCriteria(Offer.class);

		crit.createAlias("user", "u");

		crit.add(Restrictions.eq("u.enabled", true));
		crit.add(Restrictions.idEq(id));

		return (Offer) crit.uniqueResult();

	}

	// This method is not being used but the way to change to hibernate is:
	// search for hibernate batch
	// use the same session().save() method we used in create
	// use it in a loop
	// set a property that checks the jdbc batch update size
	// also need to flush the session after a few inserts.

	// @Transactional
	// public int[] create(List<Offer> offers) {
	//
	// SqlParameterSource[] params = SqlParameterSourceUtils
	// .createBatch(offers.toArray());
	//
	// return jdbc.batchUpdate(
	// "insert into offer (text, username) values (:text, :username)",
	// params);
	// }

	// REPLACED BOTH OF THESE METHODS TO REFACTOR AS "saveOrUpdate" BELOW
	// public void update(Offer offer) {
	// session().update(offer);
	// }
	//
	// public void create(Offer offer) {
	//
	// session().save(offer);
	// }

}
