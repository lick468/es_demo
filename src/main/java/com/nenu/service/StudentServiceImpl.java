package com.nenu.service;

import com.alibaba.fastjson.JSON;
import com.nenu.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：lichuankang
 * @date ：2021/2/26 17:23
 * @description ：
 */
@Service
@Slf4j
public class StudentServiceImpl {

    @Autowired
    private RestHighLevelClient client;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    private static final String NBA_INDEX = "stu_v1";
    private static final Integer START_OFFSET = 1;
    private static final Integer MAX_COUNT = 100;


    public boolean addStudent(Student student, String id) {
//        IndexRequest request = new IndexRequest(NBA_INDEX).id(id).source(beanToMap(student));
//        try {
//            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//            log.info("addStudent-- {}", JSON.toJSON(response));
//        } catch (Exception e) {
//            log.error("add S occur error ", e);
//        }

        Student save = elasticsearchRestTemplate.save(student);
        log.info("student ---> {}", save);

        return false;
    }


    public boolean updateStudent(Student student, String id) {
        UpdateRequest request = new UpdateRequest(NBA_INDEX, id).doc(beanToMap(student));
        try {
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            log.info("updateStudent-- {}", JSON.toJSON(response));
        } catch (Exception e) {
            log.error("updateStudent occur error ", e);
        }

        return false;
    }

    public boolean deleteStudent(String id) {
        DeleteRequest request = new DeleteRequest(NBA_INDEX, id);
        try {
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            log.info("deleteStudent-- {}", JSON.toJSON(response));
        } catch (Exception e) {
            log.error("deleteStudent occur error ", e);
        }

        return false;
    }

    public Map<String, Object> getStudent(String id) {


        Student student = elasticsearchRestTemplate.get(id, Student.class);

        log.info("getS---> {}", student);

        GetRequest getRequest = new GetRequest(NBA_INDEX, id);
        try {
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            log.info("getStudent {}", JSON.toJSON(response));
            return response.getSource();
        } catch (Exception e) {
            log.error("get occur error", e);
        }
        return new HashMap<>();

    }

    public List<Student> searchStudent(String key, String value) {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(key, value));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            return Arrays.stream(response.getHits().getHits())
                    .map(hit -> JSON.parseObject(hit.getSourceAsString(), Student.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("searchStudent occur error", e);
        }
        return new ArrayList<>();

    }


    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);

            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) != null)
                    map.put(key + "", beanMap.get(key));
            }
        }

        return map;
    }


}
