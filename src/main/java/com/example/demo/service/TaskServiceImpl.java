package com.example.demo.service;

import com.example.demo.aspect.annotation.LogException;
import com.example.demo.aspect.annotation.LogExecution;
import com.example.demo.aspect.annotation.LogResult;
import com.example.demo.aspect.annotation.LogTracking;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.model.Task;
import com.example.demo.model.dto.TaskRequestDto;
import com.example.demo.model.dto.TaskResponseDto;
import com.example.demo.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

        Optional<Task> task = findTaskById(id);
        return taskMapper.mapToDto(task.get());
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
    public void saveTask(final TaskRequestDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        taskRepository.save(task);
    }

    @LogExecution
    @LogResult
    @LogTracking
    @Transactional
    @Override
    public TaskResponseDto updateTask(final TaskRequestDto taskDto, final Long id) {
        Optional<Task> task = findTaskById(id);
        if (task.isPresent()) {
            task = Optional.ofNullable(taskMapper.mapToTask(taskDto));
            task.get().setId(id);
            taskRepository.save(task.get());
        }
        return taskMapper.mapToDto(task.get());
    }

    private Optional<Task> findTaskById(final Long id) {
        return Optional.ofNullable(taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found")));
    }
}
