/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.apress.spring_quick.di;

import com.apress.spring_quick.config.AppSpringConfig;
import com.apress.spring_quick.di.model.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        final ApplicationContext applicationContext;
        final String type = args.length == 0 ? "annotation" : args[0];
        switch (type) {
            case "xml":
                applicationContext =
                        new ClassPathXmlApplicationContext("classpath:/application.xml");
                break;
            case "annotation":
                applicationContext =
                        new AnnotationConfigApplicationContext(AppSpringConfig.class);
                break;
            default:
                throw new IllegalArgumentException(type + " unknown");
        }
        final MyBeanInterface myBean = applicationContext.getBean(MyBeanInterface.class);

        System.out.println("Ran using " + type + " type Spring config");
        System.out.println(myBean.getComment());

        final MessageRepository messageRepository = applicationContext.getBean(MessageRepository.class);
        messageRepository.save(new Message("My first message"));
        messageRepository.save(new Message("My second message"));

        System.out.println("All Messages = " + messageRepository.findAll());
    }

}
