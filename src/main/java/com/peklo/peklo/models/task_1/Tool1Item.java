package com.peklo.peklo.models.task_1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tool_1_items")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class Tool1Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromUrl;
    @Lob
    private String htmlValue;

    private String cssPath;

    private String time;

    private LocalDateTime localDateTime;

    private String userChatId;
}
