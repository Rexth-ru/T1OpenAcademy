package com.example.demo.service;

import com.example.demo.model.dto.TaskDto;
import com.example.demo.model.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {

    TaskResponseDto getTaskById(Long id);

    List<TaskResponseDto> getAllTasks();

    void deleteTaskById(Long id);

    void saveTask(TaskDto taskDto);

    TaskResponseDto updateTask(TaskDto dto, Long id);
}
