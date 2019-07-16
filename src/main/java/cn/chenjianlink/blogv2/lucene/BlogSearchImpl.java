package cn.chenjianlink.blogv2.lucene;

import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.pojo.Blog;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * BlogSearch实现
 *
 * @author chenjian
 */
@Service
public class BlogSearchImpl implements BlogSearch {

    @Value("${blog.index-path}")
    private String indexPath;

    /**
     * 添加日志索引
     */
    @Override
    public void addBlogIndex(Blog blog) throws BlogSearchException {
        IndexWriter indexWriter = this.getIndexWriter();
        Document document = this.getDocument(blog);
        try {
            //添加文档到索引库
            indexWriter.addDocument(document);
            indexWriter.commit();
        } catch (IOException e) {
            throw new BlogSearchException("添加日志索引失败:" + e.getMessage(), e);
        } finally {
            this.releaseIndexWriter(indexWriter);
        }
    }

    /**
     * 删除日志索引
     */
    @Override
    public void deleteBlogIndex(Integer... blogIds) throws BlogSearchException {
        IndexWriter indexWriter = this.getIndexWriter();
        try {
            for (Integer blogId : blogIds) {
                Term term = new Term("id", blogId.toString());
                indexWriter.deleteDocuments(term);
                indexWriter.forceMergeDeletes();
            }
            indexWriter.commit();
        } catch (IOException e) {
            throw new BlogSearchException("日志索引删除失败:" + e.getMessage(), e);
        } finally {
            this.releaseIndexWriter(indexWriter);
        }
    }

    /**
     * 更新日志索引
     */
    @Override
    public void updateBlogIndex(Blog blog) throws BlogSearchException {
        IndexWriter indexWriter = this.getIndexWriter();
        Document document = this.getDocument(blog);
        try {
            //更新
            indexWriter.updateDocument(new Term("id", blog.getId().toString()), document);
            indexWriter.commit();
        } catch (IOException e) {
            throw new BlogSearchException("日志索引更新失败:" + e.getMessage(), e);
        } finally {
            this.releaseIndexWriter(indexWriter);
        }
    }

    /**
     * 根据关键字查询日志索引
     */
    @Override
    public List<Blog> searchBlogIndex(String query) throws BlogSearchException {
        try {
            IndexSearcher indexSearcher = this.getIndexSearcher();
            //创建ik分词器
            Analyzer analyzer = new IKAnalyzer();
            //设置查询条件
            QueryParser parser = new QueryParser("title", analyzer);
            Query query1 = parser.parse(query);
            QueryParser parser2 = new QueryParser("content", analyzer);
            Query query2 = parser2.parse(query);
            //查询组合
            BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
            booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
            booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
            //查询
            TopDocs topDocs = indexSearcher.search(booleanQuery.build(), 100);
            //高亮处理
            QueryScorer scorer = new QueryScorer(query1);
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
            highlighter.setTextFragmenter(fragmenter);
            //数据处理
            List<Blog> blogList = new LinkedList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                Blog blog = new Blog();
                //将数据进行封装
                blog.setId(Integer.parseInt(document.get("id")));
                blog.setReleaseDateStr(document.get("releaseDate"));
                String title = document.get("title");
                String content = document.get("content");
                //对标题高亮设置
                if (title != null) {
                    TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
                    String hTitle = highlighter.getBestFragment(tokenStream, title);
                    if (StringUtils.isBlank(hTitle)) {
                        blog.setTitle(title);
                    } else {
                        blog.setTitle(hTitle);
                    }
                }
                //对内容高亮设置
                if (content != null) {
                    TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                    String hContent = highlighter.getBestFragment(tokenStream, content);
                    if (StringUtils.isBlank(hContent)) {
                        if (content.length() <= 150) {
                            blog.setContent(content);
                        } else {
                            blog.setContent(content.substring(0, 150));
                        }
                    } else {
                        blog.setContent(hContent);
                    }
                }
                blogList.add(blog);
            }
            return blogList;
        } catch (IOException e) {
            throw new BlogSearchException("查询日志失败:" + e.getMessage(), e);
        } catch (InvalidTokenOffsetsException e) {
            throw new BlogSearchException("查询域名匹配失败:" + e.getMessage(), e);
        } catch (ParseException e) {
            throw new BlogSearchException("高亮内容匹配失败:" + e.getMessage(), e);
        }
    }

    /**
     * 重建全部日志索引
     */
    @Override
    public void rebuildBlogIndex(List<Blog> blogList) throws BlogSearchException {
        IndexWriter indexWriter = this.getIndexWriter();
        try {
            indexWriter.deleteAll();
            for (Blog blog : blogList) {
                Document document = this.getDocument(blog);
                indexWriter.addDocument(document);
            }
            indexWriter.commit();
        } catch (IOException e) {
            throw new BlogSearchException("重建日志索引失败:" + e.getMessage(), e);
        } finally {
            this.releaseIndexWriter(indexWriter);
        }
    }

    /**
     * 获取indexwriter对象
     *
     * @return indexwriter对象
     * @throws BlogSearchException 创建indexwriter对象失败，抛出自定义异常
     */
    private IndexWriter getIndexWriter() throws BlogSearchException {
        try {
            //创建一个indexwriter对象。
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            //设置分析器为IK分词器
            Analyzer analyzer = new IKAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            return new IndexWriter(directory, config);
        } catch (IOException e) {
            throw new BlogSearchException("IndexWriter创建失败:" + e.getMessage(), e);
        }

    }

    /**
     * 获取IndexSearcher对象
     *
     * @return IndexSearcher对象
     * @throws BlogSearchException 没有获取到IndexSearcher对象，抛出自定义异常
     */
    private IndexSearcher getIndexSearcher() throws BlogSearchException {
        try {
            //创建索引库存放的位置。
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            //创建一个indexReader对象
            IndexReader indexReader = DirectoryReader.open(directory);
            //创建一个indexsearcher对象
            return new IndexSearcher(indexReader);
        } catch (IOException e) {
            throw new BlogSearchException("IndexSearcher创建失败:" + e.getMessage(), e);
        }

    }

    /**
     * 释放indexWriter占用的资源
     *
     * @param indexWriter 要关闭的indexWriter对象
     * @throws BlogSearchException indexWriter关闭失败，记录原因抛出自定义异常
     */
    private void releaseIndexWriter(IndexWriter indexWriter) throws BlogSearchException {
        try {
            if (indexWriter != null) {
                indexWriter.close();
            }
        } catch (IOException e) {
            throw new BlogSearchException("IndexWriter关闭失败:" + e.getMessage(), e);
        }
    }

    /**
     * 获取日志对象对应的document对象
     *
     * @param blog 日志对象
     * @return document对象
     */
    private Document getDocument(Blog blog) {
        Document document = new Document();
        document.add(new StringField("id", blog.getId().toString(), Field.Store.YES));
        document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        document.add(new StringField("releaseDate", DateFormatUtils.format(blog.getReleaseDate(), "yyyy-MM-dd"), Field.Store.YES));
        document.add(new TextField("content", blog.getSearchContent(), Field.Store.YES));
        return document;
    }
}
