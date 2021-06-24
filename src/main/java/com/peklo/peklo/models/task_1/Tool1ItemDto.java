package com.peklo.peklo.models.task_1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tool1ItemDto {

    private Long id;
    private String url;
    private String size;
    private String htmlValueState;
    private String time;

    public static Tool1ItemDto from(Tool1Item item){
        String state = item.getHtmlValue().trim().strip().isBlank() ? "red" : "green";
        String[] time = item.getTime().split("_");
        String timeString = "";
        switch (time[0]){
            case "h":
                timeString = String.format("%s часа/часов", time[1]);
                break;
            case "d":
                timeString = String.format("%s дня/дней", time[1]);
                break;
        }
        String size = String.valueOf(item.getHtmlValue().length());
        return new Tool1ItemDto(item.getId(), item.getFromUrl(), size, state, timeString);
    }
}
