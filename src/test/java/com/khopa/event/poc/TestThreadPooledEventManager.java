package com.khopa.event.poc;


import com.khopa.event.EventManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * These isn't unit nor integration tests, but merely a "test" use case as proof of concept
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:pocContextThreadPooled.xml")
public class TestThreadPooledEventManager {

    @Autowired
    private EventManager eventManager;

    @Autowired
    private ComponentA componentA;

    @Test
    public void testEvent(){
        eventManager.fire("EventA");
        eventManager.fire("EventA");
        eventManager.fire("EventA");
        eventManager.fire("EventB");
        eventManager.fire("EventC");
        eventManager.fire("evt_125634");
        eventManager.fire("evt_125634", "TEST");
        SomeModel someModel = new SomeModel();
        someModel.setA(15);
        someModel.setB(45);
        someModel.setName("SomeName");
        eventManager.fire("evt_274896", someModel);

        Runnable a = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++){
                    eventManager.fire("EventB");
                }
            }
        };

        Runnable b = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++){
                    eventManager.fire("EventC");
                }
            }
        };

        Thread at = new Thread(a);
        Thread bt = new Thread(b);
        at.start();
        bt.start();

        while(at.isAlive() && bt.isAlive()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
