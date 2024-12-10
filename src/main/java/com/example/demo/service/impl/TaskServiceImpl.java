package com.example.demo.service.impl;

import com.example.demo.aspect.annotation.LogException;
import com.example.demo.aspect.annotation.LogExecution;
import com.example.demo.aspect.annotation.LogResult;
import com.example.demo.aspect.annotation.LogTracking;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.model.Task;
import com.example.demo.model.dto.TaskDto;
import com.example.demo.model.dto.TaskResponseDto;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @LogException
    @LogResult
    @Transactional
    @Override
    public TaskResponseDto getTaskById(final Long id) {

        Task task = findTaskById(id);
        return taskMapper.mapToDto(task);
    }

    @LogTracking
    @Transactional
    @Override
    public List<TaskResponseDto> getAllTasks() {

        List<Task> tasks = Objects.requireNonNullElse(taskRepository.findAll(), Collections.emptyList());

        return tasks.stream().map(taskMapper::mapToDto).collect(Collectors.toList());
    }

    @LogException
    @Transactional
    @Override
    public void deleteTaskById(final Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task with id " + id + " does not exist");
        } else {
            taskRepository.deleteById(id);
        }
    }

    @LogTracking
    @LogExecution
    @LogException
    @Transactional
    @Override
    public void saveTask(final TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        taskRepository.save(task);
    }

    @LogExecution
    @LogResult
    @LogTracking
    @LogException
    @Transactional
    @Override
    public TaskResponseDto updateTask(final TaskDto taskDto, final Long id) {
        Task task = findTaskById(id);
        task.setDescription(taskDto.description());
        task.setTitle(taskDto.title());
        task.setUserId(taskDto.userId());
        task.setStatus(taskDto.status());

        taskRepository.save(task);
        return taskMapper.mapToDto(task);
    }

    private Task findTaskById(final Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }
}