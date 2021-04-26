package com.peklo.peklo.models.task_4;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Results4Task {
    private String urlFrom;
    private String urlTo;
    private int code;
    private String message;
}
