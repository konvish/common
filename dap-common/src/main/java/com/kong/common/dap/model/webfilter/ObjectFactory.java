package com.kong.common.dap.model.webfilter;

import javax.xml.namespace.QName;

import com.kong.common.dap.model.webfilter.Content;
import com.kong.common.dap.model.webfilter.Creator;
import com.kong.common.dap.model.webfilter.Entry;
import com.kong.common.dap.model.webfilter.Har;
import com.kong.common.dap.model.webfilter.Log;
import com.kong.common.dap.model.webfilter.Message;
import com.kong.common.dap.model.webfilter.NameValuePair;
import com.kong.common.dap.model.webfilter.Request;
import com.kong.common.dap.model.webfilter.Response;
import com.kong.common.dap.model.webfilter.Timings;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
/**
 * Created by kong on 2016/1/3.
 */
@XmlRegistry
public class ObjectFactory {
    private static final QName _Message_QNAME = new QName("http://www.mashape.com/analytics", "Message");

    public ObjectFactory() {
    }

    public Message createMessage() {
        return new Message();
    }

    public Timings createTimings() {
        return new Timings();
    }

    public Response createResponse() {
        return new Response();
    }

    public NameValuePair createNameValuePair() {
        return new NameValuePair();
    }

    public Entry createEntry() {
        return new Entry();
    }

    public Log createLog() {
        return new Log();
    }

    public Content createContent() {
        return new Content();
    }

    public Har createHar() {
        return new Har();
    }

    public Request createRequest() {
        return new Request();
    }

    public Creator createCreator() {
        return new Creator();
    }

    @XmlElementDecl(
            namespace = "http://www.mashape.com/analytics",
            name = "Message"
    )
    public JAXBElement<Message> createMessage(Message value) {
        return new JAXBElement(_Message_QNAME, Message.class, (Class)null, value);
    }
}
