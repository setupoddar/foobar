package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.QCFSN;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import java.util.List;

@Slf4j
public class QCFSNDAO extends GenericDAO<QCFSN> {

    @Inject
    protected QCFSNDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<QCFSN> getAll() {
        Criteria criteria = criteria();
        return list(criteria);
    }
}
