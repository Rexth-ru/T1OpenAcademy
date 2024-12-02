package com.example.demo.mapper;

import com.example.demo.model.Task;
import com.example.demo.model.dto.TaskResponseDto;
import com.example.demo.model.dto.TaskRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskResponseDto mapToDto(Task task);
@Mapping(target = "id", ignore = true)
    Task mapToTask(TaskRequestDto taskDto);
}
