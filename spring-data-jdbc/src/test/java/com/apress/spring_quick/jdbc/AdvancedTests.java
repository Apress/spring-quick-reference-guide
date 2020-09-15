/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apress.spring_quick.jdbc;

import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Period;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates various possibilities to customize the behavior of a repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvancedConfiguration.class)
@AutoConfigureJdbc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdvancedTests {

    @Autowired
    ChannelRepository repository;

    @Test
    public void exerciseSomewhatComplexEntity() {

        Channel smallCar = createChannel("Small Car 01", 5, 12);
        smallCar.setMessage(new Message("Just put all the pieces together in the right order", "Jens Schauder"));
        smallCar.addTopic("suv", "SUV with sliding doors.");
        smallCar.addTopic("roadster", "Slick red roadster.");

        repository.save(smallCar);
        Iterable<Channel> channels = repository.findAll();
        Output.list(channels, "Original Channel");
        checkChannels(channels, "Just put all the pieces together in the right order", 2);

        smallCar.getMessage().setText("Just make it so it looks like a car.");
        smallCar.addTopic("pickup", "A pickup truck with some tools in the back.");

        repository.save(smallCar);
        channels = repository.findAll();
        Output.list(channels, "Updated");
        checkChannels(channels, "Just make it so it looks like a car.", 3);

        smallCar.setMessage(new Message("One last attempt: Just build a car! Ok?", "Jens Schauder"));

        repository.save(smallCar);
        channels = repository.findAll();
        Output.list(channels, "Manual replaced");
        checkChannels(channels, "One last attempt: Just build a car! Ok?", 3);

    }

    @Test
    public void customQueries() {

        Channel smallCars = createChannel("Small Car - 01", 5, 10);
        smallCars.setMessage(new Message("Just put all the pieces together in the right order", "Jens Schauder"));

        smallCars.addTopic("SUV", "SUV with sliding doors.");
        smallCars.addTopic("roadster", "Slick red roadster.");

        Channel f1Racer = createChannel("F1 Racer", 6, 15);
        f1Racer.setMessage(new Message("Build a helicopter or a plane", "M. Shoemaker"));

        f1Racer.addTopic("F1 Ferrari 2018", "A very fast red car.");

        Channel constructionVehicles = createChannel("Construction Vehicles", 3, 6);
        constructionVehicles.setMessage(
                new Message("Build a Road Roler, a Mobile Crane, a Tracked Dumper, or a Backhoe Loader ", "Bob the Builder"));

        constructionVehicles.addTopic("scoop", "A backhoe loader");
        constructionVehicles.addTopic("Muck", "Muck is a continuous tracked dump truck with an added bulldozer blade");
        constructionVehicles.addTopic("lofty", "A mobile crane");
        constructionVehicles.addTopic("roley",
                "A road roller that loves to make up songs and frequently spins his eyes when he is excited.");

        repository.saveAll(Arrays.asList(smallCars, f1Racer, constructionVehicles));

        List<ChannelReport> report = repository.channelReportForAge(6);
        Output.list(report, "Model Report");

        assertThat(report).hasSize(7)
                .allMatch(m -> m.getDescription() != null && m.getTopicName() != null && m.getChannelName() != null);

        int updated = repository.lowerCaseTopicNames();
        // SUV, F1 Ferrari 2018 and Muck get updated
        assertThat(updated).isEqualTo(3);

    }

    private Channel createChannel(String name, int minimumAge, int maximumAge) {

        Channel channel = new Channel();

        channel.setName(name);
        channel.setMinimumAge(Period.ofYears(minimumAge));
        channel.setMaximumAge(Period.ofYears(maximumAge));

        return channel;
    }

    private void checkChannels(Iterable<Channel> channels, String messageText, int numberOfTopics) {
        assertThat(channels)
				.extracting(
                        channel -> channel.getMessage().getText(),
                        channel -> channel.getTopics().size())
                .containsExactly(new Tuple(messageText, numberOfTopics));
    }
}
