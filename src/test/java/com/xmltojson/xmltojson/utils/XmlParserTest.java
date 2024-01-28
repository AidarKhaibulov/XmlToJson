package com.xmltojson.xmltojson.utils;

import com.xmltojson.xmltojson.xmlmodel.XmlNode;
import com.xmltojson.xmltojson.xmlmodel.XmlTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlParserTest {

    @Nested
    @DisplayName("Test method: parseXml")
    class ParseXmlTestClass {

        @Test
        @DisplayName("Basic scenario")
        void test01() throws Exception {
            String xmlFile = "<root>Node 4<nodeA>Node 1</nodeA><nodeB>Node 10<nodeC>Node 2</nodeC><nodeD>Node 3</nodeD></nodeB></root>";
            MockMultipartFile mockFile = new MockMultipartFile("file.xml", xmlFile.getBytes());

            XmlTree xmlTree = XmlParser.parseXml(mockFile);

            assertNotNull(xmlTree);
            assertRoot(xmlTree.getRoot());
        }

        @Test
        @DisplayName("Empty tree")
        void test02() throws Exception {
            String xmlFile = "<root></root>";
            MockMultipartFile mockFile = new MockMultipartFile("file.xml", xmlFile.getBytes());

            XmlTree xmlTree = XmlParser.parseXml(mockFile);

            assertNotNull(xmlTree);
            assertNodeHasNoChildren(xmlTree.getRoot());
        }

        @Test
        @DisplayName("Wrong xml")
        void test03() {
            String xmlFile = "Definitely not an xml";
            MockMultipartFile mockFile = new MockMultipartFile("file.xml", xmlFile.getBytes());

            Assertions.assertThrows(IOException.class, () -> XmlParser.parseXml(mockFile));
        }

        @Test
        @DisplayName("Wrong xml: missing closing tag")
        void test04() {
            String xmlFile = "<root><root>";
            MockMultipartFile mockFile = new MockMultipartFile("file.xml", xmlFile.getBytes());

            Assertions.assertThrows(IOException.class, () -> XmlParser.parseXml(mockFile));
        }


        private void assertNodeHasNoChildren(XmlNode node) {
            assertEquals(0, node.getChildren().size());
        }

        private void assertRoot(XmlNode root) {
            assertEquals("root", root.getName());
            assertEquals("Node 4", root.getContent());
            assertEquals(2, root.getChildren().size());

            assertNodeA(root.getChildren().get(0));
            assertNodeB(root.getChildren().get(1));
        }

        private void assertNodeA(XmlNode nodeA) {
            assertEquals("nodeA", nodeA.getName());
            assertEquals("Node 1", nodeA.getContent());
        }

        private void assertNodeB(XmlNode nodeB) {
            assertEquals("nodeB", nodeB.getName());
            assertEquals("Node 10", nodeB.getContent());
            assertEquals(2, nodeB.getChildren().size());

            assertNodeC(nodeB.getChildren().get(0));
            assertNodeD(nodeB.getChildren().get(1));
        }

        private void assertNodeC(XmlNode nodeC) {
            assertEquals("nodeC", nodeC.getName());
            assertEquals("Node 2", nodeC.getContent());
        }

        private void assertNodeD(XmlNode nodeD) {
            assertEquals("nodeD", nodeD.getName());
            assertEquals("Node 3", nodeD.getContent());
        }

    }
}