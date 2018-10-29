package com.gzsf.task;

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.CronType.UNIX;

public class CronTrigger implements Trigger {

    private final String cron;
    private final ExecutionTime executionTime;

    public CronTrigger(String cron) {
        this.cron = cron;
        CronDefinition cronDefinition =
                CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
        CronParser parser = new CronParser(cronDefinition);
        executionTime = ExecutionTime.forCron(parser.parse(this.cron));
    }

    @Override
    public long next() {
        ZonedDateTime now = ZonedDateTime.now();
        Optional<ZonedDateTime> timeToNextExecution = executionTime.nextExecution(now);
        return timeToNextExecution.map(zonedDateTime -> zonedDateTime.toInstant().toEpochMilli()).orElse(0L);
    }
}
