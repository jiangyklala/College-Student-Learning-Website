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
  props: ['queryStr', 'userID', 'historyID', 'isStatic'],
  components: {
    FireTwoTone,
    mavonEditor,
  },
  setup: function (props: any, {emit}) {

    const mavonEditorRef = ref();

    const contentMD = ref("");
    const contentPlain = ref("");
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
      // console.log(resJson);

      if (resJson.end === true) {
        // console.log("message----end");
        // console.log(resJson.message);
        emit('update:historyID', resJson.message);
        eventSource.value.close();
        removeListen();
      } else {
        contentPlain.value += resJson.message.replace(/\n/g, '<br>');
        contentMD.value = mavonEditorRef.value.render(contentPlain.value.replace(/<br>/g, '\n'));
      }

    };

    const errorEventListener = (res : any) => {
      eventSource.value.close();
      message.error("对话内容过长(max 4096 tokens)或接口超时, 请开启一个新对话, 或者联系我呦", 5);
      console.log("error!!!");

    }

    const removeListen = () => {
      eventSource.value.removeEventListener('open', openEventListener);
      eventSource.value.removeEventListener('message', messageEventListener);
      eventSource.value.removeEventListener('error', errorEventListener);
    }

    const initListen = () => {
      eventSource.value = new EventSource(process.env.VUE_APP_LOCAL_GPT_TEST + "/gpt/completions/stream/" + props.userID + "&" + props.historyID + "&" + props.queryStr);

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