<template>
  <a-layout-content class="layout-content" id="layout-content">
    <a-row>
      <a-col :span="4">
        <div class="column-name-div"> {{ columnItemName }} </div>
        <a-tree
            class="category-tree"
            :tree-data="categoryTreeData"
            :default-expand-all="true"
            :fieldNames="{title: 'name', key:'id'}"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            @select="docContentByDocIdQuery"
        />
      </a-col>
      <a-col :span="20">
        <div :innerHTML="insertHtml"
             id="editor-content-view"
             class="editor-content-view"></div>
      </a-col>
    </a-row>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";
import mavonEditor from "mavon-editor";


export default defineComponent({
  components: {

  },
  name: "Doc",
  setup() {
    const columnItemId = ref();

    const categoryTreeData = ref(
    );

    //-------------目录-----------------

    const columnItemName = ref();

    /**
     * 根据专栏 id 查询其所包含的文档
     */
    const docByColumnIdQuery = (columnId : any) => {
      axios.get("/doc/selectByColumnId/" + columnId).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          categoryTreeData.value = Tool.array2Tree(response.data.content, 0);
          deleteParent(categoryTreeData.value);
          console.log(categoryTreeData.value);

        } else {
          message.error(response.data.message);
        }
      });
    }

    const deleteParent = (treeSelectData : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        delete treeSelectData[i].parent;
        if (Tool.isNotEmpty(treeSelectData[i].children)) {
          deleteParent(treeSelectData[i].children);
        }
      }
      // console.log(treeSelectData);
    }

    const insertHtml = ref();
    const mavonEditorRef = ref();


    /**
     * 根据文档 id 查询对应的文档内容
     */
    const docContentByDocIdQuery = (DocId : any) => {
      // console.log(DocId);
      axios.get("/doc/selectContentById/" + DocId).then((response) => {

        if (response.data.success) {
          // editorRef.value.txt.html(response.data.content);
          insertHtml.value = mavonEditorRef.value.render(response.data.content);
        } else {
          message.error(response.data.message);
        }
      });
    }

    onMounted(() => {
      columnItemId.value = sessionStorage.getItem("ColumnItemId");
      mavonEditorRef.value = mavonEditor.markdownIt;
      columnItemName.value = sessionStorage.getItem("ColumnItemName");
      docByColumnIdQuery(columnItemId.value);
    });

    return {
      categoryTreeData,
      insertHtml,
      docContentByDocIdQuery,
      columnItemName,
    };
  }
})
</script>

<style>

.layout-content {
  padding-top: 70px;
  width: 1400px;
  height: 100%;
  min-height: 1100px;
  margin: 10px auto 100px;
  overflow: hidden;
  background: #fff;
}

.ant-tree-list-holder-inner {
  align-items: flex-start;
  padding-top: 15px;
}

.column-name-div {
  text-align: center;
  font-size: 19px;
  font-weight: 800;
}

.editor-content-view {
  /*border: 3px solid #ccc;*/
  /*border-radius: 5px;*/
  padding: 0 10px;
  margin-top: 20px;
  overflow-x: auto;
  min-height: 1000px;
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