<template>
  <a-layout-content class="layout-content">
    <a-space direction="vertical" style="width: 100%" size="large">
      <a-card style="width: 100%">
        <a-col :span="12">
          <a-statistic title="我的邀请余额&nbsp;(CNY)" :precision="2" :value="inviterInfo.inviteBalance"
                       :value-style="{fontSize: '50px'}"/>
        </a-col>
        <br>
        <a-button shape="round" type="primary" size="middle" @click="withdrawalsClick">提现</a-button>
      </a-card>
      <a-card :body-style="{ display: 'flex', flexDirection: 'column' }" style="width: 100%">
        <div>
          <p style="float: left; color: rgb(156, 160, 164)">已邀请人数</p>
          <p style="float: right; color: rgb(156, 160, 164)">{{inviterInfo.invitedCount}}</p>
        </div>
        <div>
          <p style="float: left; color: rgb(156, 160, 164)">佣金比例</p>
          <p style="float: right; color: rgb(156, 160, 164)">{{inviterInfo.earnRate}}%</p>
        </div>
        <div>
          <p style="float: left; color: rgb(156, 160, 164); margin: 0">累计获得佣金&nbsp;</p>
          <p style="float: right; color: rgb(156, 160, 164); margin: 0">￥{{inviterInfo.earnings}}</p>
        </div>
      </a-card>
      <a-card :body-style="{ display: 'flex', flexDirection: 'column' }" style="width: 100%">
        <div>
          <p style="float: left; color: rgb(156, 160, 164); font-size: 30px">
            邀请码管理
          </p>
          <a-tooltip>
            <template #title>点击下面的 "复制链接" , 让朋友通过此链接注册即可</template>
            <bell-two-tone style="padding-top: 12px; padding-left: 6px; font-size: 20px"/>
          </a-tooltip>
          <p style="float: right; color: rgb(156, 160, 164)">
            <a-button shape="round" type="primary" size="middle" @click="getInviteCode" :disabled="generateInviteCodeBtnDisable">生成邀请码</a-button>
          </p>
        </div>
        <a-table :columns="inviteCodeColumns"
                 :data-source="inviteCodeList"
                 style="width: 100%"
                 :pagination="false"
                 :scroll="{ x: 0 , y: 250 }">
          <template #bodyCell="{ column, text }">
            <template v-if="column.dataIndex === 'inviteCode'">
              {{ text }}&nbsp;&nbsp;<a style="color: rgb(156, 160, 164)" @click="copyToClipboard(text)">复制链接</a>
            </template>
          </template>
        </a-table>
      </a-card>
      <a-card :body-style="{ display: 'flex', flexDirection: 'column' }" style="width: 100%">
        <div>
          <p style="float: left; color: rgb(156, 160, 164); font-size: 30px">
            邀请记录
          </p>
        </div>
        <a-table :columns="inviteInfoColumns"
                 :data-source="inviteInfoList"
                 style="width: 100%"
                 :pagination="false"
                 :scroll="{ x: 0 , y: 400 }">
          <template #bodyCell="{ column, text }">

          </template>
        </a-table>
      </a-card>

    </a-space>

  </a-layout-content>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from "vue";
import {message} from "ant-design-vue";
import store from "@/store";
import axios from "axios";

