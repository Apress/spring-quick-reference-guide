package com.apress.spring_quick.ws.webservices;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

/*
 * Copyright 2020, Adam L. Davis
 */
@Endpoint
public class CourseEndpoint {

    private XPathExpression<Element> numberExpression;

    private XPathExpression<Element> titleExpression;

    private XPathExpression<Element> subtitleExpression;

    private XPathExpression<Element> descriptionExpression;

    @Autowired
    public CourseEndpoint() throws JDOMException {
        Namespace namespace = Namespace.getNamespace("my", "http://mycompany.com/my/schemas");
        XPathFactory xPathFactory = XPathFactory.instance();
        numberExpression = xPathFactory.compile("//my:Number", Filters.element(), null, namespace);
        titleExpression = xPathFactory.compile("//my:Title", Filters.element(), null, namespace);
        subtitleExpression = xPathFactory.compile("//my:Subtitle", Filters.element(), null, namespace);
        descriptionExpression = xPathFactory.compile("//my:Description", Filters.element(), null, namespace);
    }

    @PayloadRoot(namespace = "http://mycompany.com/my/schemas", localPart = "CourseRequest")
    public void handleRequest(@RequestPayload Element courseRequest) throws Exception {
        Long number = Long.parseLong(numberExpression.evaluateFirst(courseRequest).getText());
        String description = descriptionExpression.evaluateFirst(courseRequest).getText();
        String fullTitle = titleExpression.evaluateFirst(courseRequest).getText() + ":"
                + subtitleExpression.evaluateFirst(courseRequest).getText();

        // handleCourse(number, fullTitle, description)
    }

}
