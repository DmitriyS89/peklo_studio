package com.peklo.peklo.models.task_3;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiteWith3rdDomain implements SiteWithDomain {
    private String protocol;
    private String domain_3rd;
    private String domain_2rd;
    private String domain_1rd;
    private String domain_0rd;
}
