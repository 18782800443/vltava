package com.dmall.vltava.domain.git;

import lombok.Data;

/**
 * @author Rob
 * @date Create in 1:12 PM 2019/11/21
 */
@Data
public class GitLineVO {
    private Boolean origin;
    private Boolean newAdd;
    private Boolean newDelete;
    private Boolean locate;
    private String code;
    private Integer newLineNum;
    private Integer oldLineNum;


}
