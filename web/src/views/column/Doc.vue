<template>
  <a-layout-content class="layout-content" id="layout-content">
    <a-row>
      <a-col :span="4">
        <a-tree
            class="category-tree"
            :tree-data="categoryTreeData"
            :default-expand-all="true"
            :fieldNames="{title: 'name', key:'id'}"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"

        />
      </a-col>
      <a-col :span="20">col-6</a-col>
    </a-row>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";


export default defineComponent({
  components: {

  },
  name: "Doc",
  setup() {
    const columnItemId = ref();

    const categoryTreeData = ref(
    );

    //-------------目录-----------------

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

    // /**
    //  * 根据文档 id 查询对应的文档内容
    //  */
    // const docContentByDocIdQuery = (DocId : any) => {
    //   axios.get("/doc/selectContentById/" + DocId).then((response) => {
    //
    //     if (response.data.success) {
    //       // editorRef.value.txt.html(response.data.content);
    //       editorRef.value.setHtml(response.data.content);
    //     } else {
    //       message.error(response.data.message);
    //     }
    //   });
    // }

    onMounted(() => {
      columnItemId.value = sessionStorage.getItem("ColumnItemId");
      docByColumnIdQuery(columnItemId.value);
    });

    return {
      categoryTreeData,
    };
  }
})
</script>

<style scoped>

</style>