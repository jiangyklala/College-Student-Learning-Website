<template>
  <a-layout-content class="layout-content">
    <div class="body">

    </div>
    <a-table
        :columns="columns"
        :data-source="tableData"
        :row-key="record => record.id"
        :loading="loading"
        :pagination="false"
        bordered
        class="doc-table"
        :scroll="{y : 400}"
    >
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="buttonEdit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除吗"
                ok-text="确认"
                cancel-text="取消"
                @confirm="buttonDelete(record.id)"
            >
              <a-button type="link">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
    <p style="font-size: 25px; text-align: center; padding-top: 20px">编辑区</p>
    <div class="save-btn-div"
         style="text-align: center">
      <a-space direction="horizontal" size="large">
        <a-button type="primary" @click="addCategoryItem">
          新增
        </a-button>
        <a-button type="primary"
                  @click="handleModalOk">
          保存
        </a-button>
      </a-space>
    </div>
    <div class="edit-div">
      <a-form
          :model="doc"
          :label-col="{ span : 1 }"
      >
        <a-form-item label="名称">
          <a-input v-model:value="doc.name"/>
        </a-form-item>
        <a-form-item label="父文档">
          <a-tree-select
              v-model:value="doc.parent"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              placeholder="Please select"
              tree-default-expand-all
              :tree-data="treeSelectData"
              :fieldNames="{label: 'name', key:'id', value:'id'}"
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="排序">
          <a-input v-model:value="doc.sort"/>
        </a-form-item>
      </a-form>
    </div>
    <div class="mavonEditor">
      <no-ssr>
        <mavon-editor :toolbars="markdownOption"
                      ref="md"
                      @change="mavonChange"
                      v-model="mavonVModel"
        />
      </no-ssr>
    </div>
  </a-layout-content>
</template>

<script lang="ts">
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import {defineComponent, onBeforeUnmount, onMounted, reactive, ref, shallowRef} from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/utils/tool";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'