export default defineComponent({
  name: "Invite",
  setup() {

    const inviteCodeColumns = [
      {
        title: '邀请码',
        dataIndex: 'inviteCode',
        fixed: 'left',
        width: '75%',
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        fixed: 'right',
        width: '25%',
      }
    ];

    const inviteInfoColumns = [
      {
        title: '邀请信息',
        dataIndex: 'kind',
        fixed: 'left',
        width: '45%',
      },
      {
        title: '收益',
        dataIndex: 'count',
        fixed: 'right',
        width: '30%',
      },
      {
        title: '时间',
        dataIndex: 'createTime',
        fixed: 'right',
        width: '25%',
      },
    ];

    const userID = computed(() => {
      return store.state.userInfo.id;
    });

    const inviterInfo = ref();
    inviterInfo.value = [];
    const inviteCodeList = ref();
    inviteCodeList.value = [];
    const inviteInfoList = ref();
    inviteInfoList.value = [];
    const generateInviteCodeBtnDisable = ref(false);

    const withdrawalsClick = () => {
      message.info("功能开发中, 敬请期待!")
    }

    const copyToClipboard = (code : any) => {
      navigator.clipboard.writeText(process.env.VUE_APP_WEB + "/chatgpt/Chatgpt?register=" + code).then(() => {
        message.success('内容已复制到剪贴板');
      }, () => {
        console.error('复制失败');
      });
    };

    /**
     * 生成邀请码
     */
    const getInviteCode = () => {
      generateInviteCodeBtnDisable.value = true;
      axios.get("/invite/getInviteCode/" + userID.value).then((response) => {
        generateInviteCodeBtnDisable.value = false;
        let data = response.data;
        if (data.success) {
          console.log(data);
          allInviteCodeQuery();
        } else {
          message.error(data.message);
        }
      })
    }

    const inviterInfoQuery = () => {
      axios.get("/invite/selectInfoByID/" + userID.value).then((response) => {
        let data = response.data;
        if (data.success) {
          console.log(data);
          inviterInfo.value = data.content;
        } else {
          message.error(data.message);
        }
      })
    }

    const selectAllInviteInfo = () => {
      axios.get("/invite/selectAllInviteInfo/" + userID.value).then((response) => {
        let data = response.data;
        if (data.success) {
          // console.log(data);
          inviteInfoList.value = data.content;

          for (let i = 0; i < inviteInfoList.value.length; ++i) {
            const singleInfo = inviteInfoList.value[i];
            singleInfo.createTime = getMyDate(singleInfo.createTime);
            if (singleInfo.kind === 0) {
              singleInfo.kind = singleInfo.inviterName + " 注册"
              singleInfo.count = "获得 " + singleInfo.count  + " 提问次数";
            } else {
              singleInfo.kind = singleInfo.inviterName + " 充值"
              singleInfo.count = "获得 " + singleInfo.count  + " 元佣金";
            }
          }

          console.log(inviteInfoList.value);
        } else {
          message.error(data.message);
        }
      })
    }

    const allInviteCodeQuery = () => {
      axios.get("/invite/selectAllInviteCode/" + userID.value).then((response) => {
        let data = response.data;
        if (data.success) {
          console.log(data);
          console.log("data");
          inviteCodeList.value = data.content;

          for (let i = 0; i < inviteCodeList.value.length; ++i) {
            const singleCode = inviteCodeList.value[i];
            singleCode.createTime = getMyDate(singleCode.createTime);
          }
        } else {
          message.error(data.message);
        }
      })
    }

    const getMyDate = (date : any) => {
      const dateNew = new Date(date);
      const year = dateNew.getFullYear();
      const month = ('0' + (dateNew.getMonth() + 1)).slice(-2);
      const day = ('0' + dateNew.getDate()).slice(-2);

      // console.log(`${year}/${month}/${day}`);

      return `${year} 年 ${month} 月 ${day} 日`;
    }

    onMounted(() => {
      console.log("lala");
      inviterInfoQuery();
      allInviteCodeQuery();
      selectAllInviteInfo();
    })

    return {
      inviteCodeColumns,
      inviteInfoColumns,
      inviterInfo,
      inviteCodeList,
      inviteInfoList,
      generateInviteCodeBtnDisable,
      withdrawalsClick,
      getInviteCode,
      getMyDate,
      copyToClipboard,
    };
  }
})
</script>

<style scoped>
.layout-content {
  padding-top: 60px;
  width: 80%;
  height: 100%;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(237, 239, 242);
}

.show-invite-span {
  display: block;
  width: 200px;
  height: 300px;
  background: white;
}
</style>