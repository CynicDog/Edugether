package org.example;

import io.vertx.junit5.VertxExtension;
import org.example.repository.CourseRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.UserRepository;
import org.example.repository.implementation.CourseRepositoryImpl;
import org.example.repository.implementation.ReviewRepositoryImpl;
import org.example.repository.implementation.UserRepositoryImpl;
import org.example.service.CourseService;
import org.example.util.enums.FETCHING_OPTION;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openjdk.jmh.annotations.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.concurrent.TimeUnit;

@Threads(1)
@State(Scope.Benchmark)
@ExtendWith(VertxExtension.class)
public class BenchmarkTest {
    private CourseService courseService;

    @Setup(Level.Iteration)
    public void setupIteration() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("edugether");
        CourseRepository courseRepository = new CourseRepositoryImpl(emf);
        UserRepository userRepository = new UserRepositoryImpl(emf);
        ReviewRepository reviewRepository = new ReviewRepositoryImpl(emf);

        courseService = new CourseService(courseRepository, userRepository, reviewRepository);
    }

    @Benchmark @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 5)
    @Measurement(iterations = 4, time = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void AVGT_getPaginatedCoursesByOption() throws Exception {

        courseService.getPaginatedCoursesByOption(
                FETCHING_OPTION.NEWEST.toString().toLowerCase(),0, 100);
    }

    @Benchmark @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 5)
    @Measurement(iterations = 4, time = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void AVGT_getPaginatedCoursesByOptionNative() throws Exception {

        courseService.getPaginatedCoursesByPublishedDateDescNative(0, 100);
    }

    @Test
    public void benchmark() throws Exception {
        String[] argv = {};
        org.openjdk.jmh.Main.main(argv);
    }
}
