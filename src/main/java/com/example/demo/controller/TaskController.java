package com.example.demo.controller;

import com.example.demo.model.dto.TaskRequestDto;
import com.example.demo.model.dto.TaskResponseDto;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto task) {
        taskService.saveTask(task);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping()
    public ResponseEntity<List<TaskResponseDto>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto task, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTask(task, id));
    }
}
