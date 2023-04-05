<template>
  <a-layout-content class="layout-content">

<!--    历史记录--抽屉-->
    <a-button type="primary" @click="showDrawer" class="drawer-button">历<br>史<br>记<br>录</a-button>
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
      <left-chat-item v-if="msg.type === 1" :content="msg.content" ></left-chat-item>
      <right-chat-item v-if="msg.type === 2" :content="msg.content"></right-chat-item>
    </div>



    <a-input-search
        class="input-search"
        v-model:value="gptQuestion"
        placeholder="问点啥呗 QAQ"
        :loading="searchLoading"
        enter-button
        @search="onSearch"
    />

<!--    <a-spin-->
<!--        class="spin"-->
<!--        tip="加载过程比较慢, 请耐心等待..."-->
<!--        :spinning="spinning">-->
<!--    </a-spin>-->



  </a-layout-content>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
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

    const gptQuestion = ref("");
    const insertHtml2 = ref();
    insertHtml2.value = "";
    const searchLoading = ref(false);   // 搜索框 loading
    // const spinning = ref(false);             // 加载中 loading

    const mavonEditorRef = ref();

    const chatCplQueryReq = ref();
    chatCplQueryReq.value = {
      userID: 1,
      historyID: -1,
      queryStr: ""
    };


    onMounted(() => {
      mavonEditorRef.value = mavonEditor.markdownIt;
    });

    const onSearch = () => {
      searchLoading.value = true;
      msglist.value.push({           // 显示 [human] 对话
        type: 2,
        content: gptQuestion.value,
      });
      console.log(msglist);
      chatCplQueryReq.value.queryStr = gptQuestion.value.replaceAll('"', "\"");
      axios.post("/gpt/chatCompletion2", chatCplQueryReq.value).then((response) => {
        searchLoading.value = false;

        if (response.data.success) {
          let resp = response.data.content;
          msglist.value.push({      // 显示 [robot] 对话
            type: 1,
            content: resp.content,
          })
          // insertHtml2.value = insertHtml2.value + "<br><br>" + mavonEditorRef.value.render(resp.content);
          chatCplQueryReq.value.historyID = resp.historyID;
          selectHistoryList();     // 刷新历史记录

        } else {
          message.error(response.data.message);
        }
      })
    }

    //-----------------抽屉------------------
    const drawerVisible = ref<boolean>(false);
    const historyList = ref();
    historyList.value = [];

    const afterVisibleChange = (bool: boolean) => {
      // console.log('drawerVisible', bool);
    };

    const showDrawer = () => {
      drawerVisible.value = true;
    };

    const selectHistoryList = () => {
      axios.get("/gpt/selectAll").then((response) => {
        // loading.value = false;
        if (response.data.success) {  // 判断后端接口返回是否出错
          historyList.value = response.data.content;
          // console.log(historyList);
        } else {
          message.error(response.data.message);
        }
      })
    }

    const historyItemClick = (historyID : number) => {
      // chatCplQueryReq.value.userID = computed(() => {
      //   return store.state.userInfo;
      // }).value.id;
      msglist.value = [];
      chatCplQueryReq.value.historyID = historyID;
      drawerVisible.value = false;
      // console.log("chatCplQueryReq:");
      // console.log(chatCplQueryReq);
      axios.get("/gpt/selectContentByID/" + historyID).then((response) => {
        if (response.data.success) {  // 判断后端接口返回是否出错
          extractAndShowChat(response.data.content);
        } else {
          message.error(response.data.message);
        }
      })

    }

    const extractAndShowChat = (content : any) => {
      content = "[" + content + "{}]";
      content = JSON.parse(content);// JSON.stringify(content);
      console.log(content);
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

    //-----------------对话显示------------------

    const msglist = ref();
    msglist.value = [];


    onMounted(() => {
      selectHistoryList();
    })


    return {
      gptQuestion,
      insertHtml2,
      searchLoading,
      onSearch,
      // spinning,
      drawerVisible,
      afterVisibleChange,
      showDrawer,
      historyList,
      historyItemClick,
      msglist,
    };
  },
});
</script>

<style scoped>

.drawer-button {
  position: fixed;
  width: 40px;
  height: 100px;
  left: 5px;
}

.input-search {
  position: fixed;
  width: 60%;
  padding-left: 20%;
  bottom: 5%;
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
