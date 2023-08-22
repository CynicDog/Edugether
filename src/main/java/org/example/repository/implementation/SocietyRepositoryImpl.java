package org.example.repository.implementation;

import org.example.entity.socials.Society;
import org.example.repository.SocietyRepository;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;

public class SocietyRepositoryImpl implements SocietyRepository {


    private final Logger logger = Logger.getLogger(CourseRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public SocietyRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void insertSociety(Society society) {

    }
}
