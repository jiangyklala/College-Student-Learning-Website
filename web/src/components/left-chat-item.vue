<template>
  <div class="container">
    <fire-two-tone style="font-size: 35px; padding-top: 5px"/>

    <div class="content">

      <div class="text">
        <div :innerHTML="contentMD"
             id="my-editor-content-view"
             class="my-editor-content-view"></div>
<!--        <mavon-editor  v-model="mavonVModel"-->
<!--                       :subfield="false"-->
<!--                       defaultOpen="preview"-->
<!--                       :toolbarsFlag="false"-->
<!--                       :editable="false"-->
<!--        ></mavon-editor>-->

      </div>


    </div>

  </div>
</template>

<script lang="ts">

import {defineComponent, onMounted, ref} from "vue";
import {FireTwoTone} from '@ant-design/icons-vue';
import mavonEditor from "mavon-editor";
import 'mavon-editor/dist/css/index.css';


export default defineComponent({
  name: 'left-chat-item',
  props: ['content'],
  components: {
    FireTwoTone,
    mavonEditor,
  },
  setup : function (props : any) {

    const mavonEditorRef = ref();

    const contentMD = ref();


    onMounted(() => {
      mavonEditorRef.value = mavonEditor.markdownIt;
      contentMD.value = mavonEditorRef.value.render(props.content);
      // console.log(contentMD.value);
    })

    return {
      contentMD,
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
.my-editor-content-view pre>code {
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