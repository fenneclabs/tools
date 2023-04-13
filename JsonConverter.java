package com.fenneclabs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonTool {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("file to convert must be provided as the only arg");
            System.exit(-1);
        }

        ObjectNode export = MAPPER.createObjectNode();
        export.put("_type", "export");
        export.put("__export_format", 4);
        export.put("__export_date", "2023-04-13T08:41:19.070Z");
        export.put("__export_source", "insomnia.desktop.app:v2023.1.0");
        ArrayNode resources = MAPPER.createArrayNode();
        export.set("resources", resources);

        long metaSortKey = 0;

        ArrayNode entities = (ArrayNode) MAPPER.readTree(new File(args[0])).get("entities");
        for (JsonNode entity : entities) {
            //project
            String project = entity.get("entity").get("name").asText();
            String projectId = entity.get("entity").get("id").asText();
            String parentId = "wrk_" + projectId;

            ObjectNode collection = MAPPER.createObjectNode();
            collection.put("_id", parentId);
            collection.putNull("parentId");
            collection.put("created", 1645450750693L);
            collection.put("modified", 1645450750693L);
            collection.put("name", project);
            collection.put("scope", "collection");
            collection.put("_type", "workspace");
            resources.add(collection);

            ArrayNode children = (ArrayNode) entity.get("children");
            for (JsonNode child : children) {
                metaSortKey++;
                child = child.get("entity");
                String name = child.get("name").asText();
                String id = child.get("id").asText();
                String method = child.get("method").asText();
                String textBody = child.get("body").get("textBody") == null ? null : child.get("body").get("textBody").asText();
                String host = child.get("uri").get("host") == null ? "http://localhost" : child.get("uri").get("host").asText();
                String path = child.get("uri").get("path") == null ? "/" : child.get("uri").get("path").asText();

                ObjectNode resource = MAPPER.createObjectNode();
                resource.put("_id", "req_" + id);
                resource.put("parentId", parentId);
                resource.put("created", 1645450750693L);
                resource.put("modified", 1645450750693L);
                resource.put("url", host + path);
                resource.put("name", name);
                resource.put("description", "");
                resource.put("method", method);

                if (textBody != null) {
                    ObjectNode body = MAPPER.createObjectNode();
                    body.put("mimeType", "application/json");
                    body.put("text", textBody);

                    resource.set("body", body);
                }

                resource.set("parameters", MAPPER.createArrayNode());

                ArrayNode headers = MAPPER.createArrayNode();
                headers.add(MAPPER.createObjectNode().put("name", "Content-Type").put("value", "application/json"));
                resource.set("headers", headers);

                resource.set("authentication", MAPPER.createObjectNode());

                resource.put("metaSortKey", metaSortKey);
                resource.put("isPrivate", false);
                resource.put("settingStoreCookies", true);
                resource.put("settingSendCookies", true);
                resource.put("settingDisableRenderRequestBody", false);
                resource.put("settingEncodeUrl", true);
                resource.put("settingRebuildPath", true);
                resource.put("settingFollowRedirects", "global");
                resource.put("_type", "request");

                resources.add(resource);
            }
        }

        System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(export));
        System.exit(1);
    }
}
