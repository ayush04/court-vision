/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.cv.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

/**
 *
 * @author ayush
 */
public class JAXBWrapper<T> {
    public String jaxbObjectToXML(T object, Class<T> cl) {
        StringWriter writer = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(cl);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            m.marshal(object, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    public String jaxbObjectToJSON(T object) {
        
        Gson gson = new Gson();
        return gson.toJson(object).toString();
    }
}
