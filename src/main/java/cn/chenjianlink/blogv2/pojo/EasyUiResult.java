package cn.chenjianlink.blogv2.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * EasyUI页面显示pojo
 *
 * @author chenjian
 */
@Getter
@Setter
@ToString
public class EasyUiResult implements Serializable {


    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 当前查询的记录数
     */
    private List<?> rows;

    public EasyUiResult(Integer total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public EasyUiResult(long total, List<?> rows) {
        this.total = (int) total;
        this.rows = rows;
    }

}
