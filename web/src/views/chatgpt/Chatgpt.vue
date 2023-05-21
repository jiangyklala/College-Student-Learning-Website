<template>
  <a-layout-content class="layout-content">

    <!--    历史记录--抽屉-->
    <a-button type="primary" @click="showDrawer" class="drawer-button">历<br>史<br>记<br>录</a-button>
    <a-button type="primary" @click="newChat" class="new-chat-button">新<br>对<br>话</a-button>
<!--    <a-button type="primary" @click="showAttention" class="show-attention-button">提<br>问<br>消<br>耗<br>规<br>则-->
<!--    </a-button>-->
    <a-button type="primary" @click="showAddCredit" class="show-add-credit-button">充<br>值<br></a-button>
    <a-button type="primary" @click="showChatGroup" class="show-chat-group-button">交<br>流<br>群</a-button>
    <a-button type="primary" @click="showHelp" class="show-help-button">联<br>系<br>客<br>服</a-button>

    <a-drawer
        v-model:visible="drawerVisible"
        class="drawer"
        style="color: red"
        title="历史记录"
        placement="left"
        @after-visible-change="afterVisibleChange"
    >
      <span v-for="item in historyList" v-bind:key="item">
        <a style="font-size: 18px; padding-left: 13px" @click="historyItemClick(item.id)">--> {{ item.title }}</a><br>
      </span>
    </a-drawer>

    <div v-for="msg in msglist" :key="msg" ref="msgDiv">
      <left-chat-item v-if="msg.type === 1"
                      :userID="msg.userID"
                      :historyID="msg.historyID"
                      :queryStr="msg.queryStr"
                      :totalTokens="msg.totalTokens"
                      :userType="msg.userType"
                      :isStatic="msg.isStatic"
                      v-on:update:historyID="updateHistoryID"
      ></left-chat-item>
      <right-chat-item v-if="msg.type === 2" :content="msg.content"></right-chat-item>
    </div>


    <a-textarea v-model:value="gptQuestion"
                placeholder="问点啥呗 QAQ"
                enter-button
                class="input-search"
                :autoSize="{ minRows: 2, maxRows: 6 }"/>
    <a-button type="primary"
              shape="circle"
              size="large"
              :disabled="searchLoading"
              class="search-icon-btn"
              @click="onSearch(gptQuestion)">
      <template #icon>
        <rocket-two-tone class="search-icon"/>
      </template>
    </a-button>

    <!--    <a-input-search-->
    <!--        class="input-search"-->
    <!--        v-model:value="gptQuestion"-->
    <!--        placeholder="问点啥呗 QAQ"-->
    <!--        :loading="searchLoading"-->
    <!--        @search="onSearch"-->
    <!--    />-->

    <!--    <a-spin-->
    <!--        class="spin"-->
    <!--        tip="加载过程比较慢, 请耐心等待..."-->
    <!--        :spinning="spinning">-->
    <!--    </a-spin>-->
   <div style="height: 30px"></div>

  </a-layout-content>
</template>