export default defineComponent({
  components: {Editor, Toolbar},
  name: "AdminDoc",
  setup: function () {

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        width: '40%',
      },
      {
        title: '父文档',
        dataIndex: 'parent',
        width: '20%',
      },
      {
        title: '排序',
        dataIndex: 'sort',
        width: '20%',
      },
      {
        title: '操作',
        dataIndex: 'action',
        width: '30%',
      },
    ];


    //-------------页面--------------

    const loading = ref(true);
    const listData = ref();
    const tableData = ref();
    const columnId = ref();
    const mavonVModel = ref();

    /**
     * 根据专栏 id 查询其所包含的文档
     */
    const docByColumnIdQuery = (columnId : any) => {
      loading.value = true;
      axios.get("/doc/selectByColumnId/" + columnId).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          loading.value = false;
          tableData.value = Tool.array2Tree(response.data.content, 0);

          init();

        } else {
          message.error(response.data.message);
        }
      });
    }

    /**
     * 根据文档 id 查询对应的文档内容
     */
    const docContentByDocIdQuery = (DocId : any) => {
      axios.get("/doc/selectContentById/" + DocId).then((response) => {

        if (response.data.success) {
          // editorRef.value.txt.html(response.data.content);
          mavonVModel.value = response.data.content;
        } else {
          message.error(response.data.message);
        }
      });
    }

    const init = () => {
      doc.value = {                                             // 清空当前的数据信息, 避免冗余显示上一次编辑的内容
        columnId: columnId.value,                               // 记得赋值所属专栏Id
      };

      treeSelectData.value = Tool.copy(tableData.value);        // 加载选择树
      deleteParent(treeSelectData.value);                       // 这里还需删除 parent 字段
      treeSelectData.value.unshift({id: 0, name: '无'});        // 为树最前面添加一个"0"级分类, "无"
    }

    //-------------wangEditor富文本编辑器--------------


    const markdownHTML = ref();
    const mavonEditorRef = ref();

    const initEditor = () => {
      columnId.value = sessionStorage.getItem("ColumnId");
      mavonEditorRef.value = mavonEditor.markdownIt;
    }
    //
    // // 编辑器实例，必须用 shallowRef
    // const editorRef = shallowRef()
    //
    // // 内容 HTML
    // const valueHtml = ref()
    //
    // const toolbarConfig = {}
    // const editorConfig = { placeholder: '请输入内容...' }
    //
    // // 组件销毁时，也及时销毁编辑器
    // onBeforeUnmount(() => {
    //   const editor = editorRef.value
    //   if (editor != null) editor.destroy();
    // })
    //
    // const handleCreated = (editor : any) => {
    //   editorRef.value = editor // 记录 editor 实例，重要！
    // }

    const mavonChange = (value: any, render: any) => {
      markdownHTML.value = value;
    }




    //-------------表格--------------

    const treeSelectData = ref();
    treeSelectData.value = [];
    const deleteIdStr : Array<string> = [];

    /**
     * 表格的编辑按钮
     */
    const buttonEdit = (record: any) => {
      doc.value = Tool.copy(record);
      // editorRef.value.setHtml("");

      // console.log(doc.value);

      // 父文档的树选择设置
      treeSelectData.value = Tool.copy(tableData.value);
      setDisable(treeSelectData.value, record.id);              // 不能选择自己以及自己的子节点
      setParentDocDisable(treeSelectData.value, record.parent); // 不能选择自己的直接父节点, 没意义
      deleteParent(treeSelectData.value);                       // bug: 不能有 parent 字段, 否则会出现: [vue warn]: invalid prop: type check failed for prop "parent". expected object, got number.
      treeSelectData.value.unshift({id: 0, name: '无'});        // 为树最前面添加一个"0"级分类, "无"

      // 查出该文档下的文档内容
      docContentByDocIdQuery(doc.value.id);
    };

    /**
     * 表格的删除按钮
     */
    const buttonDelete = (id: number) => {
      getDeleteIdStr(tableData.value, id);
      console.log(deleteIdStr.join(","));
      axios.delete("/doc/deleteIdStr/" + deleteIdStr.join(",")).then((response) => {
        const data = response.data;

        if (data.success) {
          // 重新加载列表
          docByColumnIdQuery(columnId.value);
        }
      })
    };

    //-------------编辑区--------------
    const doc = ref();
    doc.value = {};

    /**
     * 新增按钮
     * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
     */
    const addCategoryItem = () => {

      // editorRef.value.setHtml("");

      treeSelectData.value = Tool.copy(tableData.value);        // 更新选择树
      deleteParent(treeSelectData.value);                       // 这里还需删除 parent 字段
      treeSelectData.value.unshift({id: 0, name: '无'});        // 为树最前面添加一个"0"级分类, "无"

    };

    /**
     * 保存按钮
     */
    const handleModalOk = () => {
      // console.log(doc);
      // doc.value.content = editorRef.value.getHtml();
      doc.value.content = mavonVModel.value;
      axios.post("/doc/save", doc.value).then((response) => {
        // console.log(response);
        const data = response.data;


        if (data.success) {
          // 重新加载列表
          docByColumnIdQuery(columnId.value);
          message.success("保存成功");
        } else {
          message.error(response.data.message);
        }
      })

    };


    //-------------其它--------------

    /**
     * 将选择树中指定 id 节点及其子节点都添加 disabled = true 的属性
     * @param treeSelectData
     * @param id
     */
    const setDisable = (treeSelectData : any, id : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        const node = treeSelectData[i];
        if (node.id === id) {
          node.disabled = true;

          if (Tool.isNotEmpty(node.children)) {
            for (let j = 0; j < node.children.length; ++j) {
              setDisable(node.children, node.children[j].id);
            }
          }

        } else if (Tool.isNotEmpty(node.children)) {
          setDisable(node.children, id);
        }
      }
    }

    /**
     * 将 id 与给定 parentId 相同的节点添加 disabled = true 的属性
     * @param treeSelectData
     * @param parentId
     */
    const setParentDocDisable = (treeSelectData : any, parentId : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        const node = treeSelectData[i];
        if (node.id === parentId) {
          node.disabled = true;
          break;
        }
        if (Tool.isNotEmpty(node.children)) {
          setParentDocDisable(node.children, parentId);
        }
      }
    }

    /**
     * 将选择树中的所有节点的 parent 属性删除
     * @param treeSelectData
     */
    const deleteParent = (treeSelectData : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        delete treeSelectData[i].parent;
        if (Tool.isNotEmpty(treeSelectData[i].children)) {
          deleteParent(treeSelectData[i].children);
        }
      }
      // console.log(treeSelectData);
    }

    /**
     * 将选择树中指定 id 节点及其子节点都的 id 都添加到 deleteIdStr 中
     * @param treeSelectData
     * @param id
     */
    const getDeleteIdStr = (treeSelectData : any, id : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        const node = treeSelectData[i];
        if (node.id === id) {
          deleteIdStr.push(id);  // 将目标 id 放入结果集

          if (Tool.isNotEmpty(node.children)) {
            for (let j = 0; j < node.children.length; ++j) {
              getDeleteIdStr(node.children, node.children[j].id);
            }
          }

        } else if (Tool.isNotEmpty(node.children)) {
          getDeleteIdStr(node.children, id);
        }
      }
    }



    onMounted(() => {
      initEditor();
      // console.log(columnId);
      docByColumnIdQuery(columnId.value);
    });

    return {
      loading,
      tableData,
      listData,
      columns,
      treeSelectData,

      buttonEdit,
      addCategoryItem,
      buttonDelete,

      doc,
      handleModalOk,

      // editorRef,
      // valueHtml,
      mode: 'default', // 或 'simple'
      // toolbarConfig,
      // editorConfig,
      // handleCreated,

      markdownOption: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
      mavonEditorRef,
      mavonChange,
      mavonVModel,
    };

  },
});
</script>

<style scoped>

.layout-content {
  padding: 30px;
  width: 1200px;
  height: 1800px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(237,239,242);
}

.doc-table {
  width: 1100px;
}

.wangEditor-div {
  /*border: 1px solid #ccc;*/
  padding-top: 20px;
  width: 1100px;

}

.edit-div {
  padding-top: 20px;
  width: 1100px;
}

.mavonEditor {
  padding-top: 20px;
  width: 1100px;
}

</style>