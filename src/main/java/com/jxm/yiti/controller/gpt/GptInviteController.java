package com.jxm.yiti.controller.gpt;

import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.GptInviteCodeResp;
import com.jxm.yiti.resp.GptInviteeResp;
import com.jxm.yiti.resp.GptInviterResp;
import com.jxm.yiti.service.GptInviteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/invite")
public class GptInviteController {

    @Resource
    GptInviteService gptInviteService;


    /**
     * 查询某个邀请人的全部信息
     * @param userID 用户 ID
     * @return
     */
    @GetMapping("/selectInfoByID/{userID}")
    @ResponseBody
    public CommonResp selectInfoByID(@PathVariable Long userID) {
        CommonResp<GptInviterResp> resp = new CommonResp<>();

        gptInviteService.selectInfoByID(userID, resp);

        return resp;
    }

    /**
     * 查询某个用户下的所有邀请码
     * @param userID 用户 ID
     * @return
     */
    @GetMapping("/selectAllInviteCode/{userID}")
    @ResponseBody
    public CommonResp selectAllInviteCode(@PathVariable Long userID) {
        CommonResp<List<GptInviteCodeResp>> resp = new CommonResp<>();

        gptInviteService.selectAllInviteCode(userID, resp);

        return resp;
    }

    /**
     * 查询某个用户下的所有邀请码
     * @param userID 用户 ID
     * @return
     */
    @GetMapping("/selectAllInviteInfo/{userID}")
    @ResponseBody
    public CommonResp selectAllInviteInfo(@PathVariable Long userID) {
        CommonResp<List<GptInviteeResp>> resp = new CommonResp<>();

        gptInviteService.selectAllInviteInfo(userID, resp);

        return resp;
    }

    /**
     * 添加一个邀请人
     * @param userID 用户 ID
     * @return
     */
    @GetMapping("/addInviter/{userID}")
    @ResponseBody
    public CommonResp addInviter(@PathVariable Long userID) {
        CommonResp resp = new CommonResp();

        gptInviteService.addInviter(userID, resp);

        return resp;
    }

    /**
     * 生成邀请码
     * @param userID 用户 ID
     * @return
     */
    @GetMapping("/getInviteCode/{userID}")
    @ResponseBody
    public CommonResp<String> getInviteCode(@PathVariable Long userID) {
        CommonResp<String> resp = new CommonResp<>();

        gptInviteService.generateInviteCode(userID, resp);

        return resp;
    }
}
