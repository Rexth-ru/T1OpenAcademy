package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskUpdateDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 8737313889853602225L;
    private Long id;
    private String status;
}
