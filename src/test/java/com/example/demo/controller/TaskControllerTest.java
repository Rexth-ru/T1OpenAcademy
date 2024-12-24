package com.example.demo.controller;

import com.example.demo.kafka.KafkaTaskProducer;
import com.example.demo.model.Task;
import com.example.demo.model.dto.TaskDto;
import com.example.demo.model.dto.TaskUpdateDto;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest extends ContainersForTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @MockBean
    private KafkaTaskProducer kafkaTaskProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Task task = new Task();

    @BeforeEach
    void setUp() {
        task.setUserId("123");
        task.setDescription("Test Description");
        task.setTitle("Test Task");
        task.setStatus("PENDING");
        taskRepository.save(task);
    }

    @AfterEach
    void shutDown() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Тест запроса на создание задачи")
    void testCreateTask() throws Exception {
        TaskDto taskDto = new TaskDto("Test Task", "Test Description", "PENDING", "123");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Тест на запрос получения задачи по id ")
    void testGetTaskById() throws Exception {
        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    @DisplayName("Тест на запрос получения задачи по id с ошибкой")
    void testGetTaskByIdWithException() throws Exception {
        Long nonExistentId = 999L;

        mockMvc.perform(get("/tasks/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Task with id " + nonExistentId + " not found"));
    }


    @Test
    @DisplayName("Тест на запрос получения всех задач")
    void testGetTasks() throws Exception {
        TaskDto taskDto = new TaskDto("Task 1", "Description 1", "PENDING", "234");
        taskService.saveTask(taskDto);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Тест на удаление задачи по id")
    void testDeleteTask() throws Exception {

        mockMvc.perform(delete("/tasks/{id}", task.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Тест на запрос по обновлению задачи")
    void testUpdateTask() throws Exception {
        TaskDto taskDto = new TaskDto("New Task", "In Progress", "PENDING", "234");

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(kafkaTaskProducer, times(1)).sendTo(eq("task-status-topic"), any(TaskUpdateDto.class));
    }
}