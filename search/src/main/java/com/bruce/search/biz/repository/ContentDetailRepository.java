package com.bruce.search.biz.repository;

import com.alibaba.fastjson.JSONObject;
import com.bruce.common.utils.BeanUtil;
import com.bruce.search.biz.domain.ContentStatistics;
import com.bruce.search.biz.model.ContentDetailModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * SceneSearchRepository
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/11/1
 */
@Repository
@Slf4j
public class ContentDetailRepository {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static final String INDEX = "content_detail";

    public final static String LETTER_REGEX = "[a-zA-Z]";

    private static final String PRE_TAG = "<b><font color='red'>";
    private static final String POST_TAG = "</font></b>";

    public void save(List<ContentDetailModel> models) throws Exception {
        for (ContentDetailModel model : models) {
            IndexRequest request = new IndexRequest(INDEX);
            request.source(BeanUtil.toMap(model));
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }
    }

    public void save(List<ContentDetailModel> models,String indexName) throws Exception {
        for (ContentDetailModel model : models) {
            IndexRequest request = new IndexRequest(indexName);
            request.source(BeanUtil.toMap(model));
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }
    }

    public List<ContentDetailModel> search(String keyword) throws Exception {
        int docTypes[] = {1, 2, 3, 4};
        //文档类型
        BoolQueryBuilder docTypeQuery = QueryBuilders.boolQuery().filter(QueryBuilders.termsQuery("docType", docTypes));
        //关键词
        BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title", keyword).boost(1))
                .should(QueryBuilders.matchQuery("content", keyword).boost(0.5f));
        //拼音
        boolean pyFlag = Pattern.compile(LETTER_REGEX).matcher(keyword).find();
        if (pyFlag) {
            keywordQuery.should(QueryBuilders.matchQuery("title.py", keyword))
                    .should(QueryBuilders.matchQuery("content.py", keyword));
        }

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(docTypeQuery).must(keywordQuery);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0).size(100);
        sourceBuilder.query(queryBuilder);

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //多次段高亮需要设置为false
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field("title").field("content");
        if (pyFlag) {
            highlightBuilder.field("title.py").field("content.py");
        }
        highlightBuilder.preTags(PRE_TAG);
        highlightBuilder.postTags(POST_TAG);
        sourceBuilder.highlighter(highlightBuilder);

        log.info("dsl:" + sourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(new SearchRequest(INDEX).source(sourceBuilder), RequestOptions.DEFAULT);

        SearchHit[] hits = response.getHits().getHits();

        List<ContentDetailModel> result = new ArrayList<>();
        for (SearchHit hit : hits) {
            ContentDetailModel detailModel = JSONObject.parseObject(hit.getSourceAsString(), ContentDetailModel.class);
            detailModel.setScore(hit.getScore());

            String titleLight = getSummary(hit, "title");
            String contentLight = getSummary(hit, "content");
            if (pyFlag) {
                titleLight = getSummary(hit, "title.py");
                contentLight = getSummary(hit, "content.py");
            }
            detailModel.setSummary(titleLight + "</br>" + contentLight);

            result.add(detailModel);
        }
        return result;
    }

    private String getSummary(SearchHit hit, String field) {
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        HighlightField title = highlightFields.get(field);
        if (title != null) {
            Text[] fragments = title.fragments();
            StringBuilder builder = new StringBuilder();
            for (Text fragment : fragments) {
                builder.append(fragment.toString());
            }
            return builder.toString();
        }
        return "";
    }


    public List<ContentStatistics> statistics() throws Exception {
        SearchRequest searchRequest = new SearchRequest(INDEX);

        //按作者 文章类型 文章状态 分组 排序 总数
        TermsAggregationBuilder termsAggregationBuilder =
                AggregationBuilders.terms("total").field("author").size(1000)
                        .subAggregation(AggregationBuilders.terms("docTypeTotal").field("docType")
                                .subAggregation(AggregationBuilders.terms("statusTotal").field("status")));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.aggregation(termsAggregationBuilder);

        log.info("dsl:" + sourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(new SearchRequest(INDEX).source(sourceBuilder), RequestOptions.DEFAULT);

        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms parsedStringTerms = aggregations.get("total");
        List<? extends Terms.Bucket> authorBuckets = parsedStringTerms.getBuckets();

        List<ContentStatistics> result = new ArrayList<>();
        for (Terms.Bucket authorBucket : authorBuckets) {
            //作者
            String author = authorBucket.getKeyAsString();
            long count = authorBucket.getDocCount();
            //文章类型
            List<? extends Terms.Bucket> docTypeBuckets = getAggregationBucket(authorBucket, "docTypeTotal");
            for (Terms.Bucket docTypeBucket : docTypeBuckets) {
                String docType = docTypeBucket.getKeyAsString();
                long docTypeTotal = docTypeBucket.getDocCount();

                //文章状态
                List<? extends Terms.Bucket> statusBuckets = getAggregationBucket(docTypeBucket, "statusTotal");
                for (Terms.Bucket statusBucket : statusBuckets) {
                    String status = statusBucket.getKeyAsString();
                    long statusTotal = statusBucket.getDocCount();

                    ContentStatistics statistics = new ContentStatistics();
                    statistics.setAuthor(author);
                    statistics.setTotal(count);
                    statistics.setDocTypeTotal(docTypeTotal);
                    statistics.setDocType(docType);
                    statistics.setStatusTotal(statusTotal);
                    statistics.setStatus(status);
                    result.add(statistics);
                }
            }

        }
        return result;
    }

    private List<? extends Terms.Bucket> getAggregationBucket(Terms.Bucket bucket, String name) {
        Aggregations docTypeAgg = bucket.getAggregations();
        ParsedLongTerms docTypeTerms = docTypeAgg.get(name);
        List<? extends Terms.Bucket> docTypeBuckets = docTypeTerms.getBuckets();
        return docTypeBuckets;
    }


}
