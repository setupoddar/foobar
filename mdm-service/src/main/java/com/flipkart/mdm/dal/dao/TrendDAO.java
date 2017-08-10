package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.Trend;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

@Slf4j
public class TrendDAO extends GenericDAO<Trend> {

    @Inject
    protected TrendDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
