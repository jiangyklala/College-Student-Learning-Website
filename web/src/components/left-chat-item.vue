<template>
  <div class="container">
    <fire-two-tone style="font-size: 35px; padding-top: 5px"/>

    <div class="content">
      <div class="text">
        <v-md-editor v-model="markdownText" @copy-code-success="handleCopyCodeSuccess" mode="preview"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts">

import {defineComponent, onMounted, ref} from "vue";
import {FireTwoTone} from '@ant-design/icons-vue';
import {message} from "ant-design-vue";


export default defineComponent({
  name: 'left-chat-item',
  emits: ['update:historyID'],
  props: ['queryStr', 'userID', 'historyID','totalTokens', 'isStatic', 'userType'],
  components: {
    FireTwoTone,
  },
  setup: function (props: any, {emit}) {

    const eventSource = ref();
    const markdownText = ref("");

    const openEventListener = () => {
      // console.log("open!!");
    };

    const messageEventListener = (res: any) => {

      let resJson = JSON.parse(res.data)

      if (resJson.end === true) {
        // 向父组件提交变更后的 historyID
        emit('update:historyID', resJson.message);

        // 关闭对于接口的监听, 移除各种监听事件
        eventSource.value.close();
        removeListen();
      } else {
        markdownText.value += resJson.message;
      }
    };

    /**
     * 错误事件监听
     */
    const errorEventListener = () => {
      eventSource.value.close();
      message.error("连续对话内容过长(max 4096 tokens)或接口超时, 请开启一个新对话, 或者联系我呦", 5);
      console.log("error!!!");

    }

    /**
     * 移除各种监听事件
     */
    const removeListen = () => {
      eventSource.value.removeEventListener('open', openEventListener);
      eventSource.value.removeEventListener('message', messageEventListener);
      eventSource.value.removeEventListener('error', errorEventListener);
    }

    /**
     * 初始化监听时间
     */
    const initListen = () => {
      eventSource.value = new EventSource(process.env.VUE_APP_LOCAL_GPT_TEST +
                                              "/gpt/completions/stream/" +
                                              props.userID + "&" +
                                              props.userType + "&" +
                                              props.historyID + "&" +
                                              props.totalTokens + "&" +
                                              props.queryStr);

      eventSource.value.addEventListener('open', openEventListener);
      eventSource.value.addEventListener('message', messageEventListener);
      eventSource.value.addEventListener('error', errorEventListener);
    }

    const showMessage = () => {
      markdownText.value = props.queryStr;
    }

    const handleCopyCodeSuccess = () => {
      message.success("Copy Success!")
    }


    onMounted(() => {
      // 判断是 [静态消息] 还是需要监听接口 (动态)
      if (props.isStatic) {
        showMessage();
      } else {
        initListen();
      }
    })

    return {
      markdownText,
      handleCopyCodeSuccess,
    }

  }
});

</script>

<style scoped>
.container {
  display: flex;
  padding: 10px 15px;
  margin-right: 60px;
}

.content {
  margin-left: 10px;
  margin-top: 10px;
  max-width: 100%;
}

.text {
  background-color: deepskyblue;
  border-bottom-right-radius: 10px;
  padding: 5px 5px;
  font-size: 14px;
  color: #ffffff;
}

* {
  margin-bottom: 0 !important;
}

</style>