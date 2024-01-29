package com.xmltojson.xmltojson.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class XmlToJsonConverterTest {
    @Nested
    @DisplayName("Test method: convertXmlToJson")
    class ConvertXmlToJsonTestClass {
        @Test
        @SneakyThrows
        @DisplayName("Base scenario")
        void test01() {
            MockMultipartFile mockFile = createMockMultipartFile("xmlfiles/test1.xml");

            JsonNode result = XmlToJsonConverter.convertXmlToJson(mockFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expected = objectMapper.readTree("""
                    {
                      "root": {
                        "value": "20",
                        "nodeA": {
                          "value": "1"
                        },
                        "nodeB": {
                          "value": "15",
                          "nodeC": {
                            "value": "2"
                          },
                          "nodeD": {
                            "value": "3"
                          }
                        }
                      }
                    }
                    """);

            assertEquals(expected, result);
        }

        @Test
        @SneakyThrows
        @DisplayName("Task example scenario")
        void test02() {
            MockMultipartFile mockFile = createMockMultipartFile("xmlfiles/test2.xml");

            JsonNode result = XmlToJsonConverter.convertXmlToJson(mockFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expected = objectMapper.readTree("""
                    {
                      "root": {
                        "value": "6",
                        "nodeA": { "value": "1" },
                        "nodeB": {
                          "value": "5",
                          "nodeC": { "value": "2" },
                          "nodeD": { "value": "3" }
                        }
                      }
                    }""");

            assertEquals(expected, result);
        }

        @Test
        @SneakyThrows
        @DisplayName("Has no numbers in nodes")
        void test03() {
            MockMultipartFile mockFile = createMockMultipartFile("xmlfiles/test3.xml");

            JsonNode result = XmlToJsonConverter.convertXmlToJson(mockFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expected = objectMapper.readTree("""
                    {
                      "root": {
                        "value": "0",
                        "nodeA": {
                          "value": "0"
                        },
                        "nodeB": {
                          "value": "0",
                          "nodeC": {
                            "value": "0"
                          },
                          "nodeD": {
                            "value": "0"
                          }
                        }
                      }
                    }
                    """);

            assertEquals(expected, result);
        }

        @Test
        @SneakyThrows
        @DisplayName("Decimal numbers in nodes")
        void test04() {
            MockMultipartFile mockFile = createMockMultipartFile("xmlfiles/test4.xml");

            JsonNode result = XmlToJsonConverter.convertXmlToJson(mockFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expected = objectMapper.readTree("""
                    {
                      "root": {
                        "value": "12",
                        "nodeA": {
                          "value": "1"
                        },
                        "nodeB": {
                          "value": "11",
                          "nodeC": {
                            "value": "0.5"
                          },
                          "nodeD": {
                            "value": "10.5"
                          }
                        }
                      }
                    }
                    """);

            assertEquals(expected, result);
        }

        @Test
        @SneakyThrows
        @DisplayName("Extra text")
        void test05() {
            MockMultipartFile mockFile = createMockMultipartFile("xmlfiles/test5.xml");

            JsonNode result = XmlToJsonConverter.convertXmlToJson(mockFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expected = objectMapper.readTree("""
                    {
                      "root": {
                        "value": "28",
                        "nodeA": {
                          "value": "1"
                        },
                        "nodeB": {
                          "value": "14",
                          "nodeC": {
                            "value": "2"
                          },
                          "nodeD": {
                            "value": "3"
                          },
                          "nodeE": {
                            "value": "9",
                            "nodeF": {
                              "value": "4"
                            },
                            "nodeG": {
                              "value": "5"
                            }
                          }
                        },
                        "nodeH": {
                          "value": "13",
                          "nodeI": {
                            "value": "6"
                          },
                          "nodeJ": {
                            "value": "7"
                          }
                        }
                      }
                    }
                    """);

            assertEquals(expected, result);
        }

        @Test
        @SneakyThrows
        @DisplayName("Negative numbers in nodes")
        void test06() {
            MockMultipartFile mockFile = createMockMultipartFile("xmlfiles/test6.xml");

            JsonNode result = XmlToJsonConverter.convertXmlToJson(mockFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expected = objectMapper.readTree("""
                    {
                      "root": {
                        "value": "7",
                        "nodeA": {
                          "value": "1"
                        },
                        "nodeB": {
                          "value": "10",
                          "nodeC": {
                            "value": "2"
                          },
                          "nodeD": {
                            "value": "-2"
                          }
                        }
                      }
                    }
                    """);

            assertEquals(expected, result);
        }


        public static MockMultipartFile createMockMultipartFile(String fileName) throws IOException {
            Path filePath = ResourceUtils.getFile("classpath:" + fileName).toPath();
            byte[] fileContent = Files.readAllBytes(filePath);
            return new MockMultipartFile("file", fileName, "application/xml", fileContent);
        }
    }


}