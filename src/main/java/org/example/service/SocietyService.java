package org.example.service;

import org.example.repository.CourseRepository;
import org.example.repository.SocietyRepository;
import org.jboss.logging.Logger;

public class SocietyService {

    private final Logger logger = Logger.getLogger(CourseService.class);
    private final SocietyRepository societyRepository;

    public SocietyService(SocietyRepository societyRepository) {
        this.societyRepository = societyRepository;
    }
}
