package com.nenu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author ：lichuankang
 * @date ：2021/2/26 17:22
 * @description ：
 */
@Document(indexName = "stu_v1")
@Data
public class Student {

    private String name;

    @Id
    private Integer id;

    private String address;

    private String school;

    private String gender;

    private String desc;
}
