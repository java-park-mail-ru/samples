package com.example.data.jaxb;

import com.example.data.Resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Solovyev on 09/11/2016.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "com.example.data.jaxb.SerializationObject")
@XmlAccessorType(XmlAccessType.NONE)
public class SerializationObject  {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "age")
    private int age;

    public SerializationObject() {
    }

    public SerializationObject(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "SerializationObject{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}