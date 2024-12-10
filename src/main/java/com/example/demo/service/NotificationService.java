package com.example.demo.service;

import com.example.demo.model.dto.TaskUpdateDto;

public interface NotificationService {

    void sendEmail(TaskUpdateDto taskDto);

}
