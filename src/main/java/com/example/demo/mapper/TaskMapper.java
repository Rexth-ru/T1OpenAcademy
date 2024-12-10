package com.example.demo.mapper;

import com.example.demo.model.Task;
import com.example.demo.model.dto.TaskDto;
import com.example.demo.model.dto.TaskResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskResponseDto mapToDto(Task task);

    Task mapToTask(TaskDto taskDto);
}
