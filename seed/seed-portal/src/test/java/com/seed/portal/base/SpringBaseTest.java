package com.seed.portal.base;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract test, used to do something before and after do real business.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 20:19
 */
@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class SpringBaseTest {

    @Before
    public void before() {
        // do something
    }

    @After
    public void after() {
        // do something
    }
}
