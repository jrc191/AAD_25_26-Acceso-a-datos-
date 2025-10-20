package com.jramcon398.jrc.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Students Class: Wrapper class for a list of Student objects for XML serialization.
 */

@JacksonXmlRootElement(localName = "Students")
public class Students {
    @Setter
    @Getter
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Student> students;

    public Students() {
    }

    public Students(List<Student> students) {
        this.students = students;
    }

}
