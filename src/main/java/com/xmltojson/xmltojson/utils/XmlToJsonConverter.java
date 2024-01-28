package com.xmltojson.xmltojson.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xmltojson.xmltojson.xmlmodel.XmlNode;
import com.xmltojson.xmltojson.xmlmodel.XmlTree;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlToJsonConverter {

    public static JsonNode convertXmlToJson(MultipartFile file) throws IOException {
        XmlTree xmlTree = XmlParser.parseXml(file);
        return convertXmlTreeToJsonTree(xmlTree.getRoot(), true);
    }

    private static JsonNode convertXmlTreeToJsonTree(XmlNode xmlNode, boolean isRoot) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("root", jsonNode);

        jsonNode.put("value", 0);

        for (XmlNode child : xmlNode.getChildren()) {
            jsonNode.set(child.getName(), convertXmlTreeToJsonTree(child, false));
        }

        jsonNode.put("value", calculateNodeValue(xmlNode));

        return isRoot ? rootNode : jsonNode;
    }


    private static String calculateNodeValue(XmlNode xmlNode) {
        double sum = 0;

        String nodeValue = xmlNode.getContent();
        if (nodeValue != null && !nodeValue.isEmpty()) {
            sum += extractNumbersFromValue(nodeValue);
        }

        for (XmlNode child : xmlNode.getChildren()) {
            sum += child.getValue();
        }

        xmlNode.setValue(sum);

        if (sum % 1 == 0) {
            return String.valueOf((int) sum);
        } else {
            return String.valueOf(sum);
        }
    }

    public static double extractNumbersFromValue(String input) {
        double result = 0.0;

        try {
            result = Double.parseDouble(extractNumber(input));
        } catch (NumberFormatException e) {
            System.out.println("Unable to convert the string to a number.");
        }

        return result;
    }

    private static String extractNumber(String input) {
        String numberRegex = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
        String extractedNumber = input.replaceAll("[^\\d.+-]", "");
        return extractedNumber.matches(numberRegex) ? extractedNumber : "0";
    }
}