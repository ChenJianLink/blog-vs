package cn.chenjianlink.blogv2.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页显示工具类
 *
 * @author chenjian
 */
@Setter
@Getter
public class PageResult implements Serializable {
    /**
     * 当前页码
     */
    private int currentPage;
    /**
     * 总记录数
     */
    private int totalRows;
    /**
     * 每页记录数
     */
    private int rows;
    /**
     * 当前页的记录
     */
    private List<?> pageList;
    /**
     * 分页跳转路径
     */
    private String url;

    private final Integer ten = 10;

    public PageResult(int currentPage, int totalRows, int rows, List<?> pageList) {
        this.currentPage = currentPage;
        this.totalRows = totalRows;
        this.rows = rows;
        this.pageList = pageList;
    }

    /**
     * 计算总页数
     *
     * @return 总页数
     */
    public int getTotalPage() {
        int totalPage = totalRows / rows;
        return totalRows % rows == 0 ? totalPage : totalPage + 1;
    }

    /**
     * 起始页面
     *
     * @return 页码
     */
    public int getBegin() {
        int begin = this.setBeginAndEnd()[0];
        return begin;
    }

    /**
     * 结束页码
     *
     * @return 页码
     */
    public int getEnd() {
        int end = this.setBeginAndEnd()[1];
        return end;
    }

    /**
     * 头尾相关性处理
     *
     * @return 头尾数组
     */
    private int[] setBeginAndEnd() {
        int totalPage = this.getTotalPage();
        int[] scope = new int[2];
        if (totalPage <= ten) {
            scope[0] = 1;
            scope[1] = this.getTotalPage();
            return scope;
        } else {
            //处理头尾溢出
            int begin = this.currentPage - 5;
            int end = this.currentPage + 4;
            if (begin < 1) {
                begin = 1;
                end = 10;
            }
            if (end > this.getTotalPage()) {
                begin = this.getTotalPage() - 9;
                end = this.getTotalPage();
            }
            scope[0] = begin;
            scope[1] = end;
            return scope;
        }
    }

}
