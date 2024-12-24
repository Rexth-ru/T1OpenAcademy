package com.example.demo.controller;

import com.example.demo.kafka.KafkaTaskProducer;
import com.example.demo.model.dto.TaskDto;
import com.example.demo.model.dto.TaskResponseDto;
import com.example.demo.model.dto.TaskUpdateDto;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final KafkaTaskProducer kafkaTaskProducer;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody TaskDto task) {
        taskService.saveTask(task);
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTask(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping()
    public List<TaskResponseDto> getTasks() {
        return taskService.getAllTasks();
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@RequestBody TaskDto task, @PathVariable Long id) {
        TaskResponseDto taskResponseDto = taskService.updateTask(task, id);
        TaskUpdateDto taskUpdateDto = new TaskUpdateDto(id, taskResponseDto.status());
        kafkaTaskProducer.sendTo("task-status-topic", taskUpdateDto);
        return taskResponseDto;
    }
}
