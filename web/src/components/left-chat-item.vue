<template>
  <div class="container">
    <fire-two-tone style="font-size: 35px; padding-top: 5px"/>

    <div class="content">
      <div class="text">
        <div :innerHTML="contentMD"
             id="my-editor-content-view"
             class="my-editor-content-view"
             @click="copyToClipboard">
        </div>

      </div>
    </div>

  </div>
</template>

<script lang="ts">

import {defineComponent, onMounted, ref} from "vue";
import {FireTwoTone} from '@ant-design/icons-vue';
import mavonEditor from "mavon-editor";
import 'mavon-editor/dist/css/index.css';
import {message} from "ant-design-vue";


export default defineComponent({
  name: 'left-chat-item',
  emits: ['update:historyID'],
  props: ['queryStr', 'userID', 'historyID','totalTokens', 'isStatic', 'userType'],
  components: {
    FireTwoTone,
    mavonEditor,
  },
  setup: function (props: any, {emit}) {

    const mavonEditorRef = ref();

    const contentMD = ref("");      // 显示出来的经 markdown 渲染过的文本
    const contentPlain = ref("");   // 返回答案的原始文本
    const eventSource = ref();

    const copyToClipboard = () => {
      navigator.clipboard.writeText(contentPlain.value.replace(/<br>/g, '\n')).then(() => {
        message.success('内容已复制到剪贴板');
      }, () => {
        console.error('复制失败');
      });
    };

    const openEventListener = () => {
      console.log("open!!");
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
        // 将 \n 替换成 <br> 以正确显示换行
        contentPlain.value += resJson.message.replace(/\n/g, '<br>');

        // 进行 markdown 渲染时需要再将其转换过来
        contentMD.value = mavonEditorRef.value.render(contentPlain.value.replace(/<br>/g, '\n'));
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
      contentPlain.value =  props.queryStr;      // 粘贴板
      contentMD.value = mavonEditorRef.value.render(props.queryStr);
    }


    onMounted(() => {
      mavonEditorRef.value = mavonEditor.markdownIt;

      // 判断是 [静态消息] 还是需要监听接口 (动态)
      if (props.isStatic) {
        showMessage();
      } else {
        initListen();
      }
    })

    return {
      contentMD,
      copyToClipboard,
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

/*p {*/
/*  margin-top: 0;*/
/*  margin-bottom: 12em !important;*/
/*  */
/*}*/

.my-editor-content-view {
  padding: 0 10px;
}

.my-editor-content-view p, li {
  white-space: pre-wrap; /* 保留空格 */
  font-size: 14px !important;
  /*margin-top: 0;*/
  /*margin-bottom: 0em !important;*/
}

.my-editor-content-view blockquote {
  border-left: 8px solid #d0e5f2;
  padding: 10px 10px;
  margin: 10px 0;
  background-color: #f1f1f1;
}

.my-editor-content-view code {
  font-family: monospace;
  background-color: #eee;
  padding: 3px;
  border-radius: 3px;
}

.my-editor-content-view pre > code {
  display: block;
  padding: 10px;
}

.my-editor-content-view table {
  border-collapse: collapse;
}

.my-editor-content-view td,
.my-editor-content-view th {
  border: 1px solid #ccc;
  min-width: 50px;
  height: 20px;
}

.my-editor-content-view th {
  background-color: #f1f1f1;
}

.my-editor-content-view ul,
.my-editor-content-view ol {
  padding-left: 20px;
}

.my-editor-content-view input[type="checkbox"] {
  margin-right: 5px;
}


</style>