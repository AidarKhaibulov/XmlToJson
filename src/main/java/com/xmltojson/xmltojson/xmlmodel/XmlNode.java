package com.xmltojson.xmltojson.xmlmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class XmlNode {
    private String name;
    private String content;
    private List<XmlNode> children;
    private double value;

    public XmlNode(String name, String content) {
        this.name = name;
        this.content = content;
        this.children = new ArrayList<>();
    }

    public void addChild(XmlNode child) {
        this.children.add(child);
    }

}
