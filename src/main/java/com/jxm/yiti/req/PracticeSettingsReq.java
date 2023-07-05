package com.jxm.yiti.req;

import com.jxm.yiti.enums.ProblemCount;
import com.jxm.yiti.enums.ProblemLevel;
import com.jxm.yiti.enums.ProblemSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeSettingsReq {
    Long userId;
    Integer categoryId2;
    ProblemCount problemCount;
    ProblemSource problemSource;
    ProblemLevel problemLevel;
}
