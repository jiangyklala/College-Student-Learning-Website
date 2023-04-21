<template>
  <a-layout-content class="layout-content">

<!--    历史记录--抽屉-->
    <a-button type="primary" @click="showDrawer" class="drawer-button">历<br>史<br>记<br>录</a-button>
    <a-button type="primary" @click="newChat" class="new-chat-button">新<br>对<br>话</a-button>
    <a-button type="primary" @click="showAttention" class="show-attention-button">注<br>意<br></a-button>

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

    <div v-for="msg in msglist" :key="msg">
      <left-chat-item v-if="msg.type === 1"
                      :userID="msg.userID"
                      :historyID="msg.historyID"
                      :queryStr="msg.queryStr"
                      :isStatic="msg.isStatic"
                      v-on:update:historyID="updateHistoryID"
      ></left-chat-item>
      <right-chat-item v-if="msg.type === 2" :content="msg.content"></right-chat-item>
    </div>



    <a-textarea v-model:value="gptQuestion"
                placeholder="问点啥呗 QAQ"
                enter-button
                class="input-search"
                :autosize="{ minRows: 2, maxRows: 6 }"/>
    <a-button type="primary"
              shape="circle"
              size="large"
              :disabled="searchLoading"
              class="search-icon-btn"
              @click="onSearch(gptQuestion)">
      <template #icon><rocket-two-tone class="search-icon" /></template>
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



  </a-layout-content>
</template>


<script lang="ts">
import {computed, defineComponent, h, onMounted, ref} from 'vue';
import axios from "axios";
import {message, Modal} from "ant-design-vue";
import mavonEditor from "mavon-editor";
import {Tool} from "@/utils/tool";
import store from "@/store";
import LeftChatItem from "@/components/left-chat-item.vue";
import RightChatItem from "@/components/right-chat-item.vue";

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
    const chatCost = 1;
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
    const updateHistoryID = (newHistoryID : any) => {
      historyID.value = newHistoryID;
      searchLoading.value = false;
      // console.log("new historyID!!: " + historyID.value);
    }

    /**
     * 查询按钮
     * @param searchStr 用户输入的问题
     */
    const onSearch = (searchStr : any) => {

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
      axios.get(process.env.VUE_APP_LOCAL_GPT_TEST + "/gpt/payForAns/" + userInfo.value.id + "/" + historyID.value).then((response) => {
        if (response.data.success) {
          // 认证通过, 进行提问逻辑 (再显示 [bot] 对话)
          msglist.value.push({
            type: 1,
            queryStr: searchStr,
            userID: userInfo.value.id,
            historyID: historyID.value,
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
    const historyItemClick = (thisHistoryID : number) => {

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

    }


    //-----------------对话显示------------------

    const msglist = ref();
    msglist.value = [];

    /**
     * 提取并显示对话 2.0
     * @param content
     */
    const extractAndShowChat2 = (content : any) => {
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
    const extractAndShowChat1 = (content : any) => {
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
          h('p', '2、具备上下文的语境的连续提问，会消耗 1+ 次，最多消耗 15 次，具体计算规则如下：'),
          h('p', 'GPT具备上下文语境，比如点击开启一次新对话：'),
          h('p', '第一次提问，会消耗 1 次提问次数，第二次继续提问，会累加你上一次提问消耗的 token。如果 token 太大，那么会消耗 1+ 次，最大消耗 20 次，但如果你连续对话 1000 次，那么会提问失败 (超过单次连续对所限制的 4096 token)。'),
          h('p', '如果你并不需要上下文语境，那么可以点击「新对话」，那么此时就开启了一个新的上下文语境。'),
        ]),
        width: 710,
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
          case '"': return '\\"';
          case '\'': return '\\\'';
          case '\\': return '\\\\';
          case '\n': return '\\n';
          case '\r': return '\\r';
          case '\t': return '\\t';
          case '\v': return '\\v';
          case ' ': return '\\s';
          default: return match;
        }
      });
    }

    onMounted(() => {
      mavonEditorRef.value = mavonEditor.markdownIt;
      selectHistoryList();
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
    };
  },
});
</script>

<style scoped>

.new-chat-button {
  position: fixed;
  width: 40px;
  height: 80px;
  left: 5px;
  /*padding-top: 100px;*/
  top: 200px;
}

.drawer-button {
  position: fixed;
  width: 40px;
  height: 100px;
  left: 5px;
}

.show-attention-button {
  position: fixed;
  width: 40px;
  height: 80px;
  left: 5px;
  /*padding-top: 100px;*/
  top: 305px;
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
  width: 85%;
  height: 100%;
  min-height: 1100px;
  margin: 10px auto 100px;
  overflow: hidden;
  background: #fff;
}



</style>