<script lang="ts">
import {computed, defineComponent, h, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {message, Modal} from "ant-design-vue";
import mavonEditor from "mavon-editor";
import {Tool} from "@/utils/tool";
import store from "@/store";
import LeftChatItem from "@/components/left-chat-item.vue";
import RightChatItem from "@/components/right-chat-item.vue";
import router from "@/router";

// import axios from 'axios';


export default defineComponent({
  components: {
    RightChatItem,
    LeftChatItem
  },
  name: 'Chatgpt',
  setup() {

    const userInfo = computed(() => {
      return store.state.userInfo;
    });

    const gptQuestion = ref("");        // 用户提问的问题
    const searchLoading = ref(false);   // 搜索框 loading
    const mavonEditorRef = ref();             // mavonEditor
    // const chatCost = 1;
    const historyID = ref(-1);

    const chatCplQueryReq = ref();            // 查询 gpt 参数
    chatCplQueryReq.value = {
      userID: userInfo.value.id,
      historyID: -1,
      queryStr: ""
    };

    const newChat = () => {
      // msglist.value = [];
      // historyID.value = -1;
      location.reload();
    }

    /**
     * 监听子组件返回的 historyID
     * @param newHistoryID
     */
    const updateHistoryID = (newHistoryID: any) => {
      historyID.value = newHistoryID;
      searchLoading.value = false;

      // 刷新历史记录列表
      selectHistoryList();
      // console.log("new historyID!!: " + historyID.value);
      // TODO
    }

    /**
     * 提问按钮
     * @param searchStr 用户输入的问题
     */
    const onSearch = (searchStr: any) => {
      // 检测内容是否为空
      if (Tool.isEmpty(searchStr)) {
        message.info("输入不能为空呦");
        return;
      }

      // 检测是否登录
      if (Tool.isEmpty(userInfo.value)) {
        message.warn("需要先登录才能用呦~~~");
        return;
      }

      // 先显示 [human] 对话
      searchLoading.value = true;
      msglist.value.push({
        type: 2,
        content: searchStr,
      });

      // 清空问题输入框
      gptQuestion.value = "";

      // 对问题进行编码
      searchStr = btoa(encodeURIComponent(searchStr));

      // console.log("encodeURIComponent = ", searchStr);

      // 先进行权限验证
      axios.post(process.env.VUE_APP_LOCAL_GPT_TEST + "/gpt/payForAns", {
        userID: userInfo.value.id,
        historyID: historyID.value,
        queryStr: searchStr,
      }).then((response) => {
        if (response.data.success) {
          // 认证通过, 进行提问逻辑 (再显示 [bot] 对话)
          msglist.value.push({
            type: 1,
            queryStr: searchStr,
            userID: userInfo.value.id,
            historyID: historyID.value,
            totalTokens: response.data.content.finalToken,
            userType: response.data.content.userType,
            isStatic: false,
          })
        } else {
          searchLoading.value = false;
          message.error(response.data.message);
        }
      })
    }

    //-----------------抽屉------------------
    const drawerVisible = ref<boolean>(false);
    const historyList = ref();
    historyList.value = [];

    /**
     *
     */
    const afterVisibleChange = (bool: boolean) => {
      // console.log('drawerVisible', bool);
    };

    /**
     * 显示 [抽屉]
     */
    const showDrawer = () => {
      drawerVisible.value = true;
    };

    /**
     * 查询所有对话记录
     */
    const selectHistoryList = () => {
      if (Tool.isNotEmpty(userInfo.value)) {
        axios.get("/gpt/selectAllByID/" + userInfo.value.id).then((response) => {
          // loading.value = false;
          if (response.data.success) {  // 判断后端接口返回是否出错
            historyList.value = response.data.content;
            // console.log(historyList);
          } else {
            message.error(response.data.message);
          }
        })
      }
    }

    /**
     * 点击某个对话, 显示这个对话的内容
     * @param thisHistoryID
     */
    const historyItemClick = (thisHistoryID: number) => {

      msglist.value = [];                 // 清空显示的对话内容
      historyID.value = thisHistoryID;    // 重新复制 historyID
      drawerVisible.value = false;        // 关闭抽屉显示
      // console.log("chatCplQueryReq:" + chatCplQueryReq);
      axios.get("/gpt/selectContentByID/" + thisHistoryID).then((response) => {
        if (response.data.success) {
          extractAndShowChat2(response.data.content);
        } else {
          message.error(response.data.message);
        }
      })

      // console.log("点击了这个对话, historyID = " + historyID.value);
      scrollToBottom();

    }


    //-----------------对话显示------------------

    const msglist = ref();
    msglist.value = [];

    /**
     * 提取并显示对话 2.0
     * @param content
     */
    const extractAndShowChat2 = (content: any) => {
      content = JSON.parse(content);// JSON.stringify(content);
      // console.log(content);
      for (let i = 0; i < content.length; ++i) {
        if (content[i].userType === 0) {
          msglist.value.push({
            type: 2,
            content: content[i].message,
          })
        } else {
          msglist.value.push({
            type: 1,
            queryStr: content[i].message,
            isStatic: true,
          })
        }
      }
    }

    /**
     * 提取并显示对话 1.0
     * @param content
     */
    const extractAndShowChat1 = (content: any) => {
      content = "[" + content + "{}]";
      content = JSON.parse(content);// JSON.stringify(content);
      // console.log(content);
      for (let i = 0; i < content.length - 1; ++i) {
        if (content[i].role === "user") {
          msglist.value.push({
            type: 2,
            content: content[i].content,
          })
        } else {
          msglist.value.push({
            type: 1,
            content: content[i].content,
          })
        }
      }
    }

    //-----------------其它------------------

    /**
     * "注意" 对话框显示
     */
    const showAttention = () => {
      Modal.info({
        title: '提问次数消耗计算规则',
        content: h('div', {}, [
          h('p', '由于本网站具体连续对话功能 + 画图功能，这些功能的 token 计算比较复杂，这里只给出本网站简单的「提问次数消耗规则」：'),
          h('p', '1、每次普通提问，会消耗 1 次提问。'),
          h('p', '2、具备上下文的语境的连续提问，会消耗 1+ 次，最多消耗 8 次，具体计算规则如下：'),
          h('p', 'GPT具备上下文语境，比如点击开启一次新对话：'),
          h('p', '第一次提问，会消耗 1 次提问次数，第二次继续提问，会累加你上一次提问消耗的 token。如果 token 太大，那么会消耗 1+ 次，最大消耗 8 次，但如果你连续对话太多次，那么提问有可能失败（超过官方最大限制）。'),
          h('p', '如果你并不需要上下文语境，那么可以点击「新对话」，那么此时就开启了一个新的上下文语境。'),
          h('p', 'ps: 如果内容输出突然中断, 请发送「继续」'),
        ]),
        width: 710,
        okText: '了然'
      });
    }

    /**
     * 显示充值页面
     */
    const showAddCredit = () => {
        router.push({
          name: 'Pay',
        });

      // Modal.info({
      //   title: 'GPT 在线网站提问次数购买',
      //   content: h('div', {}, [
      //     h('p', '目前仅提供三种额度购买: '),
      //     h('p', [
      //       h('strong', '月会员'), '：35元，一个月无限次提问'
      //     ]),
      //     h('p', [
      //       h('strong', '季度会员'), '：70元，三个月无限次提问'
      //     ]),
      //     h('p', [
      //       h('strong', '年度会员'), '：200元，一年无限次提问'
      //     ]),
      //     h('h4', '如何购买?'),
      //     h('p', '具体可以扫下面帅地的微信小店购买对应的面额，购买成功后联系帅地即可。'),
      //     h('img', {src: 'https://xiaoj-1309630359.cos.ap-nanjing.myqcloud.com/202304221452920.jpg'})
      //   ]),
      //   width: 610,
      //   okText: '了然'
      // });
    }

    /**
     * 显示充值页面
     */
    const showChatGroup = () => {
      Modal.info({
        title: 'GPT 交流群',
        content: h('div', {}, [
          h('img', {
            src: 'https://shuaidi-picture-1257337429.cos.ap-guangzhou.myqcloud.com/temp/GPT.jpeg',
            style: { maxWidth: '380px', width: '100%', height: 'auto'}
          })
        ]),
        width: 510,
        okText: '了然'
      });
    }

    const showHelp = () => {
      Modal.info({
        title: '联系客服',
        content: h('div', {}, [
          h('p', '如果有任何问题，都可以联系帅地进行反馈'),
          h('p', [
            h('strong', '邮箱'), '：1326194964@qq.com'
          ]),
          h('p', [
            h('strong', '微信'), '：iamshuaidi0'
          ]),
          h('p', '如果是加微信，加的时候请备注来意'),
        ]),
        width: 510,
        okText: '了然'
      });
    }

    /**
     * 对特殊字符进行转义
     * @param str
     */
    function escapeSpecialChars(str: string): string {
      return str.replace(/["'\\\n\r\t\v\s]/g, (match) => {
        switch (match) {
          case '"':
            return '\\"';
          case '\'':
            return '\\\'';
          case '\\':
            return '\\\\';
          case '\n':
            return '\\n';
          case '\r':
            return '\\r';
          case '\t':
            return '\\t';
          case '\v':
            return '\\v';
          case ' ':
            return '\\s';
          default:
            return match;
        }
      });
    }

    // 监听 count 的变化
    watch(userInfo, (newValue: number, oldValue: number) => {
      // console.log(`count 变化了：${oldValue} => ${newValue}`);
      selectHistoryList();
    });

    const msgDiv = ref();

    const scrollToTop = () => {
      window.scrollTo({top: 0, behavior: "smooth"});
    }

    const scrollToBottom = () => {
      setTimeout(() => {
        window.scrollTo({top: document.body.scrollHeight, behavior: "smooth"});
      }, 500);
    }

    onMounted(() => {
      mavonEditorRef.value = mavonEditor.markdownIt;
      selectHistoryList();
      scrollToTop();
    })


    return {
      gptQuestion,
      searchLoading,
      drawerVisible,
      historyList,
      msglist,

      onSearch,
      afterVisibleChange,
      showDrawer,
      historyItemClick,
      newChat,
      updateHistoryID,
      showAttention,
      showAddCredit,
      showChatGroup,
      showHelp,
    };
  },
});
</script>

<style scoped>

@media (max-width: 768px) {

  .layout-content {
    width: 87% !important;
    margin-right: 5px !important;
  }

  .new-chat-button {
    transform: scale(0.7);
    position: fixed;
    width: 43px;
    height: 80px;
    left: 0px !important;

    /*padding-top: 100px;*/
    top: 170px !important;
  }

  .drawer-button {
    transform: scale(0.7);
    left: 0px !important;
  }

  .show-attention-button {
    transform: scale(0.7);
    left: 0px !important;
    top: 235px !important;
  }

  .show-add-credit-button {
    transform: scale(0.7);
    left: 0px !important;
    top: 255px !important;
  }

  .show-chat-group-button {
    transform: scale(0.7);
    left: 0px !important;
    top: 315px !important;
  }

  .show-help-button {
    transform: scale(0.7);
    left: 0px !important;
    top: 385px !important;
  }

  .input-search {
    position: fixed;
    width: 75% !important;
    left: 11.5% !important;
    bottom: 5%;
  }

  .search-icon-btn {
    position: fixed;
    left: 87% !important;
    bottom: 6%;
  }
}

.new-chat-button {
  position: fixed;
  width: 43px;
  height: 80px;
  left: 10px;
  /*padding-top: 100px;*/
  top: 200px;
}

.drawer-button {
  position: fixed;
  width: 43px;
  height: 100px;
  left: 10px;
}

.show-attention-button {
  position: fixed;
  width: 43px;
  height: 150px;
  left: 10px;
  top: 305px;
}

.show-add-credit-button {
  position: fixed;
  width: 43px;
  height: 60px;
  left: 10px;
  top: 310px;
}

.show-chat-group-button {
  position: fixed;
  width: 43px;
  height: 80px;
  left: 10px;
  top: 400px;
}

.show-help-button {
  position: fixed;
  width: 43px;
  height: 100px;
  left: 10px;
  top: 510px;
}

.input-search {
  position: fixed;
  width: 45%;
  left: 26%;
  bottom: 5%;
}

.search-icon-btn {
  position: fixed;
  left: 72%;
  bottom: 6%;
}

.search-icon {
  font-size: 35px !important;
}

.spin {
  padding-left: 40%;
  padding-top: 10%;
}

.showDiv {
  white-space: pre-line;
  border: #87d068;
  padding-top: 10%;
  width: 90%;
  padding-left: 10%;
  height: 90%;
  min-height: 500px;
}

.layout-content {
  padding-top: 60px;
  /*padding-bottom: 30vh;*/
  width: 85%;
  height: 110%;
  min-height: 91vh;
  margin: 10px auto;
  overflow: hidden;
  background: #fff;
}


</style>
