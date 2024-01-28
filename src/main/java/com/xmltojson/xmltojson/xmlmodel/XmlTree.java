package com.xmltojson.xmltojson.xmlmodel;

import lombok.Getter;

@Getter
public class XmlTree {

    private final XmlNode root;

    public XmlTree(XmlNode root) {
        this.root = root;
    }

}