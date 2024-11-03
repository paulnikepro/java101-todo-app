package org.paulnikepro.hw4.todoapp.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TodoControllerIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateTodo() {
        Map<String, Object> requestBody = createTodoRequest("Test Todo", "This is a test todo item.", LocalDateTime.now().plusDays(1), "HIGH", "NEW");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, createHeaders());

        var response = restTemplate.postForEntity("/todos", requestEntity, Map.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).get("id"));
        assertEquals("Test Todo", response.getBody().get("title"));
    }

    @Test
    void testUpdateTodo() {

        Map<String, Object> createRequestBody = createTodoRequest("Initial Todo", "Description", LocalDateTime.now().plusDays(1), "HIGH", "NEW");
        var createResponse = restTemplate.postForEntity("/todos", createRequestBody, Map.class);
        long todoId = Long.parseLong(Objects.requireNonNull(createResponse.getBody()).get("id").toString());

        // Prepare the update request body
        Map<String, Object> updateRequestBody = createTodoRequest("Updated Todo", "Updated description", LocalDateTime.now().plusDays(2), "LOW", "IN_PROGRESS");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(updateRequestBody, createHeaders());

        // PUT request
        var updateResponse = restTemplate.exchange("/todos/" + todoId, HttpMethod.PUT, requestEntity, Map.class);

        // Check if response status is not OK
        if (updateResponse.getStatusCode() != HttpStatus.OK) {
            System.out.println("Response body: " + updateResponse.getBody());
        }

        // Assert the response
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Updated Todo", Objects.requireNonNull(updateResponse.getBody()).get("title"));
    }

    @Test
    void testDeleteTodo() {
        // First, create a Todo
        Map<String, Object> requestBody = createTodoRequest("Todo to delete", "This todo will be deleted.", LocalDateTime.now().plusDays(1), "HIGH", "NEW");
        var createResponse = restTemplate.postForEntity("/todos", requestBody, Map.class);
        long todoId = Long.parseLong(Objects.requireNonNull(createResponse.getBody()).get("id").toString());

        // Perform the DELETE request
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/todos/" + todoId, HttpMethod.DELETE, null, Void.class);

        // Assert the response for deletion
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Assert that the todo is actually deleted
        var getResponse = restTemplate.getForEntity("/todos/" + todoId, Map.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }


    @Test
    void testGetTaskHistory() {
        // First, create a Todo with a status
        Map<String, Object> requestBody = createTodoRequest("Todo for history", "This todo will have history.", LocalDateTime.now().plusDays(1), "HIGH", "NEW");
        var createResponse = restTemplate.postForEntity("/todos", requestBody, Map.class);
        long todoId = Long.parseLong(Objects.requireNonNull(createResponse.getBody()).get("id").toString());

        // Retrieve task history
        var historyResponse = restTemplate.getForEntity("/todos/" + todoId + "/history", List.class);

        // Assert the response
        assertEquals(HttpStatus.OK, historyResponse.getStatusCode());
        assertNotNull(historyResponse.getBody());
    }

    private Map<String, Object> createTodoRequest(String title, String description, LocalDateTime dueDate, String priority, String status) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("description", description);
        requestBody.put("dueDate", dueDate);
        requestBody.put("priority", priority);
        requestBody.put("status", status); // Include status here
        return requestBody;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
