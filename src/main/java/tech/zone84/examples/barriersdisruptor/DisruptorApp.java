package tech.zone84.examples.barriersdisruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class DisruptorApp {
    private static final int BUFFER_SIZE = 1024;
    private static final int EVENT_COUNT = 4096;
    private static final Logger log = LoggerFactory.getLogger(DisruptorApp.class);

    public static void main(String[] args) {
        Disruptor<Event> disruptor = new Disruptor<>(
            Event::new, BUFFER_SIZE, Executors.defaultThreadFactory() // 1
        );

        disruptor
            .handleEventsWith(new Consumer("journal"), new Consumer("replication")) // 2
            .then(new Consumer("logic"));

        disruptor.start();
        produceSomeEvents(disruptor);
        disruptor.shutdown();
    }

    private static void produceSomeEvents(Disruptor<Event> disruptor) {
        RingBuffer<Event> ringBuffer = disruptor.getRingBuffer();
        for (int i = 0; i < EVENT_COUNT; i++) {
            var current = i;
            ringBuffer.publishEvent((event, sequence) -> event.value = current); // 3
        }
    }

    public static class Event {
        private int value;
    }

    public static class Consumer implements EventHandler<Event> {
        private final String name;

        public Consumer(String name) {
            this.name = name;
        }

        @Override
        public void onEvent(Event event, long sequence, boolean endOfBatch) { // 4
            log.info(name+": " + event.value);
        }
    }
}
