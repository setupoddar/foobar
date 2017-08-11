package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.DataUtils;
import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.Trend;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class TrendDAO extends GenericDAO<Trend> {

    @Inject
    protected TrendDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Trend> getAll() throws DBException {
        try {
            Criteria criteria = criteria();
            criteria.add(Restrictions.gt("startDate", DataUtils.getDate(getPastDate(30))));
            criteria.add(Restrictions.lt("endDate", DataUtils.getDate(getFutureDate(30))));
            return list(criteria);
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public String getPastDate(int i) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);
        return dateFormat.format(cal.getTime());
    }

    public String getFutureDate(int i) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, i);
        return dateFormat.format(cal.getTime());
    }
}
