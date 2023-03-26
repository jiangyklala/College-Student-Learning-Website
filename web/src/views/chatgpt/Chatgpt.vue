<template>
  <a-layout-content class="layout-content">

    <a-input-search
        class="input-search"
        v-model:value="gptQuestion"
        placeholder="问点啥呗 QAQ"
        :loading="searchLoading"
        enter-button
        @search="onSearch"
    />
    <a-spin
        class="spin"
        tip="加载过程比较慢, 请耐心等待..."
        :spinning="spinning">
    </a-spin>

    <div :innerHTML="insertHtml2"
         id="editor-content-view"
         class="editor-content-view"></div>

  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import mavonEditor from "mavon-editor";

// import axios from 'axios';


export default defineComponent({
  components: {
  },
  name: 'Chatgpt',
  setup() {

    const gptQuestion = ref();
    const insertHtml2 = ref();
    insertHtml2.value = "";
    const searchLoading = ref(false);
    const spinning = ref(false);

    const mavonEditorRef = ref();


    onMounted(() => {
      mavonEditorRef.value = mavonEditor.markdownIt;
    });

    const onSearch = () => {
      searchLoading.value = true;
      spinning.value = true;
      axios.post("/gpt/getData2/" + gptQuestion.value).then((response) => {
        // console.log(response);
        searchLoading.value = false;
        spinning.value = false;

        if (response.data.success) {
          insertHtml2.value = mavonEditorRef.value.render(response.data.content);


        } else {
          message.error(response.data.message);
        }
      })
    }

    return {
      gptQuestion,
      insertHtml2,
      searchLoading,
      onSearch,
      spinning,
    };
  },
});
</script>

<style scoped>

.input-search {
  width: 80%;
  padding-left: 20%;
  padding-top: 5%;
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


.editor-content-view {
  /*border: 3px solid #ccc;*/
  /*border-radius: 5px;*/
  /*padding: 0 10px;*/
  padding-top: 6%;
  padding-left: 10%;
  padding-bottom: 4%;
  width: 90%;
  height: 90%;
  min-height: 500px;
  margin-top: 20px;
  overflow-x: auto;
  /*min-height: 1000px;*/
}

.editor-content-view p,
.editor-content-view li {
  white-space: pre-wrap; /* 保留空格 */
  font-size: 20px;
}

.editor-content-view blockquote {
  border-left: 8px solid #d0e5f2;
  padding: 10px 10px;
  margin: 10px 0;
  background-color: #f1f1f1;
}

.editor-content-view code {
  font-family: monospace;
  background-color: #eee;
  padding: 3px;
  border-radius: 3px;
}
.editor-content-view pre>code {
  display: block;
  padding: 10px;
}

.editor-content-view table {
  border-collapse: collapse;
}
.editor-content-view td,
.editor-content-view th {
  border: 1px solid #ccc;
  min-width: 50px;
  height: 20px;
}
.editor-content-view th {
  background-color: #f1f1f1;
}

.editor-content-view ul,
.editor-content-view ol {
  padding-left: 20px;
}

.editor-content-view input[type="checkbox"] {
  margin-right: 5px;
}

</style>
